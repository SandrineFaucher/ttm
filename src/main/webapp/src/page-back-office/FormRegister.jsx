import React,{useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import CustomSelect from "../components/CustomSelect.jsx";

const FormRegister = () => {
    const roles = [
        { label: 'Godparent', value: 'GODPARENT' },
        { label: 'Leader Project', value: 'LEADERPROJECT' },
        { label: 'Admin', value: 'ADMIN' },
        { label: 'User', value: 'USER' },
    ];
    const [formData, setFormData] = useState({
        username: "",
        email:"",
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
    const handleRoleChange = (selectedRole) => {
        setFormData({ ...formData, role: selectedRole });
    };
    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Form submitted:", formData);
    };
    return (
        <form onSubmit={handleSubmit}>
            <CustomInput
                label="Pseudo"
                typ="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                placeholder="Entrez votre pseudo"
                required
            />
            <CustomInput
                label="Email"
                typ="email"
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
            <label>Role:</label>
            <CustomSelect
                options={roles}
                value={formData.role}
                onChange={handleRoleChange}
                placeholder="Select a role"
            />
            <button type="submit">Submit</button>
        </form>

    )
}

export default FormRegister;