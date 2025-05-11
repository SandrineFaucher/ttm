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
    const usersWithProfil = users.filter(user => user.profil);



    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const profils = await getProfilsByRoles();

                console.log("Données reçues de l'API :", JSON.stringify(profils, null, 2));

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
            {usersWithProfil.length > 0 ? (
                usersWithProfil.map((user) => {
                    const sector = user.profil?.sectors.map(s => s.content) || ["Non renseigné"];
                    const accompaniement = user.profil?.accompaniements.map(a => a.content) || ["Non renseigné"];
                    const baseUrl = "http://localhost:8080/";
                    const imagePath = user.profil?.image;
                    const defaultImage = "uploads/profil_images/default_profile_picture.png";
                    const imageUrl = imagePath ? `${baseUrl}${imagePath}` : `${baseUrl}${defaultImage}`;
                    const userRole = user?.role;

                    // Condition si l'utilisateur a le rôle GODPARENT on ne voit pas tout
                    if (userRole === "GODPARENT") {
                        return (
                            <CustomCard
                                userRole={userRole}
                                clickable={true}
                                onClick={() => {
                                    setSelectedUser(user); // Stocke l'utilisateur dans le contexte
                                    navigate(`/detailCard/${user.id}`);
                                }}
                                key={user.id}
                                title={user.username}
                                region={user.profil?.region || "Région inconnue"}
                                availability={user.profil?.availability || "Non renseignées"}
                                image={imageUrl}
                                description={user.profil?.description || "Pas de description"}
                            />
                        );
                    }

                    // Condition pour LEADERPROJECT, ADMIN ou USER ont peut tout voir
                    if (userRole === "LEADERPROJECT" || userRole === "ADMIN" || userRole === "USER") {
                        return (
                            <CustomCard
                                userRole={userRole}
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
                                availability={user.profil?.availability|| "Non renseignées"}
                            sectors={sector}
                            accompaniements={accompaniement}
                            image={imageUrl}content={user.profil?.content || "Pas de description"}
                            />
                        );
                    }


                    return null; // Optionnel : si aucun rôle ne correspond, ne rien afficher


                })
            ) : (
                <p>Aucun utilisateur trouvé.</p>
            )}
        </div>
    );
};

export default UsersList;