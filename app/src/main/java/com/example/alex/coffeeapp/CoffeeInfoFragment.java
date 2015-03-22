package com.example.alex.coffeeapp;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class CoffeeInfoFragment extends Fragment {

    private CofeInfoAdapter mForecastAdapter;

    public CoffeeInfoFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        FetchDataTask dataTask = new FetchDataTask();
        dataTask.execute(getString(R.string.web_url));

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_coffeefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            FetchDataTask dataTask = new FetchDataTask();
            dataTask.execute(getString(R.string.web_url));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        List<CoffeeInfo> infoList = new ArrayList<CoffeeInfo>();


        // Now that we have some dummy forecast data, create an ArrayAdapter.
        // The ArrayAdapter will take data from a source (like our dummy forecast) and
        // use it to populate the ListView it's attached to.
        mForecastAdapter =
                new CofeInfoAdapter(
                        getActivity(), // The current context (this activity)
                        infoList);

        View rootView = inflater.inflate(R.layout.fragment_coffee_info, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_coffee);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }

    public class FetchDataTask extends AsyncTask<String ,Void ,CoffeeInfo[]>{

        private final String LOG_TAG = FetchDataTask.class.getSimpleName();

        @Override
        protected CoffeeInfo[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String coffeeInfoStr = null;

            try{
                final String coffeeInfoBaseUrl = getString(R.string.web_url);
                final String QUERY_PARAM = "api_key";
                final String api_key = getString(R.string.api_key);


                Uri builtUri = Uri.parse(coffeeInfoBaseUrl).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, api_key)
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to percolate, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                coffeeInfoStr = buffer.toString();


                Log.v(LOG_TAG, "CoffeeInfo string: " + coffeeInfoStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getDataFromJson(coffeeInfoStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(CoffeeInfo[] result) {
            if (result != null) {
                mForecastAdapter.clear();
                for(CoffeeInfo coffeeInfo : result) {
                    mForecastAdapter.add(coffeeInfo);
                }
                // New data is back from the server.  Hooray!
            }
        }

        private CoffeeInfo[] getDataFromJson(String jsonStr) throws JSONException {
            final String OWM_DESC = "desc";
            final String OWM_IMAGE_URL = "image_url";
            final String OWM_ID = "id";
            final String OWM_NAME = "name";

            JSONArray cofeInfoJSONArray = new JSONArray(jsonStr);

            CoffeeInfo result[] = new CoffeeInfo[cofeInfoJSONArray.length()];
            for(int i = 0; i<cofeInfoJSONArray.length();i++){
                String desc;
                String url;
                String id;
                String name;

                JSONObject cofeInfoJson = cofeInfoJSONArray.getJSONObject(i);
                desc = cofeInfoJson.getString(OWM_DESC);
                url = cofeInfoJson.getString(OWM_IMAGE_URL);
                id = cofeInfoJson.getString(OWM_ID);
                name = cofeInfoJson.getString(OWM_NAME);

                result[i] = new CoffeeInfo(desc,url,id,name);
                Log.e("info", result[i].toString());
            }
            return result;
        }
    }
}
