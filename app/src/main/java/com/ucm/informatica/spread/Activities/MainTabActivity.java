package com.ucm.informatica.spread.Activities;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tuyenmonkey.mkloader.MKLoader;
import com.ucm.informatica.spread.NameContract;
import com.ucm.informatica.spread.Presenter.MainTabPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.MainTabView;
import com.ucm.informatica.spread.ViewPagerAdapter;
import com.ucm.informatica.spread.ViewPagerTab;

import static com.ucm.informatica.spread.Constants.NUMBER_TABS;

public class MainTabActivity extends AppCompatActivity implements MainTabView{


    private MainTabPresenter presenter;

    private RelativeLayout relativeLayout;

    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_profile,
            R.drawable.ic_historial,
            R.drawable.ic_map,
            R.drawable.ic_settings
    };
    private int[] tabNames = {
            R.string.tab_text_home,
            R.string.tab_text_profile,
            R.string.tab_text_historial,
            R.string.tab_text_map,
            R.string.tab_text_settings
    };

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        presenter = new MainTabPresenter(this, this);
        presenter.start(getFilesDir().getAbsolutePath());
    }

    @Override
    public void initViewContent(){
        ViewPagerTab mViewPager =  findViewById(R.id.container);
        mViewPager.setMotionEventSplittingEnabled(false);
        setupViewPager(mViewPager);

        tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        tabLayout.setupWithViewPager(mViewPager);

        setupTabContent();
    }

    @Override
    public void showLoading() {
       relativeLayout=findViewById(R.id.loadingAnimationLayout);
       relativeLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
       relativeLayout.setVisibility(View.GONE);

    }
    public SmartContract getSmartContract(){ return presenter.getSmartContract();}

    public NameContract getNameContract() { return presenter.getNameContract(); }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0; i< NUMBER_TABS; i++){
            adapter.addFragment(presenter.getFragment(i), getResources().getString(tabNames[i]));
        }
        viewPager.setAdapter(adapter);
    }
    private void setupTabContent(){
        TextView tabCurrent;
        for(int i=0; i < NUMBER_TABS; i++) {
            tabCurrent = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
            tabCurrent.setTextColor(getResources().getColor(R.color.mainTextColor));
            tabCurrent.setGravity(Gravity.CENTER);
            tabCurrent.setText(getResources().getString(tabNames[i]));
            tabCurrent.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[i], 0, 0);
            tabLayout.getTabAt(i).setCustomView(tabCurrent);
        }
    }


}
