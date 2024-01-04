window.onload = function() {
    var successAlert = document.getElementById("successAlert");
    if (successAlert) {
        setTimeout(function() {
            successAlert.style.display = 'none';
        }, 3000);
    }
};