

/**
 * Méthode pour enregistrer un utilisateur
 * @param formData
 * @returns {Promise<any>}
 * @constructor
 */
export async function Register(formData) {
    try {
        const response = await fetch("/api/admin/register", {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            body: JSON.stringify(formData),
            credentials: "include"
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

export async function UpdateUser(formData){
    try {
        const response = await fetch("/api/userUpdate", {
            method: 'PUT',
            headers:{
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            body: JSON.stringify(formData),
            credentials: "include"
        });
        if(!response.ok){
            throw new Error(`Erreur HTTP : ${response.status}`);
        }
        const data = await response.json();
        console.log("Parsed response JSON:", data);
        return data;
    } catch (error) {
        console.error("Erreur lors de la mise à jour de l'utilisateur :", error);
        throw error;
    }
}
export async function UpdateUserPassword(formData){
    try{
        const response = await fetch("/api/userPasswordUpdate", {
            method: 'PUT',
            headers:{
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            body: JSON.stringify(formData),
            credentials: "include"
        });
        if(!response.ok){
            throw new Error(`Eerreur HTTP : ${response.status}`);
        }
        const data = await response.json();
        return data;
    }catch (error){
        console.error("Erreur lors de la mise à jour du mot de passe :", error);
        throw error;
    }
}

/**
 * Méthode pour le endpoint de login
 * @param formData données du formulaire login
 * @returns {Promise<any>}
 * @constructor
 */
export async function Login(formData){
    try{
        const response = await fetch("/api/login", {
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

/**
 * Méthode pour le endpoint authenticate
 * @returns {Promise<{id}|any>}
 */
export async function getAuthenticateUser() {
    try {
        const response = await fetch("/api/authenticate", {
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

/**
 * Méthode appelant le login et l'authentification afin de mettre le user authentifié dans un AuthContext
 * @param formData
 * @returns {Promise<{id}|*>}
 */
export async function handleLoginAndAuthenticate(formData) {
    try {
        // 1. D'abord, appelle la fonction de login
        const loginResult = await Login(formData);

        console.log("Login successful:", loginResult);

        // 2. Ensuite, une fois le login réussi, appelle la fonction de récupération des infos utilisateur
        const authenticatedUser = await getAuthenticateUser();

        console.log("Authenticated user:", authenticatedUser);  // Affiche les infos de l'utilisateur authentifié.

        // Retourne les informations utilisateur
        return authenticatedUser;

    } catch (error) {
        console.error("Error in login or authenticate process:", error);
        throw error;
    }
}

export async function addMatch(userId2){
    try{
        const response = await fetch(`/api/match/${userId2}`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            credentials: 'include'  // Inclut le cookie JWT (HttpOnly) dans la requête
        });
        // Vérification de la réponse
        if (response.ok) {
            const data = await response.text();  // Récupère la réponse JSON

            // Traite la réponse si le match est bien enregistré
            console.log("Match enregistré avec succès!", data);
        }
    } catch (error) {
        console.error("Erreur lors du match :", error);

    }
}

export async function getMatches(){
    try{
        const response = await fetch("/api/matches", {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            credentials: 'include'  // Inclut le cookie JWT (HttpOnly) dans la requête
        });
        if (response.ok) {
            const data = await response.json();
            console.log("matchesIds : ", data);
            return data;

        }
    }catch (error){
        console.error("Erreur lors de la récupération des matchs :", error);
    }
}



export async function getUserDetails(userId) {
    try {
        const response = await fetch(`/api/users/${userId}`, {
            method: 'GET',
            headers: {
                "Content-Type": "application/json",
                Accept: "application/json"
            },
            credentials: 'include'
        });
        if (response.ok) {
            const data = await response.json();
            return data;
        }
    } catch (error) {
        console.error("Erreur lors de la récupération des détails de l'utilisateur :", error);
    }
}




