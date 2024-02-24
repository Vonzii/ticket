package com.example.ticket;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

public class FragmentTwo extends Fragment {

    private TextView startValidTopView;
    private Handler timerHandler = new Handler();
    private long startTime = 0;
    public String userName = null;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            String startValid = generateStartValid();
            startValidTopView.setText(startValid + " / " + String.format("%02d:%02d", minutes, seconds));
            timerHandler.postDelayed(this, 1000);
        }
    };

    public FragmentTwo() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        initializeBackgroundImage(view);
        initializeUsername(view);
        initializeTicketViews(view);
        initializeUserImageView(view);
        initializeIdentificationTypeView(view);
        initializeTicketInfoView(view);
        initializeJGUView(view);
        initializeInformationView(view);

        return view;
    }

    private void initializeBackgroundImage(View view) {
        ImageView imageView = view.findViewById(R.id.imageBehind);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate);
        imageView.startAnimation(animation);
    }

    private void initializeTicketViews(View view) {
        TextView ticketIdViewTop = view.findViewById(R.id.ticketIdTop);
        TextView ticketIdViewBot = view.findViewById(R.id.ticketIdBot);
        startValidTopView = view.findViewById(R.id.startValidTop);
        TextView startValidBotView = view.findViewById(R.id.startValidBot);
        TextView endValidBotView = view.findViewById(R.id.endValidBot);
        TextView semesterView = view.findViewById(R.id.semester);

        startTimer();

        String ticketId = generateTicketId();
        ticketIdViewTop.setText(ticketId);
        ticketIdViewBot.setText("Ticket-ID:    " + ticketId);

        String startValid = generateStartValid();
        startValidBotView.setText("Gültig von: " + startValid);

        String endValid = generateEndValid();
        endValidBotView.setText("Gültig bis:  " + endValid);

        String semester = generateSemester();
        semesterView.setText("Semester:  " + semester);
    }

    private void initializeUserImageView(View view) {
        ImageView userImageView = view.findViewById(R.id.userImage);
        File imgFile = new File(getContext().getFilesDir(), "profile.jpg");

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            userImageView.setImageBitmap(myBitmap);
        } else {
            userImageView.setImageResource(R.drawable.cappucinobear);
        }
    }

    private void initializeUsername(View view) {
        TextView courseView = view.findViewById(R.id.course);
        File file = new File(getContext().getFilesDir(), "username.txt");
        if (file.exists()) {
            StringBuilder text = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line);

                    //text.append("\n"); // Uncomment if multiline content is expected
                }
                userName = text.toString();
                courseView.setText(text.toString() + " Semesterticket B Sc");
            } catch (IOException e) {
                Log.e("FragmentTwo", "Error reading username.txt", e);
            }
        } else {
            courseView.setText("Daniel Düsentrieb Semesterticket M Sc");
        }
    }


    private void initializeIdentificationTypeView(View view) {
        TextView idTypeView = view.findViewById(R.id.idType);
        idTypeView.setText("Ausweis: digitaler Studierendenausweis");
    }

    private void initializeTicketInfoView(View view) {
        TextView ticketView = view.findViewById(R.id.ticket);
        ticketView.setText("RMV-HandyTicket");
    }

    private void initializeJGUView(View view) {
        TextView jguView = view.findViewById(R.id.jgu);
        jguView.setText("SemesterTicket JGU Mainz");
    }

    private void initializeInformationView(View view) {
        TextView informationView = view.findViewById(R.id.information);
        informationView.setText("Gültig auf allen Linien im Gesamten RMV Verbundnetz und VRN- Übergangstarifgebieten. Gültig auf allen Linien im Verbundnetz des...");
    }

    private String generateTicketId() {
        Random random;
        if(userName != null){
            random = new Random(userName.hashCode());
            Log.d("yoo1", random.toString());
        }else{
            random = new Random(69);
            Log.d("yoo2", random.toString());
        }
        String part1 = String.valueOf(random.nextInt(10)) + String.valueOf(random.nextInt(10));
        String part2 = "";
        for (int i = 0; i < 9; i++) {
            part2 += String.valueOf(random.nextInt(10));
        }
        String part3 = String.valueOf(random.nextInt(10)) + String.valueOf(random.nextInt(10)) + String.valueOf(random.nextInt(10));
        return part1 + " - " + part2 + " - " + part3;
    }

    private String generateStartValid() {
        LocalDate currentDate = LocalDate.now();
        if (isDateInRangeSummer(currentDate)) {
            return "01.04." + currentDate.getYear();
        } else {
            int year = currentDate.getMonthValue() <= 3 ? currentDate.getYear() - 1 : currentDate.getYear();
            return "01.10." + year;
        }
    }

    private String generateEndValid() {
        LocalDate currentDate = LocalDate.now();
        if (isDateInRangeSummer(currentDate)) {
            return String.format("30.09.%d", currentDate.getYear());
        } else {
            int year = currentDate.getMonthValue() >= 10 ? currentDate.getYear() + 1 : currentDate.getYear();
            return String.format("31.03.%d", year);
        }
    }

    private boolean isDateInRangeSummer(LocalDate currentDate) {
        LocalDate start = LocalDate.of(currentDate.getYear(), Month.APRIL, 1);
        LocalDate end = LocalDate.of(currentDate.getYear(), Month.SEPTEMBER, 30);
        return !currentDate.isBefore(start) && !currentDate.isAfter(end);
    }

    private String generateSemester() {
        LocalDate currentDate = LocalDate.now();
        if (isDateInRangeSummer(currentDate)) {
            return "SoSe " + currentDate.getYear();
        } else {
            int year = currentDate.getMonthValue() >= 10 ? currentDate.getYear() + 1 : currentDate.getYear();
            return "WiSe " + year;
        }
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    private void stopTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }
}
