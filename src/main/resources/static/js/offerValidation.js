function validateIntegerFields() {
    console.log("Skrypt walidacji załadowany.");
    var price = document.getElementById("price").value;
    var deposit = document.getElementById("deposit").value;

    var maxIntegerValue = 2147483647;
    var minIntegerValue = -2147483648;

    if (!isIntegerInRange(price, minIntegerValue, maxIntegerValue) ||
        !isIntegerInRange(deposit, minIntegerValue, maxIntegerValue)) {
        alert("Jedna lub więcej wprowadzonych wartości liczbowych jest nieprawidłowa.");
        return false;
    }

    return true;
}

function isIntegerInRange(value, min, max) {
    var number = parseInt(value);
    return !isNaN(number) && number >= min && number <= max;
}