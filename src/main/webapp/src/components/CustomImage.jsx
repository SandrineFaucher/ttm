import React, {useEffect, useRef, useState} from "react";
import PropTypes from "prop-types";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import { faTrash } from '@fortawesome/free-solid-svg-icons';

const CustomImage = ({ label, name, onChange, className }) => {
    const [preview, setPreview] = useState(null);
    const fileInputRef = useRef(null);

    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            // Cache l'input après sélection
            e.target.style.display = "none";

            const reader = new FileReader();
            reader.onloadend = () => {
                setPreview(reader.result);
                onChange(file); // On envoie le fichier au parent
            };
            reader.readAsDataURL(file);
        }
    };

    const handleRemoveImage = () => {
        setPreview(null);
        onChange(null); // Réinitialisation dans le parent
    };

    // useEffect pour gérer la réapparition de l'input
    useEffect(() => {
        if (preview === null && fileInputRef.current) {
            // Réinitialise le champ input
            fileInputRef.current.value = "";
            // Réaffiche l'input
            fileInputRef.current.style.display = "block";
        }
    }, [preview]); // Exécute le code à chaque changement de `preview`


    return (
        <div className={`custom-image ${className}`}>
            {label && <label htmlFor={name}>{label}</label>}

            <input
                type="file"
                id={name}
                name={name}
                accept="image/*"
                onChange={handleImageChange}
                ref={fileInputRef} // Référence pour manipuler l'input
                className="input-file"
            />

            {preview && (
                <div className="image-preview">
                    <img src={preview} alt="Aperçu" className="preview-img"/>
                    <div className="remove-btn"
                         onClick={handleRemoveImage}
                    >
                        <FontAwesomeIcon icon={faTrash}/>
                    </div>
                </div>
            )}
        </div>
    );
};

CustomImage.propTypes = {
    label: PropTypes.string,
    name: PropTypes.string.isRequired,
    onChange: PropTypes.func.isRequired,
    className: PropTypes.string,
};

export default CustomImage;