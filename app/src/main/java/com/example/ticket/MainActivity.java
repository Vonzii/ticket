package com.example.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    boolean isFragmentOneLoaded = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initially load the First Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new FragmentOne())
                .commit();

        Button switchButton = findViewById(R.id.switch_fragment_button);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                if (isFragmentOneLoaded) {
                    fragment = new FragmentTwo();
                } else {
                    fragment = new FragmentOne();
                }
                // Switch fragments
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();

                isFragmentOneLoaded = !isFragmentOneLoaded;
            }
        });
    }
}
