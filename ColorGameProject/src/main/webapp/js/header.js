document.addEventListener("DOMContentLoaded", function () {
    const navbarMenu = document.getElementById("headerMenu");
    const username = sessionStorage.getItem("username");
    const isAdmin = sessionStorage.getItem("tipo") === "admin";
    const profilePicBlob = sessionStorage.getItem("fotoProfilo");
    let profilePic;
    if (profilePicBlob) {
        const blob = new Blob([new Uint8Array(profilePicBlob)], { type: 'image/jpeg' });
        profilePic = URL.createObjectURL(blob);
    } else {
        profilePic = "../img/default-profile.png";
    }
    profilePic = "../img/default-profile.png";
    if (username) {
        navbarMenu.innerHTML = `
                  <div class="dropdown">
                      <button class="btn btn-secondary dropdown-toggle" type="button" id="userMenu" data-bs-toggle="dropdown">
                          <img src="${profilePic}" alt="Profilo" class="rounded-circle" width="40" height="40">
                      </button>
                      <ul class="dropdown-menu dropdown-menu-end">
                          <li><a class="dropdown-item" href="profilo.html">Profilo</a></li>
                          <li><a class="dropdown-item" href="gioco.html">Gioco</a></li>
                          <li><a class="dropdown-item" href="how-to-play.html">Come si gioca</a></li>
                          ${isAdmin ? '<li><a class="dropdown-item" href="admin-panel.html">Pannello Admin</a></li>' : ""}
                          <li><hr class="dropdown-divider"></li>
                          <li><a class="dropdown-item text-danger" onclick="Auth.logout()">Log Out</a></li>
                      </ul>
                  </div>
              `;
    } else {
        navbarMenu.innerHTML = `
                  <li class="nav-item"><a class="nav-link" href="accedi.html">Accedi</a></li>
                  <li class="nav-item"><a class="nav-link" href="registrati.html">Registrati</a></li>
              `;
    }
});