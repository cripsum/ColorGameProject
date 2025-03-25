const Auth = (function () {
    const API_URL = "http://localhost:8080/ColorGameProject/rest/auth"; // Modifica in base al tuo backend

    /**
     * Effettua il login e salva i dati nella sessione
     * @param {string} email 
     * @param {string} password 
     * @returns {Promise<boolean>} true se il login ha successo, false altrimenti
     */
    async function login(email, password) {
        try {
            console.log("Email:", email);
            console.log("Password:", password);
        
            // Costruisci l'URL con i parametri di query
            const url = `${API_URL}/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
        
            // Esegui la richiesta fetch
            const response = await fetch(url, {
                method: "POST", // Metodo POST, ma i parametri sono passati via URL
                credentials: "include", // Invia i cookie con la richiesta
                headers: { "Content-Type": "application/json" } // Anche se usi query param, puoi mantenere questo header
            });
        
            // Log della risposta per il debug
            console.log(response);

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
                console.log("Login riuscito:", data);
                return true;
            } else {
                console.error("Errore durante il login:", data);
                alert(data.MESSAGGIO);
                return false;
            }
        } catch (error) {
            console.error("Errore di connessione:", error);
            alert("Errore di connessione al server");
            return false;
        }
    }

    /**
     * Controlla se l'utente è autenticato e aggiorna l'interfaccia
     */
    async function checkSession() {
        try {
            const response = await fetch(`${API_URL}/check`, {
                method: "POST",
                credentials: "include"
            });

            if (response.ok) {
                const username = sessionStorage.getItem("username"); // Recupera il nome utente
                if (username) {
                    document.getElementById("header").innerHTML = `
                        <p>Benvenuto, ${username} 
                        <button onclick="Auth.logout()">Logout</button></p>`;
                }
            } else {
                console.log("Sessione scaduta o inesistente");
                sessionStorage.clear();
                document.getElementById("header").innerHTML = `
                    <a href="login.html">Login</a> | <a href="register.html">Registrati</a>`;
            }
        } catch (error) {
            console.error("Errore durante il controllo della sessione:", error);
        }
    }

    /**
     * Effettua il logout e cancella i dati della sessione
     */
    async function logout() {
        try {
            await fetch(`${API_URL}/logout`, {
                method: "POST",
                credentials: "include"
            });

            sessionStorage.clear();
            window.location.href = "/login.html";
        } catch (error) {
            console.error("Errore durante il logout:", error);
        }
    }

    return {
        login,
        checkSession,
        logout
    };
})();

// Controlla la sessione all'avvio della pagina
document.addEventListener("DOMContentLoaded", Auth.checkSession);
document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");

    if (loginForm) {
        loginForm.addEventListener("submit", async function (event) {
            event.preventDefault(); // Impedisce il refresh della pagina

            // Recupera i valori dai campi input
            const email = document.getElementById("loginName").value;
            const password = document.getElementById("password").value;

            // Chiama la funzione login e gestisci il risultato
            const success = await Auth.login(email, password);

            if (success) {
                window.location.href = "home.html"; // Reindirizza dopo il login
            } else {
                alert("Credenziali errate o errore nel server.");
            }
        });
    }
});
