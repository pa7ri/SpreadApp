package com.ucm.informatica.spread.Data;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ucm.informatica.spread.Utils.Constants.IPFS.CMD_INIT;
import static com.ucm.informatica.spread.Utils.Constants.IPFS.CMD_RUN;


public class IPFS {

    private Context context;

    private File binaryFile;
    private File repositoryPath;

    public IPFS(Context context){
        this.context = context;
    }

    public void initIPFSConnection() {
        onDownloadObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onCompleted() {
                        Log.i("IPFS download ", "SUCCESS");
                        Observable.just(onInitCommand(CMD_INIT)).flatMap(
                                resultRun-> Observable.just(onInitCommand(CMD_RUN)));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("IPFS download ", e.getMessage());
                    }

                    @Override
                    public void onNext(File file) {

                    }
                });
    }

    private Observable<File> onDownloadObservable(){
        return Observable.create(emitter -> {
            try {
                binaryFile = new File(context.getFilesDir(), "ipfsbin");
                downloadBinaryFiles();
                binaryFile.setExecutable(true);
                emitter.onCompleted();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    private Observable onInitCommand(String cmd){
        return Observable.create(emitter -> {
            try {
                repositoryPath = new File(context.getFilesDir(), ".ipfs_repo");
                String[] env = new String[]{"IPFS_PATH=" + repositoryPath.getAbsolutePath()};
                String command = binaryFile.getAbsolutePath() + cmd;

                Runtime.getRuntime().exec(command, env);
            } catch (IOException e) {
                Log.e("IPFS service ", e.getMessage());
            }
        });
    }

    private void downloadBinaryFiles(){
        try {
            BufferedSource source = Okio.buffer(Okio.source(context.getAssets().open(getBinaryFileByABI(Build.CPU_ABI))));
            BufferedSink sink = Okio.buffer(Okio.sink(binaryFile));
            while (!source.exhausted()) {
                source.read(sink.buffer(), 1024);
            }
            source.close();
            sink.close();
        } catch (IOException e) {
            Log.e("IPFS download ", e.getMessage());
        }
    }

    private String getBinaryFileByABI(String abi) {
        String binFileString;
        if (abi.toLowerCase().startsWith("x86")) {
            binFileString = "x86";
        } else {
            binFileString = "arm";
        }
        return binFileString;
    }
}
