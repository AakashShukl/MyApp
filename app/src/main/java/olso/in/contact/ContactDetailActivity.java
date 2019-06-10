package olso.in.contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ContactDetailActivity extends AppCompatActivity {
    ImageView imageView;
    TextView Name, Email, Phone;
    String mphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        imageView = (ImageView) findViewById(R.id.image);
        Name = (TextView) findViewById(R.id.Name);
        Email = (TextView) findViewById(R.id.email);
        Phone = (TextView) findViewById(R.id.mobile);
        mphone = getIntent().getStringExtra("phone");

        Name.setText(getIntent().getStringExtra("name"));
        Phone.setText(mphone);
        Email.setText(Contact.hashMap.get(mphone));
Bitmap image=retrieveContactPhoto(getBaseContext(),mphone);
if(image!=null)
imageView.setImageBitmap(image);
else
    imageView.setImageResource(R.drawable.images);
    }

    public static Bitmap retrieveContactPhoto(Context context, String number) {
        ContentResolver contentResolver = context.getContentResolver();
        String contactId = null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));

        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID};

        Cursor cursor =
                contentResolver.query(
                        uri,
                        projection,
                        null,
                        null,
                        null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            cursor.close();
        }

        Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.images);



if(contactId!=null) {

    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));
    Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

    cursor = context.getContentResolver().query(photoUri,
            new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
    if (cursor == null) {
        return null;
    }
}
        try {
            if(cursor!=null) {
                if (cursor.moveToFirst()) {
                    byte[] data = cursor.getBlob(0);
                    if (data != null) {
                        return BitmapFactory.decodeStream(new ByteArrayInputStream(data));
                    }
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Contact.start=true;
        Log.d("list",Contact.start+"");
    }
}


