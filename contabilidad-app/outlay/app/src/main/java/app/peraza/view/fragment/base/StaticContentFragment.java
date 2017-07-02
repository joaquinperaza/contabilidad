package app.peraza.view.fragment.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import app.peraza.App;
import app.peraza.analytics.Analytics;
import app.peraza.di.component.AppComponent;
import app.peraza.impl.AppPreferences;
import app.peraza.utils.ResourceHelper;
import app.peraza.view.OutlayTheme;
import app.peraza.view.activity.base.BaseActivity;
import app.peraza.view.activity.base.ParentActivity;

/**
 * Created by Bogdan Melnychuk on 1/20/16.
 */
public class StaticContentFragment extends Fragment implements BaseFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setToolbar(Toolbar toolbar) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);
    }

    @Override
    public Analytics analytics() {
        return getAppComponent().analytics();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setDisplayHomeAsUpEnabled(boolean value) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(value);
    }

    public void setTitle(String value) {
        getActivity().setTitle(value);
    }

    public View getRootView() {
        return ((ParentActivity) getActivity()).getRootView();
    }

    @Override
    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public App getApp() {
        return getBaseActivity().getApp();
    }

    @Override
    public ResourceHelper getResourceHelper() {
        return getBaseActivity().getResourceHelper();
    }

    @Override
    public AppComponent getAppComponent() {
        return getBaseActivity().getApplicationComponent();
    }

    @Override
    public OutlayTheme getOutlayTheme() {
        return getBaseActivity().getOutlayTheme();
    }

    @Override
    public AppPreferences appPreferences() {
        return getBaseActivity().appPreferences();
    }
}
