package app.peraza.di.component;

import app.peraza.di.module.UserModule;
import app.peraza.di.scope.UserScope;
import app.peraza.view.activity.MainActivity;
import app.peraza.view.fragment.AnalysisFragment;
import app.peraza.view.fragment.CategoriesFragment;
import app.peraza.view.fragment.CategoryDetailsFragment;
import app.peraza.view.fragment.ExpensesDetailsFragment;
import app.peraza.view.fragment.ExpensesListFragment;
import app.peraza.view.fragment.MainFragment;
import app.peraza.view.fragment.ReportFragment;

import app.peraza.view.fragment.SettingsFragment;
import dagger.Subcomponent;

/**
 * Created by bmelnychuk on 10/27/16.
 */

@UserScope
@Subcomponent(modules = {UserModule.class})
public interface UserComponent {
    void inject(CategoriesFragment fragment);

    void inject(CategoryDetailsFragment fragment);

    void inject(ReportFragment fragment);

    void inject(ExpensesListFragment fragment);

    void inject(ExpensesDetailsFragment fragment);

    void inject(MainActivity mainActivity);

    void inject(MainFragment mainFragment2);

    void inject(AnalysisFragment analysisFragment);

    void inject(SettingsFragment settingsFragment);
}
