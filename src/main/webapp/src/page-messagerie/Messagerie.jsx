import React, {useState, useMemo, useEffect, useContext} from "react";
import { useParams } from "react-router-dom";
import { Stomp } from '@stomp/stompjs';
import { AuthContext } from "../context/AuthContext.jsx";

export default function Messagerie(){
    const { id: destId } = useParams();
    const {auth } = useContext(AuthContext);
    const senderId = auth?.id;
    const [loadMessage, setLoadMessages ] = useState(false);
    const [loader, setLoader] = useState(false);
    const [messages, setMessages] = useState([]);
    const [content, setContent] = useState("");

    //connexion au websocket
    const client = useMemo(()=>{
        return Stomp.client("ws://localhost:8080/ws")
    }, []);

    /**
     * Récupérer les messages entre deux interlocuteurs
     */
    useEffect(() => {
        setLoader(true);
        client.connect({}, () => {
            client.subscribe('/getMessages', (e) => {
                console.log('Receive Message', e.body);
                setMessages(JSON.parse(e.body));
                setLoader(false);
            });

            client.subscribe('/newMessage', (e) => {
                console.log('New Message', e.body);
                setMessages((prev) => [...prev, JSON.parse(e.body)]);
            });

            client.send('/requestMessages', {}, JSON.stringify({
                senderId: senderId,
                destId: parseInt(destId) }));
        });
    }, [client, destId]);
    /**
     * Envoyer un message qui est persisté en base mongodb pour l'utilisateur récupéré dans l'url
     */
    const sendMessage = () => {
        if (!content.trim()) return;
        client.send("/send", {}, JSON.stringify({
            destId: parseInt(destId),
            content: content.trim()
        }));
        setContent("");
    };

    return (
        <>
            <h1>Page Messagerie</h1>

            <div className="message-card">
                <div className="messages-box">
                    <ul>
                        {messages.map((message, index) => {
                            return (
                                <li
                                    key={`${message._id.timestamp}-${index}`}
                                >
                                    <p className="username">{message.sender}</p>
                                    <p className="content">{message.content}</p>
                                </li>
                            );
                        })}
                    </ul>
                </div>

                <div className="input-area">
                    <input
                        type="text"
                        value={content}
                        onChange={(e) => setContent(e.target.value)}
                        placeholder="Écris ton message ici..."
                    />
                    <button onClick={sendMessage}>Envoyer</button>
                </div>
            </div>
        </>
    )
}