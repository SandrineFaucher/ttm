import CustomCard from "../components/CustomCard.jsx";
import React, {useContext, useEffect} from "react";
import {AuthContext} from "../context/AuthContext.jsx";


export default function PreviewCard() {
    /**
     * Récupération des variables en utilisant le contexte
     */
    const {auth} = useContext(AuthContext);
    // Vérifie si auth est null ou undefined
    if (!auth) {
        return <p>Chargement...</p>;
    }
    // Surveiller les changements de auth
    // eslint-disable-next-line react-hooks/rules-of-hooks
    useEffect(() => {
        console.log("AuthContext a changé :", auth);
    }, [auth]); // Déclenché chaque fois que `auth` change

    const sectorsContent = auth?.profil?.sectors.map(s =>s.content);
    const accompaniementContent = auth?.profil?.accompaniements.map(s => s.content);

    const baseUrl = "http://localhost:8080/";
    const imagePath = auth?.profil?.image;
    const defaultImage = "uploads/profil_images/default_profile_picture.png";
    const imageUrl = imagePath ? `${baseUrl}${imagePath}` : `${baseUrl}${defaultImage}`;


    return (
        <>
        <CustomCard
        title={auth.username}
        region={auth.profil.region}
        department={auth.profil.department}
        city={auth.profil.city}
        image={imageUrl}
        sectors={sectorsContent}
        accompaniements={accompaniementContent}
        availability={auth.profil.availability}
        description={auth.profil.content}
        />
        </>
    );

}