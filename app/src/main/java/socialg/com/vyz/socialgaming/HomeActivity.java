package socialg.com.vyz.socialgaming;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import socialg.com.vyz.socialgaming.connection.UserInfo;
import socialg.com.vyz.socialgaming.fragment.FriendsFragment;
import socialg.com.vyz.socialgaming.fragment.GroupsFragment;
import socialg.com.vyz.socialgaming.fragment.NewsFragment;

public class HomeActivity extends AppCompatActivity implements FriendsFragment.OnFragmentInteractionListener, GroupsFragment.OnFragmentInteractionListener , NewsFragment.OnFragmentInteractionListener{

    private FrameLayout mainFrame;
    private BottomNavigationView bottomMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mainFrame = findViewById(R.id.main_frame_fragment);
        ((TextView)findViewById(R.id.username)).setText(UserInfo.getInstance().getPseudo());

        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        fragmentTransaction.replace(R.id.main_frame_fragment,newsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        bottomMenu = findViewById(R.id.bottom_navigation);
        bottomMenu.setSelectedItemId(R.id.news_action);
        bottomMenu.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected( MenuItem item) {
                        Intent intent;
                        FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                        switch (item.getItemId()) {
                            case R.id.news_action:
                                NewsFragment newsFragment = new NewsFragment();
                                fragmentTransaction2.replace(R.id.main_frame_fragment,newsFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break;
                            case R.id.groups_action:
                                GroupsFragment groupsFragment = new GroupsFragment();
                                fragmentTransaction2.replace(R.id.main_frame_fragment,groupsFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break;
                            case R.id.friends_action:
                                FriendsFragment friendsFragment = new FriendsFragment();
                                fragmentTransaction2.replace(R.id.main_frame_fragment,friendsFragment);
                                fragmentTransaction2.addToBackStack(null);
                                fragmentTransaction2.commit();
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
