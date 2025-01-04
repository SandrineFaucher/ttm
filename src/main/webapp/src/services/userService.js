import { useNavigate } from 'react-router-dom';

/**
 * Méthode pour enregistrer un utilisateur
 * @param formData
 * @returns {Promise<any>}
 * @constructor
 */
export async function Register(formData) {
    try {
        const response = await fetch("http://localhost:8080/admin/register", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            body: JSON.stringify(formData)
        });

        if (!response.ok) {
            throw new Error(`Erreur HTTP : ${response.status}`);
        }

        const data = await response.json();
        console.log("Parsed response JSON:", data);
        return data;
    } catch (error) {
        console.error("Error during register:", error);
        alert("An error occurred during registration: " + error.message);
        throw error;
    }
}

/**
 * Méthode pour connecter un utilisateur
 * @param formData
 * @returns {Promise<any>}
 * @constructor
 */
export async function Login(formData){
    try{
        const response = await fetch("http://localhost:8080/login", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            body: JSON.stringify(formData),
            credentials: 'include'  // Permet d'envoyer le cookie qui contient le jwt avec la requête
        });

        if (!response.ok) {
            const errorDetails = await response.text();  // Récupère les détails d'erreur
            throw new Error(`Erreur HTTP : ${response.status} - ${errorDetails}`);
        }

        return response.json();
    }catch (error){
        console.error("Error during login:", error);
        throw error;
    }
}

export async function getAuthenticateUser() {
    try {
        const response = await fetch("http://localhost:8080/authenticate", {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            credentials: 'include'  // Inclut le cookie JWT (HttpOnly) dans la requête
        });

        // Vérifie si la réponse est correcte (status 200)
        if (!response.ok) {
            throw new Error(`Failed to fetch authenticated user: ${response.statusText}`);
        }

        // Récupère les données de l'utilisateur
        const userData = await response.json();

        // Vérifie que les données utilisateur sont présentes
        if (!userData || !userData.id) {
            throw new Error("No user data found in the response");
        }

        // Retourne les données utilisateur
        return userData;

    } catch (error) {
        console.error("Error fetching authenticated user:", error);
        throw new Error("Failed to fetch authenticated user");
    }
}

// Fonction principale pour gérer les deux appels successifs
export async function handleLoginAndAuthenticate(formData) {
    try {
        // 1. D'abord, appelle la fonction de login
        const loginResult = await Login(formData);

        console.log("Login successful:", loginResult);  // Si vous avez une réponse utile, vous pouvez la loguer ici.

        // 2. Ensuite, une fois le login réussi, appelle la fonction de récupération des infos utilisateur
        const authenticatedUser = await getAuthenticateUser();

        console.log("Authenticated user:", authenticatedUser);  // Affiche les infos de l'utilisateur authentifié.

        // Vous pouvez retourner les informations utilisateur ici ou traiter d'autres actions
        return authenticatedUser;

    } catch (error) {
        console.error("Error in login or authenticate process:", error);
        throw error;
    }
}



