package com.example.pm2e100970094;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.pm2e100970094.configuracion.SQLiteConexion;
import com.example.pm2e100970094.transacciones.Transacciones;

import java.io.ByteArrayInputStream;

public class ActivityMostrarFoto extends AppCompatActivity {

    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

    ImageView picture;

    Button regresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_foto);
        picture = (ImageView) findViewById(R.id.imageView);
        regresar = (Button)   findViewById(R.id.btnatras);

       regresar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), ActivityListView.class);
               startActivity(intent);
           }


       });
        Bitmap retornarFoto = buscarPicture(getIntent().getStringExtra("codigo"));
        picture.setImageBitmap(retornarFoto);
    }

    //Metodo Retornar
    public Bitmap buscarPicture(String id) {
        SQLiteDatabase db = conexion.getWritableDatabase();

        String sql = "SELECT imagen FROM contactos WHERE id ="+ id;
        Cursor cursor = db.rawQuery(sql, new String[] {});
        Bitmap bitmap = null;
        if(cursor.moveToFirst()){
            byte[] blob = cursor.getBlob(0);
            ByteArrayInputStream bais = new ByteArrayInputStream(blob);
            bitmap = BitmapFactory.decodeStream(bais);
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
        return bitmap;
    }
}