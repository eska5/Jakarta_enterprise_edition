window.addEventListener('load', () => {
    const urlParams = new URLSearchParams(window.location.search);
    loadType(urlParams.get('id'));
});

function loadType(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const response = JSON.parse(this.responseText);
            for (const [key, value] of Object.entries(response)) {
                const element = document.getElementById(key);
                if (element) {
                    updateElementText(element, value);
                }
            }
            document.getElementById('photo').src =(getContextRoot() + '/api/pokemon/' + id + '/photo');
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/pokemon/' + id, true);
    xhttp.send();
}

function updateElementText(element, text) {
    clearElementChildren(element);
    element.appendChild(document.createTextNode(text));
}
