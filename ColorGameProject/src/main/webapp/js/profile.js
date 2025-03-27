const API_URL = "http://localhost:8080/ColorGameProject/rest";

document.addEventListener("DOMContentLoaded", profilo());

async function profilo() {
  try {
    const urlParams = new URLSearchParams(window.location.search);
    const idUtente = urlParams.get("idUtente") || sessionStorage.getItem("idUtente");
    const token = sessionStorage.getItem("token");

    let response = null;

    if (idUtente && idUtente === sessionStorage.getItem("idUtente")) {
      if (!token) {
        console.error("Token non trovato, impossibile caricare il profilo privato.");
        return;
      }
      response = await fetch(`${API_URL}/userProfile/getProfile`, {
        method: "GET",
        credentials: "include",
        headers: { 
          "Content-Type": "application/json", 
          "Authorization": `Bearer ${token}` },
      });
    } else {
      response = await fetch(`${API_URL}/userProfile/getPublicProfile?idUtente=${idUtente}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" },
      });
    }

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.messaggio || "Errore sconosciuto");
    }

    const data = await response.json();

    document.getElementById("profilePicture").src = data.fotoProfilo || "../img/default-profile.png";
    document.getElementById("username").textContent = data.username;
    document.getElementById("userId").textContent = data.idUtente;

    if (idUtente === sessionStorage.getItem("idUtente")) {
      document.getElementById("privateInfo").style.display = "block";
      document.getElementById("firstName").textContent = data.nome;
      document.getElementById("lastName").textContent = data.cognome;
      document.getElementById("email").textContent = data.email;
      document.getElementById("birthDate").textContent = data.dataNascita;
    }

  } catch (error) {
    console.error("Errore:", error.message);
  }
}

function openEditPopup() {
  document.getElementById("editPopup").style.display = "block";
  document.getElementById("editUsername").placeholder = document.getElementById("username").textContent;
  document.getElementById("editFirstName").placeholder = document.getElementById("firstName").textContent;
  document.getElementById("editLastName").placeholder = document.getElementById("lastName").textContent;
  document.getElementById("editEmail").placeholder = document.getElementById("email").textContent;
  document.getElementById("editBirthDate").placeholder = document.getElementById("birthDate").textContent;
}

function closeEditPopup() {
  document.getElementById("editPopup").style.display = "none";
}

function saveProfileChanges() {
  const token = sessionStorage.getItem("token");

  const updatedData = {};

  if (document.getElementById("editUsername").value!== "") {
    updatedData.username = document.getElementById("editUsername").value;
  }
  
  if (document.getElementById("editPassword").value !== "") {
    updatedData.password = document.getElementById("editPassword").value;
  }
  
  if (document.getElementById("editFirstName").value !== "") {
    updatedData.nome = document.getElementById("editFirstName").value;
  }
  
  if (document.getElementById("editLastName").value !== "") {
    updatedData.cognome = document.getElementById("editLastName").value;
  }
  
  if (document.getElementById("editEmail").value !== "") {
    updatedData.email = document.getElementById("editEmail").value;
  }
  
  if (document.getElementById("editBirthDate").value !== "") {
    updatedData.dataNascita = document.getElementById("editBirthDate").value;
  }

  const response = fetch(`${API_URL}/userProfile/updateProfile`, {
    method: "POST",
    credentials: "include",
    headers: { "Content-Type": "application/json", "Authorization": `Bearer ${token}` },
    body: JSON.stringify(updatedData),
  });
  
  alert("Profilo aggiornato con successo! riloggati per rendere effettive le modifiche.");
  location.reload();
}