import React,{useState} from "react";
import CustomInput from "../components/CustomImput.jsx";

const FormLogin = () => {

    const [formData, setFormData] = useState({
        username: "",
        password: "",
        passwordConfirm : "",
        role: ""
    });
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Form submitted:", formData);
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
                placeholder="Entrez un mot de passe"
                required
            />
            <button type="submit">Submit</button>
        </form>

    )
}

export default FormLogin;