package com.example.ticket;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import java.io.File;

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

        // Check if username and profile image exist
        boolean usernameExists = new File(getApplicationContext().getFilesDir(), "username.txt").exists();
        boolean profileImageExists = new File(getApplicationContext().getFilesDir(), "profile.jpg").exists();

        // If both files exist, switch to the other fragment immediately
        if (usernameExists && profileImageExists) {
            Fragment fragment = new FragmentTwo(); // Assuming you want to switch to FragmentTwo in this case
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            isFragmentOneLoaded = false; // Update the flag accordingly
        }

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.black));
            //window.setNavigationBarColor(ContextCompat.getColor(this, R.color.red));
        }
    }
}

