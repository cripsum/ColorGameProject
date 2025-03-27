document.addEventListener("DOMContentLoaded", function () {
    fetchClassifica();
});

const API_URL = "http://localhost:8080/ColorGameProject/rest";

async function fetchClassifica() {
    try {
        const response = await fetch(`${API_URL}/gioco/getClassifica`, {
            method: "GET",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
        });
        if (!response.ok) {
            throw new Error(response.json().messaggio);
        }
        
        const classifica = await response.json();
        popolaTabella(classifica);
    } catch (error) {
        console.error("Errore:", error);
        document.getElementById("classificaContainer").innerHTML = "<p>Errore nel caricamento della classifica.</p>";
    }
}

function popolaTabella(classifica) {
    const tbody = document.getElementById("classificaBody");
    tbody.innerHTML = ""; // Pulisce la tabella prima di aggiungere nuovi dati
    let posizione = 1;
    classifica.forEach(record => {
        const row = document.createElement("tr");
        row.innerHTML = 
        `
            <td>${posizione++}</td>
            <td>${record.username}</td>
            <td>${record.punteggio}</td>
        `;
        row.addEventListener("click", function () {
            window.location.href = `profilo.html?idUtente=${record.id}`;
        });

        tbody.appendChild(row);
    });
}