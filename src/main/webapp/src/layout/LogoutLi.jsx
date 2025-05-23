import {Link, useNavigate} from "react-router-dom";
import {useContext} from "react";
import {AuthContext} from "../context/AuthContext.jsx";

export const LogoutLi = () => {
    const navigate = useNavigate();
    const {setAuth } = useContext(AuthContext);

    const handleLogout = async () => {
        try {
            await fetch("/api/logout", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
            });
            setAuth(null);
            navigate("/");
        } catch (e) {
            console.error("Error during logout", e);
        }
    };
    return <Link onClick={handleLogout}>Déconnexion</Link>;
}