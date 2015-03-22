package com.example.alex.coffeeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by alex on 2015/3/20.
 */
public class CofeInfoAdapter extends ArrayAdapter<CoffeeInfo> {
    public CofeInfoAdapter(Context context, List<CoffeeInfo> cofeInfos) {
        super(context, 0, cofeInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final CoffeeInfo coffeeInfo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_item, parent, false);
        }

        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvDesc = (TextView) convertView.findViewById(R.id.tvDescription);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);

        // Populate the data into the template view using the data object
        tvName.setText(coffeeInfo.getName());
        tvDesc.setText(coffeeInfo.getDesc());

        String picUrl = coffeeInfo.getImage_url();
        if(picUrl !=""){
            new Thread(new Runnable() {
                public void run() {
                    final Bitmap bitmap = loadImageFromNetwork(coffeeInfo.getImage_url());
                    imageView.post(new Runnable() {
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        }



        return convertView;
    }

    protected static Bitmap loadImageFromNetwork(String url) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
