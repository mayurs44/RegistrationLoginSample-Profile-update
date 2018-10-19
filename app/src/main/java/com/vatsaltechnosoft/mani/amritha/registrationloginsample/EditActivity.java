package com.vatsaltechnosoft.mani.amritha.registrationloginsample;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class EditActivity extends AppCompatActivity implements View.OnClickListener {

    InputValidation inputValidation;

    User user;

    Intent intent;

    AppCompatImageView imageUpdate;

    AppCompatButton buttonUpdate;
    int index;
    TextInputEditText firstName, lastName, emailId, userName, password, hobbies;
    String uName, textFirst, textLast, textEmail, textUserName, textPasswordView, gender, textHobbies, userType;
    Bitmap bitmap;
    private DatabaseHelper databaseHelper;
    private AppCompatActivity activity = EditActivity.this;
    private byte[] recordImage;
    private String fullName;

    public static byte[] imageViewToByte(ImageView imageView) {

        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_dialog);

        intent = getIntent();

        //going to previous activity

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initViews();

        getValues();
        initObjects();
        initListeners();
    }

    private void initViews() {
        imageUpdate = findViewById(R.id.img_update_icon);

        firstName = findViewById(R.id.textViewFirstName);

        lastName = findViewById(R.id.textViewLastName);

        emailId = findViewById(R.id.textViewEmailId);

        userName = findViewById(R.id.textViewUserName);

        password = findViewById(R.id.textViewPassword);

        hobbies = findViewById(R.id.textViewHobbies);

        buttonUpdate = findViewById(R.id.textViewUpdate);

    }

    /*final byte[] imgUpdate = imageViewToByte(imageUpdate);

    final Bitmap bitmapUpdate = BitmapFactory.decodeByteArray(imgUpdate, 0, imgUpdate.length);*/

    private void getValues() {

        index = intent.getIntExtra("id", 0);

        uName = intent.getStringExtra("USER NAME");

        recordImage = intent.getByteArrayExtra("profilePicture");

        bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);

        /*imageView.setImageBitmap(bitmap);*/

        textFirst = (intent.getStringExtra("firstName"));

        textLast = (intent.getStringExtra("lastName"));

        fullName = intent.getStringExtra("firstName") + " " + intent.getStringExtra("lastName");

        textEmail = (intent.getStringExtra("emailId"));

        textUserName = intent.getStringExtra("userName");

        textPasswordView = (intent.getStringExtra("password"));

        gender = (intent.getStringExtra("gender"));

        userType = (intent.getStringExtra("userType"));

        textHobbies = (intent.getStringExtra("hobbies"));
    }

    private void initObjects() {
        inputValidation = new InputValidation(activity);

        databaseHelper = new DatabaseHelper(activity);

        user = new User();
    }

    private void initListeners() {
        imageUpdate.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_update_icon:
                imgUpdate();
                break;
            case R.id.textViewUpdate:
                postDataToSQLite();
                break;
        }
    }

    private void imgUpdate() {

        //click image to update

        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check external storage permission

                ActivityCompat.requestPermissions(EditActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 888);
            }
        });
    }

    private void postDataToSQLite() {

        if (!databaseHelper.checkUser(userName.getText().toString().trim())) {

            user.setId(index);

            if (bitmap != null) {
                user.setProfilePicture(imageViewToByte(imageUpdate));
            } else {
                user.setProfilePicture(recordImage);
            }

            if (firstName.getText().toString().isEmpty()) {
                user.setFirstName(textFirst);
            } else {

                user.setFirstName(firstName.getText().toString().trim());

            }

            if (lastName.getText().toString().isEmpty()) {
                user.setLastName(textLast);
            } else {

                user.setLastName(lastName.getText().toString().trim());

            }

            if (emailId.getText().toString().isEmpty()) {
                user.setEmail(textEmail);
            } else {

                user.setEmail(emailId.getText().toString().trim());

            }

            if (userName.getText().toString().isEmpty()) {
                user.setUserName(textUserName);
            } else {

                user.setUserName(userName.getText().toString().trim());

            }

            if (password.getText().toString().isEmpty()) {

                user.setPassword(textPasswordView);
            } else {

                user.setPassword(password.getText().toString().trim());

            }

            user.setGender(gender);

            user.setUserType(userType);

            if (hobbies.getText().toString().isEmpty()) {
                user.setHobbies(textHobbies);
            } else {

                user.setHobbies(hobbies.getText().toString().trim());

            }

            databaseHelper.updateUser(user);

            // Snack Bar to show success message that record saved successfully
            Toast.makeText(activity, "Updated Successfully", Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(activity, "User Name Already Exists", Toast.LENGTH_LONG).show();

        }
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 888) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 888);
            } else {
                Toast.makeText(this, "Don't have permissions to access file location", Toast.LENGTH_SHORT).show();
            }

            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 888 && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON).
                    setAspectRatio(1, 1).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                //set image choose from gallery
                imageUpdate.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}