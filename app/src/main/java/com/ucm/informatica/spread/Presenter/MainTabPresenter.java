package com.ucm.informatica.spread.Presenter;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Contracts.AlertContract;
import com.ucm.informatica.spread.Contracts.PosterContract;
import com.ucm.informatica.spread.Utils.Constants;
import com.ucm.informatica.spread.Fragments.HistoricalFragment;
import com.ucm.informatica.spread.Fragments.HomeFragment;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.Fragments.ProfileFragment;
import com.ucm.informatica.spread.Fragments.SettingsFragment;
import com.ucm.informatica.spread.Utils.LocalWallet;
import com.ucm.informatica.spread.Utils.SmartContract;
import com.ucm.informatica.spread.View.MainTabView;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.ucm.informatica.spread.Utils.Constants.Contract.CONTRACT_ADDRESS_ALERT;
import static com.ucm.informatica.spread.Utils.Constants.Contract.CONTRACT_ADDRESS_POSTER;
import static com.ucm.informatica.spread.Utils.Constants.Map.UPDATE_MAP;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER_CAMERA;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER_GALLERY;

public class MainTabPresenter {

    private MainTabView mainTabView;
    private MainTabActivity context;
    private String walletPath;

    private LocalWallet localWallet;
    private Web3j web3j;

    private AlertContract alertContract;
    private PosterContract posterContract;
    private SmartContract smartContract;

    private Location latestLocation;
    private Bitmap imageBitmap;

    private String mode = "";

    public MainTabPresenter(MainTabView mainTabView, MainTabActivity context){
        this.mainTabView = mainTabView;
        this.context = context;
    }

    public void start(String path){
        walletPath = path;
        mainTabView.showLoading();
        initLocationManager();
        initEthConnection();
    }

    public Fragment getFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new ProfileFragment();
                break;
            case 2:
                if(mode.equals(UPDATE_MAP)){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] imagePoster = stream.toByteArray();
                    fragment = MapFragment.newInstance(true, imagePoster);
                } else {
                    fragment = new MapFragment();
                }
                break;
            case 3:
                fragment = new HistoricalFragment();
                break;
            default:
                fragment = SettingsFragment.newInstance(getAddress(), "password", getBalance());
                break;
        }
        return fragment;
    }

    private String getAddress(){
        return localWallet.getCredentials() != null ? localWallet.getCredentials().getAddress() : "No disponible";
    }

    private String getBalance() {
        EthGetBalance ethGetBalance = null;
        if(localWallet.getCredentials() != null) {
            try {
                ethGetBalance = web3j
                        .ethGetBalance(localWallet.getCredentials().getAddress(), DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                Timber.e(e);
            }
        }
        return ethGetBalance != null ? ethGetBalance.getBalance().toString() + " ETH" : "No disponible";

    }

    private void initLocationManager() {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new CustomLocationListener();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions( context,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
    }

    private void initEthConnection() {
        localWallet = new LocalWallet(context);
        if(web3j == null) {
            web3j = Web3jFactory.build(new HttpService(Constants.INFURA_PATH + Constants.INFURA_PUBLIC_PROYECT_ADDRESS));
        }
        web3j.web3ClientVersion().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Web3ClientVersion>() {
                    @Override
                    public void onCompleted() {
                            Timber.e("Conexión completada");
                            if (!localWallet.existWallet()) {
                                localWallet.createWallet(localWallet.getPasswordWallet(), walletPath);
                            }
                            String filenameWallet = localWallet.getFilenameWallet();
                            String passwordWallet = localWallet.getPasswordWallet();
                            if (filenameWallet != null && !filenameWallet.isEmpty()) {
                                localWallet.setCredentials(localWallet.loadWallet(passwordWallet, walletPath));
                                localWallet.loadContract(web3j);
                                mainTabView.initView();
                                mainTabView.hideLoading();

                                loadData();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }

                    @Override
                    public void onNext(Web3ClientVersion web3ClientVersion) {
                        Timber.e("Conectado a %s", web3ClientVersion.getWeb3ClientVersion());
                    }
                });
    }

    public AlertContract getAlertContract(){
        smartContract = new SmartContract(web3j, localWallet.getCredentials());
        alertContract = smartContract.loadAlertSmartContract(CONTRACT_ADDRESS_ALERT);
        return alertContract;
    }

    public PosterContract getPosterContract(){
        smartContract = new SmartContract(web3j, localWallet.getCredentials());
        posterContract = smartContract.loadPosterSmartContract(CONTRACT_ADDRESS_POSTER);
        return posterContract;
    }

    public Location getLatestLocation() {
        return latestLocation;
    }

    public void loadData() {
        if(alertContract == null) { //|| !nameContract.isValid()) {
            alertContract = context.getAlertContract();
        }
        alertContract.getAlertsCount().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (countCoords) -> {
                            for(int i =0; i<countCoords.intValue(); i++){
                                alertContract.getAlertByIndex(BigInteger.valueOf(i)).observable()
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                (result) ->
                                                        mainTabView.loadDataEventSmartContract(
                                                                result.getValue1(),
                                                                result.getValue2(),
                                                                result.getValue3(),
                                                                result.getValue4(),
                                                                result.getValue5())
                                                ,
                                                (error) -> mainTabView.showErrorTransition()
                                        );
                            }
                        },
                        (error) -> mainTabView.showErrorTransition()
                );
        if(posterContract == null) { //|| !nameContract.isValid()) {
            posterContract = context.getPosterContract();
        }
        posterContract.getPostersCount().observable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (countCoords) -> {
                            for(int i =0; i<countCoords.intValue(); i++){
                                posterContract.getPosterByIndex(BigInteger.valueOf(i)).observable()
                                        .subscribeOn(Schedulers.newThread())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(
                                                (result) ->
                                                        mainTabView.loadDataPosterSmartContract(
                                                                result.getValue1(),
                                                                result.getValue2(),
                                                                result.getValue3(),
                                                                result.getValue4(),
                                                                result.getValue5(),
                                                                result.getValue6())
                                                ,
                                                (error) -> mainTabView.showErrorTransition()
                                        );
                            }
                        },
                        (error) -> mainTabView.showErrorTransition()
                );
    }

    public void manageOnActivityResult(int requestCode, int resultCode, Intent data, ContentResolver contentResolver) {
        if(resultCode == RESULT_OK) {
            mode = UPDATE_MAP;
            switch (requestCode) {
                case REQUEST_IMAGE_POSTER_CAMERA:
                    Bundle extras = data.getExtras();
                    imageBitmap = (Bitmap) extras.get("data");

                    break;
                case REQUEST_IMAGE_POSTER_GALLERY:
                    try {
                        imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, data.getData());
                    } catch (IOException e) {
                        Timber.e(e);
                    }
                    break;
                //TODO : ask for profile
            }
            mainTabView.initView();
        }
        else {
            mode = "";
        }
    }

    private class CustomLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            latestLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }


}
