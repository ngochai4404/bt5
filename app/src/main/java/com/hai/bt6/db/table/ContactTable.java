package com.hai.bt6.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hai.bt6.db.DatabaseManager;
import com.hai.bt6.model.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hai on 22/07/2018.
 */

public class ContactTable {
    public static final String TABLE_NAME = "contact";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE= "phome";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_PHONE + " TEXT"
                    + ")";
    public int insertContact(Contact contact, DatabaseManager data) {
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getNumber());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return (int) id;
    }
    public List<Contact> getAllContact(DatabaseManager databaseManager) {
        List<Contact> contacts = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;

        SQLiteDatabase db = databaseManager.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                contact.setNumber(cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));

                contacts.add(contact);
            } while (cursor.moveToNext());
        }

        db.close();
        return contacts;
    }
    public void deleteContact(Contact contact,DatabaseManager databaseManager) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }
    public void updateNote(Contact contact,DatabaseManager databaseManager) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getNumber());


        // updating row
        int id =  db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }
}