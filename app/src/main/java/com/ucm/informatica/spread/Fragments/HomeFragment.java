package com.ucm.informatica.spread.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jackandphantom.androidlikebutton.AndroidLikeButton;
import com.ucm.informatica.spread.Activities.MainTabActivity;
import com.ucm.informatica.spread.Presenter.HomeFragmentPresenter;
import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.CustomLocationListener;
import com.ucm.informatica.spread.View.HomeFragmentView;

import static com.ucm.informatica.spread.Utils.Constants.LocalPreferences.PROFILE_PREF;
import static com.ucm.informatica.spread.Utils.Constants.REQUEST_IMAGE_POSTER;


public class HomeFragment extends Fragment implements HomeFragmentView{

    private View view;
    private HomeFragmentPresenter homeFragmentPresenter;

    private AndroidLikeButton helpButton;
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
        homeFragmentPresenter = new HomeFragmentPresenter(getContext(), this,
                    ((MainTabActivity) getActivity()).getAlertContract());
        homeFragmentPresenter.start();
        return view;
    }

    @Override
    public void initView() {
        helpButton = view.findViewById(R.id.helpButton);
        cameraButton = view.findViewById(R.id.addPosterButton);
    }

    @Override
    public CustomLocationListener getCustomLocationListener() {
        return ((MainTabActivity) getActivity()).getCustomLocationListener();
    }

    @Override
    public void setupListeners(){
        helpButton.setOnLikeEventListener(new AndroidLikeButton.OnLikeEventListener() {
            @Override
            public void onLikeClicked(AndroidLikeButton androidLikeButton) {
                androidLikeButton.setCurrentlyLiked(false);
                Location location = ((MainTabActivity) getActivity()).getCustomLocationListener().getLatestLocation();
                homeFragmentPresenter.onHelpButtonPressed(location,getResources(),
                        getContext().getSharedPreferences(PROFILE_PREF, Context.MODE_PRIVATE));
            }

            @Override
            public void onUnlikeClicked(AndroidLikeButton androidLikeButton) {
                androidLikeButton.setCurrentlyLiked(false);
            }
        });
        cameraButton.setOnClickListener(view -> {
            ((MainTabActivity) getActivity()).createPictureIntentPicker(REQUEST_IMAGE_POSTER);
        });
    }

    @Override
    public void showConfirmationTransaction() {
        ((MainTabActivity) getActivity()).showConfirmationTransaction();
    }

    @Override
    public void showErrorTransaction() {
        ((MainTabActivity) getActivity()).getErrorSnackBar(R.string.snackbar_alert_transaction).show();
    }

    @Override
    public void showErrorGPS() {
        ((MainTabActivity) getActivity()).getAlertSnackBarGPS().show();
    }

    @Override
    public void showSendConfirmation() {
        ((MainTabActivity) getActivity()).getInformationSnackBar().show();
    }
}
