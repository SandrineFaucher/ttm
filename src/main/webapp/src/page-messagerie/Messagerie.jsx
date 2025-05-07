
import React, { useEffect, useRef, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import { Stomp } from "@stomp/stompjs";
import { AuthContext } from "../context/AuthContext";
import {useUser} from "../context/UserContext.jsx";
import {faXmark, faPaperPlane} from "@fortawesome/free-solid-svg-icons";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import './messagerie.css';
import {getUserDetails} from "../services/userService.js";

export default function Messagerie() {
    const { id: _destId } = useParams();
    const destId = Number(_destId);
    const { selectedUser: userDest } = useUser();
    const destUsername = userDest?.username;
    const { auth } = useContext(AuthContext);
    const senderId = auth?.id;
    const authUsername = auth?.username
    const [userDetails, setUserDetails] = useState({});

    const [messages, setMessages] = useState([]);
    const [content, setContent] = useState("");
    const [loader, setLoader] = useState(false);
    const [isConnected, setIsConnected] = useState(false);

    const clientRef = useRef(null);
    const subscriptionsRef = useRef({});

    // --- Setup WebSocket ---
    useEffect(() => {
        if (!clientRef.current) {
            clientRef.current = Stomp.client("ws://localhost:8080/ws");
        }

        const client = clientRef.current;

        if (!client.connected && !isConnected) {
            setLoader(true);

            client.connect({}, () => {
                setIsConnected(true);

                // Souscrit à getMessages si non existant
                if (!subscriptionsRef.current.getMessages) {
                    const sub = client.subscribe("/getMessages", (e) => {
                        console.log("Receive Message", e.body);
                        setMessages(JSON.parse(e.body));
                        setLoader(false);
                    });
                    subscriptionsRef.current.getMessages = sub;
                }

                //Souscrit à newMessage si non existant
                if (!subscriptionsRef.current.newMessage) {
                    const sub = client.subscribe("/newMessage", (e) => {
                        console.log("New Message", e.body);
                        setMessages((prev) => [...prev, JSON.parse(e.body)]);
                    });
                    subscriptionsRef.current.newMessage = sub;
                }

                // Souscrit à deleteMessage si non existant
                if (!subscriptionsRef.current.deleteMessage) {
                    const sub = clientRef.current.subscribe("/deleteMessage", (e) => {
                        const deletedId = e.body;
                        console.log("Message supprimé reçu :", deletedId);

                        setMessages((prev) =>
                            prev.filter((msg) =>
                                msg._id !== deletedId && msg._id?.$oid !== deletedId
                            )
                        );
                    });
                    subscriptionsRef.current.deleteMessage = sub;
                }

                // Request initial messages
                client.send(
                    "/requestMessages",
                    {},
                    JSON.stringify({
                        senderId,
                        destId: parseInt(destId),
                    })
                );
            });
        }

        // Optional cleanup if component unmounts
        return () => {
            if (client.connected) {
                Object.values(subscriptionsRef.current).forEach((sub) => sub.unsubscribe());
                client.disconnect(() => {
                    console.log("WebSocket disconnected");
                    setIsConnected(false);
                });
            }
        };
    }, [senderId, destId]);

    // --- Send Message ---
    const sendMessage = () => {
        if (!content.trim()) return;

        const client = clientRef.current;
        if (client && client.connected) {
            client.send(
                "/send",
                {},
                JSON.stringify({
                    destId: parseInt(destId),
                    content: content.trim(),
                })
            );
            setContent("");
        } else {
            console.warn("WebSocket non connecté !");
        }
    };

    //--- Delete Message ---
    const handleDelete = (id) => {
        console.log("Message ID à supprimer :", id);
        const client = clientRef.current;
        if (client && client.connected) {
            client.send(
                "/delete",
                {},
                JSON.stringify({ _id: id })
            );
        } else {
            console.warn("WebSocket non connecté !");
        }
    };

    useEffect(() => {
        const fetchUserDetails = async () => {
            const knownIds = new Set(Object.keys(userDetails));
            const idsToFetch = messages
                .map((m) => m.sender)
                .filter((id) => id && !knownIds.has(String(id)));

            if (idsToFetch.length === 0) return;

            const newDetails = { ...userDetails };

            for (const id of idsToFetch) {
                const user = await getUserDetails(id);
                if (user) {
                    newDetails[id] = user;
                }
            }

            setUserDetails(newDetails);
        };

        if (messages.length > 0) {
            fetchUserDetails();
        }
    }, [messages]);

    return (
        <>
            <h1>Page Messagerie</h1>

            <div className="message-card">
                <div className="messages-box">
                    {loader && <p>Chargement des messages...</p>}
                    <ul>
                        {messages.map((message) => {
                            const messageId = message._id;
                            const idSender = message.sender;

                            return (
                                <li key={messageId}>
                                    <p className="username">
                                        {userDetails[idSender]?.username ?? "utilisateur inconnu"}
                                    </p>
                                    <div className="message">
                                        <p className="content">{message.content}</p>
                                        <div onClick={() => handleDelete(messageId)}><FontAwesomeIcon className="icon-delete" icon={faXmark}/></div>
                                    </div>
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
                    <div onClick={sendMessage}><FontAwesomeIcon icon={faPaperPlane} /></div>

                </div>
            </div>
        </>
    );
}
