import {useContext, useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import { AuthContext } from "../context/AuthContext.jsx";
import { UpdateUser} from "../services/userService.js";

export default function UserUpdate() {
    const {auth, setAuth } = useContext(AuthContext);
    const [formData, setFormData] = useState({
        id:(auth.id),
        username: (auth.username),
        email: (auth.email),
        password: (auth.password)
    });

// Gestion générique des champs d'entrée
const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevData) => ({
        ...prevData,
        [name]: value,
    }));
};

const handleSubmit = async (e) => {
    e.preventDefault();
    console.log('Form submitted:', formData);

    try {
        const response = await UpdateUser(formData);
        console.log("Mise à jour réussie:", response);
        setAuth({
            ...auth,
            username: formData.username,
            email: formData.email,
        });
        alert("Utilisateur mis à jour avec succès !");
    } catch (error) {
        console.error("Erreur lors de la mise à  jour :", error.message || error);
        alert(`Échec de la mise à jour : ${error.message || "Erreur inconnue."}`);
    }
};

return (
    <form onSubmit={handleSubmit}>
        <CustomInput
            label="Pseudo"
            type="text"
            name="username"
            value={formData.username}
            onChange={handleChange} // Mise à jour générique via `handleChange`
            placeholder="Entrez votre pseudo"
            required
        />
        <CustomInput
            label="Email"
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            placeholder="Entrez votre email"
            required
        />
        <button type="submit">Valider</button>
    </form>
);
}