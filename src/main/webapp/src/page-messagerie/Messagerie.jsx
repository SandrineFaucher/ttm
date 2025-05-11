
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
        // Si le client WebSocket n'existe pas encore, on l'initialise
        if (!clientRef.current) {
            clientRef.current = Stomp.client("ws://localhost:8080/ws");
        }

        const client = clientRef.current;

        // Si le client n'est pas encore connecté, on établit la connexion
        if (!client.connected && !isConnected) {
            setLoader(true); // Active l'indicateur de chargement

            // Connexion au serveur WebSocket
            client.connect({}, () => {
                setIsConnected(true); // Met à jour l'état de connexion

                // Envoie une requête pour récupérer les messages initiaux de la conversation
                client.send(
                    "/requestMessages",
                    {},
                    JSON.stringify({
                        senderId,
                        destId: parseInt(destId),
                    })
                );

                // Souscrit à la réception des messages initiaux (liste complète des messages)
                if (!subscriptionsRef.current.getMessages) {
                    const sub = client.subscribe("/getMessages", (e) => {
                        console.log("Receive Message", e.body);
                        setMessages(JSON.parse(e.body)); // Met à jour la liste des messages
                        setLoader(false); // Désactive le chargement une fois les messages reçus
                    });
                    subscriptionsRef.current.getMessages = sub; // Stocke l'abonnement
                }

                // Souscrit à l'arrivée de nouveaux messages
                if (!subscriptionsRef.current.newMessage) {
                    const sub = client.subscribe("/newMessage", (e) => {
                        console.log("New Message", e.body);
                        const newMsg = JSON.parse(e.body);

                        // Ajoute le message seulement s'il n'existe pas déjà dans la liste
                        setMessages((prev) =>
                            prev.some((msg) => msg._id === newMsg._id) ? prev : [...prev, newMsg]
                        );
                    });
                    subscriptionsRef.current.newMessage = sub;
                }

                // Souscrit aux notifications de suppression de messages
                if (!subscriptionsRef.current.deleteMessage) {
                    const sub = client.subscribe("/deleteMessage", (e) => {
                        const deletedId = e.body;
                        console.log("Message supprimé reçu :", deletedId);

                        // Retire le message de la liste s'il correspond à l'ID reçu
                        setMessages((prev) =>
                            prev.filter((msg) =>
                                msg._id !== deletedId && msg._id?.$oid !== deletedId
                            )
                        );
                    });
                    subscriptionsRef.current.deleteMessage = sub;
                }
            });
        }

        // Nettoyage à la désactivation du composant ou changement des dépendances
        return () => {
            if (client.connected) {
                // Désabonne de tous les topics
                Object.values(subscriptionsRef.current).forEach((sub) => sub.unsubscribe());
                subscriptionsRef.current = {}; // Réinitialise les références d'abonnements

                // Déconnecte le client WebSocket proprement
                client.disconnect(() => {
                    console.log("WebSocket disconnected");
                    setIsConnected(false); // Met à jour l'état de connexion
                });
            }
        };
    }, [senderId, destId]); // Relance le useEffect quand l'expéditeur ou le destinataire change

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
