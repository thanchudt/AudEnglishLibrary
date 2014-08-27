package com.dangle1107.audenglishlibrary;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SentenceDataSource {
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	private String[] allColumns = { DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_SENTENCES_LESSONID,
			DatabaseHelper.COLUMN_SENTENCES_POSITION, DatabaseHelper.COLUMN_SENTENCES_FROMTIME, 
			DatabaseHelper.COLUMN_SENTENCES_TOTIME, DatabaseHelper.COLUMN_SENTENCES_LYRIC
	};
	private static final int COLUMN_ID_INDEX = 0;
	private static final int COLUMN_SENTENCES_LESSONID_INDEX = 1;
	private static final int COLUMN_SENTENCES_POSITION_INDEX = 2;
	private static final int COLUMN_SENTENCES_FROMTIME_INDEX = 3;
	private static final int COLUMN_SENTENCES_TOTIME_INDEX = 4;
	private static final int COLUMN_SENTENCES_LYRIC_INDEX = 5;
	
	public SentenceDataSource(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	public void open() throws SQLException {
		//database = dbHelper.getWritableDatabase();
		database = dbHelper.getDb();
	}
	
	public void close() {
		dbHelper.close();
	}
	
	public List<Sentence> getAllSentences(long songId){
		List<Sentence> sentences = new ArrayList<Sentence>();
		
		Cursor cursor = database.query(DatabaseHelper.TABLE_SENTENCES, allColumns, DatabaseHelper.COLUMN_SENTENCES_LESSONID + " = ?", new String[] { Long.toString(songId) }, null, null, null);
		
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Sentence lyric = cursorToSentence(cursor);
			sentences.add(lyric);
			cursor.moveToNext();
		}
		
		//make sure to close the cursor
		cursor.close();
		return sentences;
	}
	
	private Sentence cursorToSentence(Cursor cursor) {
		Sentence lyric = new Sentence();
		lyric.setId(cursor.getLong(COLUMN_ID_INDEX));
		lyric.setLessonId(cursor.getLong(COLUMN_SENTENCES_LESSONID_INDEX));
		lyric.setPosition(cursor.getLong(COLUMN_SENTENCES_POSITION_INDEX));
		lyric.setFromTime(cursor.getString(COLUMN_SENTENCES_FROMTIME_INDEX));		
		lyric.setToTime(cursor.getString(COLUMN_SENTENCES_TOTIME_INDEX));
		lyric.setLyric(cursor.getString(COLUMN_SENTENCES_LYRIC_INDEX));			
		return lyric;
	}	
}
