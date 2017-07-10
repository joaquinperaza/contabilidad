var debuger;
var categorias;
var tablememory;

$('#epochto').on('dp.change', function (e) {
    getExpenses(document.getElementById("userbanner").innerHTML);
})
$('#epochfrom').on('dp.change', function (e) {
    getExpenses(document.getElementById("userbanner").innerHTML);
})


$(document).ready(function () {
    categorias = getCategory();
});



function getExpenses(usermail) {

    document.getElementById('desde').innerHTML = "Desde: " + document.getElementById('epochfrom').value;
    document.getElementById('hasta').innerHTML = "Hasta: " + document.getElementById('epochto').value;
    document.getElementById("username").innerHTML = usermail;
    var to = new Date(Date.parse($('#epochto').val()) + 86390000);
    var from = new Date(Date.parse($('#epochfrom').val()));
    var expenses;

    var user = usermail.replace(/[^A-Za-z]+/g, '');
    console.log(user);
    var query = firebase.database().ref('users/' + user + '/expenses').orderByKey();
    query.once("value")
        .then(function (snapshot) {
            debuger = snapshot;
            var expenses = [];
            snapshot.forEach(function (childSnapshot) {
                // key will be "ada" the first time and "alan" the second time
                // childData will be the actual contents of the child 
                childSnapshot.forEach(function (childSnapshot2) {
                    var cData = childSnapshot2;
                    var expense = {
                        id: childSnapshot.key,
                        amount: cData.child("amount").val(),
                        cotizado: cData.child("cotizado").val(),
                        moneda: cData.child("moneda").val(),
                        detalle: cData.child("note").val(),
                        fecha: cData.child("reportedWhen").val(),
                        categoria: categories[cData.child("categoryId").val()],
                    };
                    var check = new Date(expense.fecha);
                    if (check > from && check < to) {
                        expenses.push(expense);
                    }

                });

            });

            ///return

            var datos = categorizar(expenses);
            //     display(expenses);
             reportar(datos);
            
          
            
            // $("#data-table-basic").bootgrid("reload");

            /////////
        });

}
var categories = [];

function getCategory() {
    var query = firebase.database().ref("categories").orderByKey();
    query.once("value")
        .then(function (snapshot) {

            snapshot.forEach(function (childSnapshot) {
                var loc;
                if (childSnapshot.child("color").val() == -11751600) {
                    loc = "empresa";
                }
                if (childSnapshot.child("color").val() == -769226) {
                    loc = "particular";
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
            console.log(categories);
            getExpenses(document.getElementById("userbanner").innerHTML);
            return categories;
        });


}

function display(expenses) {
    console.log(expenses);
}




var oldtable = document.getElementById('tabla').innerHTML;

function sumar(part,empresa) {
    var tp=0;
    var te=0;
    function sp(gasto){
        tp+=gasto.data;
    }
    function se(gasto){
        te+=gasto.data;
    }
    part.forEach(sp);
    empresa.forEach(se);
    document.getElementById('particulargastos').innerHTML='Total: USD'+Math.round(tp).toString();
     document.getElementById('empresagastos').innerHTML='Total: USD'+Math.round(te).toString();
}
function reportar(categorias) {
    var tabla = document.getElementById('tabla');
    tabla.innerHTML = oldtable;
    var emph = tabla.insertRow(0);
    emph.insertCell(0).innerHTML = '<h5 class="t-uppercase f-400">Empresa</h5>';
    
 var emph2 = tabla.insertRow(1);
    emph2.insertCell(0).innerHTML = "";
     emph2.insertCell(1).innerHTML = "";
     emph2.insertCell(2).innerHTML = "";
    emph2.insertCell(3).innerHTML = '<h5 class="t-uppercase f-400" id="finalemp"></h5>';
    

    var ph = tabla.insertRow(-1);
    ph.insertCell(0).innerHTML = '<h5 class="t-uppercase f-400">Particulares</h5>';
    var particulares=0;
    var empresa=0;
   for(var cat in categorias) {
   item=categorias[cat];
        if (item.type == 'empresa') {
            var empe = tabla.insertRow(1);
             empe.insertCell(0).innerHTML = "";
             empe.insertCell(1).innerHTML = item.name;
             empe.insertCell(2).innerHTML = Math.round(item.amount);
            
            empresa+=item.amount;
        } else {
        
             var empe = tabla.insertRow(-1);
             empe.insertCell(0).innerHTML = "";
             empe.insertCell(1).innerHTML = item.name;
             empe.insertCell(2).innerHTML = Math.round(item.amount);
            
            particulares+=item.amount;
        }
    }
     var emph3 = tabla.insertRow(-1);
    emph3.insertCell(0).innerHTML = "";
     emph3.insertCell(1).innerHTML = "";
     emph3.insertCell(2).innerHTML = "";
    emph3.insertCell(3).innerHTML = '<h5 class="t-uppercase f-400" id="finalpar"></h5>';
    console.log(empresa+particulares);
    document.getElementById('eemp').innerHTML="$"+Math.round(empresa).toString();
    document.getElementById('epar').innerHTML="$"+Math.round(particulares).toString();
    document.getElementById('egresos').innerHTML="$"+Math.round(empresa+particulares).toString();
     document.getElementById('finalemp').innerHTML="$"+Math.round(empresa).toString();
    document.getElementById('finalpar').innerHTML="$"+Math.round(particulares).toString();
    
    
    
   



}


function categorizar(expenses) {
    var result = [];


    function sumarcat(item, index) {
        var expense = item;
        if (result[expense.categoria.id]) {
            result[expense.categoria.id] = {
                amount: result[expense.categoria.id].amount + parseFloat(expense.amount),
                type: expense.categoria.type,
                name: expense.categoria.name
            };
        } else {
            result[expense.categoria.id] = {
                amount: parseFloat(expense.amount),
                type: expense.categoria.type,
                name: expense.categoria.name
            };
        }
    }

    expenses.forEach(sumarcat);
      reportar(result);
    // console.log(result);
    return result;

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
