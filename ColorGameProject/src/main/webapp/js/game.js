const APi_URL = 'http://localhost:8080/ColorGameProject/rest/gioco';

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById('createGameBtn').addEventListener('click', createNewGame);
    document.getElementById('continueGameBtn').addEventListener('click', continueGame);
    checkExistingGame();
});

function checkExistingGame() {
    const token = sessionStorage.getItem('token');
    if (!token) {
        alert('per giocare é necessarioo effettuare il login');
        window.location.href = "accesso.html";
        return;
    }
    try {
        const response = fetch(`${APi_URL}/checkPartita`, {
            method: 'GET',
            credentials: 'include',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        })
            .then(response => {
                if (response.ok) {
                    document.getElementById('createGameBtn').classList.remove('hidden');
                    document.getElementById('continueGameBtn').classList.remove('hidden');
                    return;
                } else if (response.status === 404) {
                    document.getElementById('createGameBtn').classList.remove('hidden');
                    return;
                } else {
                    console.log('Errore durante il controllo della partita:', response.status);
                }
            }
            )
    } catch (error) {
        console.error("Errore:", error);
    }
}

function createNewGame() {
    const token = sessionStorage.getItem('token');
    if (!token) {
        alert('per giocare é necessarioo effettuare il login');
        window.location.href = "accesso.html";
        return;
    }
    try {
        const response = fetch(`${APi_URL}/addPartita`, {
            method: 'POST',
            credenzials: 'include',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            }
        })
            .then(response => {
                if (response.ok) {
                    showContinueButton();
                    getPartita();
                    return;
                } else {
                    console.log('Errore durante la creazione della partita:', response.status);
                }
            })

    } catch (error) {
        console.error("Errore:", error);
    }
}

function showContinueButton() {
    document.getElementById('createGameBtn').classList.add('hidden');
    document.getElementById('continueGameBtn').classList.remove('hidden');
}

function showCreateButton() {
    document.getElementById('createGameBtn').classList.remove('hidden');
    document.getElementById('continueGameBtn').classList.add('hidden');
}

function getPartita() {
    const token = sessionStorage.getItem('token');
    if (!token) {
        alert('per giocare é necessarioo effettuare il login');
        window.location.href = "accesso.html";
        return;
    }
    try {
        const response = fetch(`${APi_URL}/getPartita`, {
            method: 'POST',
            credenzials: 'include',
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            }
        })
            .then(response => {
                if (response.ok) {
                    updateGameGrid(response);
                }
                else {
                    console.log('Errore durante il recupero della partita:', response.status);
                }
            })
    } catch (error) {
        console.error("Errore:", error);
    }
}

function updateGameGrid(response) {
    const colorGrid = document.querySelector('.color-grid');
    const score = document.getElementById('score');
    document.getElementById('createGameBtn').classList.add('hidden');
    document.getElementById('continueGameBtn').classList.add('hidden');

    response.json().then(partita => {
        const colorNormal = partita.colore;
        const targetX = partita.corX;
        const targetY = partita.corY;
        const colorDifferent = partita.coloreDiverso;
        const gridDimension = partita.dimGriglia;

        score.textContent = partita.punteggio;

        let gridHTML = '';

        colorGrid.style.gridTemplateColumns = `repeat(${gridDimension}, 50px)`;

        for (let y = 0; y < gridDimension; y++) {
            for (let x = 0; x < gridDimension; x++) {
                let cellHTML = `<div class="grid-cell" style="background-color: ${colorNormal};" data-x="${x}" data-y="${y}"></div>`;
                if (x === targetX && y === targetY) {
                    cellHTML = `<div class="grid-cell" style="background-color: ${colorDifferent};" data-x="${x}" data-y="${y}"></div>`;
                }

                gridHTML += cellHTML;
            }
        }

        colorGrid.innerHTML = gridHTML;

        const cells = document.querySelectorAll('.grid-cell');
        cells.forEach(cell => {
            cell.addEventListener('click', handleCellClick);
        });
    }).catch(error => {
        console.error("Errore durante la lettura della risposta JSON:", error);
    });
}


function handleCellClick(event) {
    const cell = event.target;
    const x = parseInt(cell.dataset.x);
    const y = parseInt(cell.dataset.y);

    checkAnswer(x, y);

}

async function checkAnswer(x, y) {
    const token = sessionStorage.getItem('token');
    if (!token) {
        alert('Per giocare è necessario effettuare il login');
        window.location.href = "accesso.html";
        return;
    }

    try {
        const response = await fetch(`${APi_URL}/checkAnswer`, {
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify({ x, y }),
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
            }
        });

        const data = await response.json();
        console.log(data);

        if (response.ok) {
            if (data.bool) {
                getPartita();
            } else {
                const colorGrid = document.querySelector('.color-grid');
                colorGrid.innerHTML = '';
                alert("Risposta sbagliata! Hai perso totalizzando un punteggio di: " + data.punteggio);
                showCreateButton();
            }
        } else {
            console.log('Errore durante il controllo della risposta:', response.status);
        }
    } catch (error) {
        console.error("Errore:", error);
    }
}

function continueGame() {
    getPartita();
}
