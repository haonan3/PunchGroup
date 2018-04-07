package edu.illinois.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.yinglan.alphatabs.AlphaTabsIndicator;

import java.util.ArrayList;
import java.util.List;



/**
 *There are four fragments in my app.
 *They are "Group" fragment, "Punch" fragment, "Plan"fragment and "Me"fragment
 **
 * In the "Group" fragment, all users can see other users' messages.
 * In the "Punch" fragment, users can punch in this fragment and add location information to their
 * punch message.
 * In the "Plan" fragment, each users can create their own plan. Everyone can only have one
 * current plan.
 *In the "Me" fragment, each users can browse their history punch messages and see how many
 * thumb ups they receive.
 **
 * There is a AlphaTabsIndicator component to allow users change page by clicking the tabs.
 * and there is a ViewPage to allow users change pages by life/right shift.
 * In this demo, four simple fragments will be put in the ViewPage.
 * https://github.com/yingLanNull/AlphaTabsIndicator
 */
public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Punch Group";
    int SIGN_IN_REQUEST_CODE;
    private AlphaTabsIndicator alphaTabsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.mViewPager);

        FragmentAdapter mainAdapter = new FragmentAdapter(getSupportFragmentManager());

        //this is a listener for page changing
        //load viewpager and add adapter
        viewPager.setAdapter(mainAdapter);
        viewPager.addOnPageChangeListener(mainAdapter);

        //load TabLayout into viewpager
        alphaTabsIndicator = (AlphaTabsIndicator) findViewById(R.id.alphaIndicator);
        alphaTabsIndicator.setViewPager(viewPager);
    }

    //get the result of user login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this,
                        "Successfully signed in. Welcome!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "We couldn't sign you in. Please try again later.", Toast.LENGTH_LONG)
                        .show();

                // Close the app
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                                            .build(), SIGN_IN_REQUEST_CODE);
                        }
                    });
        }
        return true;
    }

//    private void displayPunchMessages() {
//    }


    /**
     * This a Adapter for Fragment
     * The constructor helps MainActivity pass information
     * The onPageSelected method is for show new page and remove the old one
     */
    private class FragmentAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private List<Fragment> fragments = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
            fragments.add(TextFragment.newInstance());
            fragments.add(PushMessageFragment.newInstance());
            fragments.add(PlanFragment.newInstance());
            fragments.add(MyPageFragment.newInstance());
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //This method will be invoked when the current page is scrolled
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                alphaTabsIndicator.getTabView(0).showNumber(alphaTabsIndicator
                        .getTabView(0).getBadgeNumber() - 1);
            } else if (2 == position) {
                alphaTabsIndicator.getCurrentItemView().removeShow();
            } else if (3 == position) {
                alphaTabsIndicator.removeAllBadge();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
