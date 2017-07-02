package app.peraza.view.activity;

import android.os.Bundle;

import app.peraza.view.activity.base.ParentActivity;
import app.peraza.view.fragment.SyncGuestFragment;

public class SyncGuestActivity extends ParentActivity {
    private SyncGuestFragment syncGuestFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(app.peraza.R.layout.activity_single_fragment);
        this.initializeActivity(savedInstanceState);
    }

    private void initializeActivity(Bundle savedInstanceState) {
        syncGuestFragment = new SyncGuestFragment();
        syncGuestFragment.setArguments(getIntent().getExtras());
        addFragment(app.peraza.R.id.fragment, syncGuestFragment);
    }
}
