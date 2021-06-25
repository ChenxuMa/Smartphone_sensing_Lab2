package com.sps.lab2;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

public class Total_service extends AppCompatActivity {
    private Button initial_belief;
    private Button locate_me;
    private Button stop;
    //private Button serial;
    private WifiManager wifiManager;
    private TextView predict_room;
    private List wifi_name_list=new ArrayList<>();
    //private Parallel parallel;
    private List parallel=new ArrayList<>();
    private List serial_list=new ArrayList<>();
    private List initial_prior=new ArrayList<>();
    private Integer feature_number=12;
    private Button online_test;
    private String[] mac_list;
    private String final_predict_result;
    private EditText label;
    private FileOutputStream fos=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String mac_string="4c:1b:86:56:7a:6f, ac:22:05:8b:d2:13, ac:22:05:8b:d2:59, 44:fe:3b:74:48:87, c8:54:4b:92:43:62, b4:75:0e:5d:b0:d5, ac:22:05:16:f1:d5, c0:25:06:92:71:5f, c8:54:4b:93:d1:02, c8:54:4b:93:d1:01, 50:7e:5d:7c:b3:32, c8:54:4b:92:43:61";
        mac_list=mac_string.split(", ");
        for (int i=0;i<mac_list.length;i++){
            wifi_name_list.add(mac_list[i]);
        }






        //wifi_name_list.add("00:24:97:17:6c:70");






        //wifi_name_list.add("22:3b:f3:89:25:6b");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_service);
        initial_belief=(Button) findViewById(R.id.button6);
        locate_me=(Button) findViewById(R.id.button5);
        predict_room=(TextView) findViewById(R.id.textView2);
        //serial=(Button)findViewById(R.id.button7);
        stop=(Button) findViewById(R.id.button8);
        online_test=(Button) findViewById(R.id.button4);
        label=(EditText)findViewById(R.id.editTextTextPersonName2);
        initial_belief.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(parallel.size()!=0){
                    parallel.clear();
                }
                for(int i=0;i<wifi_name_list.size();i++){
                    parallel.add(new Parallel());
                    Parallel item=(Parallel)parallel.get(i);

                    item.initial_prior(mac_list[i]);
                }
                /*
                Context context=getApplicationContext();
                AssetManager assetsManager = context.getAssets();


                InputStream inputStream = null;
                try {
                    inputStream = assetsManager.open("full_dataset.txt");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    feature_selection selected_feature=new feature_selection(bufferedReader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SCAN scan=new SCAN();
                wifi_name_list=scan.write_selectWIFIdata(selected_feature);


                 */
                //predict_room.setText("");

                Toast.makeText(Total_service.this,"Prior probability has been initialized", Toast.LENGTH_SHORT).show();
            }
        });
        locate_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                predict_room.setText("Detecting...");
                trigger();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shutdown();
            }
        });
        /*
        serial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(serial_list.size()!=0){
                    serial_list.clear();
                }
                for(int i=0;i<5;i++){
                    serial_list.add(new Serial());
                    Serial item=(Serial)serial_list.get(i);
                    item.initial_prior(mac_list[i]);
                }
                //trigger_serial();
            }
        });

         */
        online_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                accuracy();
            }
        });
    }
    /*
    private void trigger_test() {
        //mHandler.postDelayed(task1, )
        mHandler.postDelayed(task1, 1000);
    }

     */
    /*
    private void trigger_serial() {
        mHandler.postDelayed(task1,1000);
    }

     */


    private void shutdown() {

        mHandler.removeCallbacks(task);
        predict_room.setText("No Location");
        if(parallel.size()!=0){
            parallel.clear();
        }
    }

    public void trigger(){
        mHandler.postDelayed(task,1000);
    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    accuracy();
                case 1:
                    scanwifi(1);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private void accuracy() {
        String input_label=label.getText().toString();
        String text=input_label+" "+final_predict_result+"\r\n";
        try{
            fos=openFileOutput("test_data", MODE_APPEND);
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

    private Runnable task = new Runnable() {
        @Override
        public void run() {

            mHandler.sendEmptyMessage(1);
            //mHandler.postDelayed(this, 5 * 1000);//延迟5秒,再次执行task本身,实现了循环的效果
        }
    };

    public void scanwifi(int i) {
        //time+=1;
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        Map<String, Integer> mac_filter=new HashMap<String, Integer>();
        //String room_number=bundle.getString("room number");
        // Set text.
        //textRssi.setText(new StringBuilder().append("\n\tOne time SCAN:").append(time.toString()).toString());

        // Set wifi manager.
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // Start a wifi scan.
        wifiManager.startScan();
        // Store results in a list.
        List<ScanResult> scanResults = wifiManager.getScanResults();
        for (ScanResult scanResult:scanResults){
            if(wifi_name_list.contains(scanResult.BSSID)){
                mac_filter.put(scanResult.BSSID,scanResult.level);
            }
        }
        System.out.println(mac_filter);

        //System.out.println(scanResults);
        //predict_room.setText("");
        if(i==1){
            final_predict_result="cell"+predict_result(mac_filter,wifi_name_list);
            predict_room.setText(final_predict_result);
        }
        else if(i==0){
            predict_result_serial(mac_filter, wifi_name_list);
        }
        

        /*
        // Write results to a label
        for (ScanResult scanResult : scanResults) {
            textRssi.setText(textRssi.getText() + "\n\tBSSID = "
                    + scanResult.BSSID + "   RSSI = "
                    + scanResult.level + "dBm" +"  "+room_number);
            writeWIFItxt(textRssi,scanResult,room_number);
        }

         */
    }

    private void predict_result_serial(Map<String, Integer> scanResults, List wifi_name_list) {
        List actual_feature=new ArrayList<>();
        int[] signal=new int[5];
        int[] voting_vector=new int[feature_number];
        double[] max_prob_voting=new double[feature_number];
        int count=0;
        Map<String, Integer> signal_new=new HashMap<String,Integer>();
        for (int i=0;i<5;i++){
            String item=(String) wifi_name_list.get(i);
            actual_feature.add(item);
        }
        for(int i=0;i<serial_list.size();i++) {
            String file_Name = i + "_feature_pmf_new.txt";

            Serial item = (Serial) serial_list.get(i);
            try {
                item.readDataset(file_Name, this.getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(Map.Entry<String, Integer> entry:scanResults.entrySet()){
                if(item.mac_address.equals(entry.getKey())){
                    signal_new.put(entry.getKey(), entry.getValue());
                    //count++;
                }
            }
        }
        for(Map.Entry<String, Integer> entry:signal_new.entrySet()){
            signal[count]=abs(entry.getValue());
            count++;
        }
        Serial first_item=(Serial) serial_list.get(0);
        Serial second_item=(Serial) serial_list.get(1);
        Serial third_item=(Serial) serial_list.get(2);
        Serial fourth_item=(Serial) serial_list.get(3);
        Serial fifth_item=(Serial) serial_list.get(4);
        first_item.initial_prior(mac_list[0]);
        first_item.computeLocation(first_item.dataset, signal[0]);
        if(first_item.max_prob_localization>0.9){
            predict_room.setText("Cell"+" "+String.valueOf(first_item.max_prob_localization_index));
            Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();
        }
        else{
            second_item.priorProb =  first_item.priorProb;
            second_item.computeLocation(second_item.dataset, signal[1]);
            //second_item.priorProb = first_item.computeLocation(first_item.dataset, signal[0]);
            //second_item.computeLocation(first_item.dataset, signal[1]);
            if(second_item.max_prob_localization > 0.9)
            {
                predict_room.setText("Cell"+" "+String.valueOf(second_item.max_prob_localization_index));
                Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();
            }
            else{
                third_item.priorProb =  second_item.priorProb;
                third_item.computeLocation(third_item.dataset, signal[2]);
                if(third_item.max_prob_localization>0.9){
                    predict_room.setText("Cell"+" "+String.valueOf(third_item.max_prob_localization_index));
                    Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();
                }else{

                    fourth_item.priorProb=third_item.priorProb;
                    fourth_item.computeLocation(fourth_item.dataset,signal[3]);
                    if(fourth_item.max_prob_localization>0.9){
                        predict_room.setText("Cell"+" "+String.valueOf(fourth_item.max_prob_localization_index));
                        Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();

                    }
                    else{
                        fifth_item.priorProb=fourth_item.priorProb;
                        fifth_item.computeLocation(fifth_item.dataset,signal[4]);
                        predict_room.setText("Cell"+" "+String.valueOf(fifth_item.max_prob_localization_index));
                        Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();
                    }
                }
        }

        }

        //first_item.initial_prior();
        //first_item.computeLocation(first_item.dataset, signal[0]);


        /*
        if(first_item.max_prob_localization > 0.9)
        {
            Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();
        }
        else
        {
            Serial next_item=(Serial)
            Serial2.priorProb =  Serial1.computeLocation(testDataSet1, strength1);
            Serial2.computeLocation(testDataSet2, strength2);
            if(Serial2.max_prob_localization > 0.9)
            {
                System.out.println("The Convergence has been reached"+"Cell" + String.valueOf(Serial2.max_prob_localization_index));
            }
            else
            {
                Serial3.priorProb =  Serial2.computeLocation(testDataSet1, strength1);
                Serial3.computeLocation(testDataSet2, strength2);
                System.out.println("The Convergence has been reached"+"Cell" + String.valueOf(Serial3.max_prob_localization_index));
                //这里使用了三个表默认第三次一定收敛,在APP上可以嵌套五个表，默认第五次一定收敛，这样就与parallel统一了
            }
        }

         */

            //return null;
    }

    public String predict_result(Map<String, Integer> scanResults, List wifi_name_list){
        Boolean finish=false;
        /*
        List mac_list=new ArrayList<>();
        List identical_mac=new ArrayList<>();
        for(int i=0;i<scanResults.size();i++){
            mac_list.add(scanResults.get(i).BSSID);
        }
        for (int i=0;i<mac_list.size();i++){
            for(int j=0;j<wifi_name_list.size();j++){
                if(mac_list.get(i).equals(wifi_name_list.get(j))){
                    identical_mac.add(mac_list.get(i));
                }
            }
        }

         */
        int[] signal=new int[feature_number];
        Map<String, Integer> signal_new=new HashMap<String,Integer>();
        /*
        int count=0;
        for(ScanResult scanResult: scanResults){

            if(wifi_name_list.contains(scanResult.BSSID)){
                signal[count]=(int)abs(scanResult.level);
                count++;
            }

            if(count==18){
                break;
            }
        }

         */



        //Parallel[] model_list=new Parallel[19];
        int[] voting_vector=new int[feature_number];
        double[] max_prob_voting=new double[feature_number];
        /*
        for(int i=0;i<parallel.priorProb.length;i++){
            System.out.println("--------------");
            System.out.println(parallel.priorProb[i]);
        }

         */
        String mac_string="4c:1b:86:56:7a:6f, ac:22:05:8b:d2:13, ac:22:05:8b:d2:59, 44:fe:3b:74:48:87, c8:54:4b:92:43:62, b4:75:0e:5d:b0:d5, ac:22:05:16:f1:d5, c0:25:06:92:71:5f, c8:54:4b:93:d1:02, c8:54:4b:93:d1:01, 50:7e:5d:7c:b3:32, c8:54:4b:92:43:61";
        String[] mac_list=mac_string.split(", ");
        int count=0;
        for(int i=0;i<parallel.size();i++){
            String file_Name=i+"_feature_pmf_new.txt";

            Parallel item=(Parallel)parallel.get(i);
            try{
                item.readDataset(file_Name, this.getApplicationContext());
            }catch(IOException e){
                e.printStackTrace();
            }

            for(Map.Entry<String, Integer> entry:scanResults.entrySet()){
                if(item.mac_address.equals(entry.getKey())){
                    signal_new.put(entry.getKey(), entry.getValue());
                    //count++;
                }
            }




            /*
            Integer memory=0;
            for(ScanResult scanResult:scanResults){

                String feature_string=(String)wifi_name_list.get(i);
                String scan_string=scanResult.BSSID.substring(0,15);
                if(scanResult.BSSID.equals(feature_string)){
                    Integer wifi_aptitude=(int)abs(scanResult.level);
                    if(wifi_aptitude!=memory){
                        memory=(int)abs(scanResult.level);
                        System.out.println(feature_string);
                        signal[count]=(int)abs(scanResult.level);
                        System.out.println(signal[count]);
                        count++;
                    }
                    else{
                        continue;
                    }



                }




            }

             */


        }
        for(Map.Entry<String, Integer> entry:signal_new.entrySet()){
            signal[count]=abs(entry.getValue());
            count++;
        }
        //System.out.println("$$$$$$$$$$$$$$$$$$$");
        //System.out.println(signal.length);
        /*
        for(int i=0;i<model_list.length;i++){
            for(int j=0;j<model_list[i].priorProb.length;j++){
                System.out.println("*****************");
                System.out.println(model_list[i].priorProb[j]);
            }

        }

         */

        for(int i=0;i<parallel.size();i++){
            Parallel item=(Parallel)parallel.get(i);
            voting_vector[i]=item.computeLocation(item.dataset, signal[i]);
            max_prob_voting[i]=item.max_prob;

        }
        List actual_voting_vector=new ArrayList<>();
        List actual_max_prob_vector=new ArrayList<>();
        for(int i=0;i<voting_vector.length;i++){
            if(signal[i]!=0){
                actual_voting_vector.add(voting_vector[i]);
                actual_max_prob_vector.add(max_prob_voting[i]);

            }
        }

        System.out.println(actual_voting_vector);
        System.out.println(actual_max_prob_vector);

        /*
        for(int i=0;i<19;i++){
            voting_vector[i]=model_list[i].computeLocation(model_list[i].dataset, signal[i]);
        }

         */
        int array1[]=new int[actual_max_prob_vector.size()];
        int array2[]=new int[actual_voting_vector.size()];
        for(int i=0;i<actual_voting_vector.size();i++)
        {
            array1[i]=0;
            array2[i]=0;
        }
        for(int i=0;i<actual_voting_vector.size();i++)
        {
            Integer item=(int)actual_voting_vector.get(i);
            for(int j=0;j<actual_voting_vector.size();j++)
            {
                Integer item2=(int)actual_voting_vector.get(j);
                if(item==item2)
                {
                    array2[i]++;
                }
            }
        }



        int max=0;
        double max_prob=0;
        int temp=0;
        int temp_prob=0;
        int min=0;

        for(int i=0;i<actual_max_prob_vector.size();i++){
            double probability=(Double) actual_max_prob_vector.get(i);
            double minus=abs(probability-max_prob);
            if(minus>0.01 & probability>max_prob){
                max_prob=probability;
                temp_prob=i;
            }
            if(minus<0.01 & probability<max_prob){
                max_prob=probability;
                temp_prob=i;
            }
        }




        for(int i=0;i<actual_voting_vector.size();i++)
        {
            //如果存在多个值同时最大的情况，记录第一个出现的
            if(array2[i]>max)
            {
                max=array2[i];//记录下出现次数最多的数值
                temp=i;//记录下出现次数最多的数的下标
            }
        }
        //找出voting_vector数组中找出出现次数最多的元素，将其赋值给min
        if(actual_voting_vector.size()!=0){
            if(temp==temp_prob){
                min = (int)actual_voting_vector.get(temp);

            }else if(temp!=temp_prob) {
                Boolean ismax=false;
                for (int i=0;i<actual_max_prob_vector.size();i++){

                    double probability=(double)actual_max_prob_vector.get(i);
                    if(probability==max_prob){
                        continue;
                    }
                    double compute_threshold=abs((max_prob-probability))/max_prob;
                    if(compute_threshold>0.65){
                        ismax=true;

                        break;
                    }else{
                        ismax=false;
                    }
                }
                if(ismax){
                    min = (int) actual_voting_vector.get(temp_prob);
                }else{
                    min =(int) actual_voting_vector.get(temp);
                }

            }



        }


        //System.out.println("Cell" + String.valueOf(min));
        for(int i=0;i<parallel.size();i++){
            Parallel item=(Parallel)parallel.get(i);
            if(item.termination_mark==1){
                finish=true;
            }
            /*
            else{
                finish=false;
                break;
            }

             */
        }
        /*
        for(int i=0;i<model_list.length;i++){
            if(model_list[i].termination_mark==1) {
                finish = true;
            }else{
                finish=false;
                break;
            }
        }

         */
        if(finish){
            Toast.makeText(Total_service.this,"Convergence has been achieved", Toast.LENGTH_LONG).show();
        }
        return String.valueOf(min);


    }



}