package com.ucm.informatica.spread.Activities;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mapbox.geojson.Point;
import com.ucm.informatica.spread.Data.LocationService;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.Model.Alert;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.Model.Region;
import com.ucm.informatica.spread.Presenter.MainTabPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.CustomLocationListener;
import com.ucm.informatica.spread.Utils.CustomTabLayoutOnPageChangeListener;
import com.ucm.informatica.spread.Utils.FlashBarBuilder;
import com.ucm.informatica.spread.Utils.ViewPagerAdapter;
import com.ucm.informatica.spread.Utils.ViewPagerTab;
import com.ucm.informatica.spread.View.MainTabView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.LOCATION_SERVICE_STARTED;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.NUMBER_TABS;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.NOTIFICATION_CHANNEL_ID;
import static com.ucm.informatica.spread.Utils.Constants.Notifications.NOTIFICATION_MESSAGE;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER_CAMERA;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER_GALLERY;
import static com.ucm.informatica.spread.Utils.Constants.Wallet.WALLET_FILENAME;
import static com.ucm.informatica.spread.Utils.Constants.Wallet.WALLET_PASSWORD;

public class MainTabActivity extends AppCompatActivity implements MainTabView{

    private MainTabPresenter mainPresenter;

    private RelativeLayout loadingLayout;
    private ViewPagerTab fragmentViewPager;
    private ViewPagerAdapter fragmentAdapter;

    private Map<Point, Region> regionMap = new HashMap<>();

    private List<Alert> dataAlertSmartContractList = new ArrayList<>();
    private List<Poster> dataPosterSmartContractList = new ArrayList<>();

    private CustomLocationListener locationListener;
    private SharedPreferences sharedPreferences;

    private Boolean isLocationServiceStarted = false;


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
        sharedPreferences =  getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
        if(!isNotificationIntent()) {
            loadingLayout=findViewById(R.id.loadingAnimationLayout); //as exception
            readPolygonCoordinates();
            mainPresenter = new MainTabPresenter(this);
            mainPresenter.start();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startLocationService();
    }

    @Override
    protected void onDestroy() {
        stopLocationService();
        super.onDestroy();
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
        tabLayout.addOnTabSelectedListener(new CustomTabLayoutOnPageChangeListener(fragmentViewPager, fragmentAdapter));
        fragmentViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.removeAllTabs();
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(fragmentViewPager));
        tabLayout.setupWithViewPager(fragmentViewPager);

        setupTabContent();
    }

    @Override
    public void loadDataAlertSmartContract(String title, String description, String latitude, String longitude, String dataTime) {
        dataAlertSmartContractList.add(new Alert(title,description, latitude, longitude, dataTime));
    }

    @Override
    public void loadDataPosterIPFS(Poster poster) {
        dataPosterSmartContractList.add(poster);
    }

    @Override
    public void showErrorTransaction() {
        new FlashBarBuilder(this).getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }

    @Override
    public void showConfirmationTransaction() {
        new FlashBarBuilder(this).getConfirmationSnackBar().show();
    }

    @Override
    public void showLoading() {
       loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
       loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public String getWalletFilePath() {
        return getFilesDir().getAbsolutePath();
    }

    @Override
    public String getPasswordLocally() {
        return sharedPreferences.getString(WALLET_PASSWORD, "");
    }

    @Override
    public String getFilenameWalletLocally() {
        return sharedPreferences.getString(WALLET_FILENAME, "");
    }

    @Override
    public void initNotificationService() {
        createNotificationChannel();
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnSuccessListener(this, instanceIdResult -> {});
        }

    public void startLocationService() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        isLocationServiceStarted = sharedPreferences.getBoolean(LOCATION_SERVICE_STARTED, false);
        //start local location listener
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new CustomLocationListener(this);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);

        if (!isLocationServiceStarted) {
            //start background Service
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
            isLocationServiceStarted = true;
            sharedPreferences.edit().putBoolean(LOCATION_SERVICE_STARTED, isLocationServiceStarted).apply();
        }
    }

    public void stopLocationService() {
        isLocationServiceStarted = sharedPreferences.getBoolean(LOCATION_SERVICE_STARTED, false);
        if (isLocationServiceStarted) {
            Intent intent = new Intent(this, LocationService.class);
            stopService(intent);
            isLocationServiceStarted = false;
            sharedPreferences.edit().putBoolean(LOCATION_SERVICE_STARTED, isLocationServiceStarted).apply();
        }
    }


    @Override
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Alertas emergencia";
            String description = "Notificar a los usuarios si hay alguien en peligro cerca";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isNotificationIntent() {
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null && intent.getExtras().get(NOTIFICATION_MESSAGE)!= null) {
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

    public void saveDataPoster(String t, String d, String lat, String longi, byte[] image){
        String date = String.valueOf(System.currentTimeMillis());
        Poster poster = new Poster(t,d,lat,longi,date, image);
        dataPosterSmartContractList.add(poster);
        mainPresenter.onSaveDataPoster(poster.toJson());
    }

    public void saveDataAlert(String t, String d, String lat, String longi){
        String date = String.valueOf(System.currentTimeMillis());
        dataAlertSmartContractList.add(new Alert(t,d,lat,longi,date));
        mainPresenter.onSaveDataAlert(t,d,lat,longi,date);
    }

    public Map getPolygonData() {
        return regionMap;
    }

    public List<Alert> getDataAlertSmartContract() {
        return dataAlertSmartContractList;
    }

    public List<Poster> getDataPosterSmartContract() {
        return dataPosterSmartContractList;
    }

    public CustomLocationListener getCustomLocationListener(){
        return locationListener;
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
            Log.e("PICTURE",e.getMessage());
        }

        galleryLayout.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if(mode == REQUEST_IMAGE_POSTER) {
                    startActivityForResult(Intent.createChooser(intent, ""), REQUEST_IMAGE_POSTER_GALLERY);
                }
            dialogBuilder.dismiss();
            });

        cameraLayout.setOnClickListener(view -> {
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    if(mode == REQUEST_IMAGE_POSTER) {
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_POSTER_CAMERA);
                    }
                }
            dialogBuilder.dismiss();
            });

        cancelButton.setOnClickListener(view -> dialogBuilder.dismiss());

        dialogBuilder.setContentView(dialogView);
        dialogBuilder.show();
    }

    public void showSelectedLocation(Double latitude, Double longitude){
        fragmentViewPager.setCurrentItem(2); //force Map
        ((MapFragment) fragmentAdapter.getItem(2)).showSelectedLocation(latitude, longitude);
    }

    private void setupViewPager() {
        if(getSupportFragmentManager().getFragments() != null) {
            getSupportFragmentManager().getFragments().clear();
        }
        fragmentAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        for(int i=0; i< NUMBER_TABS; i++){
            Fragment fragment = mainPresenter.getFragment(i);
            if(!fragment.isAdded()) {
                fragmentAdapter.addFragment(fragment);
            }
        }
        fragmentViewPager.setAdapter(fragmentAdapter);
        fragmentViewPager.setOffscreenPageLimit(4);
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
            Log.e("READ COORDINATES",e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("READ COORDINATES",e.getMessage());
                }
            }
        }
    }
}
