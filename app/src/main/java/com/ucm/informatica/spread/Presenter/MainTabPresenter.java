package com.ucm.informatica.spread.Presenter;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Constants;
import com.ucm.informatica.spread.Contracts.NameContract;
import com.ucm.informatica.spread.Fragments.HistorialFragment;
import com.ucm.informatica.spread.Fragments.HomeFragment;
import com.ucm.informatica.spread.Fragments.MapFragment;
import com.ucm.informatica.spread.Fragments.ProfileFragment;
import com.ucm.informatica.spread.Fragments.SettingsFragment;
import com.ucm.informatica.spread.LocalWallet;
import com.ucm.informatica.spread.SmartContract;
import com.ucm.informatica.spread.View.MainTabView;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.ExecutionException;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.ucm.informatica.spread.Constants.Contract.CONTRACT_ADDRESS;
import static com.ucm.informatica.spread.Constants.Wallet.LOCAL_NAME_CONTRACT;
import static com.ucm.informatica.spread.Constants.Wallet.LOCAL_SMART_CONTRACT;

public class MainTabPresenter {

    private MainTabView mainTabView;
    private MainTabActivity context;
    private String walletPath;

    private LocalWallet localWallet;
    private Web3j web3j;

    private NameContract nameContract;
    private SmartContract smartContract;


    public MainTabPresenter(MainTabView mainTabView, MainTabActivity context){
        this.mainTabView = mainTabView;
        this.context = context;
    }

    public void start(String path){
        walletPath = path;
        mainTabView.showLoading();
        initEthConnection();
    }

    public Fragment getFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(LOCAL_NAME_CONTRACT, nameContract);
                bundle.putSerializable(LOCAL_SMART_CONTRACT, smartContract);
                fragment.setArguments(bundle);
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
                                mainTabView.initViewContent();
                                mainTabView.hideLoading();
                            }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("Ha habido un error");
                    }

                    @Override
                    public void onNext(Web3ClientVersion web3ClientVersion) {
                        Timber.e("Conectado a %s", web3ClientVersion.getWeb3ClientVersion());
                    }
                });
    }

    public SmartContract getSmartContract(){
        smartContract = new SmartContract(web3j, localWallet.getCredentials());
        return smartContract;
    }

    public NameContract getNameContract(){
        nameContract = smartContract.loadSmartContract(CONTRACT_ADDRESS);
        return nameContract;
    }


}
