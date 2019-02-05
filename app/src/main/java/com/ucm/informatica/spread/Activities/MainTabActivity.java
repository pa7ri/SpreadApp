package com.ucm.informatica.spread.Activities;

import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mapbox.geojson.Point;
import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Contracts.NameContract;
import com.ucm.informatica.spread.Presenter.MainTabPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.MainTabView;
import com.ucm.informatica.spread.ViewPagerAdapter;
import com.ucm.informatica.spread.ViewPagerTab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.ucm.informatica.spread.Constants.NUMBER_TABS;

public class MainTabActivity extends AppCompatActivity implements MainTabView{


    private MainTabPresenter presenter;

    private RelativeLayout relativeLayout;

    private List<List<Point>> polygonPointList = new ArrayList<>();

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
        readPolygonCoordinates();
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

    public CoordContract getNameContract() { return presenter.getCoordContract(); }

    public List<List<Point>> getPolygonPointList() {
        return polygonPointList;
    }

    public Location getLocation() {
        return presenter.getLatestLocation();
    }

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

    //read coordinates from CoordinatesPolygon.txt
    private void readPolygonCoordinates() {
        // polygonList = new ArrayList<>();
        List<Point> polyCoordList;
        BufferedReader reader = null;
        String[] coords;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("CoordinatesPolygon.txt"), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                coords = line.split("\\s+");
                polyCoordList = new ArrayList<>();
                for(int i=0; i < coords.length-1; i=i+2) {
                    polyCoordList.add(Point.fromLngLat(Double.parseDouble(coords[i+1]),Double.parseDouble(coords[i])));
                }
                polygonPointList.add(polyCoordList);
            }
        } catch (IOException e) {
            Timber.e(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Timber.e(e);
                }
            }
        }
    }

}
