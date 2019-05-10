package com.ucm.informatica.spread.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ucm.informatica.spread.R;

import static com.ucm.informatica.spread.Utils.Constants.TUTORIAL_ARGS;

public class PlaceholderTutorialFragment extends Fragment {
    private TextView titleTutorial;
    private TextView subtitleTutorial;
    private ImageView imageTutorial;

    public PlaceholderTutorialFragment() {
    }
    public static PlaceholderTutorialFragment newInstance(int sectionNumber) {
        PlaceholderTutorialFragment fragment = new PlaceholderTutorialFragment();
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
        switch (getArguments().getInt(TUTORIAL_ARGS)) {
            case 0 :
                imageTutorial.setColorFilter(ContextCompat.getColor(getContext(),R.color.hintTextColor));
                titleTutorial.setText("Bienvenido a Spread");
                imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.ic_areas));
                subtitleTutorial.setText("Veamos que puedes hacer");
                break;
            case 1 :
                titleTutorial.setText("Avisa");
                imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_alert));
                subtitleTutorial.setText("Informa con un solo click cuando lo necesites a quienes estén cerca de ti y también a conocidos.");
                break;
            case 2 :
                titleTutorial.setText("Actívate");
                imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_share));
                subtitleTutorial.setText("Si ves que hay publicidad que consideres ofensiva, compartela y para crear una denuncia colectiva.");
                break;
            case 3 :
                titleTutorial.setText("Investiga");
                imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_map));
                subtitleTutorial.setText("Curiosea que ha ocurrido recientemente en tu zona o en cualquier otra.");
                break;
            case 4 :
                titleTutorial.setText("Ayuda");
                imageTutorial.setImageDrawable(getResources().getDrawable(R.drawable.tutorial_save));
                subtitleTutorial.setText("Acude a ayudar cuando te lleguen notificaciones.");
                break;
        }
    }
}

