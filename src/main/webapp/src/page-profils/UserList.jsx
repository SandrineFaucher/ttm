import { useEffect, useState } from "react";
import { getProfilsByRoles } from "../services/profilService.js";
import UserCard from "./UserCard";

const UsersList = () => {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUsers = async () => {
            try {
                const profils = await getProfilsByRoles();
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
                users.map((user) => <CustomCard key={user.id} user={user} />)
            ) : (
                <p>Aucun utilisateur trouvé.</p>
            )}
        </div>
    );
};

export default UsersList;