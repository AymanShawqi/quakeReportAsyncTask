/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.asynctask;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=1&limit=50";
   // private  ArrayList<EarthQuake> earthquakes;
    private EarthQuakeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ListView list=(ListView) findViewById(R.id.list);
        mAdapter=new EarthQuakeAdapter(this,new ArrayList<EarthQuake>());
        list.setAdapter(mAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake earthQuake=mAdapter.getItem(position);
                Uri earthQuakeUri=Uri.parse(earthQuake.getUrl());
                Intent webIntent=new Intent(Intent.ACTION_VIEW,earthQuakeUri);
                startActivity(webIntent);
            }
        });

        EarthQuakeAsyncTask task=new EarthQuakeAsyncTask();
        task.execute(USGS_REQUEST_URL);
    }

    private class EarthQuakeAsyncTask extends AsyncTask < String , Void , List<EarthQuake> >{

        @Override
        protected List<EarthQuake> doInBackground(String... urls) {
            if(urls.length<1 || urls[0]==null){
                return null;
            }
            List<EarthQuake> result=QueryUtils.fetchEarthquakeData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<EarthQuake> data) {
            mAdapter.clear();
            if(data != null && !data.isEmpty()){
                mAdapter.addAll(data);
            }
        }
    }
}
