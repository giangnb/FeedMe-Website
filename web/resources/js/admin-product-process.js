function openViewer(src) {
    $("#img-preview").attr("src", src);
    PF("dlgImagePreview").show();
}
var newImagesUrl = "";
function addNewImageUrl() {
    var p = prompt("URL hình ảnh (đường dẫn đầy đủ)", "");
    if (p !== null) {
        omitNewImagesUrl();
        newImagesUrl += "[url]"+p + ";";
        changeNewImageUrl();
        updateUrlList();
    }
}

function updateUrlList() {
    var ctx = "";
    var str = newImagesUrl.split(";");
    for (var i=0; i<str.length; i++) {
        if (str[i].length>0) {
            ctx+="<li>"+str[i].substr(str[i].length-30)+"&nbsp;&nbsp;&nbsp;<small><a onclick=\"removeImageUrl('"+str[i]+"')\">Xóa</a></small></li>";
        }
    }
    $("#addedUrls").html(ctx);
}

function removeImageUrl(str) {
    if (confirm("Bạn có chắc chắn muốn xóa hình ảnh này?")) {
        omitNewImagesUrl();
        newImagesUrl = newImagesUrl.replace(str+";","");
        changeNewImageUrl();
        updateUrlList();
    }
}

function omitNewImagesUrl() {
    if (document.getElementById("form:newProdImg").value !== null)
        document.getElementById("form:newProdImg").value
                = document.getElementById("form:newProdImg").value.replace(newImagesUrl, "");
}

function changeNewImageUrl() {
    var val = document.getElementById("form:newProdImg").value;
    omitNewImagesUrl();
    document.getElementById("form:newProdImg").value = val + newImagesUrl;
}