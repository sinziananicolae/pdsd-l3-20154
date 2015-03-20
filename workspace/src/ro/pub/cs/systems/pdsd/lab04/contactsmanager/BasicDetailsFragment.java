package ro.pub.cs.systems.pdsd.lab04.contactsmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BasicDetailsFragment extends Fragment {
	
	protected MyButtonListener buttonListener = new MyButtonListener();
	static final int CONTACTS_MANAGER_REQUEST_CODE = 99;
	
	 @Override
	  public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle state) {
		 Log.d("mytag", "in onCreateView");
	    return layoutInflater.inflate(R.layout.fragment_basic_details, container, false);
	  }
	 
	 @Override
	 public void onActivityCreated(Bundle savedInstanceState){
		 super.onActivityCreated(savedInstanceState);
		 
		 getActivity().findViewById(R.id.manageFragments).setOnClickListener(buttonListener);
		 getActivity().findViewById(R.id.save).setOnClickListener(buttonListener);
		 getActivity().findViewById(R.id.cancel).setOnClickListener(buttonListener);
		 
		 Intent intent = getActivity().getIntent();
		 if (intent != null) {
			  String phone = intent.getStringExtra("ro.pub.cs.systems.pdsd.lab04.contactsmanager.PHONE_NUMBER_KEY");
			  if (phone != null) {
				  ((EditText)getActivity().findViewById(R.id.phone)).setText(phone);
			  } else {
			    Activity activity = getActivity();
			    Toast.makeText(activity, activity.getResources().getString(R.string.phone_error), Toast.LENGTH_LONG).show();
			  }
			} 
	 }
	 
	 protected class MyButtonListener implements View.OnClickListener {
		 @Override
		 public void onClick(View v) {
			 if (v instanceof Button) {
				 if (v.getId() == R.id.manageFragments){
					 manageFragments(v);
				 }
				 else if (v.getId() == R.id.save) {
					 Log.d("mytag", "am apasat save");
					 saveContact(v);
				 }
				 else if (v.getId() == R.id.cancel) {
					 Log.d("mytag", "am apasat cancel");
					 getActivity().setResult(Activity.RESULT_CANCELED, new Intent());
					 getActivity().finish();
				 }	
			 }
		}

		private void manageFragments(View v) {
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			AdditionalDetailsFragment additionalDetailsFragment = (AdditionalDetailsFragment)fragmentManager.findFragmentById(R.id.frame2);
			if (additionalDetailsFragment == null) {
			  fragmentTransaction.add(R.id.frame2, new AdditionalDetailsFragment());
			  ((Button)v).setText(getActivity().getResources().getString(R.string.hideAdditional));
			  fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
			} else {
			  fragmentTransaction.remove(additionalDetailsFragment);
			  ((Button)v).setText(getActivity().getResources().getString(R.string.showAdditional));
			  fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_EXIT_MASK);
			}
			fragmentTransaction.commit();	
		}
		
		private void saveContact(View v) {
			Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
			intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);
			
			String name = ((EditText)getActivity().findViewById(R.id.name)).getText().toString();
			if (name != null) {
			  intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
			}
			
			String phone = ((EditText)getActivity().findViewById(R.id.phone)).getText().toString();
			if (phone != null) {
			  intent.putExtra(ContactsContract.Intents.Insert.PHONE, phone);
			}
			
			String email = ((EditText)getActivity().findViewById(R.id.email)).getText().toString();
			if (email != null) {
			  intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
			}
			
			String address = ((EditText)getActivity().findViewById(R.id.address)).getText().toString();
			if (address != null) {
			  intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);
			}
			
			FragmentManager fragmentManager = getActivity().getFragmentManager();
			AdditionalDetailsFragment additionalDetailsFragment = (AdditionalDetailsFragment)fragmentManager.findFragmentById(R.id.frame2);
			if (additionalDetailsFragment != null) {
				String jobTitle = ((EditText)getActivity().findViewById(R.id.jobTitle)).getText().toString();
				if (jobTitle != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.JOB_TITLE, jobTitle);
				}
				
				String company = ((EditText)getActivity().findViewById(R.id.company)).getText().toString();
				if (company != null) {
				  intent.putExtra(ContactsContract.Intents.Insert.COMPANY, company);
				}
				
				ArrayList<ContentValues> contactData = new ArrayList<ContentValues>();
				String website = ((EditText)getActivity().findViewById(R.id.website)).getText().toString();
				if (website != null) {
				  ContentValues websiteRow = new ContentValues();
				  websiteRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
				  websiteRow.put(ContactsContract.CommonDataKinds.Website.URL, website);
				  contactData.add(websiteRow);
				}
				
				String im = ((EditText)getActivity().findViewById(R.id.im)).getText().toString();
				if (im != null) {
				  ContentValues imRow = new ContentValues();
				  imRow.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
				  imRow.put(ContactsContract.CommonDataKinds.Im.DATA, im);
				  contactData.add(imRow);
				}
				
				intent.putParcelableArrayListExtra(ContactsContract.Intents.Insert.DATA, contactData);
			}
			
			getActivity().startActivityForResult(intent, CONTACTS_MANAGER_REQUEST_CODE);
		}
	}
}

