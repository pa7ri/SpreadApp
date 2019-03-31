package com.ucm.informatica.spread.Data;

import com.ucm.informatica.spread.Contracts.PosterContract;
import com.ucm.informatica.spread.Model.Poster;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.MainTabView;

import io.ipfs.kotlin.defaults.InfuraIPFS;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class IPFSService {

    private MainTabView mainTabview;

    private int tries;
    private InfuraIPFS ipfs;

    public IPFSService(MainTabView view){
        this.mainTabview = view;
        this.tries =0;
        this.ipfs = new InfuraIPFS();
    }

    public void addStringGetHash(String jsonObject, PosterContract posterContract){
        Observable.just(ipfs.getAdd())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        functionResult -> Observable.just(functionResult.string(jsonObject, "", ""))
                                .subscribeOn(Schedulers.newThread())
                                .subscribe(
                                        hashResult -> {
                                            posterContract.addPoster(hashResult.getHash()).observable()
                                                    .subscribeOn(Schedulers.newThread())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(
                                                            (result) -> mainTabview.showConfirmationTransaction()
                                                            ,
                                                            (error) -> mainTabview.showErrorTransaction()
                                                    );
                                        },
                                        error ->  mainTabview.showErrorTransaction()
                                ),
                        error -> {
                            if(tries<3){
                                tries++;
                                addStringGetHash(jsonObject,posterContract);
                            } else {
                                mainTabview.showErrorTransaction();
                            }
                        }
                );
    }

    public void getDataFromHash(String hash, int index, int total){
        Observable.just(ipfs.getGet())
                .subscribeOn(Schedulers.newThread())
                .subscribe(
                        functionResult -> Observable.just(functionResult.cat(hash))
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    result -> {
                                        Poster poster = new Poster(result);
                                        mainTabview.loadDataPosterIPFS(poster);
                                        if(index==total-1) {
                                            mainTabview.initView();
                                            mainTabview.hideLoading();
                                        }
                                    },
                                    error -> mainTabview.showErrorTransaction()
                                ),
                        error -> mainTabview.showErrorTransaction()
                );
    }


}
