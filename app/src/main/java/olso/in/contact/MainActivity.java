package olso.in.contact;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    RecyclerView rvContacts;
    static Boolean start=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressDialog = new ProgressDialog(this);
        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        progressDialog.setMessage("Loading");
        // getAllContacts();



    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Contact.start==false)
        {
            Log.d("list",Contact.start+"");
            new AsyncTaskRunner().execute();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Contact.start=false;
    }

    private List<Contact> getAllContacts() {
        List<Contact> contactVOList = new ArrayList();
        Contact.hashMap = new HashMap<String, String>();
        Contact contactVO;

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
if(cursor!=null) {
    if (cursor.getCount() > 0) {
        while (cursor.moveToNext()) {

            int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
            if (hasPhoneNumber > 0) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                contactVO = new Contact();
                contactVO.setContactName(name);

                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        new String[]{id},
                        null);
                if (phoneCursor != null) {
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }
                }
                if (phoneCursor != null) {
                    phoneCursor.close();
                }
                Cursor emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                        new String[]{id}, null);
                if (emailCursor != null) {
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Contact.hashMap.put(contactVO.getContactNumber(), emailId);
                    }
                }
                contactVOList.add(contactVO);
            }
        }

    }
}
        return contactVOList;
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, List<Contact>> {


        @Override
        protected List<Contact> doInBackground(String... strings) {
            return getAllContacts();
        }

        @Override
        protected void onPreExecute() {
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Contact> contacts) {
           Contact.start=true;
            Log.d("list",""+Contact.start+"");
            progressDialog.dismiss();
            ContactAdapter contactAdapter = new ContactAdapter(contacts, getApplicationContext());
            rvContacts.setLayoutManager(new LinearLayoutManager(getBaseContext()));
            rvContacts.setAdapter(contactAdapter);
        }
    }
}
