window.addEventListener('load', () => {
    const infoForm = document.getElementById('infoForm');
    const portraitForm = document.getElementById('photoForm');

    infoForm.addEventListener('submit', event => createInfoAction(event));
    portraitForm.addEventListener('submit', event => uploadPortraitAction(event));

    updateTypeName()
});

/**
 *
 * @returns {number} character's ide from request path
 */
function updateTypeName() {
    const urlParams = new URLSearchParams(window.location.search);
    const element = document.getElementById("id");
//    clearElementChildren(element);
//    element.appendChild(document.createTextNode(urlParams.get('id')));
}

function getName(){
    return document.getElementById('name').value
}

function createInfoAction(event) {
    event.preventDefault();
    const urlParams = new URLSearchParams(window.location.search);
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            alert("New Pokemon added !!!")
        }
    };
    xhttp.open('POST', getContextRoot() + '/api/pokemons' , true);

    const request = {
        'name': document.getElementById('name').value,
        'specialAbility': document.getElementById('specialAbility').value,
        'power': parseInt(document.getElementById('power').value),
        'rarity': document.getElementById('rarity').value,
        'typeName': urlParams.get('id')


    };

    xhttp.send(JSON.stringify(request));
}

/**
 * Action event handled for uploading character portrait.
 * @param {Event} event dom event
 */
function uploadPortraitAction(event) {
    event.preventDefault();

    const xhttp = new XMLHttpRequest();
    xhttp.open('PUT', getContextRoot() + '/api/pokemon/' + getName() + '/photo', true);

    let request = new FormData();
    request.append("photo", document.getElementById('photo').files[0]);
    console.log(request);
    xhttp.send(request);

}
