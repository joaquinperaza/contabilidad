package app.peraza.mvp.presenter;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

import javax.inject.Inject;

import app.peraza.impl.AppPreferences;
import app.peraza.mvp.view.SettingsView;

/**
 * Created by Bogdan Melnychuk on 1/21/16.
 */
public class SettingsPresenter extends MvpBasePresenter<SettingsView> {
    private AppPreferences appPreferences;

    @Inject
    public SettingsPresenter(
            AppPreferences appPreferences
    ) {
        this.appPreferences = appPreferences;
    }

    public boolean updateTheme(int theme) {
        int currentTheme = appPreferences.getTheme();
        appPreferences.setTheme(theme);
        return currentTheme != theme;
    }

}
