var debuger;
var categorias;
$("#brouxls").fileinput();
$('#brouxls').on("change", function(){ console.log(this);});
$( "#load" ).click(function() {
 load();
});

function load(){
    var lines = $('#dataplace').val().split('\n');
for(var i = 0;i < lines.length;i++){
    //code here using lines[i] which will give you each line
    if(lines[i].length){
        var line = lines[i];
       var words=line.split('	');
        if(words.length>3){
           if(moment(words[0], "DD/MM/YYYY").isValid() && parseInt((words[4].split('.').join("")).split(',').join("").slice(0,-2))>0){
               var monto =parseInt((words[4].split('.').join("")).split(',').join("").slice(0,-2));
            var fecha = moment(words[0], "DD/MM/YYYY");
            var id= (words[0].split('/').join(''))+words[2];
            var ingreso ={
                monto: monto,
                fecha: fecha.valueOf(),
                id: id
            }
      var userId = firebase.auth().currentUser.email;
      var user = userId.replace(/[^A-Za-z]+/g, '');
            firebase.database().ref('users/' + user+'/ingresos/'+id).set(ingreso);
           
           
           }
            
            
            
            
            
            
            
            
            
            
            
        }
       
        for(var j = 0;j < words.length;j++){
          var word = words[j];
        }
        
        
    }
    
}
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
