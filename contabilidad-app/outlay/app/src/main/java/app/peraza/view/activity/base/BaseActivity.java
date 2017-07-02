package app.peraza.view.activity.base;

import android.view.View;

import app.peraza.App;
import app.peraza.analytics.Analytics;
import app.peraza.di.component.AppComponent;
import app.peraza.impl.AppPreferences;
import app.peraza.utils.ResourceHelper;
import app.peraza.view.OutlayTheme;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public interface BaseActivity {
    App getApp();
    View getRootView();
    OutlayTheme getOutlayTheme();
    AppComponent getApplicationComponent();
    Analytics analytics();
    ResourceHelper getResourceHelper();
    AppPreferences appPreferences();
}
