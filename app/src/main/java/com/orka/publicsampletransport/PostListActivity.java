package com.orka.publicsampletransport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.picker.CalendarConstraints;
import com.google.android.material.picker.MaterialDatePicker;
import com.google.android.material.picker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostListActivity extends AppCompatActivity  {

    LinearLayoutManager mLayoutManager;
    SharedPreferences mSharedPref;
    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    private void firebaseSearch(String searchText) {

        Query firebaseSearchQuery = mRef.orderByChild("sname").startAt(searchText).endAt(searchText + "\uf8ff");
        updatedata(firebaseSearchQuery);

    }

    private void updatedata(Query firebaseSearchQuery) {

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {
                        viewHolder.setDetails(getApplicationContext(),model.getSname(),model.getInfo(),model.getImage(),model.getStringTimestamp());

                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
//                                System.out.println("---------------------------");
//                                System.out.println(position);
                                String data = Integer.toString(position);

                                TextView mTimeTv = view.findViewById(R.id.rTime);
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mDescTv = view.findViewById(R.id.rDescriptionTv);
                                ImageView mImageView = view.findViewById(R.id.imageView);

                                String mTitle = mTitleTv.getText().toString();
                                String mDesc = mDescTv.getText().toString();
                                String mTime = mTimeTv.getText().toString();
                                System.out.println(mDesc);
                                Intent intent = new Intent(view.getContext(),PostDetailActivity.class);

                                intent.putExtra("image",data);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description",mDesc);
                                intent.putExtra("time",mTime);
                                startActivity(intent);



                            }

                            @Override
                            public void onItemlongClick(View view, int position) {

                            }
                        });
//                        return super.onCreateViewHolder(parent, viewType);
                        return viewHolder;
                    }
                };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_list);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("PostsList");



        mSharedPref = getSharedPreferences("SortSettings",MODE_PRIVATE);
        String mSorting = mSharedPref.getString("Sort","newest");

        if(mSorting.equals("newest")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
        }
        else if(mSorting.equals("oldest")){
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(false);
            mLayoutManager.setStackFromEnd(false);
        }


        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        //set linear layout

        mRecyclerView.setLayoutManager(mLayoutManager);
        //send query to firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("staff");

    }

    //load data into recyler view


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model,ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.row,
                        ViewHolder.class,
                        mRef
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int i) {

                        viewHolder.setDetails(getApplicationContext(),model.getSname(),model.getInfo(),model.getImage(),model.getStringTimestamp());
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

                        ViewHolder viewHolder = super.onCreateViewHolder(parent,viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
//                                System.out.println("---------------------------");
//                                System.out.println(position);
                                String data = Integer.toString(position);

                                TextView mTimeTv = view.findViewById(R.id.rTime);
                                TextView mTitleTv = view.findViewById(R.id.rTitleTv);
                                TextView mDescTv = view.findViewById(R.id.rDescriptionTv);
                                ImageView mImageView = view.findViewById(R.id.imageView);

                                String mTitle = mTitleTv.getText().toString();
                                String mDesc = mDescTv.getText().toString();
                                String mTime = mTimeTv.getText().toString();
                                System.out.println(mDesc);
//
//                                Drawable mDrawable = mImageView.getDrawable();
//                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();

                                Intent intent = new Intent(getApplicationContext(),PostDetailActivity.class);

                                intent.putExtra("image",data);
                                intent.putExtra("title", mTitle);
                                intent.putExtra("description",mDesc);
                                intent.putExtra("time",mTime);
                                startActivity(intent);



                            }

                            @Override
                            public void onItemlongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };

        //set adapter to recycler view

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);


                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_sort){

            showSortDialog();
            //TODO
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void showSortDialog() {

        String[] sortOptions = {"Newest","Oldest","From-To"};
        AlertDialog.Builder builder = new AlertDialog.Builder((this));
        builder.setTitle("SortBy")
                .setIcon(R.drawable.ic_action_sort)
                .setItems(sortOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","newest");
                            editor.apply();
                            recreate();
                        }
                        else if(which ==1 ){
                            SharedPreferences.Editor editor = mSharedPref.edit();
                            editor.putString("Sort","oldest");
                            editor.apply();
                            recreate();

                        }
                        else if(which ==2){
                            showDatePickerDialog();
                        }

                    }
                });
        builder.show();
    }


        private void showDatePickerDialog() {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

            MaterialDatePicker<?> picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Object>() {

                @Override
                public void onPositiveButtonClick(Object selection) {
//                    System.out.println("-------------------------------------------------------------------------------------------------------------------");
                    String str = selection.toString();
//                    System.out.println(str);
                    Pattern pattern = Pattern.compile("\\w+([0-9]+)\\w+([0-9]+)");
                    Matcher matcher = pattern.matcher(str);
                    String[] data = new String[2];
                    for(int i = 0 ; i < matcher.groupCount(); i++) {
                        matcher.find();
                        data[i] = matcher.group();
//                        System.out.println(data[i]);
                    }
                    Query firebaseSearchQuery = mRef.orderByChild("timestamp").startAt(Long.parseLong(data[0])).endAt(Long.parseLong(data[1]));
                    updatedata(firebaseSearchQuery);

                }
            });

    }

}

