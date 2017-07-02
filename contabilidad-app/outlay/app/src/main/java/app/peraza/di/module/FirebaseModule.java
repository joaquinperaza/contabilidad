package app.peraza.di.module;

import com.google.firebase.auth.FirebaseAuth;
import app.peraza.domain.repository.AuthService;
import app.peraza.firebase.FirebaseAuthRxWrapper;
import app.peraza.firebase.FirebaseAuthService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bmelnychuk on 2/8/17.
 */
@Module
public class FirebaseModule {
    @Provides
    @Singleton
    public AuthService provideOutlayAuth(
            FirebaseAuthRxWrapper firebaseRxWrapper
    ) {
        return new FirebaseAuthService(firebaseRxWrapper);
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth(
    ) {
        return FirebaseAuth.getInstance();
    }
}
