var debuger;
var categorias;
var tablememory;

$('#epochto').on('dp.change', function (e) {
    getExpenses(document.getElementById("userbanner").innerHTML);
})
$('#epochfrom').on('dp.change', function (e) {
    getExpenses(document.getElementById("userbanner").innerHTML);
})

function update(){
  
    $("#data-table-basic").bootgrid({
         rowCount:-1,
         formatters: {
                "ukdate": function(column, row)
                {
                    return moment.unix(parseInt(row[column.id])/1000).format("DD MMM YYYY");
                },
               
            },

                    css: {
                        icon: 'zmdi icon',
                        iconColumns: 'zmdi-view-module',
                        iconDown: 'zmdi-expand-more',
                        iconRefresh: 'zmdi-refresh',
                        iconUp: 'zmdi-expand-less'
                    }
                });
}
$(document).ready(function () {
    categorias = getCategory();
     tablememory=document.getElementById('tableRAM').innerHTML;
    
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
       //     display(expenses);
          //  graficar(datos);
         document.getElementById('tableRAM').innerHTML=tablememory;
            listarlast(expenses);
        update();
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

function listarlast(data) {
   
    function wormhole(worm) {
        var fecha = new Date(worm.fecha);
        var emp = false;
        if (worm.categoria.type == 'empresa') {
            emp = true;
        }
        addexp(Math.round(worm.amount), fecha.getTime(), worm.categoria.name, worm.detalle, emp);
       
    }
    var ordered = data.sort(function (a, b) {
        return parseFloat(a.fecha) - parseFloat(b.fecha);
    });

    var tableRef = document.getElementById('data-table-basic').getElementsByTagName('tbody')[0];
    while (tableRef.rows.length > 0) {
        tableRef.deleteRow(0);
    }
    ordered.forEach(wormhole)
    
    console.log('tabled');


}

function addexp(monto, fecha, categoria, detalle, emp) {
    var tableRef = document.getElementById('data-table-basic').getElementsByTagName('tbody')[0];
    var label;
    var row = tableRef.insertRow(0);
    var cell1 = row.insertCell(0);
    var cell2 = row.insertCell(1);
    var cell3 = row.insertCell(2);
    var cell4 = row.insertCell(3);
     var cell5 = row.insertCell(4);
    if (emp == true) {
        label = 'Empresa';
    } else {
       label = 'Particular';
    }
    var text1 = document.createTextNode(monto);
    cell1.appendChild(text1);
    var text2 = document.createTextNode(fecha);
    cell2.appendChild(text2);
    var text3 = document.createTextNode(categoria);
    cell3.appendChild(text3);
    var text4 = document.createTextNode(detalle);
    cell4.appendChild(text4);
    var text5 = document.createTextNode(label);
    cell5.appendChild(text5);
    
    
    


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
                color: '#' + (Math.random().toString(16) + "000000").substring(2, 8)
            };
            empresa.push(that);

        }
        if (item.type == 'particular') {
            var that = {
                data: item.amount,
                label: item.name,
                color: '#' + (Math.random().toString(16) + "000000").substring(2, 8)
            };
            particulares.push(that);
        }


    }
    //datos.forEach(parser);
    //console.log(empresa);
    // console.log(particulares);

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
            container: '#pie',
            backgroundOpacity: 0.5,
            noColumns: 0,
            backgroundColor: "white",
            lineWidth: 0
        },
        grid: {
            hoverable: true,
            clickable: true
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
            container: '#pie2',
            backgroundOpacity: 0.5,
            noColumns: 0,
            backgroundColor: "white",
            lineWidth: 0
        },
        grid: {
            hoverable: true,
            clickable: true
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
