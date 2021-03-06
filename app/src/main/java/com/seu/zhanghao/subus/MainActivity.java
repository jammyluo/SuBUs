package com.seu.zhanghao.subus;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
     private EditText editText;
     private Button searchButton;
     private TextView responseText;
     private ListView listView;
     private ArrayAdapter<String> adapter;
     private List<String> dataList = new ArrayList<String>();
     private  static final String URL_LINK ="http://www.szjt.gov.cn/apts/APTSLine.aspx";
     public  static  final  int SHOW_RESPONSE = 0;

    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response =  (String)msg.obj;
                    responseText.setText(response);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        responseText = (TextView) findViewById(R.id.textView3);
        editText= (EditText) findViewById(R.id.input_text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus){
                    editText.setHint("请输入您要查询的线路");
                }else {
                    editText.setHint(null);
                }
            }
        });

        //按钮响应
        searchButton= (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (v.getId() == R.id.search_button){
                   sendRequestWithHttpURLConnection(editText.getText().toString());
                   Log.d("debug","here");
               }
            }
        });

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
          //  Intent intent =new Intent(MainActivity.this,BusAssisActivity.class);
        } else if (id == R.id.nav_gallery) {
            Intent intent =new Intent(MainActivity.this,InfoActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //调用queryRequest1(String lineName)后解析List内的链接，并将其传入到queryRequest2(String hrefString)中
    //返回查询结果
    public static List getBusInfo(String hrefString){
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
      //  String htmlString = doGet(hrefString);
       // list=resolveHtml(htmlString);

        return list;
    }

    //执行一个HTTP GET请求，返回请求响应的HTML
    //url 请求的URL地址
    //queryString 请求的查询参数,可以为null
    //pretty是否美化
    //返回请求响应的HTML
    public String doGet(String queryString) {
        URL get_url = null;
        String responseHtml =null;
        HttpURLConnection connection = null;
        try {
            get_url = new URL(URL_LINK+queryString);
            Log.d("debug",get_url.toString());
            connection = (HttpURLConnection) get_url.openConnection();

            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                response.append(line);
            }
            Log.d("debug",responseHtml=response.toString());
            Message message =  new Message();
            message.what = SHOW_RESPONSE;
            //将服务器返回的结果存放到Message中
            message.obj = response.toString();
            handler.sendMessage(message);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return responseHtml;
    }


    private void sendRequestWithHttpURLConnection(final String queryString){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("debug","run");
                doGet(queryString);
            }
        }).start();

    }

}
