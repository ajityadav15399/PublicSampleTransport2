package com.orka.publicsampletransport;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter  extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;
    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView mTextView1,mTextView2;
         public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imagee);
            mTextView1=itemView.findViewById(R.id.texturl);
            mTextView2=itemView.findViewById(R.id.info);

        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleItems){
        mExampleList=exampleItems;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
         ExampleViewHolder evh=new ExampleViewHolder((view));
         return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem=mExampleList.get(position);
     //   holder.imageView.setImageResource(currentItem.getmImageResource());
       // holder.imageView.setImageBitmap(currentItem.getmImageResource());
        Picasso.get().load(currentItem.getmImageResource()).into(holder.imageView);

       holder.mTextView1.setText(currentItem.getmText());
        holder.mTextView2.setText(currentItem.getmText2());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
