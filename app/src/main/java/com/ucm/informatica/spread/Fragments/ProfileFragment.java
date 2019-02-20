package com.ucm.informatica.spread.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.andrognito.flashbar.Flashbar;
import com.ucm.informatica.spread.R;

public class ProfileFragment extends Fragment {

    private Button editContactsButton;
    private Button editProfileButton;

    private View view;

    public ProfileFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView();
        setupListeners();
        return view;
    }

    private void initView(){
        editContactsButton = view.findViewById(R.id.editContactsButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
    }

    private void setupListeners(){
        editContactsButton.setOnClickListener(view -> showSnackBar("Edición de contactos"));
        editProfileButton.setOnClickListener(view -> showSnackBar("Edición de información personal"));
    }

    private void showSnackBar(String text) {
        new Flashbar.Builder(getActivity())
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .backgroundColorRes(R.color.warm_grey)
                .message(text)
                .build()
                .show();
    }

}
