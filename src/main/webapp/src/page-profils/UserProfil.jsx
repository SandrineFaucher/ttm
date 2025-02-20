import React, {useContext} from "react";
import UserUpdate from "./UserUpdate.jsx";
import UpdatePassword from "./UpdatePassword.jsx";
import FormProfil from "./FormProfil.jsx";
import "./userProfil.css";
import {AuthContext} from "../context/AuthContext.jsx";
import FormUpdateProfil from "./FormUpdateProfil.jsx";
import PreviewCard from "./PreviewCard.jsx";


export default function UserProfil(){
    const { auth } = useContext(AuthContext);

    // Vérifie si auth est null ou undefined
    if (!auth) {
        return <p>Chargement...</p>;
    }
    const isAuthProfil = auth?.profil && Object.keys(auth.profil).length > 0;
    console.log("Valeur de auth.profil :", auth.profil);
    console.log("isAuthProfil :", isAuthProfil);
    return (
            <>
            <h1>Mon Profil </h1>
                <div className="container">
                    <div className="block">
                        {isAuthProfil ? (
                            <>
                                <PreviewCard/>
                                <h2>Modifier mon profil</h2>
                                <FormUpdateProfil />
                            </>
                        ) : (
                            <>
                                <h2>Remplir mon profil</h2>
                                <FormProfil />
                            </>
                        )}
                    </div>
                    <div className="block">
                        <h2>Modifier mes données</h2>
                        <div className="component">
                            <UserUpdate/>
                        </div>
                        <div className="component">
                            <UpdatePassword/>
                        </div>
                    </div>

                </div>
            </>
    )
}