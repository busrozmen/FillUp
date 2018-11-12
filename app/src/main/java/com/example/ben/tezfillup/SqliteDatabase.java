package com.example.ben.tezfillup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SqliteDatabase extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";

    public static final String DATABASE_NAME = "fillup";

    public static final String TABLE_PERSON = "person";
    public static final String PERSON_ID = "id";
    public static final String PERSON_EMAIL = "email";
    public static final String PERSON_PASSWORD = "password";
    public static final String PERSON_REMEMBERME = "rememberme";

    public static final String TABLE_ACCOUNT = "account";
    public static final String ACCOUNT_ID = "id";
    public static final String ACCOUNT_EMAIL = "email";
    public static final String ACCOUNT_TCNO = "tcno";
    public static final String ACCOUNT_SOYAD = "soyad";
    public static final String ACCOUNT_AD = "ad";
    public static final String ACCOUNT_BABAAD = "babaadi";
    public static final String ACCOUNT_ANAAD = "anaadi";
    public static final String ACCOUNT_DOGUMYERI = "dogumyeri";
    public static final String ACCOUNT_DOGUMTARIHI = "dogumtarihi";


    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_PERSON + "("
                + PERSON_ID + " INTEGER PRIMARY KEY, "
                + PERSON_EMAIL + " TEXT, "
                + PERSON_PASSWORD + " TEXT, "
                + PERSON_REMEMBERME + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ACCOUNT + "("
                + ACCOUNT_ID + " INTEGER PRIMARY KEY, "
                + ACCOUNT_EMAIL + " TEXT, "
                + ACCOUNT_TCNO + " TEXT, "
                + ACCOUNT_SOYAD + " TEXT, "
                + ACCOUNT_AD + " TEXT, "
                + ACCOUNT_BABAAD + " TEXT, "
                + ACCOUNT_ANAAD + " TEXT, "
                + ACCOUNT_DOGUMYERI + " TEXT, "
                + ACCOUNT_DOGUMTARIHI + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
        onCreate(db);
    }



    public String LoginUser(String email, String password){
        String Email = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM person WHERE email=? AND password=?",new String[]{email,password});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            Email = cursor.getString(0);
            cursor.close();
        }
        return Email;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERSON,// Selecting Table
                new String[]{PERSON_ID, PERSON_EMAIL, PERSON_PASSWORD, PERSON_REMEMBERME},//Selecting columns want to query
                PERSON_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);
        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }
        //if email does not exist return false
        return false;
    }



    public void InsertUser(String email, String password, String rememberme){
        SQLiteDatabase db = this.getReadableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(PERSON_EMAIL, email);
            cv.put(PERSON_PASSWORD, password);
            cv.put(PERSON_REMEMBERME, rememberme);
            db.insert(TABLE_PERSON, null, cv);
        }
        catch (Exception e){
        }
        db.close();
    }

    public List<String> UserList(String rememberme){
        List<String> users = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT id, email, password, rememberme FROM person WHERE rememberme=?",new String[]{rememberme});

            while (cursor.moveToNext()){
                users.add(cursor.getString(0) + "-"
                        + cursor.getString(1) + "-"
                        + cursor.getString(2) + "-"
                        + cursor.getString(3));
            }
        }
        catch (Exception e){
        }
        db.close();
        return users;
    }

    public void UpdateUser(String email,String password, String rememberme){
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            ContentValues cv = new ContentValues();
            cv.put(PERSON_EMAIL, email);
            cv.put(PERSON_PASSWORD, password);
            cv.put(PERSON_REMEMBERME, rememberme);
            String where = PERSON_EMAIL + " = '" + email + "'";
            db.update(TABLE_PERSON,cv, where, null);
        }
        catch (Exception e){
        }
        db.close();
    }



    public String InsertAccount(String email, String tcno, String soyad, String ad, String babaadi,
                                String anaadi, String dogumyeri, String dogumtarihi){
        String Email = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT email FROM account WHERE email=?", new String[]{email});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            Email = cursor.getString(0);
            cursor.close();
        }
        if (Email == null){
            try {
                ContentValues cv = new ContentValues();
                cv.put(ACCOUNT_EMAIL, email);
                cv.put(ACCOUNT_TCNO, tcno);
                cv.put(ACCOUNT_SOYAD, soyad);
                cv.put(ACCOUNT_AD, ad);
                cv.put(ACCOUNT_BABAAD, babaadi);
                cv.put(ACCOUNT_ANAAD, anaadi);
                cv.put(ACCOUNT_DOGUMYERI, dogumyeri);
                cv.put(ACCOUNT_DOGUMTARIHI, dogumtarihi);
                db.insert(TABLE_ACCOUNT, null, cv);
            }
            catch (Exception e){
            }
            db.close();
        }
        else {
            try{
                ContentValues cv = new ContentValues();
                cv.put(ACCOUNT_EMAIL, email);
                cv.put(ACCOUNT_TCNO, tcno);
                cv.put(ACCOUNT_SOYAD, soyad);
                cv.put(ACCOUNT_AD, ad);
                cv.put(ACCOUNT_BABAAD, babaadi);
                cv.put(ACCOUNT_ANAAD, anaadi);
                cv.put(ACCOUNT_DOGUMYERI, dogumyeri);
                cv.put(ACCOUNT_DOGUMTARIHI, dogumtarihi);
                String where = ACCOUNT_EMAIL + " = '" + email + "'";
                db.update(TABLE_ACCOUNT,cv, where, null);
            }
            catch (Exception e){
            }
            db.close();
        }
        return Email;
    }

  /*  public String GetGender(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String Gender = null;
        Cursor cursor = db.rawQuery("SELECT gender FROM person WHERE email=?",new String[]{email});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            Gender = cursor.getString(0);
            cursor.close();
        }
        return Gender;
    }*/

    public List<String> AccountList(String email){
        List<String> users = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        try{
            Cursor cursor = db.rawQuery("SELECT tcno, soyad, ad, babaadi, anaadi, dogumyeri, dogumtarihi FROM account WHERE email=?", new String[]{email});
            while (cursor.moveToNext()){
                users.add(cursor.getString(0) + "-"
                        + cursor.getString(1) + "-"
                        + cursor.getString(2) + "-"
                        + cursor.getString(3) + "-"
                        + cursor.getString(4) + "-"
                        + cursor.getString(5) + "-"
                        + cursor.getString(6));
            }
        }
        catch (Exception e){
        }
        db.close();
        return users;
    }

    public Boolean isUserAccountSaved(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM account WHERE email=?", new String[]{email});
        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}