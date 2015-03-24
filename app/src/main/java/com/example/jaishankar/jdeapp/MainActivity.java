package com.example.jaishankar.jdeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MainActivity extends ActionBarActivity {
    private ProgressBar pb;
    private TextView tv;
    private ListView listView;
    private String[] names;
    private Map<String, ArrayList<SKU>> skumap;
    private Context activityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityContext = this;
        pb = (ProgressBar) findViewById(R.id.progressBar);

        tv = (TextView) findViewById(R.id.textView);
        listView = (ListView) findViewById(R.id.listView);
        skumap = new HashMap<>();
        tv.setText("Please wait downloading...");
        GetJSONFromURL asynctask = new GetJSONFromURL();
        asynctask.execute("http://quiet-stone-2094.herokuapp.com/transactions.json");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class GetJSONFromURL extends AsyncTask<String, Integer, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... params) {
            JSONArray SKUobj = null;
            try {
                URL url = new URL(params[0]);
                publishProgress(1);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                publishProgress(2);
//                float size = (float)conn.getContentLength();
                InputStream in = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuffer jsonBuffer = new StringBuffer(100);
                char[] buffer = new char[100];
                int read = 0;
                float total_read = 0;
//                Log.e("Size:", Float.toString(size));
                publishProgress(20);
                while((read = br.read(buffer)) != -1) {

                    jsonBuffer.append(buffer, 0 ,read);
                    total_read += read;
                    buffer = new char[100];
//                    int progress = ((int)(total_read/size)*80) + 2;
//                    Log.e("Progress:", Integer.toString(progress));
//                    publishProgress(progress);
                }
                Log.e("last chars:", new String(buffer));
                //publishProgress(100);
                Log.e("Buffer Length: ", Integer.toString(jsonBuffer.length()));
                Log.e("Buffer Length: ", jsonBuffer.charAt(jsonBuffer.length() - 1) + "");
                SKUobj = new JSONArray(jsonBuffer.toString());
                publishProgress(70);


            } catch (MalformedURLException e) {
                Log.e("Exception", "", e);
            } catch (IOException e) {
                Log.e("Exception", "", e);
            } catch (JSONException e) {
                Log.e("Exception", "", e);
            }

            return SKUobj;
        }


        protected void onProgressUpdate(Integer... progress){
            pb.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            try {
                JSONObject jobj;

                for(int i=0; i<result.length(); i++) {
                    jobj = (JSONObject) result.get(i);
                    String name = jobj.getString("sku");
                    Double amount = jobj.getDouble("amount");
                    String curr = jobj.getString("currency");
                    ArrayList a = skumap.get(name);
                    if(a == null){
                        a = new ArrayList<>();
                        a.add(new SKU(name, amount, curr));
                        skumap.put(name,a);
                    }
                    else {
                        a.add(new SKU(name, amount, curr));
                    }


                }
                names = new String[skumap.keySet().size()];
                skumap.keySet().toArray(names);

                pb.setProgress(80);
                ArrayAdapter adapter = new ArrayAdapter(activityContext, android.R.layout.simple_list_item_1, names);
                listView.setAdapter(adapter);
                pb.setProgress(100);
                tv.setText("Download Complete!");
                pb.setVisibility(View.GONE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = names[position];
                        ArrayList items = skumap.get(name);

                        AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);

                        ListView lv = new ListView(activityContext);
                        SKUListAdapter adapter1 = new SKUListAdapter(activityContext, R.layout.row, items);
                        lv.setAdapter(adapter1);
                        builder.setTitle(name);
                        builder.setView(lv);
                        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do something
                            }
                        });
                        AlertDialog al = builder.create();
                        al.show();
                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
