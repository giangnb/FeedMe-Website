//document.getElementById('dlgOrderProcess:cart-form-submit').setAttribute("class", "hidden");

function validateCart() {
//    var name, phone, email, address, accept;
//    name = document.getElementById('dlgOrderProcess:cart-form-name').value;
//    phone = document.getElementById('dlgOrderProcess:cart-form-name').value;
//    email = document.getElementById('dlgOrderProcess:cart-form-email').value;
//    address = document.getElementById('dlgOrderProcess:cart-form-address').value;
//    accept = document.getElementById('dlgOrderProcess:cart-form-accpet').checked;
//    if (accept===null) {
//        accept = false;
//    }
//    if (validateCartFrom(name, phone, email, address, accept)) {
//        $('#cart-form-incomplete').hide();
//        document.getElementById('dlgOrderProcess:cart-form-submit').setAttribute("class", "btn btn-lg btn-warning");
//    } else {
//        $('#cart-form-incomplete').show();
//        document.getElementById('dlgOrderProcess:cart-form-submit').setAttribute("class", "hidden");
//    }
}

function validateCartFrom(name, phone, email, address, accept) {
    if (accept===null || !accept) {
        return false;
    }
    if (name.length>64 || name.length<5) {
        return false;
    }
    if (!phone.match(/^([(]?[\+]?[0-9]+[)]?)?[\s]?[0-9]{4,}$/)) {
        return false;
    }
    if (!email.match(/^[a-z0-9-_.]+[@]([a-z0-9-]+[.])+[a-z]+$/i)) {
        return false;
    }
    if (address.length<10) {
        return false;
    }
    return true;
}