package app.peraza.view.fragment.base;

import android.support.v7.widget.Toolbar;

import app.peraza.App;
import app.peraza.analytics.Analytics;
import app.peraza.di.component.AppComponent;
import app.peraza.impl.AppPreferences;
import app.peraza.utils.ResourceHelper;
import app.peraza.view.OutlayTheme;
import app.peraza.view.activity.base.BaseActivity;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseFragment {
    BaseActivity getBaseActivity();
    App getApp();
    AppComponent getAppComponent();
    void setToolbar(Toolbar toolbar);
    Analytics analytics();
    ResourceHelper getResourceHelper();
    OutlayTheme getOutlayTheme();
    AppPreferences appPreferences();
}
