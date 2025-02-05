import React, {useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus,  faTrash} from '@fortawesome/free-solid-svg-icons';
import "./formProfil.css";

export default function FormProfil () {
    // State des données
    const [formData, setFormData] = useState({
        availability: [], // Tableau des disponibilités
    });
    const [newAvailability, setNewAvailability] = useState(""); // Disponibilité à ajouter

    // Ajouter une disponibilité
    function handleAddAvailability() {
        if (newAvailability.trim()) {
            setFormData((prev) => ({
                ...prev,
                availability: [...prev.availability, newAvailability.trim()],
            }));
            setNewAvailability(""); // Réinitialiser le champ
        }
    }

    // Supprimer une disponibilité
    function handleRemoveAvailability(index) {
        setFormData((prev) => ({
            ...prev,
            availability: prev.availability.filter((_, i) => i !== index),
        }));
    }
    // Gestion du changement dans les champs d’entrée
    const handleChange = (e) => {
        const { name, value } = e.target;

        if (name === "newAvailability") {
            // Mise à jour de newAvailability
            setNewAvailability(value);
        } else {
            // Mise à jour de formData pour d'autres champs
            setFormData((prev) => ({
                ...prev,
                [name]: value,
            }));
        }
    };

    // Soumission du formulaire
    const handleSubmit = (e) => {
        e.preventDefault();
        console.log("Données soumises :", formData);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div className="rowWithIcon">
                <CustomInput
                    label="Mes disponibilités"
                    name="newAvailability"
                    value={newAvailability}
                    onChange={handleChange}
                    placeholder="Ex : Lundi matin"
                />
                <button className="iconAdd" onClick={handleAddAvailability}>
                    <FontAwesomeIcon icon={faPlus} />
                </button>
            </div>

            <ul>
                {formData.availability.map((availability, index) => (
                    <li key={index} className="rowWithIcon">
                        {availability}
                        <div className="iconDelete"
                            onClick={() => handleRemoveAvailability(index)}
                        >
                            <FontAwesomeIcon icon={faTrash}  />
                        </div>
                    </li>
                ))}
            </ul>

            <button type="submit">Soumettre</button>
        </form>
    );


}