var ususario;


firebase.auth().onAuthStateChanged(function(user) {
  if (user) {
   usuario=user;
      notify('Hola '+user.email+'!', 'inverse');
      document.getElementById("userbanner").innerHTML = user.email;
           getUser();
        getExpenses(document.getElementById("userbanner").innerHTML);
       
  } else {
   redirect('auth.html');
  }
});

$( "#users" ).change(function() {
 document.getElementById("userbanner").innerHTML=$( "#users" ).val();
    getExpenses(document.getElementById("userbanner").innerHTML);
});
function getUser(){
        var query = firebase.database().ref("users").orderByKey();
    query.once("value")
        .then(function (snapshot) {
            var string='';
            snapshot.forEach(function (childSnapshot) {
                string+='<option>';
               string+=childSnapshot.key;
                string+='</option>';
    
                // key will be "ada" the first time and "alan" the second time

                // childData will be the actual contents of the child

            });
        document.getElementById('users').innerHTML=string;
            
        });

}


function redirect (url) {
    var ua        = navigator.userAgent.toLowerCase(),
        isIE      = ua.indexOf('msie') !== -1,
        version   = parseInt(ua.substr(4, 2), 10);

    // Internet Explorer 8 and lower
    if (isIE && version < 9) {
        var link = document.createElement('a');
        link.href = url;
        document.body.appendChild(link);
        link.click();
    }

    // All other browsers can use the standard window.location.href (they don't lose HTTP_REFERER like Internet Explorer 8 & lower does)
    else { 
        window.location.href = url; 
    }
}

   function notify(message, type){
        $.growl({
            message: message
        },{
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
