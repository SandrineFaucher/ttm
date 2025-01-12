import React, { createContext, useState, useEffect } from "react";
import * as userService from "../services/userService.js";

export const AuthContext = createContext(null);

// eslint-disable-next-line react/prop-types
export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState(null);

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
        <AuthContext.Provider value={{ auth, setAuth, logout }}>
            {children}
        </AuthContext.Provider>
    );
};



