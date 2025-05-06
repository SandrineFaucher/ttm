import React, {useContext, useEffect, useState} from "react";
import CustomInput from "../components/CustomImput.jsx";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {faPlus,  faTrash} from '@fortawesome/free-solid-svg-icons';
import "./formProfil.css";
import CustomSelect from "../components/CustomSelect.jsx";
import CustomTextarea from "../components/CustomTextarea.jsx";
import CustomImage from "../components/CustomImage.jsx";
import {getSectors, getAccompaniements, getCities, getRegionName, updateProfil} from "../services/profilService.js";
import {AuthContext} from "../context/AuthContext.jsx";
import { useNotification } from '../context/NotificationContext.jsx';

export default function FormUpdateProfil () {
    const { notifySuccess, notifyError } = useNotification();
    /**
     * State des données
     */
    const { auth, setAuth } = useContext(AuthContext);
    const authProfil = auth?.profil;

    const [formData, setFormData] = useState({
        availability:[authProfil.availability],
        sectors:[authProfil.sectors.map(s =>s.id)],
        accompaniements:[authProfil.accompaniements.map(a => a.id)] ,
        content:authProfil.content ?? "",
        city:authProfil.city ?? "",
        department:authProfil.department ?? "",
        region:authProfil.region ?? "",
        image:authProfil.image ?? ""
    });

    const [changeAvailability, setChangeAvailability] = useState(""); // Disponibilité à ajouter
    const [secteurs, setSecteurs] = useState([]); // Stocke la liste des secteurs
    const [accompagnements, setAccompagnements] = useState([]); //Stocke la liste des accompagnements
    const [cities, setCities] = useState([]); // Liste des villes proposées


    /**
     * HANDLE FUNCTIONS
     */
    function handleAddAvailability() {
        if (changeAvailability.trim()) {
            setFormData((prev) => ({
                ...prev,
                availability: [...prev.availability, changeAvailability.trim()],
            }));
            setChangeAvailability(""); // Réinitialise le champ
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
                const sectorData = await getSectors();
                setSecteurs(sectorData); // Met à jour le state avec les données
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
                const accompaniementData = await getAccompaniements();
                setAccompagnements(accompaniementData); // Met à jour le state avec les données
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

        if (name === "changeAvailability") {
            setChangeAvailability(value);
        } else if (name === "city") {
            setFormData((prev) => ({ ...prev, city: value }));

            if (value.length > 2) { // Lance la recherche après 3 caractères
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
            sectors: selectedSector,
        }));
    };
    /**
     * Transformation des secteurs pour `CustomSelect`
     */
    const sectorOptions = secteurs.map((sector) => ({
        value: parseInt(sector.id), // Ce qui est stocké dans formData.sectors
        label: sector.content, // Ce qui est affiché dans le Select
    }));
    const handleAccompaniementChange = (selectedAccompaniement) => {
        console.log(`Accompaniement selected: ${selectedAccompaniement}`);
        setFormData((prevData) => ({
            ...prevData,
            accompaniements: selectedAccompaniement,
        }));
    };
    /**
     * Transformation des accompagnements pour `CustomSelect`
     */
    const accompaniementOptions = accompagnements.map((accompaniement) => ({
        value: parseInt(accompaniement.id), // Ce qui est stocké dans formData.accompaniements
        label: accompaniement.content, // Ce qui est affiché dans le Select
    }));

    const handleCitySelect = async (city) => {
        const regionName = await getRegionName(city.codeRegion);
        setFormData((prev) => ({
            ...prev,
            city: city.nom,
            department: city.codeDepartement,
            region: regionName
        }));
        setCities([]); // Ferme la liste après sélection
    };
    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const result = await updateProfil(formData);
            console.log("Profil mis à jour :", result);
            setAuth({
                ...auth,
                availability: formData.availability,
                sectors: formData.sectors,
                accompaniements: formData.accompaniements,
                content: formData.content,
                city: formData.city,
                department: formData.department,
                region: formData.region

            });
            notifySuccess("Votre profil a été mis à jour avec succès !");

        } catch (error) {
            console.error("Erreur lors de l'envoi du formulaire :", error);
            notifyError("Une erreur est survenue lors de l'envoi du formulaire.");
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <CustomImage
                label="Photo du profil"
                name="image"
                className="input-image"
                onChange={(file) => setFormData((prev) => ({ ...prev, image: file }))}
            />
            <div className="rowWithIcon">
                <CustomInput
                    label="Mes disponibilités"
                    name="changeAvailability"
                    value={changeAvailability}
                    onChange={handleChange}
                    placeholder="Ex : Lundi matin"
                />
                <div className="iconAdd" onClick={handleAddAvailability}>
                    <FontAwesomeIcon icon={faPlus} />
                </div>
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
                name="sector"
                options={sectorOptions}
                value={formData.sectors}
                onChange={handleSectorChange}
                placeholder="Sélectionnez un secteur"
                required
            />
            <CustomSelect
                label="Accompagnement"
                name="accompaniement"
                options={accompaniementOptions}
                value={formData.accompaniements}
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
            <button type="submit">Modifier</button>
        </form>
    );
}