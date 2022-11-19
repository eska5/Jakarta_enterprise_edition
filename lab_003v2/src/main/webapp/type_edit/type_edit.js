window.addEventListener('load', () => {
    const infoForm = document.getElementById('infoForm');
    const portraitForm = document.getElementById('iconForm');

    infoForm.addEventListener('submit', event => updateInfoAction(event));
    portraitForm.addEventListener('submit', event => uploadPortraitAction(event));

    loadType(getTypeId());
});

function getTypeId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id');
}

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

function updateInfoAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            loadType(getTypeId());
        }
    };
    xhttp.open('PUT', getContextRoot() + '/api/type/' + getTypeId(), true);

    const request = {
        'typeName': document.getElementById('typeName').value,
        'multiplier': parseInt(document.getElementById('multiplier').value),
        'combatList': document.getElementById('combatList').value
    };

    xhttp.send(JSON.stringify(request));
}

function uploadPortraitAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.open('PUT', getContextRoot() + '/api/type/' + getTypeId() + '/icon', true);

    let request = new FormData();
    request.append("icon", document.getElementById('icon').files[0]);
    console.log(request);
    xhttp.send(request);

}
