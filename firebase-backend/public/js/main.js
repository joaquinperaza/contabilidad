var debuger;
var categorias;

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
            display(expenses);
            graficar(datos);
            listarlast(expenses);
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
          getExpenses(document.getElementById("userbanner").innerHTML);
            console.log(categories);
            return categories;
        });


}

function display(expenses) {
    console.log(expenses);
}
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

function listarlast(data) {
    function wormhole(worm) {
        var fecha = new Date(worm.fecha);
        var emp = false;
        if (worm.categoria.type == 'empresa') {
            emp = true;
        }
        addexp(Math.round(worm.amount), fecha.toLocaleDateString(), worm.categoria.name, worm.detalle, emp);
    }
    var ordered = data.sort(function (a, b) {
        return parseFloat(a.fecha) - parseFloat(b.fecha);
    });

    var tableRef = document.getElementById('lastexp').getElementsByTagName('tbody')[0];
    while (tableRef.rows.length > 0) {
        tableRef.deleteRow(0);
    }
    ordered.forEach(wormhole)
    console.log(ordered);


}

function addexp(monto, fecha, categoria, detalle, emp) {
    var tableRef = document.getElementById('lastexp').getElementsByTagName('tbody')[0];

    var row = tableRef.insertRow(0);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
    var text1 = document.createTextNode(monto);
    cell1.appendChild(text1);
    var text2 = document.createTextNode(fecha);
    cell2.appendChild(text2);
    var text3 = document.createTextNode(categoria);
    cell3.appendChild(text3);
    var text4 = document.createTextNode(detalle);
    cell4.appendChild(text4);
    if (emp == true) {
        cell1.className = 'f-500 c-green';
    } else {
        cell1.className = 'f-500 c-red';
    }


}

function graficar(datos) {
    var particulares = [];
    var empresa = [];
    for (var index in datos) {
        console.log(item);
        console.log(index);
        var item = datos[index];
        if (item.type == 'empresa') {
            var that = {
                data: item.amount,
                label: item.name,
                color: '#' + '00AA00'
            };
            empresa.push(that);

        }
        if (item.type == 'particular') {
            var that = {
                data: item.amount,
                label: item.name,
                color: '#' + 'EE0000'
            };
            particulares.push(that);
        }


    }
    //datos.forEach(parser);
    //console.log(empresa);
    // console.log(particulares);
    sumar(particulares,empresa);

    $.plot('#pie-chart', particulares, {
        series: {
            pie: {
                show: true,
                radius: 1,
                label: {
                    show: true,
                    radius: 2 / 3,
                    formatter: function (label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">' + label + '<br/>' + Math.round(series.data[0][1]) + '</div>';

                    },
                    threshold: 0.01
                }
            }
        },
        legend: {
             show: false
        },
       
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false,
            cssClass: 'flot-tooltip'
        }

    });
    $.plot('#pie-chart2', empresa, {
        series: {
            pie: {
                show: true,
                radius: 1,
                label: {
                    show: true,
                    radius: 2 / 3,
                    formatter: function (label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">' + label + '<br/>' + Math.round(series.data[0][1]) + '</div>';

                    },
                    threshold: 0.01
                }
            }
        },
        legend: {
             show: false
        },
        
        tooltip: true,
        tooltipOpts: {
            content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
            shifts: {
                x: 20,
                y: 0
            },
            defaultTheme: false,
            cssClass: 'flot-tooltip'
        }

    });



};




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
