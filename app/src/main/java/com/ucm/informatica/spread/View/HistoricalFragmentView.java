package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Utils.CustomRecyclerAdapter;


public interface HistoricalFragmentView {

    void initView(CustomRecyclerAdapter adapter);

    void showErrorTransition();
}
