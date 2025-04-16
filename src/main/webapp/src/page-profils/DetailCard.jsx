
import React from "react";
import {useNavigate, useParams} from "react-router-dom";
import {useUser} from "../context/UserContext.jsx";
import "./detailCard.css";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMessage, faHandBackFist} from '@fortawesome/free-solid-svg-icons';



export default function DetailCard(){
    // const { id } = useParams();
    const navigate = useNavigate();
    const { selectedUser } = useUser();
    const baseUrl = "http://localhost:8080/";
    const imagePath = selectedUser?.profil?.image;
    const defaultImage = "uploads/profil_images/default_profile_picture.png";
    const imageUrl = imagePath ? `${baseUrl}${imagePath}` : `${baseUrl}${defaultImage}`;

    const sectorsContent = selectedUser?.profil?.sectors.map(s =>s.content);
    const accompaniementContent = selectedUser?.profil?.accompaniements.map(s => s.content);
    if (!selectedUser) {
        return <p>Aucun utilisateur sélectionné.</p>;
    }

    return (
        <section className="detail-card-container">
            <section className="header-card-detail">
            <h2>{selectedUser.username}</h2>
            <img src={imageUrl} alt="Profil" />
            <p>Ville : {selectedUser.profil?.city}</p>
            <p>Région : {selectedUser.profil?.region}</p>
            <p>Secteurs d'activité : {sectorsContent}</p>
            <p>Accompagnements : {accompaniementContent}</p>
            </section>
            <article className="article-descriptif">
                <h3>Description</h3>
                <p>{selectedUser.profil?.content}</p>
            </article>
            <nav className="card-detail-nav">
                <FontAwesomeIcon className="icon-message" icon={faMessage}
                                 onClick={() => navigate(`/messagerie/${selectedUser.id}`)}/>
                <div className="icon-container">
                <FontAwesomeIcon className="left-hand" icon={faHandBackFist} />
                <FontAwesomeIcon className="right-hand" icon={faHandBackFist} />
                </div>
            </nav>
        </section>
    );

}