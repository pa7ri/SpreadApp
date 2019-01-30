package com.ucm.informatica.spread.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ucm.informatica.spread.Fragments.HistorialFragment;
import com.ucm.informatica.spread.Fragments.HomeFragment;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.Fragments.ProfileFragment;
import com.ucm.informatica.spread.Fragments.SettingsFragment;
import com.ucm.informatica.spread.LocalWallet;
import com.ucm.informatica.spread.R;

import org.web3j.protocol.Web3j;

public class MainTabActivity extends AppCompatActivity {

    private LocalWallet localWallet;
    private Web3j web3j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initEthConnection();

        setContentView(R.layout.activity_main_tab);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main_tab, container, false);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new ProfileFragment();
                    break;
                case 2:
                    fragment = new HistorialFragment();
                    break;
                case 3:
                    fragment = new MapFragment();
                    break;
                default:
                    fragment = new SettingsFragment(); // create new instance (localWallet, web3j)
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

    }


    private void initEthConnection() {
        localWallet = new LocalWallet(this);
        web3j = localWallet.initWeb3j(getFilesDir().getAbsolutePath());
    }
}
