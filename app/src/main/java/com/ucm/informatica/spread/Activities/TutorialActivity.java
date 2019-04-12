package com.ucm.informatica.spread.Activities;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.ucm.informatica.spread.R;

import me.relex.circleindicator.CircleIndicator;

import static com.ucm.informatica.spread.Utils.Constants.TUTORIAL_ARGS;


public class TutorialActivity extends AppCompatActivity {

    private SectionsPagerAdapter tutorialPagerAdapter;

    private ViewPager tutorialViewPager;
    private Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        tutorialPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        CircleIndicator indicator = findViewById(R.id.indicator);
        tutorialViewPager = findViewById(R.id.tutorialViewPager);
        continueButton = findViewById(R.id.continueButton);


        tutorialViewPager.setAdapter(tutorialPagerAdapter);
        indicator.setViewPager(tutorialViewPager);

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

    public static class PlaceholderFragment extends Fragment {
        private TextView titleTutorial;
        private TextView subtitleTutorial;
        private ImageView imageTutorial;

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(TUTORIAL_ARGS, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tutorial, container, false);
            titleTutorial = rootView.findViewById(R.id.titleTutorial);
            subtitleTutorial = rootView.findViewById(R.id.subtitleTutorial);
            imageTutorial = rootView.findViewById(R.id.imageTutorial);

            initContent();
            return rootView;
        }

        private void initContent(){
            imageTutorial.setColorFilter(ContextCompat.getColor(getContext(),R.color.hintTextColor));
            switch (getArguments().getInt(TUTORIAL_ARGS)) {
                case 0 :
                    titleTutorial.setText("Bienvenido a Spread");
                    imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ic_areas));
                    subtitleTutorial.setText("Veamos que puedes hacer");
                    break;
                case 1 :
                    titleTutorial.setText("Avisa");
                    imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ic_alert));
                    subtitleTutorial.setText("Informa con un solo click cuando lo necesites a quienes estén cerca de ti y también a conocidos.");
                    break;
                case 2 :
                    titleTutorial.setText("Actívate");
                    imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ic_pic));
                    subtitleTutorial.setText("Si ves que hay publicidad que consideres ofensiva, compartela y para crear una denuncia colectiva.");
                    break;
                case 3 :
                    titleTutorial.setText("Investiga");
                    imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ic_map));
                    subtitleTutorial.setText("Curiosea que ha ocurrido recientemente en tu zona o en cualquier otra.");
                    break;
                case 4 :
                    titleTutorial.setText("Ayuda");
                    imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ic_add));
                    subtitleTutorial.setText("Acude a ayudar cuando te lleguen notificaciones.");
                    break;
            }
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}
