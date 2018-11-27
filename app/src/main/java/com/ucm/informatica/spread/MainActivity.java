package com.ucm.informatica.spread;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

import static com.ucm.informatica.spread.Constants.INFURA_PATH;
import static com.ucm.informatica.spread.Constants.INFURA_PUBLIC_PROYECT_ADDRESS;


public class MainActivity extends AppCompatActivity {


    private EditText nameInputText;
    private Button homeButton;
    private Button sendButton;
    private Button showButton;

    private Web3j web3j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWeb3jClient();

        initView();
        setUpListeners();
    }

    private void initView(){
        nameInputText =  findViewById(R.id.nameInputText);
        homeButton = findViewById(R.id.homeButton);
        sendButton = findViewById(R.id.sendButton);
        showButton = findViewById(R.id.showButton);
    }

    private void setUpListeners() {
        homeButton.setOnClickListener(view ->
                Snackbar.make(view, "Ya estás en la pantalla principal", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        sendButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(),MapActivity.class);
            startActivity(intent);
        });
    }

    private void initWeb3jClient(){
        if(web3j == null) {
            web3j = Web3jFactory.build(new HttpService(INFURA_PATH + INFURA_PUBLIC_PROYECT_ADDRESS));
        }
    }
}
