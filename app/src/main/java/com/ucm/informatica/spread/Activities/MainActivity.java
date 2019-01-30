package com.ucm.informatica.spread.Activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ucm.informatica.spread.LocalWallet;
import com.ucm.informatica.spread.NameContract;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.SmartContract;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import timber.log.Timber;

import static com.ucm.informatica.spread.Constants.Contract.CONTRACT_ADDRESS;


public class MainActivity extends AppCompatActivity {


    private NameContract nameContract;
    private SmartContract smartContract;

    private LocalWallet localWallet;
    private Web3j web3j;

    private EditText nameInputText;
    private Button homeButton;
    private Button sendButton;
    private Button saveButton;
    private Button loadButton;
    private Button loadWalletButton;

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
        loadWalletButton = findViewById(R.id.loadWalletDataButton);
    }

    private void setUpListeners() {
        homeButton.setOnClickListener(view ->
                Snackbar.make(view, "Ya estás en la pantalla principal", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        sendButton.setOnClickListener(view -> {
            //Intent intent = new Intent(getBaseContext(),MapActivity.class);
            //startActivity(intent);
        });
        saveButton.setOnClickListener(view -> {
            try {
                saveData(nameInputText.getText().toString());
            } catch (ExecutionException | IOException | InterruptedException e) {
                Timber.e(e);
            }

        });
        loadButton.setOnClickListener(view -> {
            try {
                String data = loadData();
                Snackbar.make(view, "Ultimo dato guardado : " + data, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } catch (ExecutionException | IOException | InterruptedException e) {
                Timber.e(e);
            }
        });
        loadWalletButton.setOnClickListener(view -> {
            Credentials credentials = localWallet.getCredentials();
            String address = credentials.getAddress();
            EthGetBalance ethGetBalance = null;
            try {
                ethGetBalance = web3j
                        .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
            } catch (InterruptedException | ExecutionException e) {
                Timber.e(e);
            }

            Snackbar.make(view, "Address : " + address +
                    "\n Balance :"+ (ethGetBalance != null ? ethGetBalance.getBalance() : "None"),
                    Snackbar.LENGTH_LONG).setAction("Action", null).show();
        });
    }

    private void initEthConnection() {
        localWallet = new LocalWallet(this);
        web3j = localWallet.initWeb3j(getFilesDir().getAbsolutePath());
    }

    private void loadContract(){
        smartContract = new SmartContract(web3j, localWallet.getCredentials());
        nameContract = smartContract.loadSmartContract(CONTRACT_ADDRESS);
    }

    private void saveData(String data) throws ExecutionException, InterruptedException, IOException {
        if(nameContract == null || !nameContract.isValid()) {
            loadContract();
        }
        String result = smartContract.writeNameToSmartContract(nameContract, data);
        Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
    }

    private String loadData() throws ExecutionException, InterruptedException, IOException {
        if(nameContract == null || !nameContract.isValid()) {
            loadContract();
        }
        return smartContract.readNameFromSmartContract(nameContract);
    }

}
