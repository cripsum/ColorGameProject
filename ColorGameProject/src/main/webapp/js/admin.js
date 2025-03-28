const API_URL = "http://localhost:8080/ColorGameProject/rest/admin"; 
document.addEventListener("DOMContentLoaded", () => {
    document.getElementById("btnAddAdmin").addEventListener("click", () => callWS("addAdmin"));
    document.getElementById("btnRemoveAdmin").addEventListener("click", () => callWS("removeAdmin"));
    document.getElementById("btnBanUser").addEventListener("click", () => callWS("banUser"));
    document.getElementById("btnUnbanUser").addEventListener("click", () => callWS("unbanUser"));
    document.getElementById("btnRefresh").addEventListener("click", refreshUsers);
    refreshUsers();
});
async function callWS(action) {
    const token = sessionStorage.getItem("token");
    if (!token) {
        if (!token) {
            console.error("Token non trovato");
            window.location.href = "accedi.html";
            return;
        }
    }

    const selectedIds = Array.from(document.querySelectorAll("input[type=checkbox]:checked"))
                            .map(cb => cb.value);

    if (selectedIds.length === 0) {
        alert("Seleziona almeno un utente.");
        return;
    }

    try {
        const response = await fetch(`${API_URL}/${action}`, {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + token
            },
            body: "["+selectedIds.join(",")+"]"	
        });

        const data = await response.json();
        document.getElementById("output").textContent = JSON.stringify(data, null, 2);
        refreshUsers();

    } catch (error) {
        document.getElementById("output").textContent = "Errore durante la richiesta: " + error;
    }
}

async function refreshUsers() {
    const token = sessionStorage.getItem("token");
    if (!token) {
        document.getElementById("userTable").innerHTML = "<i>Inserisci il token per caricare gli utenti</i>";
        return;
    }

    try {
        const response = await fetch(`${API_URL}/getUsers`, {
            method: "GET",
            headers: { "Authorization": "Bearer " + token }
        });

        const utenti = await response.json();
        renderTable(utenti);

    } catch (error) {
        document.getElementById("userTable").textContent = "Errore durante la richiesta: " + error;
    }
}

function renderTable(utenti) {
    if (!Array.isArray(utenti) || utenti.length === 0) {
        document.getElementById("userTable").innerHTML = "Nessun utente trovato.";
        return;
    }

    let html = `<table border="1" class="table table-dark table-striped center">
        <tr>
            <th>Seleziona</th>
            <th>ID</th>
            <th>Username</th>
            <th>Nome</th>
            <th>Cognome</th>
            <th>Email</th>
            <th>Data Nascita</th>
            <th>Data Registrazione</th>
            <th>Tipo</th>
            <th>Bannato</th>
        </tr>`;

    utenti.forEach(u => {
        html += `<tr>
            <td><input type="checkbox" value="${u.idUtente}"></td>
            <td>${u.idUtente || ''}</td>
            <td>${u.username || ''}</td>
            <td>${u.nome || ''}</td>
            <td>${u.cognome || ''}</td>
            <td>${u.email || ''}</td>
            <td>${u.dataNascita || ''}</td>
            <td>${u.dataRegistrazione || ''}</td>
            <td>${u.tipo || ''}</td>
            <td>${u.utenteBannato ? 'SI' : 'NO'}</td>
        </tr>`;
    });
    html += "</table>";

    document.getElementById("userTable").innerHTML = html;
}