import React, { createContext, useState, useEffect } from "react";
import * as userService from "../services/userService.js";
import PropTypes from "prop-types";
import { getMatches } from "../services/userService";


export const AuthContext = createContext(null);


export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState(null);
    const [matchedUserIds, setMatchedUserIds] = useState([]);


    // Récupération des matches depuis le back
    const fetchMatchedUsers = async () => {
        try {
            const data = await getMatches(); // tu vas retourner data plus bas
            if (Array.isArray(data)) {
                setMatchedUserIds(data);
            }
        } catch (error) {
            console.error("Erreur lors du chargement des matchs :", error);
        }
    };

    const addMatchedUser = (id) => {
        setMatchedUserIds((prev) => [...prev, id]);
    };

    // Charger les matchs une seule fois au démarrage
    useEffect(() => {
        fetchMatchedUsers();
    }, []);

    // Charge l'utilisateur authentifié au démarrage
    useEffect(() => {
        const fetchAuthenticatedUser = async () => {
            try {
                const authenticatedUser = await userService.getAuthenticateUser();
                setAuth(authenticatedUser); // Met l'utilisateur dans le state
                console.log("Utilisateur authentifié :", authenticatedUser);
            } catch (error) {
                console.error("Échec de récupération de l'utilisateur :", error);
                setAuth(null); // Aucun utilisateur authentifié
            }
        };

        fetchAuthenticatedUser();
    }, []);

    // Fonction pour gérer la déconnexion
    const logout = async () => {
        try {
            await fetch("/logout", { method: "POST", credentials: "include" }); // Déconnexion serveur
            setAuth(null); // Réinitialise le state local
        } catch (error) {
            console.error("Erreur lors de la déconnexion :", error);
        }
    };

    return (
        <AuthContext.Provider value={{ auth, setAuth, logout, matchedUserIds, addMatchedUser}}>
            {children}
        </AuthContext.Provider>
    );
};
AuthProvider.propTypes = {
    children: PropTypes.node.isRequired,
};


