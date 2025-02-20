import CustomCard from "../components/CustomCard.jsx";
import React, {useContext} from "react";
import {AuthContext} from "../context/AuthContext.jsx";

export default function PreviewCard() {
    const {auth} = useContext(AuthContext);
    // Vérifie si auth est null ou undefined
    if (!auth) {
        return <p>Chargement...</p>;
    }

    return (
        <>
        <CustomCard
        title={auth.username}

        />
        </>
    );

}