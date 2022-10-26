window.addEventListener('load', () => {
    const infoForm = document.getElementById('infoForm');
    const photoForm = document.getElementById('photoForm');

    infoForm.addEventListener('submit', event => updateInfoAction(event));
    photoForm.addEventListener('submit', event => uploadPhotoAction(event));


    loadPokemon(getPokemonId());
});

/**
 *
 * @returns {number} character's ide from request path
 */
function getPokemonId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

/**
 * Fetches currently logged user's characters and updates edit form.
 * @param {number} id character's id
 */
function loadPokemon(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            for (const [key, value] of Object.entries(response)) {
                let input = document.getElementById(key);
                console.log(input);
                console.log(value);
                if (input) {
                    input.value = value;
                }
            }
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/pokemon/' + id, true);
    xhttp.send();
}

function getName(){
    return document.getElementById('name').value
}

function updateInfoAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            loadPokemon(getPokemonId());
        }
    };
    xhttp.open('PUT', getContextRoot() + '/api/pokemon/' + getPokemonId(), true);

    const request = {
        'name': document.getElementById('name').value,
        'specialAbility': document.getElementById('specialAbility').value,
        'power': parseInt(document.getElementById('power').value),
        'rarity': document.getElementById('rarity').value,
    };
    console.log(request);
    xhttp.send(JSON.stringify(request));
}

function uploadPhotoAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.open('PUT', getContextRoot() + '/api/pokemon/' + getName() + '/photo', true);

    let request = new FormData();
    request.append("photo", document.getElementById('photo').files[0]);
    console.log(request);
    xhttp.send(request);
}
