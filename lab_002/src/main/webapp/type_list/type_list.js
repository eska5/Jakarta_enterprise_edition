window.addEventListener('load', () => {
    loadTrainerTypes();
});

function createButtonCell(text, action) {
    const td = document.createElement('td');
    const button = document.createElement('button');
    button.appendChild(document.createTextNode(text));
    button.classList.add('ui-control', 'ui-button');
    td.appendChild(button);
    button.addEventListener('click', action);
    return td;
}


function createLinkCell(text, url) {
    const td = document.createElement('td');
    const a = document.createElement('a');
    a.appendChild(document.createTextNode(text));
    a.href = url;
    td.appendChild(a);
    return td;
}


function deleteType(type) {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 204) {
            loadTrainerTypes()
        }
    };
    xhttp.open("DELETE", getContextRoot() + '/api/type/' + type, true);
    xhttp.send();
}

function createTypeRow(type) {
    const tr = document.createElement('tr');

    const name = document.createElement('td');
    name.appendChild(document.createTextCell(type.typeName));
    tr.appendChild(name);
    name.appendChild(document.createTextNode(type.name));
    tr.appendChild(name);

    tr.appendChild(createLinkCell('view', '../character_view/character_view.html?id=' + character.id));

    tr.appendChild(createLinkCell('edit', '../character_edit/character_edit.html?id=' + character.id));

    tr.appendChild(createButtonCell('delete', () => {
        deleteCharacter(character.id);
    }));

    return tr;
}

/**
 * Fetches currently logged user's characters and displays them in table.
 */
function loadTrainerTypes() {
    const xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            let response = JSON.parse(this.responseText);
            let tbody = document.getElementById('charactersTableBody');
            clearElementChildren(tbody);
            response.types.forEach(type => {
                tbody.appendChild(createTypeRow(type));
            })
        }
    };
    xhttp.open("GET", getContextRoot() + '/api/types', true);
    xhttp.send();
}
