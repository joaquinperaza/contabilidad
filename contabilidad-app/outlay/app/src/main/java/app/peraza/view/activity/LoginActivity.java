package app.peraza.view.activity;

import android.os.Bundle;

import app.peraza.view.activity.base.ParentActivity;
import app.peraza.view.fragment.LoginFragment;

public class LoginActivity extends ParentActivity {
    private LoginFragment loginFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.peraza.R.layout.activity_single_fragment);
        this.initializeActivity(savedInstanceState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        loginFragment = new LoginFragment();
        loginFragment.setArguments(getIntent().getExtras());
        addFragment(app.peraza.R.id.fragment, loginFragment);
    }
}
