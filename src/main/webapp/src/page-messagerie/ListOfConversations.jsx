import {useNavigate} from "react-router-dom";
import {useUser} from "../context/UserContext.jsx";
import { AuthContext } from "../context/AuthContext.jsx";
import {useContext, useEffect, useState} from "react";
import {getMatches, getUserDetails} from "../services/userService.js";


export default function ListOfConversations (){
    const {auth} = useContext(AuthContext);
    const navigate = useNavigate();
    const { selectedUser } = useUser();
    const [listOfLeaderProject, setListOfLeaderproject ] = useState([])
    const [userDetails, setUserDetails] = useState({});
    console.log("auth :", auth)




    useEffect(() => {
        const fetchMatches = async () => {
            try {
                const matches = await getMatches();
                if (matches) {
                    setListOfLeaderproject(matches);
                    // Récupérer les détails des utilisateurs
                    const details = await Promise.all(matches.map(id => getUserDetails(id)));
                    const detailsMap = details.reduce((acc, user) => {
                        acc[user.id] = user;
                        return acc;
                    }, {});
                    setUserDetails(detailsMap);
                }

            } catch (error) {
                console.error("Erreur lors de la récupération des matchs :", error);
            }
        };

        fetchMatches();
    }, [auth]);



    const handleConversationClick = (userId) => {
        navigate(`/messagerie/${userId}`);
    };

    return (
        <>
            <h2>Liste des conversations </h2>
            <ul>
                {listOfLeaderProject.map(userId => (
                    <li key={userId}>
                        <button onClick={() => handleConversationClick(userId)}>
                            Conversation avec {userDetails[userId]?.username }
                        </button>
                    </li>
                ))}
            </ul>


        </>
    )


}