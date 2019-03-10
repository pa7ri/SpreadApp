package com.ucm.informatica.spread.Presenter;

import com.ucm.informatica.spread.View.ProfileFragmentView;

import com.ucm.informatica.spread.Model.Colours;

public class ProfileFragmentPresenter {

    private Boolean editView = false;
    private Boolean editWatchword = false;
    private Colours shirtColour = Colours.NA;
    private Colours pantsColour = Colours.NA;

    private ProfileFragmentView profileFragmentView;

    public ProfileFragmentPresenter(ProfileFragmentView profileFragmentView){
        this.profileFragmentView = profileFragmentView;
    }

    public void onEditPressed(){
        editView = !editView;
        profileFragmentView.onRenderView(editView);
    }

    public void onSavePressed(){
        onEditPressed();
        profileFragmentView.saveData();
    }

    public void onEditWatchwordPressed(){
        editWatchword = !editWatchword;
        profileFragmentView.onRenderWatchwordView(editWatchword);
    }

    public void onSaveWatchwordPressed(){
        onEditWatchwordPressed();
        profileFragmentView.saveWatchwordData();
    }

    public void onShirtPressed(){
        shirtColour = Colours.values()[(shirtColour.ordinal()+1)%Colours.values().length];
        profileFragmentView.changeShirt(shirtColour);
    }

    public void onPantsPressed(){
        pantsColour = Colours.values()[(pantsColour.ordinal()+1)%Colours.values().length];
        profileFragmentView.changePants(pantsColour);
    }
}
