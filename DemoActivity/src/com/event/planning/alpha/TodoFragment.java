package com.event.planning.alpha;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseQuery.CachePolicy;
import com.sothree.slidinguppanel.demo.R;

public class TodoFragment extends Fragment implements OnItemClickListener, OnClickListener{
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	private EditText mTaskInput;
	private ListView mListView;
	private TaskAdapter mAdapter;
	private Button submit;
	private TextWatcher textWatcher;
	public TodoFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_todo, container, false);
		updateData();
		//			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
		//			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
		////			Toast.makeText(getActivity(), Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)), Toast.LENGTH_SHORT).show();
		return rootView;
	}



	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		
		submit = (Button)getActivity().findViewById(R.id.submit_button);
		submit.setText("Refresh");
		submit.setOnClickListener(this);
		submit.setBackgroundColor(Color.argb(100, 67, 192, 250));
		mAdapter = new TaskAdapter(getActivity(), new ArrayList<Task>());

		mTaskInput = (EditText) getActivity().findViewById(R.id.task_input);
		textWatcher = new TextWatcher() {

			  public void afterTextChanged(Editable s) {
				  if(mTaskInput.getText().toString().equals("")){
					  submit.setText("Refresh");
				  }else{
					  submit.setText("Submit");
				  }
			  }

			  public void onTextChanged(CharSequence s, int start, int before,
			          int count) {

			  }

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
		};
				//TextWatcher textWatcher;
		mTaskInput.addTextChangedListener(textWatcher);
		mListView = (ListView) getActivity().findViewById(R.id.task_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);		

		updateData();

	}

	public void updateData(){
		if(mAdapter != null){
			ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
			query.whereEqualTo("user", ParseUser.getCurrentUser());
			query.setCachePolicy(CachePolicy.CACHE_THEN_NETWORK);
			query.findInBackground(new FindCallback<Task>() {
				@Override
				public void done(List<Task> tasks, ParseException error) {
					if(tasks != null){
						mAdapter.clear();
						for (int i = 0; i < tasks.size(); i++) {
							mAdapter.add(tasks.get(i));
						}
					}
				}
			});
		}
	}

	public void createTask(View v) {
		if (mTaskInput.getText().length() > 0){
			Task t = new Task();
			t.setACL(new ParseACL(ParseUser.getCurrentUser()));
			t.setUser(ParseUser.getCurrentUser());
			t.setDescription(mTaskInput.getText().toString());
			t.setCompleted(false);
			t.saveEventually();
			mAdapter.insert(t, 0);
			mTaskInput.setText("");
		}
	} 

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_logout: 
			ParseUser.logOut();
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
			return true; 
		} 
		return false; 
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Task task = mAdapter.getItem(position);
		TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

		task.setCompleted(!task.isCompleted());

		if(task.isCompleted()){
			taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}else{
			taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
		}

		task.saveEventually();
	}

	@Override
	public void onClick(View arg0) {
		if(mTaskInput.getText().length() > 0){
			createTask(arg0);
		}else{
			updateData();
		}
	}

}