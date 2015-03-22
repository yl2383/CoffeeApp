package com.example.alex.coffeeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        private CoffeeInfo coffeeInfo;
        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

            // The detail Activity called via intent.  Inspect the intent for coffee data.
            Intent intent = getActivity().getIntent();
            if (intent != null && intent.hasExtra("info")) {
                coffeeInfo = (CoffeeInfo) intent.getExtras().getSerializable("info");
                ((TextView) rootView.findViewById(R.id.detailTV1))
                        .setText(coffeeInfo.getName());
                ((TextView) rootView.findViewById(R.id.detailTV2))
                        .setText(coffeeInfo.getDesc());
                Log.e("a", coffeeInfo.getDesc());
                final ImageView imageView = (ImageView) rootView.findViewById(R.id.detailImage);
                String picUrl = coffeeInfo.getImage_url();
                if (picUrl != "") {
                    new Thread(new Runnable() {
                        public void run() {
                            final Bitmap bitmap = CofeInfoAdapter.loadImageFromNetwork(coffeeInfo.getImage_url());
                            imageView.post(new Runnable() {
                                public void run() {
                                    imageView.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }).start();
                }
            }

            return rootView;
        }
    }
}

