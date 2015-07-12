package com.athvillar.tausy.top10downloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {

    Button btnParse;
    ListView listApps;
    String xmlData;
    //TextView xmlFileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnParse = (Button) findViewById(R.id.btnParse);
        listApps = (ListView) findViewById(R.id.listApps);

        //xmlFileData = (TextView) findViewById(R.id.txtViewTitle);


        //xmlFileData.setText(xmlData);
        btnParse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ParseApplications parseApplications = new ParseApplications(xmlData);
                System.out.println(xmlData);
                boolean operationStatus = parseApplications.process();
                Log.d("OP_STATUS",operationStatus+"");
                if(operationStatus){
                    ArrayList<Application> allApps = parseApplications.getApplications();
                    ArrayAdapter<Application> adapter = new ArrayAdapter<>(MainActivity.this,R.layout.list_item,allApps);
                    listApps.setVisibility(listApps.VISIBLE);
                    listApps.setAdapter(adapter);
                }else{
                    Log.d("MainActivity","Error Parsing File");
                }

            }
        });

        new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
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

    private class DownloadData extends AsyncTask<String,Void, String>{

        String myXmlData;

        protected String doInBackground(String... urls) {
            try {
                myXmlData = downloadXML(urls[0]);

            } catch(IOException e) {
                return "Unable to download XML file.";
            }

            return "";
        }

        protected void onPostExecute(String result) {
            Log.d("OnPostExecute", myXmlData);
            xmlData = myXmlData;
        }

        private String downloadXML(String theUrl) throws IOException {
            int BUFFER_SIZE=2000;
            InputStream is=null;

            String xmlContents = "";

            try {
                URL url = new URL(theUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                int response = conn.getResponseCode();
                Log.d("DownloadXML", "The response returned is: " + response);
                is = conn.getInputStream();

                InputStreamReader isr = new InputStreamReader(is);
                int charRead;
                char[] inputBuffer = new char[BUFFER_SIZE];
                try {
                    while((charRead = isr.read(inputBuffer))>0)
                    {
                        String readString = String.copyValueOf(inputBuffer, 0, charRead);
                        xmlContents = xmlContents + readString;
                        inputBuffer = new char[BUFFER_SIZE];
                    }

                    return xmlContents;

                } catch(IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } finally {
                if(is != null)
                    is.close();
            }

        }
    }
}
