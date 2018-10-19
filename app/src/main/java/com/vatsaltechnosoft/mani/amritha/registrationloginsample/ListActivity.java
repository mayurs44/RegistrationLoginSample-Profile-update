package com.vatsaltechnosoft.mani.amritha.registrationloginsample;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amritha on 6/30/18.
 */
public class ListActivity extends AppCompatActivity {

    private AppCompatActivity activity = ListActivity.this;
    private RecyclerView recyclerViewUsers;
    private List<User> listUsers;
    private ListViewRecyclerAdapter listViewRecyclerAdapter;
    private DatabaseHelper databaseHelper;

    ImageView imageView;

    String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        initViews();
        initObjects();
    }

    /**
     * This method is to initialize views
     */

    private void initViews() {

        recyclerViewUsers = findViewById(R.id.recycler_view_users);

    }

    /**
     * This method is to initialize objects to be used
     */

    private void initObjects() {

        listUsers = new ArrayList<>();
        listViewRecyclerAdapter = new ListViewRecyclerAdapter(listUsers);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewUsers.setLayoutManager(mLayoutManager);
        recyclerViewUsers.setItemAnimator(new DefaultItemAnimator());
        recyclerViewUsers.setHasFixedSize(true);
        recyclerViewUsers.setAdapter(listViewRecyclerAdapter);

        final Intent intent = getIntent();

        //Getting the passed value from MainActivity
        userName = intent.getStringExtra("USER NAME");

        recyclerViewUsers.addOnItemTouchListener(new RecyclerViewTouchListener(this,
                recyclerViewUsers, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, final int position) {

                //creating the dialog clicking List View

                final Dialog openDialog = new Dialog(ListActivity.this);

                openDialog.setContentView(R.layout.update_delete_layout);//setting layout for dialog

                openDialog.show();//showing dialog

                //finding views for TextViews

                TextView updateList = openDialog.findViewById(R.id.update_text_view);

                TextView deleteList = openDialog.findViewById(R.id.delete_text_view);

                //On Click Event for TextViews

                updateList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(ListActivity.this, DetailsActivity.class);

                        i.putExtra("USER NAME", userName);
                        i.putExtra("id", listUsers.get(position).getId());
                        i.putExtra("profilePicture", listUsers.get(position).getProfilePicture());
                        i.putExtra("firstName", listUsers.get(position).getFirstName());
                        i.putExtra("lastName", listUsers.get(position).getLastName());
                        i.putExtra("emailId", listUsers.get(position).getEmail());
                        i.putExtra("userName", listUsers.get(position).getUserName());
                        i.putExtra("password", listUsers.get(position).getPassword());
                        i.putExtra("gender", listUsers.get(position).getGender());
                        i.putExtra("userType", listUsers.get(position).getUserType());
                        i.putExtra("hobbies", listUsers.get(position).getHobbies());


                        startActivity(i);
                        openDialog.dismiss();
                    }
                });

                deleteList.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String uType, uName = "";
                        int index;
                        for (index = 0; index < listUsers.size(); index++) {

                            uName = listUsers.get(index).getUserName();
                            uType = listUsers.get(index).getUserType();
                            if (uType.equals("Admin") && userName.equals(uName)) {
                                databaseHelper.deleteUser(listUsers.get(position));

                                Toast.makeText(activity, "User deleted", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

            }


            @Override
            public void onLongClick(View view, int position) {
                //
            }
        }));
        databaseHelper = new DatabaseHelper(activity);

    }

    //creating instance of Database using MainActivity

    public void populateData() {
        databaseHelper = new DatabaseHelper(activity);

    }

    //After the page loaded it will update with this class

    @Override
    public void onResume() {
        super.onResume();
        LoadTask loadTask = new LoadTask();//creating a instance of LoadTask class
        loadTask.execute();//executing that class

        populateData();//calling the method

    }

    /**
     * This method is to fetch all user records from SQLite
     */

    // AsyncTask is used that SQLite operation not blocks the UI Thread.

    class LoadTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {

            //clearing the lists variable using Hash Map

            listUsers.clear();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            listUsers.clear();
            listUsers.addAll(databaseHelper.getAllUser());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listViewRecyclerAdapter.notifyDataSetChanged();
        }

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
                imageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
