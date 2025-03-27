const Auth = (function () {
    const API_URL = "http://localhost:8080/ColorGameProject/rest/auth";

    async function login(event) {
        event.preventDefault();
        const email = document.getElementById("loginName").value;
        const password = document.getElementById("password").value;
        try {
            const response = await fetch(`${API_URL}/login`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            });
            const data = await response.json();
            if (response.ok) {
                sessionStorage.setItem("idUtente", data.idUtente);
                sessionStorage.setItem("username", data.username);
                sessionStorage.setItem("email", data.email);
                sessionStorage.setItem("nome", data.nome);
                sessionStorage.setItem("cognome", data.cognome);
                sessionStorage.setItem("dataNascita", data.dataNascita);
                sessionStorage.setItem("tipo", data.tipoUtente);
                sessionStorage.setItem("token", data.token);
                sessionStorage.setItem("fotoProfilo", data.fotoProfilo);
                window.location.href = "ColorGameProject/pagine/game.html";
                return true;
            } else {
                alert(data.messaggio);
                return false;
            }
        } catch (error) {
            alert("Errore di connessione al server");
            return false;
        }
    }

    async function register(event) {
        event.preventDefault();
        const userData = {
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
            username: document.getElementById("username").value,
            nome: document.getElementById("nome").value,
            cognome: document.getElementById("cognome").value,
            dataNascita: document.getElementById("dataDiNascita").value
        };
        try {
            const response = await fetch(`${API_URL}/register`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });
            const data = await response.json();

            if (response.ok) {
                alert(data.messaggio);
                return true;
            } else {
                alert(data.messaggio);
                return false;
            }
        } catch (error) {
            alert("Errore di connessione al server");
            return false;
        }
    }

    
    async function checkSession() {
        try {
            const token = sessionStorage.getItem("token");
            if (!token) {
                console.log("Nessun token trovato");
                sessionStorage.clear();
                return;
            }
    
            const response = await fetch(`${API_URL}/check`, {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                }
            });
    
            if (!response.ok) {
                console.log(response.json().messaggio);
                sessionStorage.clear();
            } else {
                console.log("Sessione valida.");
            }
        } catch (error) {
            console.error("Errore durante il controllo della sessione:", error);
            sessionStorage.clear();
        }
    }
        

    async function logout() {
        try {
            sessionStorage.clear();
            window.location.href = "ColorGameProject/pagine/home.html";
        } catch (error) {
            console.error("Errore durante il logout:", error);
        }
    }

    return {
        login,
        register,
        checkSession,
        logout,
    };
})();

document.addEventListener("DOMContentLoaded", Auth.checkSession);
