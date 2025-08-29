import React,{useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import CustomSelect from "../components/CustomSelect.jsx";
import { Register } from "../services/userService.js";
import { useNotification } from '../context/NotificationContext.jsx';

export default function FormRegister() {
    const { notifySuccess, notifyError } = useNotification();
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
        // Empêche le comportement par défaut du formulaire (rechargement de la page)
        e.preventDefault();
        //débogage
        //console.log('Form submitted:', formData);

        // Validation des mots de passe
        if (formData.password !== formData.passwordConfirm) {
            // Affiche une notification d’erreur si les mots de passe ne correspondent pas
            notifyError("Les mots de passe ne correspondent pas !");
            // Arrête l’exécution de la fonction si la validation échoue
            return;
        }
        try {
            const response = await Register(formData);
            // Envoie les données du formulaire à la fonction register de l'API
            console.log("Registration successful:", response);
            // Affiche une notification de succès
            notifySuccess("Utilisateur enregistré avec succès !");

        } catch (error) {
            console.error("Erreur lors de l'enregistrement :", error.message || error);
            // Affiche une notification d’erreur à l’utilisateur
            notifyError(`Échec de l'enregistrement : ${error.message || "Erreur lors de l'enregistrement"}`);
        }
    };

    return (
        <form className="form-register" onSubmit={handleSubmit}>
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
