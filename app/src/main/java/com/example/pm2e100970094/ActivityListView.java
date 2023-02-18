package com.example.pm2e100970094;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm2e100970094.configuracion.SQLiteConexion;
import com.example.pm2e100970094.transacciones.Contactos;
import com.example.pm2e100970094.transacciones.Transacciones;

import java.util.ArrayList;

public class ActivityListView extends AppCompatActivity {
    Button btnatras;
    Button btneliminar;
    Button btnllamar;
    Button btnmodificar;

    Button btnverImagen;

    Button btnEnviar;

    private EditText busqueda;
    private String Dato;
    private String Nombre;
    private String Telefono;
    private String Nota;
    private Boolean SelectedRow = false;


    private static final int REQUEST_CALL = 1;
    SQLiteConexion conexion;
    EditText buscar;
    ListView ListaContactos;
    ArrayList<Contactos> ArrayLista;
    ArrayList<String> ArrayContactos;

    Contactos Contacto;

    private AlertDialog.Builder EliminarItem;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        buscar = (EditText) findViewById(R.id.txtbuscar);
        ListaContactos = (ListView) findViewById(R.id.txtlista);
        busqueda = (EditText) findViewById(R.id.txtbuscar);
        btneliminar = (Button) findViewById(R.id.btn_eliminar);
        btnllamar = (Button) findViewById(R.id.btn_llamar);
        btnmodificar = (Button) findViewById(R.id.btn_actualizar);
        btnverImagen = (Button) findViewById(R.id.btn_ver);
        btnEnviar = (Button) findViewById(R.id.btn_compartir);

        //LLENADO DE LA LISTA
        ObtenerListaContactos();

        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, ArrayContactos);
        ListaContactos.setAdapter(adp);

        buscar = (EditText) findViewById(R.id.txtbuscar);
        buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                adp.getFilter().filter(charSequence);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ListaContactos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ListaContactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                Dato = "" + ArrayLista.get(position).getId();
                Nombre = "" + ArrayLista.get(position).getNombre();
                Telefono = "+" + ArrayLista.get(position).getTelefono();
                Nota = "" + ArrayLista.get(position).getNota();
                SelectedRow = true;
            }
        });

        //BOTON DE ATRAS
        btnatras = (Button) findViewById(R.id.btn_atras);
        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        //BOTON ELIMINAR
        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedRow == true) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActivityListView.this);
                    builder.setMessage("Desea eliminar a " + Nombre);
                    builder.setTitle("Eliminar");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            eliminar();
                            Intent intent = new Intent(ActivityListView.this, ActivityListView.class);
                            startActivity(intent);
                            finish();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Toast.makeText(ActivityListView.this, "Seleccione un contacto de la lista", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //BOTON LLAMAR
        btnllamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedRow==true){
                    AlertDialog.Builder builder= new AlertDialog.Builder(ActivityListView.this);
                    builder.setMessage("llamar a "+ Nombre);
                    builder.setTitle("Llamar");

                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           Realizarllamada();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else {
                    Toast.makeText( ActivityListView.this, "Seleccione un contacto de la lista", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //BOTON DE MOSTRAR Imagen

        btnverImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(getApplicationContext(), ActivityMostrarFoto.class);
                    startActivity(intent);
                    intent.putExtra("codigo", Dato + "");
                    startActivity(intent);
                } catch (NullPointerException e) {
                    Intent intent = new Intent(getApplicationContext(), ActivityMostrarFoto.class);
                    intent.putExtra("codigo", "1");
                    startActivity(intent);
                }
            }
        });

       //BOTON COMPARTIR CONTACTO
        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarContacto();
            }
        });

        //BOTON ACTUALIZAR

        btnmodificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SelectedRow==true){
                    Intent i = new Intent(getApplicationContext(), ActivityActualizarContactos.class);
                    i.putExtra("ID", Dato);
                    i.putExtra("Nombre", Nombre);
                    i.putExtra("Telefono", Telefono);
                    i.putExtra("Nota", Nota);
                    startActivity(i);
                    finish();
                }
                else {
                    Toast.makeText(ActivityListView.this, "Seleccione un contacto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




        private void eliminar () {
            SQLiteDatabase db = conexion.getWritableDatabase();
            String[] params = {Dato};
            String wherecond = Transacciones.id + "=?";
            db.delete(Transacciones.tablacontactos, wherecond, params);
            Toast.makeText(getApplicationContext(), "Dato eliminado correctamente", Toast.LENGTH_LONG).show();
        }



        private void ObtenerListaContactos () {
            SQLiteDatabase db = conexion.getReadableDatabase();
            Contactos listviewContactos = null;
            ArrayLista = new ArrayList<Contactos>();

            Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablacontactos, null);

            while (cursor.moveToNext()) {
                listviewContactos = new Contactos();
                listviewContactos.setId(cursor.getInt(0));
                listviewContactos.setNombre(cursor.getString(2));
                listviewContactos.setTelefono(cursor.getString(3));
                listviewContactos.setNota(cursor.getString(4));
                ArrayLista.add(listviewContactos);
            }
            cursor.close();
            FillList();
        }

        private void FillList () {

            ArrayContactos = new ArrayList<String>();

            for (int i = 0; i < ArrayLista.size(); i++) {

                ArrayContactos.add(ArrayLista.get(i).getNombre() + " | "
                        + ArrayLista.get(i).getTelefono());
            }
        }



    private void enviarContacto(){
        String contactoEnviado = "El numero de: "+Nombre+" es "+Telefono;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contactoEnviado);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void Realizarllamada() {
        String number = Telefono;
        if (SelectedRow==true){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
        else{
            Toast.makeText(this, "Seleccione un contacto de la Lista", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode ==REQUEST_CALL) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Realizarllamada();
            }else{
                Toast.makeText(this, "Permisos No otorgados", Toast.LENGTH_SHORT).show();
            }
        }
    }
}





