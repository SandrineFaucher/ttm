import React,{useState, useContext} from "react";
import CustomInput from "../components/CustomImput.jsx";
import {handleLoginAndAuthenticate} from "../services/userService.js";
import {AuthContext} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import { useNotification } from '../context/NotificationContext.jsx';




const FormLogin = () => {
    const { notifySuccess, notifyError } = useNotification();
    const navigate = useNavigate();
    const { setAuth } = useContext(AuthContext);

    const handleSubmit = async (e) => {

        e.preventDefault();
        const formdata = new FormData(e.target);
        try {
            // Appelle handleLoginAndAuthenticate pour effectuer les deux étapes : login et récupération des données utilisateur
            const authenticatedUser = await handleLoginAndAuthenticate({
                username: formdata.get("username"),
                password: formdata.get("password"),
            });

            // Une fois l'utilisateur authentifié, mets à jour le contexte
            setAuth(authenticatedUser); // Met à jour le contexte avec l'utilisateur connecté

            notifySuccess("Vous êtes bien connecté !");
            // redirige vers la pages des profils une fois loggé
            navigate("/Profils");
        } catch (error) {
            console.error("Erreur de connexion :", error);
            notifyError("Échec de la connexion.");
        }
    };
    return (
        <form onSubmit={handleSubmit}>
            <CustomInput
                label="Pseudo"
                name="username"
                placeholder="Entrez votre pseudo"
                required
            />
            <CustomInput
                label="Mot de passe"
                type="password"
                name="password"
                placeholder="Entrez votre mot de passe"
                required
            />
            <button type="submit">Envoyer</button>
        </form>

    )
}

export default FormLogin;