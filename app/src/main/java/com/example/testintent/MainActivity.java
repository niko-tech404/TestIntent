package com.example.testintent;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    Button btnSendSMS;
    Button btnDial;
    Button setAlarm;
    Button btnSetEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnSendSMS= findViewById(R.id.btn_send_sms);
        btnDial = findViewById(R.id.btn_dial);
        setAlarm = findViewById(R.id.btn_set_alarm);
        btnSetEvent = findViewById(R.id.btn_set_event);


        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("MainActivity", "Button SendSMS clicked!");

                 Intent intent = new Intent(Intent.ACTION_SENDTO);
                 intent.setData(Uri.parse("sms: +39 3899445710"));
                 intent.putExtra("sms_body", "Ciao Gino, arrivo appena finisco!" );
                 if(intent.resolveActivity(getPackageManager()) != null){
                     startActivity(intent);
                     Log.d("MainActivity", "Button SendSMS avviato!");
                 }
            }
        });

        btnDial.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("MainActivity", "Button call clicked!");

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: +39 3899445710"));

                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }else{
                    Log.d("MainActivity", "Nessun app disponibile per le chiamate!");
                }
            }
        });

        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Button Alarm clicked!");

                Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, "Andare a casa");
                intent.putExtra(AlarmClock.EXTRA_HOUR, 14);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, 30);

                // Risolve bug di compatibilità sui launcher Xiaomi/HyperOS
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                try {
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("MainActivity", "Errore nel lancio dell'allarme: " + e.getMessage());
                }
            }
        });

        btnSetEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "Button set event clicked");

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(2026, Calendar.NOVEMBER, 3, 15, 0);
                Calendar endTime= Calendar.getInstance();
                endTime.set(2026, Calendar.NOVEMBER, 3, 16, 0);

                Intent intent = new Intent(Intent.ACTION_INSERT);
                intent.setData(CalendarContract.Events.CONTENT_URI)
                        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                        intent.putExtra(CalendarContract.Events.TITLE, "Titolo Evento")
                        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Descrizione evento")
                        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Luogo")
                        intent.putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);

                if (intent.resolveActivity(view.getContext().getPackageManager()) != null) {
                    view.getContext().startActivity(intent);
                } else {
                    Log.w("MainActivity", "Nessuna app calendario trovata per gestire l'intent");
                }

            }
        });
    }
}