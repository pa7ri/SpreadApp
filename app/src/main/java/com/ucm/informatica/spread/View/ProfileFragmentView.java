package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Model.Colours;

public interface ProfileFragmentView {

    void onRenderView(Boolean edit);

    void saveData();

    void onRenderWatchwordView(Boolean edit);

    void saveWatchwordData();

    void changeShirt(Colours colour);

    void changePants(Colours colour);


    void initView();

    void setupListeners();

    void loadData();

    void refreshView();

}
