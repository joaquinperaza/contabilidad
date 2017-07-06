 document.querySelector('#blogin').addEventListener('click', function(e) {
      e.preventDefault();
      e.stopPropagation();
      var email = document.querySelector('#ulogin').value;
      var password = document.querySelector('#plogin').value
      var credential = firebase.auth.EmailAuthProvider.credential(email, password);
      var auth = firebase.auth();
      var currentUser = auth.currentUser;
      auth.signInWithCredential(credential).catch(function(error) {
  // Handle Errors here.
 console.log(error.code);
 console.log(error.message);
  // ...
});
      // Step 2
      //  Get a credential with firebase.auth.emailAuthProvider.credential(emailInput.value, passwordInput.value)
      //  If there is no current user, log in with auth.signInWithCredential(credential)
      //  If there is a current user an it's anonymous, atttempt to link the new user with firebase.auth().currentUser.link(credential) 
      //  The user link will fail if the user has already been created, so catch the error and sign in.
    });
    
   