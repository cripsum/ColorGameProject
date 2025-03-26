document.addEventListener("DOMContentLoaded", function () {
    fetch("/profile", { method: "GET" })
      .then((response) => response.json())
      .then((data) => {
        document.getElementById("profilePicture").src =
          data.fotoProfilo || "../img/default-profile.png";
        document.getElementById("username").textContent = data.username;
        document.getElementById("userId").textContent = data.idUtente;

        if (data.isOwnProfile) {
          document.getElementById("privateInfo").style.display = "block";
          document.getElementById("firstName").textContent = data.nome;
          document.getElementById("lastName").textContent = data.cognome;
          document.getElementById("email").textContent = data.email;
          document.getElementById("birthDate").textContent =
            data.dataNascita;
        }
      })
      .catch((error) =>
        console.error("Errore nel caricamento del profilo:", error)
      );
  });

  function openEditPopup() {
    document.getElementById("editPopup").style.display = "block";
    document.getElementById("editFirstName").value =
      document.getElementById("firstName").textContent;
    document.getElementById("editLastName").value =
      document.getElementById("lastName").textContent;
    document.getElementById("editEmail").value =
      document.getElementById("email").textContent;
    document.getElementById("editBirthDate").value =
      document.getElementById("birthDate").textContent;
  }

  function closeEditPopup() {
    document.getElementById("editPopup").style.display = "none";
  }

  function saveProfileChanges() {
    const updatedData = {
      nome: document.getElementById("editFirstName").value,
      cognome: document.getElementById("editLastName").value,
      email: document.getElementById("editEmail").value,
      dataNascita: document.getElementById("editBirthDate").value,
    };

    fetch("/updateProfile", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(updatedData),
    })
      .then((response) => response.json())
      .then((data) => {
        if (data.success) {
          document.getElementById("firstName").textContent =
            updatedData.nome;
          document.getElementById("lastName").textContent =
            updatedData.cognome;
          document.getElementById("email").textContent = updatedData.email;
          document.getElementById("birthDate").textContent =
            updatedData.dataNascita;
          alert("Informazioni aggiornate con successo!");
          closeEditPopup();
        } else {
          alert("Errore nell'aggiornamento delle informazioni.");
        }
      })
      .catch((error) =>
        console.error("Errore durante l'aggiornamento:", error)
      );
  }