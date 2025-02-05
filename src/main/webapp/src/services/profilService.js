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