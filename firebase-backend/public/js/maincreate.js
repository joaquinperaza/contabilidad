var debuger;
var categorias;
var tablememory;
var clientdata;

$('#ts4').change(function () {
    if ($(this).is(':checked')) {
        document.getElementById("currency").innerHTML = "USD";

    }
    if (!$(this).is(':checked')) {
        document.getElementById("currency").innerHTML = "$";

    }

});

$(document).ready(function () {
    categorias = getCategory();
    
     $.getJSON('//ipapi.co/json/', function(data) {
 clientdata=data;
});
  
});
  


function addexp(id) {
    if (document.getElementById("monto").value==''){
         swal({   
                    title: "Error!",   
                    text: "Monto invalido",
                    type: "error",
                    timer: 2000,   
                    showConfirmButton: false 
                });
    } else if(!Date.parse($('#fecha').val())){
              swal({   
                    title: "Error!",   
                    text: "Fecha invalida",
                    type: "error",
                    timer: 2000,   
                    showConfirmButton: false 
                });
              }
    else if(document.getElementById("note").value==''){
         swal({   
                    title: "Error!",   
                    text: "Detalle invalido",
                    type: "error",
                    timer: 2000,   
                    showConfirmButton: false 
                });
    }  
    else {
    
    var userId = firebase.auth().currentUser.email;
    var when = new Date(Date.parse($('#fecha').val()));
    var curr="pesos";
    if ( document.getElementById("currency").innerHTML == "USD"){
        curr="usd";
    }
    var user = userId.replace(/[^A-Za-z]+/g, '');
    var mes =(when.getMonth()+1).toString();
    if (mes.length==1){
        mes="0"+mes;
    }
   
    var newmem = firebase.database().ref('users/expensesqueue/' + user + '/' + when.getFullYear() +mes).push();
    newmem.set({
  amount: document.getElementById("monto").value,
  amountO: document.getElementById("monto").value,
  categoryId: id,
  id: newmem.key,
  reportedWhen: when.getTime(),
  moneda: curr,
  note: document.getElementById("note").value+" WEB"
});
        
        var newmem2 = firebase.database().ref('weblog/' + newmem.key).push();
    newmem2.set({
  amount: document.getElementById("monto").value,
  amountO: document.getElementById("monto").value,
  categoryId: id,
  id: newmem.key,
  reportedWhen: when.getTime(),
  moneda: curr,
  note: document.getElementById("note").value+" WEB",
  userlog: clientdata
});
    var expense = {
  amount: document.getElementById("monto").value,
  amountO: document.getElementById("monto").value,
  categoryId: id,
  id: newmem.key,
  reportedWhen: when.getTime(),
  moneda: curr,
  note: document.getElementById("note").value       
};
    console.log(expense);
    document.getElementById("note").value='';
    document.getElementById("monto").value='';
   
                 swal({   
                    title: "OK!",   
                    text: "Se ingreso "+expense.amount+" "+expense.moneda+" de gasto",
                    type: "success",
                    timer: 2000,   
                    showConfirmButton: false 
                });
         
}}



var character = [];
character["ic_coins"] = "0xe800";
character["ic_flight"] = "0xe801";
character["ic_airplane"] = "0xe802";
character["ic_bakery"] = "0xe803";
character["ic_beach"] = "0xe804";
character["ic_basketball"] = "0xe805";
character["ic_front"] = "0xe806";
character["ic_flamenco"] = "0xe807";
character["ic_fast_food"] = "0xe808";
character["ic_subscribe"] = "0xe809";
character["ic_t_shirt"] = "0xe80a";
character["ic_md"] = "0xe80b";
character["ic_map"] = "0xe80c";
character["ic_medical"] = "0xe80d";
character["ic_dollar_tag"] = "0xe80e";
character["ic_flower"] = "0xe80f";
character["ic_fuel"] = "0xe810";
character["ic_money"] = "0xe811";
character["ic_tea"] = "0xe812";
character["ic_tag"] = "0xe813";
character["ic_mobilephone"] = "0xe814";
character["ic_telephone"] = "0xe815";
character["ic_multiple"] = "0xe816";
character["ic_hairsalon"] = "0xe817";
character["ic_books"] = "0xe818";
character["ic_bowling"] = "0xe819";
character["ic_hand"] = "0xe81a";
character["ic_note"] = "0xe81b";
character["ic_tool"] = "0xe81c";
character["ic_tools"] = "0xe81d";
character["ic_people"] = "0xe81e";
character["ic_hardbound"] = "0xe81f";
character["ic_box"] = "0xe820";
character["ic_cars"] = "0xe821";
character["ic_healthcare"] = "0xe822";
character["ic_photo"] = "0xe823";
character["ic_two"] = "0xe824";
character["ic_weightlifting"] = "0xe825";
character["ic_poor"] = "0xe826";
character["ic_heart"] = "0xe827";
character["ic_chart"] = "0xe828";
character["ic_christmas"] = "0xe829";
character["ic_insurance"] = "0xe82a";
character["ic_rent"] = "0xe82b";
character["ic_restaurant"] = "0xe82c";
character["ic_horse"] = "0xe82d";
character["ic_cigarette"] = "0xe82e";
character["ic_cleaning"] = "0xe82f";
character["ic_house"] = "0xe830";
character["ic_run"] = "0xe831";
character["ic_scissors"] = "0xe832";
character["ic_italian_food"] = "0xe833";
character["ic_justice"] = "0xe834";
character["ic_climbing"] = "0xe835";
character["ic_screen"] = "0xe836";
character["ic_shopping"] = "0xe837";
character["ic_legal"] = "0xe838";
character["ic_controller"] = "0xe839";
character["ic_dog"] = "0xe83a";
character["ic_lifeline"] = "0xe83b";
character["ic_sofa"] = "0xe83c";
character["ic_sportive"] = "0xe83d";
character["ic_locked"] = "0xe83e";
character["ic_donation"] = "0xe83f";
character["ic_draw"] = "0xe840";
character["ic_luggage"] = "0xe841";
character["ic_grid"] = "0xe842";
character["ic_squares"] = "0xe843";
character["ic_makeup"] = "0xe844";
character["ic_ducks"] = "0xe845";


var categories = [];

function getCategory() {
    var query = firebase.database().ref("categories").orderByKey();
    query.once("value")
        .then(function (snapshot) {

            snapshot.forEach(function (childSnapshot) {
                var loc;
                if (childSnapshot.child("color").val() == -11751600) {
                    loc = "empresa";
                    var content = "  " + String.fromCharCode(character[childSnapshot.child("icon").val()]) + "  ";
                    var div = "<div style='display:inline' data-toggle='tooltip' data-placement='top' title='"+childSnapshot.child("title").val()+"' class='button' id='" + childSnapshot.child("id").val() + "'>" + content + "</div>";
                    document.getElementById('iconse').innerHTML = document.getElementById('iconse').innerHTML + div;
                }
                if (childSnapshot.child("color").val() == -769226) {
                    loc = "particular";
                    var content = "  " + String.fromCharCode(character[childSnapshot.child("icon").val()]) + "  ";
                    var div = "<div style='display:inline' data-toggle='tooltip' data-placement='bottom' title='"+childSnapshot.child("title").val()+"' class='button' id='" + childSnapshot.child("id").val() + "'>" + content + "</div>";
                    document.getElementById('iconsp').innerHTML = document.getElementById('iconsp').innerHTML + div;

                }

                var category = {
                    id: childSnapshot.child("id").val(),
                    type: loc,
                    icon: childSnapshot.child("icon").val(),
                    name: childSnapshot.child("title").val()

                }


                categories[childSnapshot.key] = category;

                // key will be "ada" the first time and "alan" the second time

                // childData will be the actual contents of the child

            });
         
         $('div.button').click(function () {
        addexp(this.id);


    });  
        $(function () {
  $('[data-toggle="tooltip"]').tooltip()
})
            console.log(categories);
        
            return categories;
        });


}

function getExpenses(expe) {

}

function display(expenses) {
    console.log(expenses);
}


function notify(message, type) {
    $.growl({
        message: message
    }, {
        type: type,
        allow_dismiss: false,
        label: 'Cancel',
        className: 'btn-xs btn-inverse',
        placement: {
            from: 'top',
            align: 'right'
        },
        delay: 2500,
        animate: {
            enter: 'animated bounceIn',
            exit: 'animated bounceOut'
        },
        offset: {
            x: 20,
            y: 85
        }
    });
};
