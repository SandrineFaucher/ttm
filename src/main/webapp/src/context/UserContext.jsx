import React, { createContext, useState, useContext } from 'react';
import PropTypes from 'prop-types';

// Création du contexte
const UserContext = createContext();

// Fournisseur du contexte
// eslint-disable-next-line react/prop-types
export const UserProvider = ({ children }) => {
    const [currentUser, setCurrentUser] = useState(null);

    // Fonction pour se connecter (par exemple, après le login)
    const loginUser = (user) => {
        setCurrentUser(user);
    };

    // Fonction pour se déconnecter
    const logoutUser = () => {
        setCurrentUser(null);
    };

    return (
        <UserContext.Provider value={{ currentUser, loginUser, logoutUser }}>
            {children}
        </UserContext.Provider>
    );
};
UserProvider.protoTypes ={
    children: PropTypes.node.isRequired,
}

// Hook personnalisé pour utiliser le contexte
export const useUser = () => useContext(UserContext);
