package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.ucm.informatica.spread.R;
import com.ucm.informatica.spread.Utils.SectionsPagerAdapter;

import me.relex.circleindicator.CircleIndicator;

public class TutorialActivity extends AppCompatActivity {

    private Button continueButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        initView();
        setUpListeners();
    }

    private void initView() {
        SectionsPagerAdapter tutorialPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        CircleIndicator indicator = findViewById(R.id.indicator);
        ViewPager tutorialViewPager = findViewById(R.id.tutorialViewPager);
        continueButton = findViewById(R.id.continueButton);
        tutorialViewPager.setAdapter(tutorialPagerAdapter);
        indicator.setViewPager(tutorialViewPager);
    }

    private void setUpListeners() {
        continueButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainTabActivity.class);
            startActivity(intent);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tutorial, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
