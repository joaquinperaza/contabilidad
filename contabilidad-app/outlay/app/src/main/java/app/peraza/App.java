package app.peraza;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import app.peraza.core.logger.LoggerFactory;
import app.peraza.di.component.AppComponent;
import app.peraza.di.component.DaggerAppComponent;
import app.peraza.di.component.UserComponent;
import app.peraza.di.module.AppModule;
import app.peraza.di.module.FirebaseModule;
import app.peraza.di.module.UserModule;
import app.peraza.domain.model.User;
import app.peraza.firebase.dto.adapter.UserAdapter;
import app.peraza.impl.AndroidLogger;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class App extends Application {
    private AppComponent appComponent;
    private UserComponent userComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeInjector();
        LoggerFactory.registerLogger(new AndroidLogger());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public UserComponent createUserComponent(User user) {
        userComponent = appComponent.plus(new UserModule(user));
        return userComponent;
    }

    public void releaseUserComponent() {
        userComponent = null;
    }

    private void initializeInjector() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .firebaseModule(new FirebaseModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public UserComponent getUserComponent() {
        return getUserComponent(true);
    }

    public UserComponent getUserComponent(boolean tryRecreate) {
        if (userComponent == null && tryRecreate) {
            FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
            if (fbUser != null) {
                User user = UserAdapter.fromFirebaseUser(fbUser);
                createUserComponent(user);
            }
        }
        return userComponent;
    }
}
