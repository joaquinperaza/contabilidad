package app.peraza.view.activity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import app.peraza.view.Navigator;
import app.peraza.view.activity.base.DrawerActivity;
import app.peraza.view.fragment.MainFragment;

public class MainActivity extends DrawerActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.peraza.R.layout.activity_single_fragment);

        Bundle b = getIntent().getExtras();
        mainFragment = new MainFragment();
        mainFragment.setArguments(b);
        addFragment(app.peraza.R.id.fragment, mainFragment);
    }

    @Override
    protected void signOut() {
        getApp().releaseUserComponent();
        FirebaseAuth.getInstance().signOut();
        Navigator.goToLoginScreen(this);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(mainFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void createUser() {
        Navigator.goToSyncGuestActivity(this);
    }
}
