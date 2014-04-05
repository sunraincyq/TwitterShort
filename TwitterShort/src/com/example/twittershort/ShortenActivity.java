package com.example.twittershort;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;

public class ShortenActivity extends Activity {

	Notes note;
	MultiAutoCompleteTextView pre_text, post_text;
	Button process, post;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shorten);
		
	pre_text=(MultiAutoCompleteTextView) findViewById(R.id.pre_text);	
	post_text=(MultiAutoCompleteTextView) findViewById(R.id.post_text);
	process=(Button) findViewById(R.id.process);
	post=(Button) findViewById(R.id.post);
	
	NotesDatabase notesdb= new NotesDatabase(this);
	
	note.setNote(notesdb.getNote(0).toString());
	
	pre_text.setText(note.getNote());
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.shorten, menu);
		return true;
	}

	
	
}
