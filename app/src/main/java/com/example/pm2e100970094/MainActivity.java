package com.example.pm2e100970094;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2e100970094.configuracion.SQLiteConexion;
import com.example.pm2e100970094.transacciones.Transacciones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
    SQLiteDatabase db;
    EditText nombre, telefono, nota;
    Spinner spninner;
    Button btnver;
    Bitmap imagen;
    static final  int REQUEST_IMAGE = 101;
    static final  int PETICION_ACCESS_CAM = 201;



    ImageView imageView;
    ImageButton btntakefoto;
    String currentPhotoPath;
    int codigoPaisSeleccionado;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre= (EditText) findViewById(R.id.nombres);
        telefono = (EditText) findViewById(R.id.telefono);
        nota = (EditText) findViewById(R.id.notas);
        imageView = (ImageView) findViewById(R.id.imageView);
        btntakefoto = (ImageButton) findViewById(R.id.takepicture);

        Button btnsalvar= (Button) findViewById(R.id.btnsalvar);

        spninner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[]{"Honduras (504)", "Costa Rica (506)", "Guatemala (502)", "El Salvador (503)"});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spninner.setAdapter(adapter);

        btntakefoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnver =(Button) findViewById(R.id.btnvercontactos);
        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
                startActivity(intent);
            }
        });
        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    validarDatos();
            }
        });


    }

    private void validarDatos() {
         if (nombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (telefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (nota.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir una nota" ,Toast.LENGTH_LONG).show();
        }else {
             salvarContacto(imagen);
         }
    }
    private void salvarContacto(Bitmap bitmap) {
try {
         conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
         db = conexion.getWritableDatabase();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayImagen  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        String Pais = spninner.getSelectedItem().toString();
        String PaisText = Pais.substring(0, Pais.length() - 6);
        String PaisNumber = Pais.substring(Pais.length() - 4, Pais.length() - 1);

        valores.put(Transacciones.pais, PaisText);
        valores.put(Transacciones.nombre, nombre.getText().toString());
        valores.put(Transacciones.telefono, PaisNumber + telefono.getText().toString());
        valores.put(Transacciones.nota, nota.getText().toString());
        valores.put(String.valueOf(Transacciones.imagen),ArrayImagen);



        Long resultado = db.insert(Transacciones.tablacontactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro ingreso con exito, Codigo " + resultado.toString()
                ,Toast.LENGTH_LONG).show();

        db.close();
    } catch (Exception ex) {
        Toast.makeText(getApplicationContext(),"Se produjo un error",Toast.LENGTH_LONG).show();
    }

        ClearScreen();
    }
    private void ClearScreen() {
        spninner.setSelection(0);
        nombre.setText("");
        telefono.setText("");
        nota.setText("");;
    }

    private void permisos()
    {
        // Metodo para obtener los permisos requeridos de la aplicacion
        if(ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},PETICION_ACCESS_CAM);
        }
        else
        {
            dispatchTakePictureIntent();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PETICION_ACCESS_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                dispatchTakePictureIntent();

            }
            else
            {
                Toast.makeText(getApplicationContext(), "se necesita el permiso de la camara",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE)
        {

            try {
                File foto = new File(currentPhotoPath);
                imagen = BitmapFactory.decodeFile(foto.getAbsolutePath());
                imageView.setImageURI(Uri.fromFile(foto));
            }
            catch (Exception ex)
            {
                ex.toString();
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.toString();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.pm2e100970094.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE);
            }
        }
    }
}