import React, {useEffect, useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus,  faTrash} from '@fortawesome/free-solid-svg-icons';
import "./formProfil.css";
import CustomSelect from "../components/CustomSelect.jsx";
import CustomTextarea from "../components/CustomTextarea.jsx";
import { getSectors, getAccompaniements, getCities, getRegionName } from "../services/profilService.js";

export default function FormProfil () {
    /**
     * State des données
     */

    const [formData, setFormData] = useState({
        availability: [],
        sector:"",
        accompaniement:"",
        content:"",
        city:"",
        department:"",
        region:"",
    });
    const [newAvailability, setNewAvailability] = useState(""); // Disponibilité à ajouter
    const [sectors, setSectors] = useState([]); // Stocke la liste des secteurs
    const [accompaniement, setAccompaniement] = useState([]); //Stocke la liste des accompagnements
    const [cities, setCities] = useState([]); // Liste des villes proposées
    /**
     * HANDLE FUNCTIONS
     */
    function handleAddAvailability() {
        if (newAvailability.trim()) {
            setFormData((prev) => ({
                ...prev,
                availability: [...prev.availability, newAvailability.trim()],
            }));
            setNewAvailability(""); // Réinitialise le champ
        }
    }
    function handleRemoveAvailability(index) {
        setFormData((prev) => ({
            ...prev,
            availability: prev.availability.filter((_, i) => i !== index),
        }));
    }

    /**
     * UseEffect pour récupérer des données du back
     */
    useEffect(() => {
        // Fonction pour récupérer les secteurs
        const fetchSectors = async () => {
            try {
                const sectorData = await getSectors(); // Appelle la fonction fetch
                setSectors(sectorData); // Met à jour le state avec les données
            } catch (error) {
                console.error("Erreur lors de la récupération des secteurs :", error);
            }
        };
        fetchSectors();
    }, []);

    useEffect(() => {
        // Fonction pour récupérer les accompagnements
        const fetchAccompaniements = async () => {
            try {
                const accompaniementData = await getAccompaniements(); // Appelle la fonction fetch
                setAccompaniement(accompaniementData); // Met à jour le state avec les données
            } catch (error) {
                console.error("Erreur lors de la récupération des accompagnements :", error);
            }
        };
        fetchAccompaniements();
    }, []);

    /**
     * Function handleChange
     * @param e
     */
    const handleChange = async (e) => {
        const { name, value } = e.target;

        if (name === "newAvailability") {
            setNewAvailability(value);
        } else if (name === "city") {
            setFormData((prev) => ({ ...prev, city: value }));

            if (value.length > 2) { // Lancer la recherche après 3 caractères
                const results = await getCities(value);
                setCities(results);
            } else {
                setCities([]); // Réinitialise la liste si l'entrée est trop courte
            }
        } else {
            setFormData((prev) => ({
                ...prev,
                [name]: value,
            }));
        }
    };
    const handleSectorChange = (selectedSector) => {
        console.log(`Sector selected: ${selectedSector}`);
        setFormData((prevData) => ({
            ...prevData,
            sector: selectedSector,
        }));
    };
    /**
     * Transformation des secteurs pour `CustomSelect`
     */
    const sectorOptions = sectors.map((sector) => ({
        value: sector.id, // Ce qui est stocké dans formData.sector
        label: sector.content, // Ce qui est affiché dans le Select
    }));
    const handleAccompaniementChange = (selectedAccompaniement) => {
        console.log(`Accompaniement selected: ${selectedAccompaniement}`);
        setFormData((prevData) => ({
            ...prevData,
            accompaniement: selectedAccompaniement,
        }));
    };
    /**
     * Transformation des accompagnements pour `CustomSelect`
     */
    const accompaniementOptions = accompaniement.map((accompaniement) => ({
        value: accompaniement.id, // Ce qui est stocké dans formData.sector
        label: accompaniement.content, // Ce qui est affiché dans le Select
    }));

    const handleCitySelect = async (city) => {
        const regionName = await getRegionName(city.codeRegion);

        setFormData((prev) => ({
            ...prev,
            city: city.nom,  // Assure-toi que seule cette clé est mise à jour
            department: city.codeDepartement,
            region: regionName
        }));

        setCities([]); // Ferme la liste après sélection
    };
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
            <CustomSelect
                label=  "Secteurs/réseaux"
                options={sectorOptions}
                value={formData.sector}
                onChange={handleSectorChange}
                placeholder="Sélectionnez un secteur"
                required
            />
            <CustomSelect
                label=  "Accompagnement"
                options={accompaniementOptions}
                value={formData.accompaniement}
                onChange={handleAccompaniementChange}
                placeholder="Sélectionnez un accompagnement"
                required
            />
            <CustomTextarea
                label= "Description"
                name={"content"}
                value={formData.content}
                onChange={handleChange}
                placeholder="Description"
            />
            <CustomInput
                label="Ville"
                name="city"
                value={formData.city}
                onChange={handleChange}
                placeholder="Entrez une ville"
            />
            {cities.length > 0 && (
                <ul className="autocomplete-list">
                    {cities.map((city) => (
                        <li key={city.code} onClick={() => handleCitySelect(city)}>
                            {city.nom} ({city.codeDepartement})
                        </li>
                    ))}
                </ul>
            )}
            <CustomInput
                label="Département"
                name="departement"
                value={formData.department}
                placeholder="Département"
                readOnly
            />

            <CustomInput
                label="Région"
                name="region"
                value={formData.region}
                placeholder="Région"
                readOnly
            />
            <button type="submit">Soumettre</button>
        </form>
    );


}