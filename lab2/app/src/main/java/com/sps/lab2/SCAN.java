package com.sps.lab2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
//import org.python.util.PythonIntepreter;

/**
 * Smart Phone Sensing Example 4. Wifi received signal strength.
 */
public class SCAN extends Activity {
    private static final int WIFI_RESCAN_INTERVAL_MS = 2 * 1000;  //default 2s
    /**
     * The wifi manager.
     */
    //private Handler mHandler;
    //private Runnable task;
    private TextView currentX, currentY, currentZ;
    private Context context;
    private File file;
    private float aX = 0;
    private float aY = 0;
    private float aZ = 0;
    private String filename = "test.txt";
    private final Timer timer=new Timer();
    private Button buttonstop;
    private WifiManager wifiManager;
    private ScrollView rssivalue;
    private Integer time=0;
    private List Wifi_list=new ArrayList<>();
    /**
     * The text view.
     */
    private TextView textRssi;
    /**
     * The button.
     */
    private Button buttonRssi;
    private Button process_data;
    private Button Close;
    private FileOutputStream fos=null;
    private byte[] buffer=null;
    public List string_list=new ArrayList<>();
    public Map<String,List<Integer>> feature_a_dict=new HashMap<String,List<Integer>>();
    public Map<String,List<Integer>> feature_b_dict=new HashMap<String,List<Integer>>();
    public Map<String,List<Integer>> feature_c_dict=new HashMap<String,List<Integer>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        // Create items.

        textRssi = (TextView) findViewById(R.id.textRSSI);
        buttonRssi = (Button) findViewById(R.id.buttonRSSI);
        buttonstop=(Button) findViewById(R.id.buttonstop);
        Close=(Button) findViewById(R.id.buttonclose);
        process_data=(Button) findViewById(R.id.buttonprocess);
        //rssivalue=(ScrollView)findViewById(R.id.rssi);

        // Set listener for the button.
        //buttonRssi.setOnClickListener(this);

        buttonstop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    stop();
                    //fos.close();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonRssi.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                trigger();
            }
        });
        Close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //writeWIFItxt(textRssi);
                finish();
                mHandler.removeCallbacks(task);
                                          /*
                                          try {
                                              fos.close();
                                          } catch (IOException e) {
                                              e.printStackTrace();
                                          }

                                           */
            }
        });
        process_data.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                try {
                    process_data();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //ActivityCompat.requestPermissions(Main.this,
        //new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 1);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void process_data() throws IOException {
        context=getApplicationContext();
        AssetManager assetsManager = context.getAssets();


        InputStream inputStream = assetsManager.open("full_dataset.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        feature_selection selected_feature=new feature_selection(bufferedReader);
        string_list=write_selectWIFIdata(selected_feature);
        //data_process(selected_feature,string_list, this.getApplicationContext(), bufferedReader);

    }
    /*
    private void data_process(feature_selection selected_feature, List string_list, Context applicationContext, BufferedReader bufferedReader) {
        //System.out.println("processing data");
        //System.out.println(string_list);
        //System.out.println(string_list.size());

        List room_list=new ArrayList<>();
        List random=new ArrayList<>();
        for (int i=0;i<string_list.size();i++){
            List alist=new ArrayList<>();
            String one_string=(String)string_list.get(i);
            String[] astring=one_string.split(" ");
            alist=Arrays.asList(astring);
            if(alist.get(0).equals(selected_feature.result.get(0))){
                if(feature_a_dict.containsKey(alist.get(2))==false){
                    List aptitude=new
                    feature_a_dict.put(alist.get(2),)
                }
            }
        }






        for(int i=0;i<string_list.size();i++) {
            List alist = new ArrayList<>();
            String one_string=(String)string_list.get(i);
            String[] astring=one_string.split(" ");
            alist = Arrays.asList(astring);
            if(room_list.size()==0){
                room_list.add((String)alist.get(2));
            }
            else if(room_list.contains(alist.get(2))==false){
                room_list.add((String)alist.get(2));
            }
        }
        if(room_list.contains("elevator")==false){
            room_list.add("elevator");
        }
        //System.out.println(room_list);

        feature_a_dict.put("689",random);
        System.out.println(feature_a_dict);
        feature_a_dict.get("689");
        feature_a_dict.get("689").add(3);
        System.out.println(feature_a_dict);


    }

     */

    private List write_selectWIFIdata(feature_selection selected_feature) throws IOException {
        List string_list=new ArrayList<>();
        System.out.println("writing");
        //List<Integer> data_list=new ArrayList<>();
        for(int i=0;i<selected_feature.wifi_list.size();i++){
            String[] data=(String[])selected_feature.wifi_list.get(i);

            if(selected_feature.result.contains(data[0])){
                String text=data[0]+" "+data[1]+" "+data[2]+"\r\n";
                String text_for_dataprocess=data[0]+" "+data[1]+" "+data[2];
                //data_list.add(Integer.parseInt(data[1].substring(1,3)));
                /*
                if(data[0].equals(selected_feature.result.get(0))){
                    feature_a_dict.put(data[2],data_list);
                }
                else if(data[0].equals(selected_feature.result.get(1))){
                    feature_b_dict.put(data[2],data_list);
                }
                else{
                    feature_c_dict.put(data[2],data_list);
                }

                 */
                try{
                    fos=openFileOutput("selectedWIFIdata", MODE_APPEND);
                    fos.write(text.getBytes());
                    string_list.add(text_for_dataprocess);
                    fos.flush();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    try{
                        fos.close();
                        //Toast.makeText(SCAN.this, "Saved successfull", Toast.LENGTH_LONG).show();
                    } catch (IOException e){
                        e.printStackTrace();
                    }

                }
            }
        }

        return string_list;

    }

    // onResume() registers the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
    }

    // onPause() unregisters the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
    }

    public void stop() throws InterruptedException {
        //timer.wait();
        mHandler.removeCallbacks(task);
        textRssi.setText("");
        time=0;
    }
    public void trigger(){
        //timer.schedule(timerTask, 2*1000, 1000);
        mHandler.postDelayed(task,1000);
    };



    public void scanwifi() {
        time+=1;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String room_number=bundle.getString("room number");
        // Set text.
        textRssi.setText(new StringBuilder().append("\n\tOne time SCAN:").append(time.toString()).toString());

        // Set wifi manager.
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Start a wifi scan.
        wifiManager.startScan();
        // Store results in a list.
        List<ScanResult> scanResults = wifiManager.getScanResults();
        System.out.println(scanResults);
        // Write results to a label
        for (ScanResult scanResult : scanResults) {
            textRssi.setText(textRssi.getText() + "\n\tBSSID = "
                    + scanResult.BSSID + "   RSSI = "
                    + scanResult.level + "dBm" +"  "+room_number);
            writeWIFItxt(textRssi,scanResult,room_number);
        }
    }
    /*
    public void predict_data(){

    }

     */

    public void writeWIFItxt(TextView textRssi, ScanResult scanResult, String room_number){

        //String directory="android.resource://"+getPackageName()+"/"+R.raw.test;
        //InputStream is = getResources().openRawResource(R.id.filename);

        //context=getApplicationContext();


        String text=scanResult.BSSID+" "
                +scanResult.level + "dBm"
                +" "+room_number+"\r\n";
        try{
            fos=openFileOutput("WIFIdata", MODE_APPEND);
            fos.write(text.getBytes());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try{
                fos.close();
                //Toast.makeText(SCAN.this, "Saved successfull", Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
            }

        }


    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    scanwifi();

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private Runnable task = new Runnable() {
        @Override
        public void run() {

            mHandler.sendEmptyMessage(1);
            mHandler.postDelayed(this, 5 * 1000);//延迟5秒,再次执行task本身,实现了循环的效果
        }
    };


    /*
    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {


            mHandler.sendEmptyMessage(1);//通知UI更新
            mHandler.postDelayed(this,2*1000);
        }
    };

     */





}