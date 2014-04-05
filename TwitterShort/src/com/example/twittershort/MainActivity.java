package com.example.twittershort;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import com.example.twittershort.Notes;
import com.example.twittershort.NotesDatabase;
import com.example.twittershort.R;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText et_input;
	Button go, post2;
	EditText after_text;
	TextView count1, count2;
	final int NUM = 40;
	Notes note;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		et_input = (EditText) findViewById(R.id.et_text);
		go = (Button) findViewById(R.id.go);
		after_text = (EditText) findViewById(R.id.after_text);
		post2 = (Button) findViewById(R.id.post2);
		count1 = (TextView) findViewById(R.id.count1);
		count2 = (TextView) findViewById(R.id.count2);
		// get real time count of characters of two edit text boxes
		et_input.addTextChangedListener(mTextEditorWatcher1);
		after_text.addTextChangedListener(mTextEditorWatcher2);

		et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		

		go.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// int len=et_input.getText().toString().length();
				// count1.setText("count:"+Integer.toString(len));
				onClickSave(findViewById(R.id.go));
				// goToNext(findViewById(R.layout.activity_main));
				changeColor(et_input);
				changeColor(after_text);
			}

		});

		post2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onClickPost(findViewById(R.id.post2));

			}

		});
	}

	protected void changeColor(EditText et) {
		Spannable wordtoSpan = new SpannableString(et.getText().toString());        
		wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 1, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		et.setText(wordtoSpan);
		
	}

	protected void onClickPost(View view) {

		String url = "https://twitter.com/intent/tweet?text=";

		try {
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url
					+ Uri.encode(after_text.getText().toString())));
			startActivity(i);
		} catch (android.content.ActivityNotFoundException e) {
			Toast.makeText(this, "Can't send tweet!", 2).show();
		}

	}

	protected void goToNext(View findViewById) {
		Intent intent = new Intent(this, ShortenActivity.class);
		startActivity(intent);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		int len = et_input.getText().toString().length();
		count1.setText("count:" + Integer.toString(len));

		int len2 = after_text.getText().toString().length();
		count2.setText("count:" + Integer.toString(len2));
	}

	public void onClickSave(View theButton) {
		// NotesDatabase db = new NotesDatabase(this);

		// db.addNote(new Notes(et_input.getText().toString()));

		// after_text.setText(db.getNote(1).toString());

		// finish();

		// String text=new String(note.getNote());
		// StringBuilder sb=new StringBuilder();
		note = new Notes(et_input.getText().toString());

		String text = note.getNote();
		String newText = "";
		if (text.length() > NUM) {
			try {
				newText = parseText(text);
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else
			newText = text;

		after_text.setText(newText);
		// int len=after_text.getText().toString().length();
		// count2.setText("count:"+Integer.toString(len));
	}

	public String parseText(String text) throws IOException {
		// Parse message text, replace words, and return new message text
		int len = text.length();
		String postText = "";

		ArrayList<String> baseWords = new ArrayList<String>();
		ArrayList<String> replacementWords = new ArrayList<String>();
		ArrayList<Integer> priority = new ArrayList<Integer>();
		ArrayList<Integer> diff = new ArrayList<Integer>();

		ArrayList<Integer> baseIndexes = new ArrayList<Integer>();
		ArrayList<Integer> messageIndexes = new ArrayList<Integer>();

		// read lib file
		try {
			readLib(baseWords, replacementWords, priority, diff);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

		System.out.println(baseWords);
		System.out.println(replacementWords);
		System.out.println(priority);
		System.out.println(diff);

		// split the input text into words array s
		String[] s = text.split(" ");
		int[] size = new int[s.length];
		int[] newSize = new int[s.length];

		for (int i = 0; i < s.length; i++) {
			size[i] = s[i].length();
			newSize[i] = s[i].length();
		}

		for (int i = 0; i < baseWords.size(); i++) {
			for (int j = 0; j < s.length; j++) {
				if (baseWords.get(i).equals(s[j])) {
					baseIndexes.add(i); // store the index of base word
					messageIndexes.add(j); // store the index of message word
											// that will be replaced
				}
			}
		}

		ArrayList<Integer> changedIndexes = new ArrayList<Integer>();
		// replace words in the array s by ordering from baseIndexes.
		for (int i = 0; i < baseIndexes.size(); i++) {
			len = len - diff.get(baseIndexes.get(i));
			s[messageIndexes.get(i)] = replacementWords.get(baseIndexes.get(i));
			newSize[messageIndexes.get(i)] = s[messageIndexes.get(i)].length();
			changedIndexes.add(messageIndexes.get(i));
			if (len <= NUM)
				break;
		}

		for (int i = 0; i < s.length; i++) {
			postText += s[i] + " ";
		}

		return postText;
	}

	private void readLib(ArrayList<String> baseWords,
			ArrayList<String> replacementWords, ArrayList<Integer> priority,
			ArrayList<Integer> diff) throws IOException {
		@SuppressWarnings("resource")
		Scanner input = new Scanner(new InputStreamReader(getAssets().open(
				"lib.txt")));

		System.out.println("starting to read the lib file");
		while (input.hasNext()) {
			baseWords.add(input.next());
			replacementWords.add(input.next());
			priority.add(input.nextInt());
			diff.add(input.nextInt());
		}
	}

	private final TextWatcher mTextEditorWatcher1 = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// This sets a textview to the current length
			count1.setText("count:"+String.valueOf(s.length()));
		}

		public void afterTextChanged(Editable s) {
		}
	};

	private final TextWatcher mTextEditorWatcher2 = new TextWatcher() {
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// This sets a textview to the current length
			count2.setText("count"+String.valueOf(s.length()));
		}

		public void afterTextChanged(Editable s) {
		}
	};

}
