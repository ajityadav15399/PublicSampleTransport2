package com.orka.publicsampletransport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

public class Capture extends AppCompatActivity {
    private Uri picUri;
    final int CROP_PIC = 2;

    int no=1;
    private Button mButton,btn;
    private ImageButton mim;
    private EditText info;
    private String iref;
   // private ImageView mImageView;
    private StorageReference mStorage;
    private ProgressDialog mprog;
    private static final int CAMERA_REQUEST_CODE=1;
    private DatabaseReference dbRef;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        iref="";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        btn=(Button) findViewById(R.id.button);
        info=(EditText) findViewById(R.id.infotext);
        mStorage= FirebaseStorage.getInstance().getReference();
        mButton=(Button) findViewById(R.id.UButton);
        mim=(ImageButton) findViewById(R.id.imgbtn);
     //   mImageView=(ImageView) findViewById(R.id.UimageView);
        mprog=new ProgressDialog(this);
        dbRef= FirebaseDatabase.getInstance().getReference("staff");
         id=dbRef.push().getKey();


mButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if(!TextUtils.isEmpty(info.getText().toString())){
            dispatchTakePictureIntent();
        }else
            Toast.makeText(Capture.this, "Please Add info of the image First.", Toast.LENGTH_SHORT).show();
    }
});

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });
    }

    String currentPhotoPath;

    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
           // ...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.orka.publicsampletransport.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        Calendar calendar = Calendar.getInstance();

        String timeStamp = DateFormat.getDateTimeInstance().format(calendar.getTime());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        File f = new File(currentPhotoPath);
         picUri = Uri.fromFile(f);
         performCrop();
        Log.d("createImageFilsdf: ",currentPhotoPath);
        return image;
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getString(final String s){
        iref=s;
        dbRef.child(id).child("image").setValue(iref);

    }

    private void performCrop() {
        // take care of exceptions
        UCrop uCrop= UCrop.of(picUri,Uri.fromFile(new File(getCacheDir(),"first.jpeg")));
        //uCrop.withMaxResultSize(2048,2048);
        uCrop.withOptions(getCropOptions());
        uCrop.start(this,CROP_PIC);
    }
    private UCrop.Options getCropOptions(){
        UCrop.Options options= new UCrop.Options();
       // options.setCompressionQuality(95);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        return options;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CAMERA_REQUEST_CODE && resultCode==RESULT_OK) {
/*
             mprog.setMessage("Uploading Image...");
            mprog.show();
            Uri uri = data.getData();
          //  if (uri == null) {
               // StorageReference filepath = mStorage.child("Photos.jpg");
               Bitmap photo = (Bitmap) data.getExtras().get("data");
                uri=getImageUri(this,photo);
                mim.setImageBitmap(photo);
                picUri=uri;
                Log.d("picUri",picUri.toString());
                performCrop();




                /*
             /*   final StorageReference filepath;
                mim.setImageURI(uri);
                filepath = mStorage.child("pics/pic1");
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String name="Harsh";

                        Staff staff = new Staff(name,no);
                        dbRef.child(id).setValue(staff);
                         filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri urk) {
                                final String imgref=urk.toString();
                                getString(imgref);

                            }
                        });




                        dbRef.child(id).child("images").child("img"+no).child("Info").setValue(info.getText().toString());
                        dbRef.child(id).child("images").child("img"+no).child("Timestamp").setValue(ServerValue.TIMESTAMP);
                        mprog.dismiss();
                        Toast.makeText(Capture.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();
                        dbRef.child(id).child("images").child("img"+no).child("image").setValue(iref);


                    }
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        mprog.dismiss();
                    }
                });
                mim.setImageBitmap(photo);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dat = baos.toByteArray();

                UploadTask uploadTask = filepath.putBytes(dat);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        mprog.dismiss();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                        // ...
                        mprog.dismiss();
                        Toast.makeText(Capture.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();
                    }

                });*/
           /* }else {
                mim.setImageURI(uri);
                StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mprog.dismiss();
                        Toast.makeText(Capture.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();
                    }
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        mprog.dismiss();
                    }
                });

                //mim.setImageURI(uri);
                //   mImageView.setImageURI(uri);
         /*
           StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mprog.dismiss();
                    Toast.makeText(Capture.this,"Uploading Finished...",Toast.LENGTH_SHORT).show();
                }
            });
            }*/
        }
        else if (requestCode == CROP_PIC && resultCode==RESULT_OK) {
            // get the returned data
            Uri urii=UCrop.getOutput(data);


         if(urii!=null){
             mprog.setMessage("Saving Data");
             mprog.show();

             mim.setImageURI(urii);
             Calendar calendarr = Calendar.getInstance();

             String timeStamp = DateFormat.getDateTimeInstance().format(calendarr.getTime());
             final StorageReference filepath;
             //     mim.setImageURI(uri);
             filepath = mStorage.child("pics/pic"+timeStamp);
             filepath.putFile(urii).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     String name;
                     FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                     if (user != null) {
                        name = user.getEmail();
                     } else {
                         // No user is signed in
                          name="Default Employee";
                     }


                     Staff staff = new Staff(name,no);
                     dbRef.child(id).setValue(staff);
                     filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                         @Override
                         public void onSuccess(Uri urk) {

                             Calendar calendarr = Calendar.getInstance();

                             String timeStamp = DateFormat.getDateTimeInstance().format(calendarr.getTime());
                             final String imgref=urk.toString()+timeStamp;
                             getString(imgref);

                         }
                     });




                /*     dbRef.child(id).child("images").child("img"+no).child("Info").setValue(info.getText().toString());
                     dbRef.child(id).child("images").child("img"+no).child("Timestamp").setValue(ServerValue.TIMESTAMP);
                     mprog.dismiss();
                     Toast.makeText(Capture.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();
                     dbRef.child(id).child("images").child("img"+no).child("image").setValue(iref);*/
                     dbRef.child(id).child("info").setValue(info.getText().toString());
                     dbRef.child(id).child("timestamp").setValue(ServerValue.TIMESTAMP);
                     mprog.dismiss();
                     Toast.makeText(Capture.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();
                     dbRef.child(id).child("image").setValue(iref);

                 }
                 public void onFailure(@NonNull Exception exception) {
                     // Handle unsuccessful uploads
                     mprog.dismiss();
                 }
             });
           //  mim.setImageBitmap(photo);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //     photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
             byte[] dat = baos.toByteArray();
             StorageMetadata metadata = new StorageMetadata.Builder()
                     .setContentType("image/jpg")
                     .build();
             UploadTask uploadTask = filepath.putBytes(dat, metadata);
             uploadTask.addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception exception) {
                     // Handle unsuccessful uploads
                     mprog.dismiss();
                 }
             }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                     // ...
                     mprog.dismiss();
                     Toast.makeText(Capture.this, "Uploading Finished...", Toast.LENGTH_SHORT).show();
                 }

             });

         }else{
             Log.d("asd","as"+urii.toString());
         }
        }
    }
}
