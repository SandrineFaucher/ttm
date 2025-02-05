import React,{useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import CustomSelect from "../components/CustomSelect.jsx";
import { Register } from "../services/userService.js";

export default function FormRegister() {
    /**
     * State du formulaire
     */
    const roles = [
        { label: 'Parrain/Marraine', value: 'GODPARENT' },
        { label: 'Porteur de projet', value: 'LEADERPROJECT' },
        { label: 'Administrateur', value: 'ADMIN' },
        { label: 'Utilisateur', value: 'USER' },
    ];

    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
        passwordConfirm: '',
        role: '', // Initialisé à une valeur vide
    });

    /**
     * FONCTIONS HANDLECHANGE
     */
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };
    const handleRoleChange = (selectedRole) => {
        console.log(`Role selected: ${selectedRole}`);
        setFormData((prevData) => ({
            ...prevData,
            role: selectedRole,
        }));
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log('Form submitted:', formData);

        // Validation des mots de passe
        if (formData.password !== formData.passwordConfirm) {
            alert("Les mots de passe ne correspondent pas !");
            return;
        }
        try {
            const response = await Register(formData);
            console.log("Registration successful:", response);
            alert("Utilisateur enregistré avec succès !");
        } catch (error) {
            console.error("Erreur lors de l'enregistrement :", error.message || error);
            alert(`Échec de l'enregistrement : ${error.message || "Erreur inconnue."}`);
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
            <CustomInput
                label="Mot de passe"
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Entrez un mot de passe"
                required
            />
            <CustomInput
                label="Confirmation du mot de passe"
                type="password"
                name="passwordConfirm"
                value={formData.passwordConfirm}
                onChange={handleChange}
                placeholder="Veuillez confirmer le mot de passe"
                required
            />
            <CustomSelect
                label=  "Role"
                options={roles}
                value={formData.role}
                onChange={handleRoleChange} // Mise à jour via handleRoleChange
                placeholder="Sélectionnez un rôle"
                required
            />
            <button type="submit">Valider</button>
        </form>
    );
}
