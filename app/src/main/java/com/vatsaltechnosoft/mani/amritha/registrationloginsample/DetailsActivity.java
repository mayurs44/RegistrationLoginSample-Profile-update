package com.vatsaltechnosoft.mani.amritha.registrationloginsample;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.function.Function;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener {

    InputValidation inputValidation;
    Intent intent;
    AppCompatImageView imageView;
    AppCompatTextView textFirst, textLast, textEmail, textUserName, textPasswordView, textHobbies;
    /*TextInputEditText firstName, lastName, emailId, userName, password, hobbies;*/
    AppCompatTextView fullName, gender, userType;
    AppCompatButton OK, Edit;
    User user;
    int index;
    private AppCompatActivity activity = DetailsActivity.this;
    private DatabaseHelper databaseHelper;

    String uName, intentUserName;

    Bitmap bitmap;

    byte[] recordImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //going to previous activity

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        initObjects();

        intent = getIntent();

        getValues();

        initListeners();

        initObjects();


    }

    private void getValues() {

        //Getting the passed value from ListActivity

        index = intent.getIntExtra("id", 0);

        uName = intent.getStringExtra("USER NAME");

        recordImage = intent.getByteArrayExtra("profilePicture");

        bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);

        imageView.setImageBitmap(bitmap);

        textFirst.setText(intent.getStringExtra("firstName"));

        textLast.setText(intent.getStringExtra("lastName"));

        String name = intent.getStringExtra("firstName") + " " + intent.getStringExtra("lastName");

        fullName.setText(name);

        textEmail.setText(intent.getStringExtra("emailId"));

        intentUserName = intent.getStringExtra("userName");

        textUserName.setText(intentUserName);

        textPasswordView.setText(intent.getStringExtra("password"));

        gender.setText(intent.getStringExtra("gender"));

        userType.setText(intent.getStringExtra("userType"));

        textHobbies.setText(intent.getStringExtra("hobbies"));

    }

    private void initViews() {

        imageView = findViewById(R.id.img_detail_icon);

        textFirst = findViewById(R.id.text_first_name);

        textLast = findViewById(R.id.text_last_name);

        textEmail = findViewById(R.id.text_email);

        textUserName = findViewById(R.id.text_user_name);

        textPasswordView = findViewById(R.id.text_password);

        textHobbies = findViewById(R.id.text_hobbies);

        fullName = findViewById(R.id.textViewFullName);

        gender = findViewById(R.id.textViewGender);

        userType = findViewById(R.id.textViewUserType);

        OK = findViewById(R.id.textViewOk);

        Edit = findViewById(R.id.textViewEdit);

    }


    private void initListeners() {

        OK.setOnClickListener(this);
        Edit.setOnClickListener(this);

    }

    /**
     * This method is to initialize objects to be used
     */


    private void initObjects() {

        inputValidation = new InputValidation(activity);

        databaseHelper = new DatabaseHelper(activity);

        user = new User();

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.textViewOk:
                finish();
                break;

            case R.id.textViewEdit:
                postDataToSQLite();
                break;
        }

    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */

    private void postDataToSQLite() {

        user.setId(index);

        if (intentUserName.equals(uName)) {

            Intent i = new Intent(DetailsActivity.this, EditActivity.class);

            i.putExtra("USER NAME", uName);
            i.putExtra("id", index);
            i.putExtra("profilePicture", recordImage);
            i.putExtra("firstName", intent.getStringExtra("firstName"));
            i.putExtra("lastName", intent.getStringExtra("lastName"));
            i.putExtra("emailId", intent.getStringExtra("emailId"));
            i.putExtra("userName", intentUserName);
            i.putExtra("password", intent.getStringExtra("password"));
            i.putExtra("gender", intent.getStringExtra("gender"));
            i.putExtra("userType", intent.getStringExtra("userType"));
            i.putExtra("hobbies", intent.getStringExtra("hobbies"));
            startActivity(i);
            finish();
        }
    }


}
