
import React from "react";
import {useParams} from "react-router-dom";
import {useUser} from "../context/UserContext.jsx";
import "./detailCard.css";



export default function DetailCard(){
    const { id } = useParams();
    const { selectedUser } = useUser();
    const baseUrl = "http://localhost:8080/";
    const imagePath = selectedUser?.profil?.image;
    const defaultImage = "uploads/profil_images/default_profile_picture.png";
    const imageUrl = imagePath ? `${baseUrl}${imagePath}` : `${baseUrl}${defaultImage}`;

    if (!selectedUser) {
        return <p>Aucun utilisateur sélectionné.</p>;
    }

    return (
        <section className="detail-card-container">
            <h2>{selectedUser.username}</h2>
            <img src={imageUrl} alt="Profil" />
            <p>Ville : {selectedUser.profil?.city}</p>
            <p>Région : {selectedUser.profil?.region}</p>
            <p>Description : {selectedUser.profil?.content}</p>
        </section>
    );

}