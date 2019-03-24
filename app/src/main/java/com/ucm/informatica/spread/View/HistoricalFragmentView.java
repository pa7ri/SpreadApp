package com.ucm.informatica.spread.View;

import com.ucm.informatica.spread.Utils.HistoricalRecyclerAdapter;


public interface HistoricalFragmentView {

    void initView(HistoricalRecyclerAdapter adapter);

    void showErrorTransaction();
}
