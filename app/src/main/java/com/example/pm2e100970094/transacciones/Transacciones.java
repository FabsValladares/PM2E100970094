package com.example.pm2e100970094.transacciones;

public class Transacciones {
    public static final String NameDatabase = "EX01DB";
    //Tabla de la base de datos
    public static final String tablacontactos = "contactos";

    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";
    public static final String imagen = "imagen";
    public static final String pais = "pais";

    //Transacciones de la base de datos EX01DB
    public static final String CreateTBContactos=
            "CREATE TABLE contactos (id INTEGER PRIMARY KEY AUTOINCREMENT, pais TEXT, "+"nombre TEXT, telefono INTEGER, nota TEXT, imagen BLOB)";

    public static final  String DropTableContactos= "DROP TABLE IF EXISTS contactos";

    //Helpers
    public static final  String Empty= "";
}
