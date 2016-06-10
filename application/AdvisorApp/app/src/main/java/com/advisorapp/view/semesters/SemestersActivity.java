package com.advisorapp.view.semesters;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.advisorapp.R;

/**
 * Created by Steeve on 08/06/2016.
 */
public class SemestersActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semesters);

        Fragment f = new SemestersFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment, f, "SemestersFragment").commit();
    }
}
