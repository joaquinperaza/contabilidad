package app.peraza.view.activity.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import app.peraza.App;
import app.peraza.R;
import app.peraza.analytics.Analytics;
import app.peraza.di.component.AppComponent;
import app.peraza.impl.AppPreferences;
import app.peraza.utils.ResourceHelper;
import app.peraza.view.OutlayTheme;
import butterknife.ButterKnife;

/**
 * Created by Bogdan Melnychuk on 1/15/16.
 */
public class ParentActivity extends AppCompatActivity implements BaseActivity {
    private ResourceHelper resourceHelper;
    private OutlayTheme theme;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (appPreferences().getTheme() == AppPreferences.THEME_LIGHT) {
            setTheme(R.style.Theme_Outlay_Light);
        }
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
        setTitle(null);
    }

    @Override
    public View getRootView() {
        return findViewById(android.R.id.content);
    }

    @Override
    public OutlayTheme getOutlayTheme() {
        if (this.theme == null) {
            theme = new OutlayTheme(
                    this,
                    appPreferences().getTheme() == AppPreferences.THEME_DARK ? R.style.Theme_Outlay_Dark : R.style.Theme_Outlay_Light
            );
        }
        return theme;
    }

    @Override
    public App getApp() {
        return (App) getApplication();
    }

    @Override
    public AppComponent getApplicationComponent() {
        return getApp().getAppComponent();
    }

    @Override
    public Analytics analytics() {
        return getApplicationComponent().analytics();
    }

    public ResourceHelper getResourceHelper() {
        if (this.resourceHelper == null) {
            resourceHelper = new ResourceHelper(getOutlayTheme(), this);
        }
        return resourceHelper;
    }

    @Override
    public AppPreferences appPreferences() {
        return getApplicationComponent().appPreferences();
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }
}
