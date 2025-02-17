export async function getSectors() {
    try {
        const response = await fetch("http://localhost:8080/sectors", {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            credentials: 'include'  // Inclut le cookie JWT (HttpOnly) dans la requête
        });

        // Vérifie si la réponse est correcte (status 200)
        if (!response.ok) {
            throw new Error(`Failed to fetch sectors : ${response.statusText}`);
        }
        const sectorData = await response.json();

        return sectorData;

    } catch (error) {
        console.error("Error fetching sectors :", error);
        throw new Error("Failed to fetch sectors");
    }
}

export async function getAccompaniements() {
    try {
        const response = await fetch("http://localhost:8080/accompaniements", {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            credentials: 'include'  // Inclut le cookie JWT (HttpOnly) dans la requête
        });

        // Vérifie si la réponse est correcte (status 200)
        if (!response.ok) {
            throw new Error(`Failed to fetch accompaniements : ${response.statusText}`);
        }
        const accompaniementData = await response.json();

        return accompaniementData;

    } catch (error) {
        console.error("Error fetching accompaniements :", error);
        throw new Error("Failed to fetch accompaniements");
    }
}

/**
 * Méthode de récupération des villes, départements, régions à partir de geo.api.gouv
 * @type {string}
 */
const API_BASE_URL = "https://geo.api.gouv.fr";

export const getCities = async (searchTerm) => {
    try {
        const response = await fetch(`${API_BASE_URL}/communes?nom=${searchTerm}&fields=nom,code,codeDepartement,codeRegion&limit=5`);
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération des villes");
        }
        return await response.json();
    } catch (error) {
        console.error("Erreur API:", error);
        return [];
    }
};

export const getRegionName = async (regionCode) => {
    try {
        const response = await fetch(`https://geo.api.gouv.fr/regions/${regionCode}`);
        if (!response.ok) {
            throw new Error("Erreur lors de la récupération de la région");
        }
        const data = await response.json();
        return data.nom; // On retourne uniquement le nom de la région
    } catch (error) {
        console.error("Erreur API (région) :", error);
        return "";
    }
};

export async function postProfil(formData) {
    const formDataToSend = new FormData();

    try {
        console.log("Image reçue :", formData.image);
        console.log("Type :", typeof formData.image);
        console.log("Instance de File ?", formData.image instanceof File);
        // Ajout explicite de l'image si elle est définie
        if (formData.image instanceof File) {
            formDataToSend.append("image", formData.image, formData.image.name);
        }
        // Remplir formDataToSend avec les données de formData
        Object.entries(formData).forEach(([key, value]) => {
            if (value !== null && key !=="image") {
                formDataToSend.append(key, value);
            }
        });
        console.log("📤 Contenu de formDataToSend AVANT envoi:");
        for (let [key, value] of formDataToSend.entries()) {
            console.log(`🔹 ${key}:`, value);
        }
        // Vérifie les données envoyées en console
        for (let [key, value] of formDataToSend.entries()) {
            console.log(key, value);
        }

        // Envoi des données au serveur
        const response = await fetch("http://localhost:8080/profil", {
            method: "POST",
            headers: { Accept: "application/json" }, // Pas de `Content-Type` avec FormData
            body: formDataToSend,
            credentials: "include"
        });

        if (!response.ok) {
            throw new Error(`Erreur HTTP : ${response.status}`);
        }

        // Vérifie si la réponse contient du JSON avant de parser pour éviter les erreurs
        const text = await response.text();
        const data = text ? JSON.parse(text) : {};
        console.log("Réponse serveur:", data);

        // Si la réponse contient une URL d'image, affichez-la
        if (data.image) {
            const img = document.createElement('img');
            img.src = data.image;
            document.body.appendChild(img);
        }
        return data;
    } catch (error) {
        console.error("Erreur lors de l'envoi du profil:", error);
        alert("Une erreur est survenue lors de l'envoi du profil : " + error.message);
        throw error;
    }
}