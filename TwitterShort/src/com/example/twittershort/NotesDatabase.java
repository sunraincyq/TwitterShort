package com.example.twittershort;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotesDatabase extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "notesDatabase";
	private static final String TABLE_NOTES = "notes";
	private static final String KEY_ID = "id";
	private static final String KEY_NOTE = "note";
	
	public NotesDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_NOTES_TABLE = "CREATE TABLE " + TABLE_NOTES + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NOTE + " TEXT" + ")";
		db.execSQL(CREATE_NOTES_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES);
		onCreate(db);
	}
	
	/** Add a note in the Database
	 * @param notes the note, which has to be saved
	 */
	public void addNote (Notes notes) {
		SQLiteDatabase db = this.getWritableDatabase(); // we have to WRITE to db
		
		ContentValues values = new ContentValues();
		values.put(KEY_NOTE, notes.getNote());
		
		db.insert(TABLE_NOTES, null, values);
		db.close(); //close db connection
	}
	
	
	/** Receives a Notes object from the database
	 * @param id the id of the note
	 * @return the Note object
	 */
	public Notes getNote (int id) {
		SQLiteDatabase db = this.getReadableDatabase(); // we have to READ from db
		Cursor cursor = db.query(TABLE_NOTES, new String[] {KEY_ID,  KEY_NOTE},  KEY_ID + "=?",  new String [] {String.valueOf(id)}, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
		}
		
		Notes notes = new Notes(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
		return notes;
	}
	
	/** Receive all saved notes from the database
	 * @return a List with all notes
	 */
	public List<Notes> getAllNotes () {
		List<Notes> notesList = new ArrayList<Notes>();
		String selectQuery = "SELECT * FROM " + TABLE_NOTES;
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		
		if (cursor.moveToFirst()) {
			do {
				Notes notes = new Notes();
				notes.setID(Integer.parseInt(cursor.getString(0)));
				notes.setNote(cursor.getString(1));
				
				notesList.add(notes);
			} while (cursor.moveToNext());
		}
		return notesList;
	}
	
	public int updateNote (Notes notes) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_NOTE, notes.getNote());
		
		return db.update(TABLE_NOTES, values, KEY_ID + " = ? ", new String [] {String.valueOf(notes.getID())});
	}
	
	public void deleteNote (Notes notes) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_NOTES, KEY_ID + " = ?", new String [] {String.valueOf(notes.getID())});
	}
	
	public int getNotesCount () {
		String countQuery = "SELECT * FROM " + TABLE_NOTES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		
		return cursor.getCount();
	}
}
