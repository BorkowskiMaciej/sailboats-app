function validateForm() {
    if (!validateFileSize()) {
        return false;
    }
    if (!validateIntegerFields()) {
        return false;
    }
    return true;
}

function validateFileSize() {
    var fileInput = document.getElementById('file');
    var file = fileInput.files[0];

    if (file && file.size > 5 * 1024 * 1024) {
        alert('Plik jest za duży. Maksymalny rozmiar to 5 MB.');
        return false;
    }
    return true;
}

function validateIntegerFields() {
    var maxHeadcount = document.getElementById("maxHeadcount").value;
    var cabinsNumber = document.getElementById("cabinsNumber").value;
    var enginePower = document.getElementById("enginePower").value;

    var maxIntegerValue = 2147483647;
    var minIntegerValue = -2147483648;

    if (!isIntegerInRange(maxHeadcount, minIntegerValue, maxIntegerValue) ||
        !isIntegerInRange(cabinsNumber, minIntegerValue, maxIntegerValue) ||
        !isIntegerInRange(enginePower, minIntegerValue, maxIntegerValue)) {
        alert("Jedna lub więcej wprowadzonych wartości liczbowych jest nieprawidłowa.");
        return false;
    }
    return true;
}

function isIntegerInRange(value, min, max) {
    var number = parseInt(value);
    return !isNaN(number) && number >= min && number <= max;
}
