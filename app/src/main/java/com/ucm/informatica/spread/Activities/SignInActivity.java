package com.ucm.informatica.spread.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hanks.htextview.fade.FadeTextView;
import com.ucm.informatica.spread.Presenter.SignInPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.SignInView;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.ONBOARDING_COMPLETE;
import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.Wallet.WALLET_FILENAME;
import static com.ucm.informatica.spread.Utils.Constants.Wallet.WALLET_PASSWORD;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SignInActivity extends AppCompatActivity implements SignInView {

    private FadeTextView fadeTextView;
    private EditText passwordEditText;
    private TextView passwordErrorText;
    private Button signInButton;
    private RelativeLayout loadingLayout;
    private RelativeLayout ethereumInfoLayout;

    private SharedPreferences sharedPreferences;

    private SignInPresenter signInPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Fabric.with(this, new Crashlytics());
        sharedPreferences  = getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE);
        boolean isOnBoardingComplete = sharedPreferences.getBoolean(ONBOARDING_COMPLETE, false);
        signInPresenter = new SignInPresenter(this, isOnBoardingComplete);
        signInPresenter.start();
    }

    @Override
    public void initView(){
        fadeTextView = findViewById(R.id.titleTextView);
        passwordEditText = findViewById(R.id.password);
        passwordErrorText = findViewById(R.id.passwordErrorText);
        signInButton = findViewById(R.id.signInbutton);
        loadingLayout = findViewById(R.id.loadingAnimationLayout);
        ethereumInfoLayout = findViewById(R.id.ethereumInfoLayout);
    }

    @Override
    public void setUpListeners(){
        fadeTextView.animateText(getResources().getString(R.string.login_title));
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordErrorText.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
        passwordEditText.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                signInPresenter.onContinuePressed(passwordEditText.getText().toString());
                hideKeyboard();
                return true;
            }
            return false;
        });
        signInButton.setOnClickListener(view -> {
            signInPresenter.onContinuePressed(passwordEditText.getText().toString());
            hideKeyboard();
        });
        ethereumInfoLayout.setOnClickListener(v -> {
            BottomSheetDialog dialogBuilder = new BottomSheetDialog(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ethereum_info, null);
            dialogBuilder.setContentView(dialogView);
            dialogBuilder.show();
        });

    }

    @Override
    public void showPasswordError() {
        passwordErrorText.setVisibility(View.VISIBLE);
        passwordEditText.requestFocus();
    }


    @Override
    public void showLoading() {
        signInButton.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void initTutorialActivity() {
        Intent intent = new Intent(this, TutorialActivity.class);
        startActivity(intent);
    }

    @Override
    public void initMainActivity() {
        Intent intent = new Intent(this, MainTabActivity.class);
        startActivity(intent);
    }

    @Override
    public void storeWalletFileLocally(String walletFilename) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WALLET_FILENAME, walletFilename);
        editor.apply();
    }

    @Override
    public void storePasswordLocally(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(WALLET_PASSWORD, password);
        editor.apply();
    }

    @Override
    public void storeOnBoardingFinishedLocally() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ONBOARDING_COMPLETE, true);
        editor.apply();
    }

    @Override
    public String getWalletFilePath() {
        return getFilesDir().getAbsolutePath();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if(v == null) { v = new View(this); }
        imm.hideSoftInputFromWindow( v.getWindowToken(), 0);
    }
}

