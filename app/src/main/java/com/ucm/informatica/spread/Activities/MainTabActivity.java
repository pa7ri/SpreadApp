package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.mapbox.geojson.Point;
import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.Contracts.PosterContract;
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.Presenter.MainTabPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.MainTabView;
import com.ucm.informatica.spread.Utils.ViewPagerAdapter;
import com.ucm.informatica.spread.Utils.ViewPagerTab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ucm.informatica.spread.Utils.Constants.NUMBER_TABS;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.NOTIFICATION_DATA;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.NOTIFICATION_MESSAGE;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER_CAMERA;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER_GALLERY;

public class MainTabActivity extends AppCompatActivity implements MainTabView{


    private MainTabPresenter mainPresenter;

    private RelativeLayout relativeLayout;
    private ViewPagerTab fragmentViewPager;
    private ViewPagerAdapter fragmentAdapter;

    private Map<Point, Region> regionMap = new HashMap<>();

    private List<Alert> dataAlertSmartContractList = new ArrayList<>();
    private List<Poster> dataPosterSmartContractList = new ArrayList<>();

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
        if(!isNotificationIntent()) {
            readPolygonCoordinates();
            mainPresenter = new MainTabPresenter(this, this);
            mainPresenter.start(getFilesDir().getAbsolutePath());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mainPresenter.manageOnActivityResult(requestCode, resultCode, data, getContentResolver(),
                fragmentAdapter.getItem(2), fragmentViewPager);
    }

    @Override
    public void initView(){
        fragmentViewPager =  findViewById(R.id.container);
        fragmentViewPager.setMotionEventSplittingEnabled(false);
        setupViewPager();

        tabLayout = findViewById(R.id.tabs);
        fragmentViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.removeAllTabs();
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(fragmentViewPager));
        tabLayout.setupWithViewPager(fragmentViewPager);

        setupTabContent();
    }

    @Override
    public void loadDataAlertSmartContract(String title, String description, String latitude, String longitude, String dataTime) {
        dataAlertSmartContractList.add(new Alert(this, title,description, latitude, longitude, dataTime));
    }

    @Override
    public void loadDataPosterIPFS(Poster poster) {
        dataPosterSmartContractList.add(poster);
    }

    @Override
    public void showErrorTransaction() {
        getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }

    @Override
    public void showConfirmationTransaction() {
        getConfirmationSnackBar().show();
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

    private boolean isNotificationIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            String notificationMessageJsonObject = intent.getExtras().get(NOTIFICATION_MESSAGE).toString();

            Intent notificationIntent = new Intent(this, AlertDetailsActivity.class);
            notificationIntent.putExtra(NOTIFICATION_MESSAGE, notificationMessageJsonObject);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(notificationIntent);
            return true;
        } else {
            return false;
        }
    }

    public void saveDataPoster(String posterJson){
        mainPresenter.onSaveDataPoster(posterJson);
    }

    public void saveDataAlert(String t, String d, String lat, String longi){
        mainPresenter.onSaveDataAlert(t,d,lat,longi);
    }

    public AlertContract getAlertContract() { return mainPresenter.getAlertContract(); }

    public PosterContract getPosterContract() { return mainPresenter.getPosterContract(); }

    public Map getPolygonData() {
        return regionMap;
    }

    public List<Alert> getDataAlertSmartContract() {
        return dataAlertSmartContractList;
    }

    public List<Poster> getDataPosterSmartContract() {
        return dataPosterSmartContractList;
    }

    public Location getLocation() {
        return mainPresenter.getLatestLocation();
    }

    public void createPictureIntentPicker(int mode){
        BottomSheetDialog dialogBuilder = new BottomSheetDialog(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pick_image, null);

        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        LinearLayout galleryLayout = dialogView.findViewById(R.id.galleryLayout);
        ImageView galleryImage = dialogView.findViewById(R.id.galleryIconImage);
        LinearLayout cameraLayout = dialogView.findViewById(R.id.cameraLayout);
        ImageView cameraImage = dialogView.findViewById(R.id.cameraIconImage);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String cameraPackage = cameraIntent.resolveActivity(getPackageManager()).getPackageName();

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        String galleryPackage = galleryIntent.resolveActivity(getPackageManager()).getPackageName();

        try {
            cameraImage.setImageDrawable(getPackageManager().getApplicationIcon(cameraPackage));
            galleryImage.setImageDrawable(getPackageManager().getApplicationIcon(galleryPackage));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("TAG",e.getMessage());
        }

        galleryLayout.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(mode == REQUEST_IMAGE_POSTER) {
                    startActivityForResult(Intent.createChooser(intent, ""), REQUEST_IMAGE_POSTER_GALLERY);
                } ///TODO : profile opt
            dialogBuilder.dismiss();
            });

        cameraLayout.setOnClickListener(view -> {
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    if(mode == REQUEST_IMAGE_POSTER) {
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_POSTER_CAMERA);
                    }///TODO : profile opt
                }
            dialogBuilder.dismiss();
            });

        cancelButton.setOnClickListener(view -> dialogBuilder.dismiss());

        dialogBuilder.setContentView(dialogView);
        dialogBuilder.show();
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
                .backgroundColorRes(R.color.snackbarBackground)
                .message(getString(R.string.snackbar_alert_gps))
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
                .backgroundColorRes(R.color.snackbarBackground)
                .message(getString(text))
                .messageColorRes(R.color.snackbarAlertColor)
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
                .backgroundColorRes(R.color.snackbarBackground)
                .message(getString(R.string.snackbar_confirmation_transaction))
                .messageColorRes(R.color.snackbarConfirmColor)
                .build();
    }
    private void setupViewPager() {
        if(getSupportFragmentManager().getFragments() != null) {
            getSupportFragmentManager().getFragments().clear();
        }
        fragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for(int i=0; i< NUMBER_TABS; i++){
            fragmentAdapter.addFragment(mainPresenter.getFragment(i));
        }
        fragmentViewPager.setAdapter(fragmentAdapter);
        fragmentAdapter.notifyDataSetChanged();
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
            Log.e("TAG",e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("TAG",e.getMessage());
                }
            }
        }
    }

}
