package com.ucm.informatica.spread.Fragments;

import android.text.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ucm.informatica.spread.R;

import static com.mapbox.mapboxsdk.Mapbox.getApplicationContext;


public class SettingsFragment extends Fragment {
    private static final String ARG_ACCOUNT = "ARG_ACCOUNT";
    private static final String ARG_PASSWORD = "ARG_PASSWORD";
    private static final String ARG_BALANCE = "ARG_BALANCE";

    private String accountData;
    private String passwordData;
    private String balanceData;

    private TextView accountText;
    private TextView passwordText;
    private TextView balanceText;


    public SettingsFragment() { }

    public static SettingsFragment newInstance(String param1, String param2, String param3) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT, param1);
        args.putString(ARG_PASSWORD, param2);
        args.putString(ARG_BALANCE, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            accountData = getArguments().getString(ARG_ACCOUNT);
            passwordData = getArguments().getString(ARG_PASSWORD);
            balanceData = getArguments().getString(ARG_BALANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initView(view);
        initContent();
        setupListeners();
        return view;
    }

    private void initView(View v) {
        accountText = v.findViewById(R.id.walletAccountDescription);
        passwordText = v.findViewById(R.id.walletPasswordDescription);
        balanceText = v.findViewById(R.id.walletBalanceDescription);
    }

    private void initContent(){
        accountText.setText(accountData.length()>22 ? accountData.substring(0,22) + "..." : accountData);
        passwordText.setText(passwordData);
        balanceText.setText(balanceData);
    }

    private void setupListeners(){
        accountText.setOnClickListener(view -> {
            ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setText(accountData);
            Toast.makeText(getApplicationContext(), "Cuenta copiada :)", Toast.LENGTH_SHORT).show();
        });
    }
}
