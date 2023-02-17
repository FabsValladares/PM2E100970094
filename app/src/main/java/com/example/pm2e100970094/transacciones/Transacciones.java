package com.example.pm2e100970094.transacciones;

public class Transacciones {
    public static final String NameDatabase = "EX01DB";
    //Tabla de la base de datos
    public static final String tablacontactos = "contactos";

    //Transacciones de la base de datos EX01DB
    public static final String CreateTBContactos=
            "CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, pais TEXT, "+"nombre TEXT, telefono TETX, nota TEXT, imagen BLOB)";

    public static final  String DropTableContactos= "DROP TABLE IF EXISTS personas";

    //Helpers
    public static final  String Empty= "";
}
