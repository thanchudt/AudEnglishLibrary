package com.dangle1107.audenglishlibrary;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LessonDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_LESSONS_NAME,
			DatabaseHelper.COLUMN_LESSONS_DESCRIPTION, DatabaseHelper.COLUMN_LESSONS_IDENTIFICATION 
	};
	private static final int COLUMN_ID_INDEX = 0;
	private static final int COLUMN_LESSONS_NAME_INDEX = 1;
	private static final int COLUMN_LESSONS_DESCRIPTION_INDEX = 2;
	private static final int COLUMN_LESSONS_IDENTIFICATION_INDEX = 3;
	
	public LessonDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		//database = dbHelper.getWritableDatabase();
		database = dbHelper.getDb();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public List<Lesson> getAllLessons(){
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_LESSONS, allColumns, null, null, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Lesson song = cursorToLesson(cursor);
			lessons.add(song);
			cursor.moveToNext();
		}
		
		//make sure to close the cursor
		cursor.close();
		return lessons;
	}
	
	public List<Lesson> getLessons(long maxId){
		List<Lesson> lessons = new ArrayList<Lesson>();
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_LESSONS, allColumns, DatabaseHelper.COLUMN_ID + " <= ?", new String[] { Long.toString(maxId) }, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Lesson song = cursorToLesson(cursor);
			lessons.add(song);
			cursor.moveToNext();
		}
		
		//make sure to close the cursor
		cursor.close();
		return lessons;
	}
	
	private Lesson cursorToLesson(Cursor cursor) {
		Lesson lesson = new Lesson();
		lesson.setId(cursor.getLong(COLUMN_ID_INDEX));
		lesson.setName(cursor.getString(COLUMN_LESSONS_NAME_INDEX));
		lesson.setDescription(cursor.getString(COLUMN_LESSONS_DESCRIPTION_INDEX));
		lesson.setIdentification(cursor.getString(COLUMN_LESSONS_IDENTIFICATION_INDEX));			
		return lesson;
	}	
}
