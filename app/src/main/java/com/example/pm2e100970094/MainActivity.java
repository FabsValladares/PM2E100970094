package com.example.pm2e100970094;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {


    Spinner spninner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spninner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[]{"Honduras (504)", "Costa Rica (506)", "Guatemala (502)", "El Salvador (503)"});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spninner.setAdapter(adapter);

    }
}