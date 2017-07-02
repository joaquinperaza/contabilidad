package app.peraza.firebase;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import app.peraza.domain.model.Credentials;
import app.peraza.domain.model.User;
import app.peraza.domain.repository.AuthService;
import app.peraza.firebase.dto.adapter.UserAdapter;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by bmelnychuk on 10/26/16.
 */

public class FirebaseAuthService implements AuthService {
    private FirebaseAuthRxWrapper firebaseWrapper;

    @Inject
    public FirebaseAuthService(FirebaseAuthRxWrapper firebaseWrapper) {
        this.firebaseWrapper = firebaseWrapper;
    }

    @Override
    public Observable<User> signIn(Credentials credentials) {
        return firebaseWrapper.signIn(credentials.getEmail(), credentials.getPassword())
                .map(authResult -> authResult.getUser())
                .map(firebaseUser -> UserAdapter.fromFirebaseUser(firebaseUser));
    }

    @Override
    public Observable<User> signUp(Credentials credentials) {
        return firebaseWrapper.signUp(credentials.getEmail(), credentials.getPassword())
                .map(authResult -> authResult.getUser())
                .map(firebaseUser -> UserAdapter.fromFirebaseUser(firebaseUser));
    }

    @Override
    public Observable<User> linkCredentials(Credentials credentials) {
        AuthCredential emailCredentials = EmailAuthProvider.getCredential(credentials.getEmail(), credentials.getPassword());
        return firebaseWrapper.linkAccount(emailCredentials)
                .map(authResult -> authResult.getUser())
                .map(firebaseUser -> UserAdapter.fromFirebaseUser(firebaseUser));
    }

    @Override
    public Observable<User> signInAnonymously() {
        return firebaseWrapper.signInAnonymously()
                .map(authResult -> authResult.getUser())
                .map(firebaseUser -> UserAdapter.fromFirebaseUser(firebaseUser));
    }

    @Override
    public Observable<Void> resetPassword(User user) {
        return firebaseWrapper.resetPassword(user);
    }
}
