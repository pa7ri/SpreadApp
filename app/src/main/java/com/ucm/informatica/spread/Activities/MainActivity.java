package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import com.ucm.informatica.spread.LocalWallet;
import com.ucm.informatica.spread.NameContract;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.SmartContract;

import org.web3j.protocol.Web3j;

import java.util.concurrent.ExecutionException;

import timber.log.Timber;

import static com.ucm.informatica.spread.Constants.Contract.CONTRACT_ADDRESS;


public class MainActivity extends AppCompatActivity {


    private NameContract nameContract;
    private SmartContract smartContract;

    private EditText nameInputText;
    private Button homeButton;
    private Button sendButton;
    private Button saveButton;
    private Button loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEthConnection();
        setUpListeners();
    }

    private void initView() {
        nameInputText =  findViewById(R.id.nameInputText);
        homeButton = findViewById(R.id.homeButton);
        sendButton = findViewById(R.id.sendButton);
        saveButton = findViewById(R.id.saveButton);
        loadButton = findViewById(R.id.loadButton);
    }

    private void setUpListeners() {
        homeButton.setOnClickListener(view ->
                Snackbar.make(view, "Ya estás en la pantalla principal", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        sendButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(),MapActivity.class);
            startActivity(intent);
        });
        saveButton.setOnClickListener(view -> {
            try {
                smartContract.writeNameToSmartContract(nameContract, nameInputText.getText().toString());
            } catch (ExecutionException | InterruptedException e) {
                Timber.e(e);
            }

        });
        loadButton.setOnClickListener(view -> {
            try {
                smartContract.readNameFromSmartContract(nameContract);
            } catch (ExecutionException | InterruptedException e) {
                Timber.e(e);
            }
        });
    }

    private void initEthConnection() {
        LocalWallet localWallet = new LocalWallet();
        Web3j web3j = localWallet.initWeb3j(getFilesDir().getAbsolutePath());
        smartContract = new SmartContract(web3j, localWallet.getCredentials());
        nameContract = smartContract.loadSmartContract(CONTRACT_ADDRESS);
    }

}
