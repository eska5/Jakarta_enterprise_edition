window.addEventListener('load', () => {
    const urlParams = new URLSearchParams(window.location.search);
    loadTypesPokemon(urlParams.get('id'));
    loadType(urlParams.get('id'));
    createPokemonButton(urlParams.get('id'));
});

/**
 * Fetches currently logged user's characters and displays them in table.
 * @param {number} id character's id
 */
function loadType(id) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            const response = JSON.parse(this.responseText);
            let tbody = document.getElementById('charactersTableBody');
            clearElementChildren(tbody);
            response.pokemons.forEach(pokemon => {
                tbody.appendChild(createPokemonRow(pokemon))
            })
            for (const [key, value] of Object.entries(response)) {
                const element = document.getElementById(key);
                console.log(key)
                if (element) {
                    updateElementText(element, value);
                }
            }
            document.getElementById('icon').src =(getContextRoot() + '/api/type/' + id + '/icon');
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/type/' + id, true);
    xhttp.send();
}

/**
 * Removes all children of the provided element and creates new text node inside it.
 * @param {HTMLElement} element parent element
 * @param {string} text text to be displayed
 */
function updateElementText(element, text) {
    clearElementChildren(element);
    element.appendChild(document.createTextNode(text));
}


// HERE STARTS THE VIEW POKEMON PART
function loadTypesPokemon(id) {
//    const tre = document.getElementById('sectiondel');
//
//    tre.appendChild(createLinkCell('add', '../pokemon_add/pokemon_add.html'));

    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            let tbody = document.getElementById('charactersTableBody');
            clearElementChildren(tbody);
            console.log(response);
            response.pokemons.forEach(pokemon => {
                tbody.appendChild(createPokemonRow(pokemon));
            })
            for (const [key, value] of Object.entries(response)) {
                const element = document.getElementById(key);
                console.log(key);
                if (element) {
                    updateElementText(element, value);
                }
            }
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/type/' + id, true);
    xhttp.send();
}

function createPokemonRow(pokemon) {

    const tr = document.createElement('tr');

    const name = document.createElement('td');
    name.appendChild(document.createTextNode(pokemon.name));
    tr.appendChild(name);

    tr.appendChild(createLinkCell('view', '../pokemon_view/pokemon_view.html?id=' + pokemon.name));

    tr.appendChild(createLinkCell('edit', '../pokemon_edit/pokemon_edit.html?id=' + pokemon.name));

    tr.appendChild(createButtonCell('delete', () => {
        deletePokemon(pokemon.name);
    }));

    return tr;
}

function deletePokemon(pokemon) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 204) {
            const urlParams = new URLSearchParams(window.location.search);
            loadType(urlParams.get('id'));
        }
    };
    xhttp.open("DELETE", getContextRoot() + '/api/pokemon/' + pokemon, true);
    xhttp.send();
}

function createLinkCell(text, url) {
    const td = document.createElement('td');
    const a = document.createElement('a');
    a.appendChild(document.createTextNode(text));
    a.href = url;
    td.appendChild(a);
    return td;
}


function createButtonCell(text, action) {
    const td = document.createElement('td');
    const button = document.createElement('button');
    button.appendChild(document.createTextNode(text));
    button.classList.add('ui-control', 'ui-button');
    td.appendChild(button);
    button.addEventListener('click', action);
    return td;
}

function createPokemonButton(name) {
    const urlParams = new URLSearchParams(window.location.search);
    loadType(urlParams.get('id'));

    let buttonDiv = document.getElementById('addButton');
    const button = document.createElement('button');
    button.appendChild(document.createTextNode("create new pokemon"));
    button.classList.add('ui-control', 'ui-button');

    button.onclick = function () {
        window.open('../pokemon_add/pokemon_add.html?id=' + name,"_self");
    }

    buttonDiv.appendChild(button);
}