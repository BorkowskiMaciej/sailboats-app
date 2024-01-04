document.addEventListener("DOMContentLoaded", function() {
    var password = document.getElementById("password");
    var confirmPassword = document.getElementById("confirmPassword");
    var validationMessage = document.getElementById("validationMessage");

    function validatePassword() {
        if (confirmPassword.value) {
            if (password.value !== confirmPassword.value) {
                validationMessage.textContent = "Hasła nie są identyczne.";
            } else {
                validationMessage.textContent = "";
            }
        } else {
            validationMessage.textContent = "";
        }
    }

    password.addEventListener("keyup", validatePassword);
    confirmPassword.addEventListener("keyup", validatePassword);
});

function validatePassword() {
    var newPassword = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirmPassword").value;

    if (newPassword !== confirmPassword) {
        alert("Hasła nie są identyczne.");
        return false;
    }
    return true;
}