window.onload = function() {
    var errorAlert = document.getElementById("errorAlert");
    if (errorAlert) {
        setTimeout(function() {
            errorAlert.style.display = 'none';
        }, 5000);
    }
};