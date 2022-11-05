window.addEventListener('load', () => {
    loadTrainerTypes();
    createTypeButton();
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
    name.appendChild(document.createTextNode(type.typeName));
    tr.appendChild(name);

    tr.appendChild(createLinkCell('view', '../type_view/type_view.html?id=' + type.typeName));

    tr.appendChild(createLinkCell('edit', '../type_edit/type_edit.html?id=' + type.typeName));

    tr.appendChild(createButtonCell('delete', () => {
        deleteType(type.typeName);
    }));

    return tr;
}

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

function createTypeButton() {

    let buttonDiv = document.getElementById('addButton');
    const button = document.createElement('button');
    button.appendChild(document.createTextNode("create new type"));
    button.classList.add('ui-control', 'ui-button');

    button.onclick = function () {
        window.open('../type_add/type_add.html','_self');
    }

    buttonDiv.appendChild(button);
}