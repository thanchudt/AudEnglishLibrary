package com.dangle1107.audenglishlibrary;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper{
		
	// Table Names	
	public static final String TABLE_LESSONS = "Lessons";
	public static final String TABLE_SENTENCES = "Sentences";
	
	// Common column names
	public static final String COLUMN_ID = "_id";
	
	// SONGS Table - column names
	public static final String COLUMN_LESSONS_NAME = "Name";
	public static final String COLUMN_LESSONS_DESCRIPTION = "Description";
	public static final String COLUMN_LESSONS_IDENTIFICATION = "identification";
		
	// LYRICS Table - column names
	public static final String COLUMN_SENTENCES_POSITION = "Position";
	public static final String COLUMN_SENTENCES_FROMTIME = "FromTime";
	public static final String COLUMN_SENTENCES_TOTIME = "ToTime";
	public static final String COLUMN_SENTENCES_LESSONID = "LessonId";
	public static final String COLUMN_SENTENCES_LYRIC = "Lyric";
	
   
    private static final int DATABASE_VERSION = 3;
    public String OLD_DB_NAME = "AudEnglish.sqlite3";
    public static final String DB_NAME = "AudEnglish_1_0_0.sqlite3";
	//Path to the device folder with databases
    public static String DB_PATH;
    //Database file name
    public SQLiteDatabase database;
    public final Context context;
    private static final String DB_FORMAT_PATH = "//data//data//%s//databases//";
    public SQLiteDatabase getDb() {
        return database;
    }
    
    public DatabaseHelper(Context context, String databaseName) {
		super(context, databaseName, null, DATABASE_VERSION);
		this.context = context;
		 //Write a full path to the databases of your application
		 String packageName = context.getPackageName();
		 DB_PATH = String.format(DB_FORMAT_PATH, packageName);
		 DeleteOldDatabase(context);
		 openDataBase();
    }
    
    private void DeleteOldDatabase(Context context) {
    	//context.deleteDatabase("AudEnglish_1_0_0.sqlite3");
    	context.deleteDatabase("AudEnglish.sqlite3");
    }
        
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        this.context = context;
	     //Write a full path to the databases of your application
	     String packageName = context.getPackageName();
	     DB_PATH = String.format(DB_FORMAT_PATH, packageName);
	     openDataBase();
    }
    
    //This piece of code will create a database if it’s not yet created
    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(this.getClass().toString(), "Copying error");
                throw new Error("Error copying database!");
            }
        } else {
            Log.i(this.getClass().toString(), "Database already exists");
        }
    }
    
    //Performing a database existence check
    private boolean checkDataBase() {
        SQLiteDatabase checkDb = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDb = SQLiteDatabase.openDatabase(path, null,
                          SQLiteDatabase.OPEN_READONLY);            
        } catch (SQLException e) {
            Log.e(this.getClass().toString(), "Error while checking db");
        }
        //Android doesn’t like resource leaks, everything should 
        // be closed
        if (checkDb != null) {
            checkDb.close();
        }
        return checkDb != null;
    }
    
    //Method for copying the database
    private void copyDataBase() throws IOException {
        //Open a stream for reading from our ready-made database
        //The stream source is located in the assets
        InputStream externalDbStream = context.getAssets().open(DB_NAME);

         //Path to the created empty database on your Android device
        String outFileName = DB_PATH + DB_NAME;

         //Now create a stream for writing the database byte by byte
        OutputStream localDbStream = new FileOutputStream(outFileName);

         //Copying the database
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = externalDbStream.read(buffer)) > 0) {
            localDbStream.write(buffer, 0, bytesRead);
        }
        //Don’t forget to close the streams
        localDbStream.close();
        externalDbStream.close();
    }
    
    public SQLiteDatabase openDataBase() throws SQLException {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDataBase();
            database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }
    
    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {    
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {    	    	
    }
}
