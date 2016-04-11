package com.adamcrossan.naoapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayFragment extends Fragment {

    public DisplayFragment() {
        // Required empty public constructor
    }

    public void changeText(String sentence){
        TextView t = (TextView) this.getView().findViewById(R.id.textLat);
        t.setText(sentence);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          View view = inflater.inflate(R.layout.fragment_display, container, false);
        return view;
    }
}
