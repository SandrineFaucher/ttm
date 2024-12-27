import React,{useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import {Login} from "../services/userService.js";
import {useNavigate} from "react-router-dom";

const FormLogin = () => {

    const [formData, setFormData] = useState({
        username: "",
        password: "",

    });

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };
    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log("Form submitted:", formData);
        try {
            const response = await Login(formData);
            console.log("Login successful:", response);
            alert("Vous êtes bien connecté !");

        } catch (error) {
            console.error("Erreur de connexion :", error);
            alert("Échec de la connexion.");
        }
    };
    return (
        <form onSubmit={handleSubmit}>
            <CustomInput
                label="Pseudo"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder="Entrez votre pseudo"
                required
            />
            <CustomInput
                label="Mot de passe"
                type="password"
                name="password"
                value={formData.password}
                onChange={handleChange}
                placeholder="Entrez votre mot de passe"
                required
            />
            <button type="submit">Envoyer</button>
        </form>

    )
}

export default FormLogin;