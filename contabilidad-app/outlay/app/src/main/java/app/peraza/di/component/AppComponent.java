package app.peraza.di.component;

import android.content.Context;

import app.peraza.analytics.Analytics;
import app.peraza.di.module.AppModule;
import app.peraza.di.module.FirebaseModule;
import app.peraza.di.module.UserModule;
import app.peraza.impl.AppPreferences;
import app.peraza.view.activity.base.ParentActivity;
import app.peraza.view.activity.LoginActivity;
import app.peraza.view.fragment.LoginFragment;
import app.peraza.view.fragment.SyncGuestFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Bogdan Melnychuk on 12/17/15.
 */
@Singleton
@Component(modules = {AppModule.class, FirebaseModule.class})
public interface AppComponent {
    UserComponent plus(UserModule userModule);

    Context getApplication();

    Analytics analytics();

    AppPreferences appPreferences();

    void inject(LoginActivity loginActivity);

    void inject(ParentActivity staticContentActivity);

    void inject(LoginFragment loginFragment);

    void inject(SyncGuestFragment syncGuestFragment);
}
