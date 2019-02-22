package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapFragment;
import com.ucm.informatica.spread.Contracts.CoordContract;
import com.ucm.informatica.spread.Model.Event;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.Presenter.MainTabPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.Constants;
import com.ucm.informatica.spread.View.MainTabView;
import com.ucm.informatica.spread.Utils.ViewPagerAdapter;
import com.ucm.informatica.spread.Utils.ViewPagerTab;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import timber.log.Timber;

import static com.ucm.informatica.spread.Utils.Constants.Map.IMAGE_POSTER;
import static com.ucm.informatica.spread.Utils.Constants.Map.UPDATE_MAP;
import static com.ucm.informatica.spread.Utils.Constants.NUMBER_TABS;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;

public class MainTabActivity extends AppCompatActivity implements MainTabView{


    private MainTabPresenter mainPresenter;

    private RelativeLayout relativeLayout;
    private ViewPagerTab fragmentViewPager;
    private ViewPagerAdapter fragmentAdapter;

    private Map<Point, Region> regionMap = new HashMap<>();

    private List<Event> dataSmartContractList = new ArrayList<>();


    private int[] tabIcons = {
            R.drawable.ic_home,
            R.drawable.ic_profile,
            R.drawable.ic_map,
            R.drawable.ic_historial,
            R.drawable.ic_settings
    };
    private int[] tabNames = {
            R.string.tab_text_home,
            R.string.tab_text_profile,
            R.string.tab_text_map,
            R.string.tab_text_historial,
            R.string.tab_text_settings
    };

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        readPolygonCoordinates();
        mainPresenter = new MainTabPresenter(this, this);
        mainPresenter.start(getFilesDir().getAbsolutePath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_POSTER && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            Bundle args = new Bundle();
            args.putBoolean(UPDATE_MAP, true);
            args.putByteArray(IMAGE_POSTER, bitmapToByteArray(imageBitmap));
            fragmentAdapter.getItem(2).setArguments(args);
            fragmentViewPager.setCurrentItem(2);
        }
    }

    private byte[] bitmapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void initView(){
        fragmentViewPager =  findViewById(R.id.container);
        fragmentViewPager.setMotionEventSplittingEnabled(false);
        setupViewPager();

        tabLayout = findViewById(R.id.tabs);
        fragmentViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(fragmentViewPager));
        tabLayout.setupWithViewPager(fragmentViewPager);

        setupTabContent();
    }

    @Override
    public void loadDataSmartContract(String title, String description, String latitude, String longitude, String dataTime) {
        dataSmartContractList.add(new Event(this, title,description, latitude, longitude, dataTime));
    }

    @Override
    public void showErrorTransition() {
        getErrorSnackBar(R.string.snackbar_alert_transaction).show();
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

    public CoordContract getCoordContract() { return mainPresenter.getCoordContract(); }

    public Map getPolygonData() {
        return regionMap;
    }

    public List<Event> getDataSmartContract() {
        return dataSmartContractList;
    }

    public Location getLocation() {
        return mainPresenter.getLatestLocation();
    }

    private void setupViewPager() {
        fragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0; i< NUMBER_TABS; i++){
            fragmentAdapter.addFragment(mainPresenter.getFragment(i), getResources().getString(tabNames[i]));
        }
        fragmentViewPager.setAdapter(fragmentAdapter);
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
        Point centroid;
        List<Point> polyCoordList;
        BufferedReader reader = null;
        String[] coords;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("CoordinatesPolygon.txt"), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                coords = line.split("\\s+");
                //first pair is the centroid of the polygon
                centroid = Point.fromLngLat(Double.parseDouble(coords[1]),Double.parseDouble(coords[0]));
                polyCoordList = new ArrayList<>();
                //the rest of pairs are the polygon coordinates
                for(int i=2; i < coords.length-1; i=i+2) {
                    polyCoordList.add(Point.fromLngLat(Double.parseDouble(coords[i+1]),Double.parseDouble(coords[i])));
                }
                regionMap.put(centroid, new Region(polyCoordList));
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


    public Flashbar getAlertSnackBarGPS(){
        return new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(450)
                        .accelerateDecelerate())
                .showIcon()
                .backgroundColorRes(R.color.snackbarAlertColor)
                .message(getString(R.string.snackbar_alert_gps))
                .primaryActionText(getResources().getString(R.string.button_active))
                .build();
    }

    public Flashbar getErrorSnackBar(int text){
        return new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(450)
                        .accelerateDecelerate())
                .backgroundColorRes(R.color.snackbarAlertColor)
                .message(getString(text))
                .build();
    }


    public Flashbar getConfirmationSnackBar(){
        return new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(450)
                        .accelerateDecelerate())
                .backgroundColorRes(R.color.snackbarConfirmColor)
                .message(getString(R.string.snackbar_confirmation_transaction))
                .build();
    }

}
