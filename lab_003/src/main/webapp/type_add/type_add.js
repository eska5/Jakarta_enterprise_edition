window.addEventListener('load', () => {
    const infoForm = document.getElementById('infoForm');
    const portraitForm = document.getElementById('iconForm');

    infoForm.addEventListener('submit', event => createInfoAction(event));
    portraitForm.addEventListener('submit', event => uploadPortraitAction(event));

    getTypeId()
});
var pomoc="";

function getTypeId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

/**
 * Fetches currently logged user's characters and updates edit form.
 * @param {number} id character's id
 */
function loadType(id) {
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
    xhttp.open("GET", getContextRoot() + '/api/type/' + id, true);
    xhttp.send();
}

function createInfoAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            loadType(getTypeId());
        }
    };
    xhttp.open('POST', getContextRoot() + '/api/types' , true);

    const request = {
        'typeName': document.getElementById('typeName').value,
        'multiplier': parseInt(document.getElementById('multiplier').value),
        'combatList': document.getElementById('combatList').value
    };

    xhttp.send(JSON.stringify(request));
}

function uploadPortraitAction(event) {
    event.preventDefault();
    console.log(document.getElementById('typeName').value)
    const xhttp = new XMLHttpRequest();
    xhttp.open('PUT', getContextRoot() + '/api/type/' + document.getElementById('typeName').value + '/icon', true);

    let request = new FormData();
    request.append("icon", document.getElementById('icon').files[0]);
    console.log(request);
    xhttp.send(request);

}
