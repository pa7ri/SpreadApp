package com.ucm.informatica.spread.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.ucm.informatica.spread.Presenter.ProfileFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.ProfileFragmentView;
import com.ucm.informatica.spread.Model.Colours;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements ProfileFragmentView {

    private Button editContactsButton;
    private Button editProfileButton;
    private Button saveProfileButton;
    private Button[] shirtButton = new Button[Colours.values().length];
    private Button[] pantsButton = new Button[Colours.values().length];
    private Button editWatchwordButton;
    private Button saveWatchwordButton;
    private TextView nameText;
    private TextView ageText;
    private EditText editName;
    private EditText editAge;
    private TextView watchwordMessageText;
    private TextView watchwordResponseText;
    private EditText editWatchwordMessage;
    private EditText editWatchwordResponse;

    private Colours shirtColour = Colours.NA;
    private Colours pantsColour = Colours.NA;

    private View view;

    public ProfileFragment() { }

    private ProfileFragmentPresenter profileFragmentPresenter;

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        profileFragmentPresenter = new ProfileFragmentPresenter(this);
        sharedPreferences = getContext().getSharedPreferences("ProfileInfo", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        //Deberían llamarse desde el presenter con un presenter.onStart o algo similar (mirar otras clases).
        profileFragmentPresenter.onStart();
        profileFragmentPresenter.onRefreshView(shirtColour, pantsColour);
        return view;
    }

    public void initView(){
        editContactsButton = view.findViewById(R.id.editContactsButton);
        editProfileButton = view.findViewById(R.id.editProfileButton);
        saveProfileButton = view.findViewById(R.id.saveProfileButton);
        editWatchwordButton = view.findViewById(R.id.editWatchwordButton);
        saveWatchwordButton = view.findViewById(R.id.saveWatchwordButton);
        initShirtButtons();
        initPantsButtons();
        nameText = view.findViewById(R.id.dataNameDescription);
        ageText = view.findViewById(R.id.dataAgeDescription);
        editName = view.findViewById(R.id.editName);
        editAge = view.findViewById(R.id.editAge);
        watchwordMessageText = view.findViewById(R.id.watchwordMessageDescription);
        watchwordResponseText = view.findViewById(R.id.watchwordResponseDescription);
        editWatchwordMessage = view.findViewById(R.id.editWatchwordMessageDescription);
        editWatchwordResponse = view.findViewById(R.id.editWatchwordResponseDescription);
    }

    private void initShirtButtons(){
        shirtButton[Colours.NA.ordinal()] = view.findViewById(R.id.upperImageNA);
        shirtButton[Colours.White.ordinal()] = view.findViewById(R.id.upperImageWhite);
        shirtButton[Colours.Gray.ordinal()] = view.findViewById(R.id.upperImageGray);
        shirtButton[Colours.Black.ordinal()] = view.findViewById(R.id.upperImageBlack);
        shirtButton[Colours.Red.ordinal()] = view.findViewById(R.id.upperImageRed);
        shirtButton[Colours.Green.ordinal()] = view.findViewById(R.id.upperImageGreen);
        shirtButton[Colours.Blue.ordinal()] = view.findViewById(R.id.upperImageBlue);
        shirtButton[Colours.Yellow.ordinal()] = view.findViewById(R.id.upperImageYellow);
    }

    private void initPantsButtons(){
        pantsButton[Colours.NA.ordinal()] = view.findViewById(R.id.downImageNA);
        pantsButton[Colours.White.ordinal()] = view.findViewById(R.id.downImageWhite);
        pantsButton[Colours.Gray.ordinal()] = view.findViewById(R.id.downImageGray);
        pantsButton[Colours.Black.ordinal()] = view.findViewById(R.id.downImageBlack);
        pantsButton[Colours.Red.ordinal()] = view.findViewById(R.id.downImageRed);
        pantsButton[Colours.Green.ordinal()] = view.findViewById(R.id.downImageGreen);
        pantsButton[Colours.Blue.ordinal()] = view.findViewById(R.id.downImageBlue);
        pantsButton[Colours.Yellow.ordinal()] = view.findViewById(R.id.downImageYellow);
    }


    public void setupListeners(){
        editContactsButton.setOnClickListener(view -> profileFragmentPresenter.onSavePressed());
        editProfileButton.setOnClickListener(view -> profileFragmentPresenter.onEditPressed());
        saveProfileButton.setOnClickListener(view -> profileFragmentPresenter.onSavePressed());
        editWatchwordButton.setOnClickListener(view -> profileFragmentPresenter.onEditWatchwordPressed());
        saveWatchwordButton.setOnClickListener(view -> profileFragmentPresenter.onSaveWatchwordPressed());
        for(int i=0; i<Colours.values().length; i++){
            shirtButton[i].setOnClickListener(view -> profileFragmentPresenter.onShirtPressed());
        }
        for(int i=0; i<Colours.values().length; i++){
            pantsButton[i].setOnClickListener(view -> profileFragmentPresenter.onPantsPressed());
        }
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

    @Override
    public void onRenderView(Boolean edit) {
        if (edit) {
            editProfileButton.setVisibility(View.GONE);
            saveProfileButton.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.GONE);
            ageText.setVisibility(View.GONE);
            editName.setText(nameText.getText());
            editAge.setText(ageText.getText());
            editName.setVisibility(View.VISIBLE);
            editAge.setVisibility(View.VISIBLE);
        } else{
            editProfileButton.setVisibility(View.VISIBLE);
            saveProfileButton.setVisibility(View.GONE);
            if(!editName.getText().toString().isEmpty())
                nameText.setText(editName.getText());
            if(!editAge.getText().toString().isEmpty())
                ageText.setText(editAge.getText());
            nameText.setVisibility(View.VISIBLE);
            ageText.setVisibility(View.VISIBLE);
            editName.setVisibility(View.GONE);
            editAge.setVisibility(View.GONE);
        }
    }

    public void saveData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!editName.getText().toString().isEmpty())
            editor.putString("Name", editName.getText().toString());
        if(!editAge.getText().toString().isEmpty())
            editor.putString("Age", editAge.getText().toString());
        editor.commit();
    }

    @Override
    public void onRenderWatchwordView(Boolean edit) {
        if (edit) {
            editWatchwordButton.setVisibility(View.GONE);
            saveWatchwordButton.setVisibility(View.VISIBLE);
            watchwordMessageText.setVisibility(View.GONE);
            watchwordResponseText.setVisibility(View.GONE);
            editWatchwordMessage.setText(watchwordMessageText.getText());
            editWatchwordResponse.setText(watchwordResponseText.getText());
            editWatchwordMessage.setVisibility(View.VISIBLE);
            editWatchwordResponse.setVisibility(View.VISIBLE);
        } else{
            editWatchwordButton.setVisibility(View.VISIBLE);
            saveWatchwordButton.setVisibility(View.GONE);
            if(!editWatchwordMessage.getText().toString().isEmpty())
                watchwordMessageText.setText(editWatchwordMessage.getText());
            if(!editWatchwordResponse.getText().toString().isEmpty())
                watchwordResponseText.setText(editWatchwordResponse.getText());
            watchwordMessageText.setVisibility(View.VISIBLE);
            watchwordResponseText.setVisibility(View.VISIBLE);
            editWatchwordMessage.setVisibility(View.GONE);
            editWatchwordResponse.setVisibility(View.GONE);
        }
    }

    public void saveWatchwordData(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(!editWatchwordMessage.getText().toString().isEmpty())
            editor.putString("Message", editWatchwordMessage.getText().toString());
        if(!editWatchwordResponse.getText().toString().isEmpty())
            editor.putString("Response", editWatchwordResponse.getText().toString());
        editor.commit();
    }

    public void loadData(){
        nameText.setText(sharedPreferences.getString("Name", ""));
        ageText.setText(sharedPreferences.getString("Age", ""));
        shirtColour = Colours.values()[sharedPreferences.getInt("Shirt", 0)];
        pantsColour = Colours.values()[sharedPreferences.getInt("Pants", 0)];
        watchwordMessageText.setText(sharedPreferences.getString("Message", ""));
        watchwordResponseText.setText(sharedPreferences.getString("Response", ""));
    }

    public void refreshView(){
        shirtButton[0].setVisibility(View.GONE);
        shirtButton[shirtColour.ordinal()].setVisibility(View.VISIBLE);
        pantsButton[0].setVisibility(View.GONE);
        pantsButton[pantsColour.ordinal()].setVisibility(View.VISIBLE);
    }

    public void changeShirt(Colours colour){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(colour.ordinal()-1 < 0)
            shirtButton[Colours.values().length-1].setVisibility(View.GONE);
        else
            shirtButton[colour.ordinal()-1].setVisibility(View.GONE);
        shirtButton[colour.ordinal()].setVisibility(View.VISIBLE);

        editor.putInt("Shirt", colour.ordinal());
        editor.commit();
    }

    public void changePants(Colours colour){
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(colour.ordinal()-1 < 0)
            pantsButton[Colours.values().length-1].setVisibility(View.GONE);
        else
            pantsButton[colour.ordinal()-1].setVisibility(View.GONE);
        pantsButton[colour.ordinal()].setVisibility(View.VISIBLE);

        editor.putInt("Pants", colour.ordinal());
        editor.commit();
    }
}
