package com.sps.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button scanwifi_datacollection;
    private Button motion_datacollection;
    private Button total_service;
    private EditText room;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        room=(EditText)findViewById(R.id.editTextTextPersonName);
        scanwifi_datacollection=(Button)findViewById(R.id.button);
        motion_datacollection =(Button)findViewById(R.id.button2);
        total_service=(Button)findViewById(R.id.button3);
        scanwifi_datacollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String room_number=room.getText().toString();
                if("".equals(room_number)){
                    Toast.makeText(MainActivity.this,"Please input the room number!", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent =new Intent(MainActivity.this, SCAN.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("room number", room_number);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }
        });
        motion_datacollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, MotionDetection.class);


                startActivity(intent);
            }
        });
        total_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, Total_service.class);
                startActivity(intent);
            }
        });

    }
}