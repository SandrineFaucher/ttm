import React, { createContext, useEffect, useState} from "react";

import * as userService from "../services/userService.js";


export const AuthContext = createContext(null);


// eslint-disable-next-line react/prop-types
export const AuthProvider = ({ children }) => {
    const [auth, setAuth] = useState(null);

    useEffect(() => {
        // Charge l'utilisateur authentifié au démarrage
        const fetchAuthenticatedUser = async () => {
            try {
                const authenticatedUser = await userService.getAuthenticateUser();
                setAuth(authenticatedUser); // Met l'utilisateur dans le state
                console.log("user authentifié :", authenticatedUser)
            } catch (error) {
                console.error("Failed to fetch authenticated user:", error);
                setAuth(null); // Aucun utilisateur authentifié
            }
        };

        fetchAuthenticatedUser();
    }, []);

    return (
        <AuthContext.Provider value={{ auth, setAuth }}>
            {children}
        </AuthContext.Provider>
    );
};



