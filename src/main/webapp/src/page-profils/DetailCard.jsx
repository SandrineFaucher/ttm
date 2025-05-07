
import React, {useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useUser} from "../context/UserContext.jsx";
import { AuthContext } from "../context/AuthContext.jsx";
import "./detailCard.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import {faMessage, faCalendarDays, faHandshake} from '@fortawesome/free-solid-svg-icons';
import { addMatch} from "../services/userService.js";
import { useNotification } from '../context/NotificationContext.jsx';
import {faMessage, faHandBackFist, faCalendarDays} from '@fortawesome/free-solid-svg-icons';
import { addMatch} from "../services/userService.js";




export default function DetailCard(){
    const { notifySuccess, notifyError } = useNotification();
    const navigate = useNavigate();
    const { selectedUser } = useUser();
    const baseUrl = "http://localhost:8080/";
    const imagePath = selectedUser?.profil?.image;
    const defaultImage = "uploads/profil_images/default_profile_picture.png";
    const imageUrl = imagePath ? `${baseUrl}${imagePath}` : `${baseUrl}${defaultImage}`;

    const { matchedUserIds, addMatchedUser} = useContext(AuthContext);
    const { auth } = useContext(AuthContext);
    const [match, setMatch] = useState(false);
    const isAuthLeaderproject = auth?.role === "LEADERPROJECT";


    useEffect(() => {
        if (selectedUser && matchedUserIds.includes(selectedUser.id)) {
            setMatch(true);
        }
    }, [selectedUser, matchedUserIds]);

    const sectorsContent = selectedUser?.profil?.sectors.map(s =>s.content);
    const accompaniementContent = selectedUser?.profil?.accompaniements.map(s => s.content);

    if (!selectedUser) {
        return <p>Aucun utilisateur sélectionné.</p>;
    }
    const handleMatchClick = async () => {
        try {
            await addMatch(selectedUser.id);
            setMatch(true);
            addMatchedUser(selectedUser.id);
            notifySuccess("Votre match est bien enregistré")

        } catch (error) {
            console.error("Erreur lors de l'envoi du match :", error);
        }
    };

    return (
        <section className="detail-card-container">
            <section className="header-card-detail">

            <h2>{selectedUser.username}</h2>
            <img src={imageUrl} alt="Profil" />
            <p>{selectedUser.profil?.city}</p>
            <p> {selectedUser.profil?.region}</p>
            </section>
            <section className="secteur-reseau">
                <p><strong>Secteurs :</strong> {sectorsContent}</p>
            <p><strong>Accompagnements :</strong> {accompaniementContent}</p>


            </section>
            <article className="article-descriptif">
                <div className="point-top"></div>
                <h3>Description</h3>
                <p>{selectedUser.profil?.content}</p>
            </article>
            <nav className="card-detail-nav">
                <div className={`icon-container ${match ? 'matched' : ''}`}>
                    <FontAwesomeIcon

                        className={`hand ${match ? 'matched' : ''}`}
                        icon={faHandshake}

                        onClick={!match ? handleMatchClick : undefined}
                    />
                </div>


                {match && isAuthLeaderproject &&(

                    <>
                        <FontAwesomeIcon
                            className="icon-message"
                            icon={faMessage}
                            onClick={() => navigate(`/messagerie/${selectedUser.id}`)}
                        />
                        <FontAwesomeIcon
                            className="icon-calendar"
                            icon={faCalendarDays}
                        />
                    </>
                )}
            </nav>
        </section>
    );

}