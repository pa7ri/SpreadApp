package com.ucm.informatica.spread.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ucm.informatica.spread.Activities.TutorialActivity;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.FlashBarBuilder;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.RADIUS_PREF;


public class SettingsFragment extends Fragment {
    private static final String ARG_ACCOUNT = "ARG_ACCOUNT";
    private static final String ARG_PASSWORD = "ARG_PASSWORD";
    private static final String ARG_BALANCE = "ARG_BALANCE";

    private String accountData;
    private String passwordData;
    private String balanceData;

    private TextView walletText;
    private TextView accountText;
    private TextView passwordText;
    private TextView balanceText;
    private TextView radiusText;
    private TextView tutorialText;

    private IndicatorSeekBar indicatorSeekBar;

    private SharedPreferences sharedPreferences;

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
        sharedPreferences = getContext().getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
        initView(view);
        initContent();
        setupListeners();
        return view;
    }

    private void initView(View v) {
        walletText = v.findViewById(R.id.walletTitle);
        accountText = v.findViewById(R.id.walletAccountDescription);
        passwordText = v.findViewById(R.id.walletPasswordDescription);
        balanceText = v.findViewById(R.id.walletBalanceDescription);
        indicatorSeekBar = v.findViewById(R.id.radiusSeekBar);
        radiusText = v.findViewById(R.id.radiusInfo);
        tutorialText = v.findViewById(R.id.tutorialTitle);
    }

    private void initContent(){
        accountText.setText(accountData.length()>22 ? accountData.substring(0,22) + "..." : accountData);
        passwordText.setText(passwordData);
        balanceText.setText(balanceData);
        int progress =sharedPreferences.getInt(RADIUS_PREF, 1);
        indicatorSeekBar.setProgress(progress);
        indicatorSeekBar.setIndicatorTextFormat("${TICK_TEXT}");
        radiusText.setVisibility(progress==0?View.VISIBLE:View.GONE);
    }

    private void setupListeners(){
        walletText.setOnClickListener(view -> {
            BottomSheetDialog dialogBuilder = new BottomSheetDialog(getContext());
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ethereum_info, null);
            dialogBuilder.setContentView(dialogView);
            dialogBuilder.show();
        });


        accountText.setOnClickListener(view -> {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(accountData);
            new FlashBarBuilder(getActivity(),getString(R.string.snackbar_information_copy)).getConfirmationSnackBar().show();
        });

        indicatorSeekBar.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) { }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(RADIUS_PREF, seekBar.getProgress());
                editor.apply();

                radiusText.setVisibility(seekBar.getProgress()==0?View.VISIBLE:View.GONE);
            }
        });

        tutorialText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TutorialActivity.class);
            startActivity(intent);
        });
    }
}
