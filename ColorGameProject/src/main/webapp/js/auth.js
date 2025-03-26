const Auth = (function () {
    const API_URL = "http://localhost:8080/ColorGameProject/rest/auth";

    async function login(email, password) {
        try {
            const url = `${API_URL}/login?email=${encodeURIComponent(email)}&password=${encodeURIComponent(password)}`;
            const response = await fetch(url, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" }
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
                return true;
            } else {
                alert(data.MESSAGGIO);
                return false;
            }
        } catch (error) {
            alert("Errore di connessione al server");
            return false;
        }
    }

    async function register(userData) {
        try {
            const response = await fetch(`${API_URL}/register`, {
                method: "POST",
                credentials: "include",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(userData)
            });
            const data = await response.json();

            if (response.ok) {
                alert("Registrazione riuscita! Ora puoi accedere.");
                return true;
            } else {
                alert(data.MESSAGGIO);
                return false;
            }
        } catch (error) {
            alert("Errore di connessione al server");
            return false;
        }
    }

    async function checkSession() {
        try {
            const response = await fetch(`${API_URL}/check`, {
                method: "POST",
                credentials: "include"
            });

            if (response.ok) {
                const username = sessionStorage.getItem("username");
                if (username) {
                    document.getElementById("header").innerHTML = `<p>Benvenuto, ${username} <button onclick="Auth.logout()">Logout</button></p>`;
                }
            } else {
                sessionStorage.clear();
                document.getElementById("header").innerHTML = `<a href="login.html">Login</a> | <a href="register.html">Registrati</a>`;
            }
        } catch (error) {
            console.error("Errore durante il controllo della sessione:", error);
        }
    }

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
        register,
        checkSession,
        logout
    };
})();

document.addEventListener("DOMContentLoaded", Auth.checkSession);
document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", async function (event) {
            event.preventDefault();
            const email = document.getElementById("loginName").value;
            const password = document.getElementById("password").value;
            const success = await Auth.login(email, password);
            if (success) {
                window.location.href = "home.html";
            } else {
                alert("Credenziali errate o errore nel server.");
            }
        });
    }
    
    const registerForm = document.getElementById("registerForm");
    if (registerForm) {
        registerForm.addEventListener("submit", async function (event) {
            event.preventDefault();
            const userData = {
                email: document.getElementById("registerEmail").value,
                password: document.getElementById("registerPassword").value,
                username: document.getElementById("registerUsername").value,
                nome: document.getElementById("registerNome").value,
                cognome: document.getElementById("registerCognome").value,
                dataNascita: document.getElementById("registerDataNascita").value
            };
            const success = await Auth.register(userData);
            if (success) {
                window.location.href = "login.html";
            }
        });
    }
});
