import {createContext, useContext} from "react";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import PropTypes from "prop-types";



const NotificationContext = createContext();

export const useNotification = ()=> useContext(NotificationContext);

export const NotificationProvider = ({children}) => {
    const notifySuccess = (message) => toast.success(message);
    const notifyError = (message) => toast.error(message);

    return (
        <NotificationContext.Provider value ={{ notifySuccess, notifyError}}>
            <ToastContainer position="top-center"/>
            {children}
        </NotificationContext.Provider>
    );
};
NotificationProvider.propTypes = {
    children: PropTypes.node.isRequired,
};