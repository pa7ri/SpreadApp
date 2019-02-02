package com.ucm.informatica.spread.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.NameContract;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.SmartContract;

import java.io.IOException;

import timber.log.Timber;


public class HomeFragment extends Fragment {
    private NameContract nameContract;
    private SmartContract smartContract;


    private TextView nameText;
    private EditText nameEditText;
    private Button saveButton;
    private Button loadButton;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        setupListeners();
        return view;
    }
    private void initView(View v) {
        nameText = v.findViewById(R.id.nameText);
        nameEditText = v.findViewById(R.id.nameEditText);
        saveButton = v.findViewById(R.id.saveNameButton);
        loadButton = v.findViewById(R.id.loadNameButton);
    }

    private void setupListeners(){
        saveButton.setOnClickListener(view -> {
            try {
                saveData(nameEditText.getText().toString());
            }catch (IOException e) {
                Timber.e(e);
            }
        });
        loadButton.setOnClickListener(view -> {
            try {
                String data = loadData();
                nameText.setText(data);
            } catch (IOException e) {
                Timber.e(e);
            }
        });
    }

    private void saveData(String data) throws IOException {
        if(nameContract == null) {
//|| !nameContract.isValid()) { TODO : isValid se ejecuta sincronamente tonses cuidado si lo lanzas dos veces sin haber acabado, va a saltar NetworkorMainException
            smartContract = ((MainTabActivity)getActivity()).getSmartContract();
            nameContract = ((MainTabActivity)getActivity()).getNameContract();
        }
        String result = smartContract.writeNameToSmartContract(nameContract, data);
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
    }

    private String loadData() throws IOException {
        if(nameContract == null) {//|| !nameContract.isValid()) {
            smartContract = ((MainTabActivity)getActivity()).getSmartContract();
            nameContract = ((MainTabActivity)getActivity()).getNameContract();
        }
        return smartContract.readNameFromSmartContract(nameContract);
    }

}
