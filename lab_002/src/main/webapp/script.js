window.addEventListener('load', () => {
});

function clearElementChildren(element) {
    while (element.firstChild) {
        element.removeChild(element.firstChild);
    }
}

function getContextRoot() {
    return '/' + location.pathname.split('/')[1];
}
