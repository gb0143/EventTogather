package com.event.planning.alpha;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.sothree.slidinguppanel.demo.R;

	public class InformationFragment extends Fragment implements OnClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private Handler myHandler = new Handler();
		private GoogleMap map;
		private TextView what,when,where, why;
		private Button saveChanges;
		View Context;
		
		public InformationFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
		
			if(rootView != null){
			what = (TextView)rootView.findViewById(R.id.what);
			when = (TextView)rootView.findViewById(R.id.when);
			where = (TextView)rootView.findViewById(R.id.where);
			why = (TextView)rootView.findViewById(R.id.why);
			saveChanges = (Button)rootView.findViewById(R.id.save);
			}
			what.setOnClickListener(this);
			when.setOnClickListener(this);
			where.setOnClickListener(this);
			why.setOnClickListener(this);
			saveChanges.setOnClickListener(this);
			Context = rootView;
//			TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
//			Toast.makeText(getActivity(), Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)), Toast.LENGTH_SHORT).show();
			return rootView;
		}

		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.save){
				String url = "http://minhazm.com/gb4/finalProj/data.php?val=";
				url += URLEncoder.encode(what.getText() + "aaaaa" + when.getText() + "aaaaa" + where.getText() + "aaaaa" + why.getText());
				Log.d("DEBUG", "char that's failing" + url.substring(50));
				new pushToServer(url).execute();
				Toast.makeText(getActivity(), "save was clicked" , Toast.LENGTH_SHORT).show();

				return;
			}
			final TextView temp = (TextView)Context.findViewById(arg0.getId());
			String Text = temp.getText().toString();
			if(Context.getContext() !=  null){
			AlertDialog.Builder alert = new AlertDialog.Builder(Context.getContext());
			alert.setTitle("Edit Text");
	        alert.setMessage("Change as necessary");
	 
	         // Use an EditText view to get user input.
	         final EditText input = new EditText(Context.getContext());
	         input.setText(Text);
	         alert.setView(input);
	         alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	                 String value = input.getText().toString().trim();
	                 saveChanges.setVisibility(View.VISIBLE);
	                 temp.setText(value);
//	                 Toast.makeText(getActivity().getApplicationContext(), value, Toast.LENGTH_SHORT).show();
	             }
	         });

	         alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	             public void onClick(DialogInterface dialog, int whichButton) {
	                 dialog.cancel();
	             }
	         });
	         alert.show();     
			}
			// TODO Auto-generated method stub
			
		}
		
		 private class pushToServer extends AsyncTask<URL, Integer, String> {
			 String url;
			 protected pushToServer(String urlofthing){
				 this.url = urlofthing;
			 }

				@Override
				protected String doInBackground(URL... arg0) {

				      ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				      
				      String response = null;
				      String val = "";
				      String res = "";
				    //attempt to POST the username and password to the specified url  
				      try {
				       response = Clientside.executeHttpGet(url);
				       Log.d("DEBUG", url);
				       res = response.toString();
//				       status.setText(label);
				      } catch (Exception e) {
				       e.printStackTrace();
				       return e.getMessage();
				      }
					return res;
				
				}

		     protected void onPostExecute(String result) {
		         if(!result.equals("error")){
		        	 String[] info = result.split("aaaaa");
		        	 what.setText(info[0]);
		        	 when.setText(info[1]);
		        	 where.setText(info[2]);
		        	 why.setText(info[3]);
		        	 Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
		         }
		     }
		 }
	}
