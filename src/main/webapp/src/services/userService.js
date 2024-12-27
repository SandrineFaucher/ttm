

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
export async function Login(formData){
    try{
        const response = await fetch("http://localhost:8080/login", {
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

        return response.json();
    }catch (error){
        console.error("Error during login:", error);
        throw error;
    }
}