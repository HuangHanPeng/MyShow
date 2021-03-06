package com.example.myshow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends ArrayAdapter<mImage>{
    private Context mContext;
    private int mId;
    private List<mImage> mImageList;
    public ImageAdapter(@NonNull Context context, int resource, @NonNull List<mImage> images) {
        super(context, resource, images);
        this.mContext = context;
        this.mImageList = images;
        this.mId = resource;

    }


    @SuppressLint("CheckResult")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        mImage img = getItem(position);
        View view = null;
        final ViewHolder vh;

        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(mId,parent,false);

            vh = new ViewHolder();
            vh.tvTitle = view.findViewById(R.id.tvtitle);
            vh.tvUsername = view.findViewById(R.id.pUserName);
            vh.tvImage = view.findViewById(R.id.tvimage);
            view.setTag(vh);


        }else {

            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        vh.tvTitle.setText(img.getTitle());
        vh.tvUsername.setText(img.getpUserName());
        vh.tvImage.setTag(position);

        Glide.with(mContext).load(img.getImageUrlList().get(0)).into(vh.tvImage);
        return view;
    }



    class ViewHolder{

        TextView tvTitle;
        TextView tvUsername;
        ImageView tvImage;


    }

}
