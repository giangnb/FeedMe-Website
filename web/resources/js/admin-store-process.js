function restoreAddr() {
    var final = document.getElementById("form:addr-final").value;
    $("#addr-short").val(final.split("::")[0]);
    $("#addr-full").val(final.split("::")[1].replace("<br/>", "\n"));
}
function updateAddr() {
    var short = $("#addr-short").val();
    var full = $("#addr-full").val();
    if (!(short.length >= 3) || short.length > 32 || !(full.length >= 10)) {
        document.getElementById("form:addr-final").value = "";
    } else {
        short = short.replace("::", ":");
        full = full.replace("::", ":");
        full = full.replace(/[<][\/]?[^>]*[\/]?[>]/, "");
        full = full.replace("<", "&lt;");
        full = full.replace(">", "&gt;");
        full = full.replace("\n", "<br/>");
        document.getElementById("form:addr-final").value = short + "::" + full;
    }
}
restoreAddr();
function restoreDefaultLogo() {
    document.getElementById("form:img-logo-url").value = "./javax.faces.resource/img/logo.png.xhtml";
}
function restoreDefaultFav() {
    document.getElementById("form:img-fav-url").value = "./javax.faces.resource/img/favico.ico.xhtml";
}