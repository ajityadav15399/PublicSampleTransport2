package com.orka.publicsampletransport;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClicklistener.onItemClick(v,getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               mClicklistener.onItemlongClick(v,getAdapterPosition());
                return true;
            }
        });
    }

    //Setting detai;s to recyler view
    public void setDetails(Context ctx, String title, String description, String image, String ts){


        TextView mTitleTv= mView.findViewById(R.id.rTitleTv);
        TextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        ImageView mImageTv = mView.findViewById(R.id.imageView);
        TextView mTimeStamp = mView.findViewById(R.id.rTime);

        //set data to view
        mTitleTv.setText(title);
        mDetailTv.setText(description);
        Picasso.get()
                .load(image)
                .fit()
                .centerCrop()
                .into(mImageTv);
//        String t = Long.toString(ts);
        mTimeStamp.setText((ts));

    }
    private ViewHolder.ClickListener mClicklistener;
    public interface ClickListener{
        void onItemClick(View view,int position);
        void onItemlongClick(View view,int position);
    }

    public void setOnClickListener(ViewHolder.ClickListener clickListener){
        mClicklistener = clickListener;
    }
}
