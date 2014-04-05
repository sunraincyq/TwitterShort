package com.example.twittershort;

import com.example.twittershort.Notes;
import com.example.twittershort.NotesDatabase;
import com.example.twittershort.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

public class MainActivity extends Activity {

	EditText et_input;
	Button go;
	EditText after_text;
	
	Notes note;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		et_input=(EditText) findViewById(R.id.et_text);
		go=(Button) findViewById(R.id.go);
		after_text=(EditText) findViewById(R.id.after_text);
		
		go.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				onClickSave(findViewById(R.id.go));
				//goToNext(findViewById(R.layout.activity_main));
					
			}
				
		});
		
	}

	protected void goToNext(View findViewById) {
		Intent intent=new Intent(this, ShortenActivity.class);
		startActivity(intent);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onClickSave(View theButton) {
		//NotesDatabase db = new NotesDatabase(this);
		
		//db.addNote(new Notes(et_input.getText().toString()));
		
		//after_text.setText(db.getNote(1).toString());
		
		//finish();
		
		//String text=new String(note.getNote());
		//StringBuilder sb=new StringBuilder();
		note=new Notes(et_input.getText().toString());
		
		String text=note.getNote();
		if (text.length()>140){
			//parseText();
		}
		
		String newText=text;
		
		after_text.setText(newText);
		
	}
	
}
