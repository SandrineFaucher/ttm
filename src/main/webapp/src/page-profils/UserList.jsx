import { useEffect, useState } from "react";
import { getProfilsByRoles } from "../services/profilService.js";
import CustomCard from "../components/CustomCard.jsx";
import {useNavigate} from "react-router-dom";
import { useUser } from "../context/UserContext.jsx";

const UsersList = () => {
    const navigate = useNavigate();
    const { setSelectedUser } = useUser();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);



    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const profils = await getProfilsByRoles();
                console.log("Données reçues :", profils);
                setUsers(profils);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        };

        fetchUsers();
    }, []);


    if (loading) return <p>Chargement en cours...</p>;
    if (error) return <p>Erreur : {error}</p>;


    return (
        <div className="users-container">
            {users.length > 0 ? (

                users.map((user) => {
                const sector = user.profil?.sectors.map(s =>s.content)|| "Non renseigné";
                const accompaniement = user.profil?.accompaniements.map(a => a.content) || "Non renseigné";
                const baseUrl = "http://localhost:8080/";
                const imagePath = user.profil?.image ;
                const defaultImage = "uploads/profil_images/default_profile_picture.png";
                const imageUrl = imagePath ? `${baseUrl}${imagePath}` : `${baseUrl}${defaultImage}`;

                    return (
                        <CustomCard
                            clickable={true}
                            onClick={() => {
                                setSelectedUser(user); // Stocke l'utilisateur dans le contexte
                                navigate(`/detailCard/${user.id}`);
                            }}
                            key={user.id}
                            title={user.username}
                            city={user.profil?.city || "Ville inconnue"}
                            department={user.profil?.department || "Département inconnu"}
                            region={user.profil?.region || "Région inconnue"}
                            sectors={sector}
                            accompaniements={accompaniement}
                            image={imageUrl}
                            description={user.profil?.content || "Aucune description"}


                        />
                    );
                })
            ) : (
                <p>Aucun utilisateur trouvé.</p>
            )}
        </div>
    );
};

export default UsersList;