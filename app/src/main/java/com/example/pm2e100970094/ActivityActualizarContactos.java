package com.example.pm2e100970094;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm2e100970094.configuracion.SQLiteConexion;
import com.example.pm2e100970094.transacciones.Transacciones;

public class ActivityActualizarContactos extends AppCompatActivity {

    SQLiteConexion conexion;
    EditText nombre, telefono, notas;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contactos);

        conexion= new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

        Intent i = getIntent();
        id  = getIntent().getExtras().getString("ID");
        String name = getIntent().getExtras().getString("Nombre");
        String fono  = getIntent().getExtras().getString("Telefono");
        String notes  = getIntent().getExtras().getString("Nota");
        Button btnUpdate = (Button) findViewById(R.id.btnActualizar);

        nombre = (EditText) findViewById(R.id.nombres);
        telefono = (EditText) findViewById(R.id.numero);
        notas = (EditText) findViewById(R.id.notas);

        nombre.setText(name);
        telefono.setText(fono);
        notas.setText(notes);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizar();
            }
        });
    }
     public void irMainActivity (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void actualizar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String []  params = {id};

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, telefono.getText().toString());
        valores.put(Transacciones.nota, notas.getText().toString());

        db.update(Transacciones.tablacontactos, valores, Transacciones.id +"=?", params);
        Toast.makeText(getApplicationContext(),"Dato actualizados", Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, ActivityListView.class);
        startActivity(i);
    }


}