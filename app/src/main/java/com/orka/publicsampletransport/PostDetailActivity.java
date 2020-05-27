package com.orka.publicsampletransport;

import androidx.annotation.NonNull;
import android.app.ProgressDialog;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.common.util.JsonUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class PostDetailActivity extends AppCompatActivity {
    OutputStream out;
    ProgressDialog ps;
    String time,imagename;
    File path,dir,file;
    PhotoView photoView;
    TextView mTitleTv,mDetailTv,mTimeTv;
    ImageView mImageTv;
    ImageButton mbtn;
    Bitmap bitmap;
    private static final int WRITE_EXTERNAL_STORAGE_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mTitleTv = (TextView)findViewById(R.id.titleTv);
        mDetailTv = (TextView)findViewById(R.id.descriptionTv);
//        mImageTv = findViewById(R.id.ImageView);
        mTimeTv = (TextView) findViewById(R.id.rTime);
        mbtn = (ImageButton) findViewById(R.id.download);
        ps=(ProgressDialog) new ProgressDialog(this);

//        byte[] bytes = getIntent().getByteArrayExtra("image");
        String ImagePosition = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String desc = getIntent().getStringExtra("description");
        String time = getIntent().getStringExtra("time");

        System.out.println("---------------------------");
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        System.out.println(database);
        final int position = Integer.parseInt(ImagePosition);

        database.child("staff").addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int count = 0;
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(count == position){

                                    Map<String, Object> newPost = (Map<String, Object>) snapshot.getValue();
                                     String str = newPost.get("image").toString();

                                    photoView = (PhotoView) findViewById(R.id.ImageView);
                                    Picasso.get().load(str).into(photoView);
                                    return;
                                }
                                count++;

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        mbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskImageDownloader asyncTask = new AsyncTaskImageDownloader();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){

                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, WRITE_EXTERNAL_STORAGE_CODE);
                    }
                    else
                        if(photoView != null){
                            asyncTask.execute();
                        }
                }
                else
                if(photoView != null){
                    asyncTask.execute();
                }
//                        saveImage();
            }
        });


        mTitleTv.setText(title);
        mDetailTv.setText(desc);
//        mTimeTv.setText(time);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case WRITE_EXTERNAL_STORAGE_CODE:{
                if(grantResults.length>0 &&grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    AsyncTaskImageDownloader asyncTask = new AsyncTaskImageDownloader();
                    asyncTask.execute();

                }
                else{
                    Toast.makeText(this,"enable Permission to download image",Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void saveImage(Bitmap bitmap) {



        try{
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
            out.flush();
            out.close();

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            sendBroadcast(intent);
            Toast.makeText(PostDetailActivity.this,"Image saved Succesfully ",Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){

            Toast.makeText(PostDetailActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

        }
        ps.dismiss();


    }
    private class AsyncTaskImageDownloader extends AsyncTask<String,String,Bitmap>{
        private String TAG = "DownloadImage";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ps = new ProgressDialog(PostDetailActivity.this);
            ps.setMessage("Please wait...It is downloading");
            ps.setIndeterminate(false);
            ps.setCancelable(false);
            ps.show();

        }


        @Override
        protected Bitmap doInBackground(String... strings) {
            bitmap = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
            time = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis());
            path = Environment.getExternalStorageDirectory();
            dir = new File(path+"/PublicSampleTransport");
            dir.mkdir();
            imagename = time+" .PNG";
            file = new File(dir,imagename);
            return bitmap;

        }

        protected void onPostExecute(Bitmap result) {
            saveImage(result);
            ps.dismiss();
        }


    }

}
