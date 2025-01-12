import React, {useContext, useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import { AuthContext } from "../context/AuthContext.jsx";


export default function UpdatePassword() {
    const {auth, setAuth } = useContext(AuthContext);
    const [formData, setFormData] = useState({
        oldPassword:(auth.password) ,
        newPassword: '',
        newPasswordConfirm: '',
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

        // Validation des mots de passe
        if (formData.newPassword !== formData.newPasswordConfirm) {
            alert("Les mots de passe ne correspondent pas !");
            return;
        }

        try {
            const response = await UpdatePassword(formData);
            console.log("UpdatePassword successful:", response);
            setAuth({
                ...auth,
                password: formData.newPassword,
            });
            alert("Mot de passe modifié avec succès !");
        } catch (error) {
            console.error("Erreur lors de la modification:", error.message || error);
            alert(`Échec de la modification: ${error.message || "Erreur inconnue."}`);
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <CustomInput
                label="Ancien mot de passe"
                type="password"
                name="password"
                value={formData.oldPassword}
                onChange={handleChange}
                placeholder="Ancien mot de passe"
                required
            />

            <CustomInput
                label="Nouveau mot de passe"
                type="password"
                name="password"
                value={formData.newPassword}
                onChange={handleChange}
                placeholder="Entrez un nouveau mot de passe"
                required
            />
            <CustomInput
                label="Confirmation du nouveau mot de passe"
                type="password"
                name="passwordConfirm"
                value={formData.newPasswordConfirm}
                onChange={handleChange}
                placeholder="Veuillez confirmer le mot de passe"
                required
            />

            <button type="submit">Valider</button>
        </form>
    );
}