package com.galacticCat.chatbleu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.galacticCat.chatbleu.model.User;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper {
    private SQLiteDatabase mDatabase;

    /**
     * Adicionar funciones de la DB para que no esten en las Activities
     *
     * @param context
     */
    public DataBaseHelper(Context context) {
        DataBase instancia = new DataBase(context);
        this.mDatabase = instancia.getWritableDatabase();
    }

    /**
     * Insertar el nuevo usuario en la DB
     *
     * @param user Obj de Usuario llenado desde Register
     */
    public void insert(User user) {
        //Key: coliumn, Value: valor
        ContentValues contentValues = new ContentValues();
        contentValues.put("usuario", user.getNombreUsuario());
        contentValues.put("edad", user.getEdad());
        contentValues.put("peso", user.getPeso());

        //Insertar el usuario
        this.mDatabase.insert("usuarios", //Tabla
                null,
                contentValues); // Valores
        this.mDatabase.close(); //Cerrar
    }

    /**
     * Llamada desde Login Activity
     *
     * @param usuario  Usuario llenado el Login
     * @param password Password llenado el login
     * @return true/false Usuario y password existen en la db
     */
    public boolean login(String usuario, String password) {
        //Parametros en String array
        String[] params = new String[2];
        params[0] = usuario;
        params[1] = password;

        Cursor cursor = this.mDatabase.rawQuery("SELECT name FROM usuarios" +
                " WHERE usuario=? AND password = ?", params);


        if (cursor.moveToFirst()) {
            Log.d("name", "" + cursor.getInt(0));
            return true;
        } else {
            return false;
        }
    }

    public List<User> getAll() {
        List<User> results = new ArrayList<>();
        Cursor cursor = this.mDatabase.rawQuery("SELECT " +
                " id," + //0
                " usuario," + //1
                " edad," + //3
                " peso" + //5
                " FROM usuarios", null);

        if (cursor.moveToFirst()) {
            do {
                //Extraemos los datos
                int id = cursor.getInt(0);
                String usuario = cursor.getString(1);
                int edad = cursor.getInt(3);
                int peso = cursor.getInt(5);

                //Llnear objeto de tipo user
                User user = new User();

                user.setNombreUsuario(usuario);

                user.setEdad(edad);

                user.setPeso(peso);

                //Adicionar a la lista
                results.add(user);
            } while (cursor.moveToNext());
        }
        return results;
    }

    public int getCount() {
        Cursor cursor = this.mDatabase.rawQuery("SELECT COUNT(0) FROM usuarios", null);
        if (cursor.moveToFirst()) {
            int cantidad = cursor.getInt(0);
            return cantidad;
        } else {
            return 0;
        }
    }

    public void update(User user) {

    }

    public void delete(int id) {
        String[] params = new String[1];
        params[0] = String.valueOf(id);

        mDatabase.delete("usuarios", "id=?", params);
    }
}

