package com.example.ticket;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.Month;
import java.util.Random;

public class FragmentTwo extends Fragment {

    public FragmentTwo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        ///// All generated elements /////
        TextView ticketIdViewTop = view.findViewById(R.id.ticketIdTop);
        TextView ticketIdViewBot = view.findViewById(R.id.ticketIdBot);
        TextView startValidTopView = view.findViewById(R.id.startValidTop);
        TextView startValidBotView = view.findViewById(R.id.startValidBot);
        TextView endValidBotView = view.findViewById(R.id.endValidBot);
        TextView semesterView = view.findViewById(R.id.semester);

        // Generate and set Ticket ID
        String ticketId = generateTicketId();
        ticketIdViewTop.setText(ticketId);
        ticketIdViewBot.setText("Ticket-ID:    "+ticketId);

        // Generate and set Start Valid Dates
        String startValid = generateStartValid();
        startValidTopView.setText(startValid+" / ");
        startValidBotView.setText("Gültig von: " + startValid);

        // Generate and set End Valid Date
        String endValid = generateEndValid();
        endValidBotView.setText("Gültig bis:  "+endValid);

        String semester = generateSemester();
        semesterView.setText("Semester:  "+semester);
        ///// End of generated Elements /////

        TextView courseView = view.findViewById(R.id.course);
        TextView idTypeView = view.findViewById(R.id.idType);
        TextView ticketView = view.findViewById(R.id.ticket);
        TextView jguView = view.findViewById(R.id.jgu);
        TextView informationView = view.findViewById(R.id.information);

        courseView.setText("Niklas von Zwehl Semesterticket M Sc");
        idTypeView.setText("Ausweis: digitaler Studierendenausweis");

        ticketView.setText("RMV-HandyTicket");
        jguView.setText("SemesterTicket JGU Mainz");

        informationView.setText(("Gültig auf allen Linien im Gesamten RMV Verbundnetz und VRN- Übergangstarifgebieten. Gültig auf allen Linien im Verbundnetz des..."));

        return view;
    }

    private String generateTicketId() {
        Random random = new Random();
        // Generates a random number between 0-9
        String part1 = String.valueOf(random.nextInt(10)) + String.valueOf(random.nextInt(10));
        String part2 = "";
        for (int i = 0; i < 9; i++) {
            part2 += String.valueOf(random.nextInt(10));
        }
        String part3 = String.valueOf(random.nextInt(10)) + String.valueOf(random.nextInt(10)) + String.valueOf(random.nextInt(10));

        String ticketId = part1 + " - " + part2 + " - " + part3;

        return ticketId;
    }

    private String generateStartValid() {
        LocalDate currentDate = LocalDate.now();
        if (isDateInRangeSummer(currentDate)) {
            int currentYear = currentDate.getYear();
            return "01.04." + currentYear;
        } else {
            if(currentDate.getMonthValue() <= 3){
                int year = currentDate.getYear()-1;
                return "01.10." + year;
            }else{
                int year = currentDate.getYear();
                return "01.10." + year;
            }
        }
        //return null;
    }

    private String generateEndValid() {
        LocalDate currentDate = LocalDate.now();
        if (isDateInRangeSummer(currentDate)) {
            return String.format("30.09.%d", currentDate.getYear());
        } else {
            int year = currentDate.getMonthValue() >= 10 ? currentDate.getYear() + 1 : currentDate.getYear();
            return String.format("31.03.%d", year);
        }
        //return null;
    }

    private boolean isDateInRangeSummer(LocalDate currentDate) {
        LocalDate start = LocalDate.of(currentDate.getYear(), Month.APRIL, 1);
        LocalDate end = LocalDate.of(currentDate.getYear(), Month.SEPTEMBER, 30);
        return !currentDate.isBefore(start) && !currentDate.isAfter(end);
    }

    /*private boolean isDateInRangeWinter(LocalDate currentDate) {
        LocalDate startOfYear = LocalDate.of(currentDate.getYear(), Month.JANUARY, 1);
        LocalDate endOfYear = LocalDate.of(currentDate.getYear(), Month.DECEMBER, 31);

        LocalDate winterStartPreviousYear = LocalDate.of(currentDate.getYear() - 1, Month.OCTOBER, 1);
        LocalDate winterEndCurrentYear = LocalDate.of(currentDate.getYear(), Month.MARCH, 31);

        LocalDate winterStartCurrentYear = LocalDate.of(currentDate.getYear(), Month.OCTOBER, 1);
        LocalDate winterEndNextYear = LocalDate.of(currentDate.getYear(), Month.MARCH, 31);

        boolean isWinterFirstHalf = !currentDate.isBefore(winterStartPreviousYear) && !currentDate.isAfter(winterEndCurrentYear);
        boolean isWinterSecondHalf = !currentDate.isBefore(winterStartCurrentYear) && !currentDate.isAfter(winterEndNextYear);

        return isWinterFirstHalf || isWinterSecondHalf;
    }*/

    private String generateSemester(){
        String semester;
        int year;
        LocalDate currentDate = LocalDate.now();
        if(isDateInRangeSummer(currentDate)){
            semester = "SoSe " + currentDate.getYear();
            return semester;
        }else{
            year = currentDate.getMonthValue() >= 10 ? currentDate.getYear() + 1 : currentDate.getYear();
            semester = "WiSe " + year;
            return semester;
        }
    }

}
