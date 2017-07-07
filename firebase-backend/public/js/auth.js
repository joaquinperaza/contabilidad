 var err;
document.querySelector('#blogin').addEventListener('click', function(e) {
   authenticate();
    
      //  The user link will fail if the user has already been created, so catch the error and sign in.
    });
    function authenticate(){
          
      var email = document.querySelector('#ulogin').value;
      var password = document.querySelector('#plogin').value;
      var credential = firebase.auth.EmailAuthProvider.credential(email, password);
      var auth = firebase.auth();
      var currentUser = auth.currentUser;
      err = 55;
      auth.signInWithCredential(credential).catch(function(error) {
             err=1;
  // Handle Errors here.
 console.log(error.code);
 console.log(error.message);
      if(error.code){  notify('Fallo el inicio de sesión, revise sus datos', 'inverse');
 }
      
          
  // ...
});
        
         notify('Autenticando...', 'inverse');
        

firebase.auth().onAuthStateChanged(function(user) {
  if (user) {       notify('Autenticado!', 'inverse');
   redirect('index.html')
  } else {
   //    notify('Fallo el inicio de sesión, revise sus datos', 'inverse');
 
  
  }
});

    
   

       
    
    }
   
      //Welcome Message (not for login page)
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