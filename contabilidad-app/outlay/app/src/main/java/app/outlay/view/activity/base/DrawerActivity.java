package app.outlay.view.activity.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.outlay.Constants;
import app.outlay.R;
import app.outlay.domain.model.Expense;
import app.outlay.domain.model.User;
import app.outlay.view.activity.SingleFragmentActivity;
import app.outlay.view.alert.Alert;
import app.outlay.view.fragment.AboutFragment;
import app.outlay.view.fragment.AnalysisFragment;
import app.outlay.view.fragment.CategoriesFragment;
import app.outlay.view.fragment.ReportFragment;
import app.outlay.view.fragment.SettingsFragment;

/**
 * Created by bmelnychuk on 12/14/16.
 */

public abstract class DrawerActivity extends ParentActivity {

    private static final int ITEM_CATEGORIES = 1;
    private static final int ITEM_FEEDBACK = 2;
    private static final int ITEM_ABOUT = 3;
    private static final int ITEM_SING_OUT = 4;
    private static final int ITEM_CREATE_USER = 5;
    private static final int ITEM_ANALYSIS = 6;
    private static final int ITEM_SETTINGS = 7;
    private static final int ITEM_GET = 8;
    boolean sat=false;
    List<String> users= new ArrayList<>();


    private Drawer mainDrawer;
    static Long selectedDate = new Date().getTime();

    public void setupDrawer(User currentUser) {
        String email = TextUtils.isEmpty(currentUser.getEmail()) ? "Guest" : currentUser.getEmail();

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTextColor(getOutlayTheme().secondaryTextColor)
                .withAlternativeProfileHeaderSwitching(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withProfileImagesClickable(false)
                .withCloseDrawerOnProfileListClick(false)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withEmail(email)
                )
                .build();

        List<IDrawerItem> items = new ArrayList<>();
        if (currentUser.isAnonymous()) {
            items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_create_user).withIcon(MaterialDesignIconic.Icon.gmi_account_add).withIdentifier(ITEM_CREATE_USER));
        }
        Activity that =this;

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference remote = db.getReference("users/");
        remote.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (sat == false) {
                    Map<String, Object> td = (HashMap<String, Object>) dataSnapshot.getValue();
try {
    List<Object> values = new ArrayList<>(td.keySet());
    int i2 = 0;
    for (Object o : values) {
        if (!o.toString().equals(currentUser.getId())) {

            if (o.toString().hashCode() > 1 || o.toString().hashCode() < -1) {

                items.add(new PrimaryDrawerItem().withName(o.toString()).withIcon(MaterialDesignIconic.Icon.gmi_graphic_eq).withIdentifier(10 + i2));
                users.add(i2, o.toString());
                i2++;
            }
        }


    }
} catch (Exception e){ Log.e("err",e.toString());}
                    sat = true;
                    items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_analysis).withIcon(MaterialDesignIconic.Icon.gmi_trending_up).withIdentifier(ITEM_ANALYSIS));
                    items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_categories).withIcon(MaterialDesignIconic.Icon.gmi_apps).withIdentifier(ITEM_CATEGORIES));
                    items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_feedback).withIcon(MaterialDesignIconic.Icon.gmi_email).withIdentifier(ITEM_FEEDBACK));
                    items.add(new PrimaryDrawerItem().withName(R.string.menu_item_settings).withIcon(MaterialDesignIconic.Icon.gmi_settings).withIdentifier(ITEM_SETTINGS));
                    items.add(new PrimaryDrawerItem().withName(app.outlay.R.string.menu_item_signout).withIcon(MaterialDesignIconic.Icon.gmi_sign_in).withIdentifier(ITEM_SING_OUT));


                    ///////////

                    mainDrawer = new DrawerBuilder()
                            .withFullscreen(true)
                            .withActivity(that)
                            .withAccountHeader(headerResult)
                            .withSelectedItem(-1)
                            .addDrawerItems(items.toArray(new IDrawerItem[items.size()]))
                            .withOnDrawerItemClickListener((view, i, iDrawerItem) -> {
                                if (iDrawerItem != null) {
                                    int id = (int) iDrawerItem.getIdentifier();
                                    switch (id) {
                                        case ITEM_CATEGORIES:
                                            analytics().trackViewCategoriesList();
                                            SingleFragmentActivity.start(that, CategoriesFragment.class);
                                            break;
                                        case ITEM_ANALYSIS:
                                            analytics().trackAnalysisView();
                                            SingleFragmentActivity.start(that, AnalysisFragment.class);
                                            break;
                                        case ITEM_SETTINGS:
                                            SingleFragmentActivity.start(that, SettingsFragment.class);
                                            break;
                                        case ITEM_FEEDBACK:
                                            analytics().trackFeedbackClick();
                                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                                    "mailto", Constants.CONTACT_EMAIL, null));
                                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{Constants.CONTACT_EMAIL});
                                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Outlay Feedback");
                                            try {
                                                startActivity(Intent.createChooser(emailIntent, getString(app.outlay.R.string.label_send_email)));
                                            } catch (android.content.ActivityNotFoundException ex) {
                                                Alert.error(getRootView(), getString(app.outlay.R.string.error_no_email_clients));
                                            }
                                            break;
                                        case ITEM_ABOUT:
                                            SingleFragmentActivity.start(that, AboutFragment.class);
                                            break;
                                        case ITEM_SING_OUT:
                                            analytics().trackSingOut();
                                            signOut();
                                            break;
                                        case ITEM_CREATE_USER:
                                            createUser();
                                            break;
                                        case 10:
                                            reportOther(10);
                                            break;
                                        case 11:
                                            reportOther(11);
                                            break;
                                        case 12:
                                            reportOther(12);
                                            break;

                                    }

                                    mainDrawer.setSelection(-1);
                                    mainDrawer.closeDrawer();
                                }
                                return false;
                            })
                            .build();
                    /////////////
                    //notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });


    }

    protected abstract void signOut();

    void reportOther(int i)
    {   i= i-10;
        Bundle b = new Bundle();

        b.putLong(ReportFragment.ARG_DATE, selectedDate);
        b.putString(ReportFragment.setUser, users.get(i));
        Log.e("reporteduserother",users.get(i));
        SingleFragmentActivity.start(this, ReportFragment.class, b);
    }
    protected abstract void createUser();

    @Override
    public void onBackPressed() {
        if (mainDrawer != null && mainDrawer.isDrawerOpen()) {
            mainDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public Drawer getMainDrawer() {
        return mainDrawer;
    }
}
