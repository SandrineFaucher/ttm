import {createContext, useContext, useState} from "react";
import PropTypes from "prop-types";

export const UserContext = createContext();

export const UserProvider = ({ children }) => {
    const [selectedUser, setSelectedUser] = useState(null);


    return (
        <UserContext.Provider value={{ selectedUser, setSelectedUser }}>
            {children}
        </UserContext.Provider>
    );
};
export const useUser = () => useContext(UserContext);



UserProvider.propTypes = {
    children: PropTypes.node.isRequired,
};