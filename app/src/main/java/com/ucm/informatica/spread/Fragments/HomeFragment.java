package com.ucm.informatica.spread.Fragments;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.andrognito.flashbar.Flashbar;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Presenter.HomeFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.View.HomeFragmentView;
public class HomeFragment extends Fragment implements HomeFragmentView{

    private View view;
    private HomeFragmentPresenter homeFragmentPresenter;

    private TextView nameText;
    private Button helpButton;
    private FloatingActionButton cameraButton;

    public HomeFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        homeFragmentPresenter = new HomeFragmentPresenter(this, this);
        setupListeners();
        return view;
    }
    private void initView() {
        nameText = view.findViewById(R.id.nameText);
        helpButton = view.findViewById(R.id.helpButton);
        cameraButton = view.findViewById(R.id.addAdvertisingButton);
    }

    private void setupListeners(){
        helpButton.setOnClickListener(view -> {
                Location location = ((MainTabActivity) getActivity()).getLocation();
                if(location != null) {
                    homeFragmentPresenter.saveData(getResources().getString(R.string.button_help),
                            getResources().getString(R.string.button_help_description),
                            Double.toString(location.getLongitude()),
                            Double.toString(location.getLatitude()));
                } else {
                    ((MainTabActivity) getActivity()).getAlertSnackBarGPS().show();
                }
        });
        cameraButton.setOnClickListener(view -> { //TODO : implement camera call
            new Flashbar.Builder(getActivity())
                    .gravity(Flashbar.Gravity.BOTTOM)
                    .duration(2500)
                    .backgroundColorRes(R.color.warm_grey)
                    .message("Saliendo de la app...se abre la cámara")
                    .build()
                    .show();
        });
    }

    @Override
    public void showSuccessfulStoredTransition(String result) {
        ((MainTabActivity) getActivity()).getConfirmationSnackBar().show();
        nameText.setText(result);
    }

    @Override
    public void showErrorTransition() {
        ((MainTabActivity) getActivity()).getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }
}
