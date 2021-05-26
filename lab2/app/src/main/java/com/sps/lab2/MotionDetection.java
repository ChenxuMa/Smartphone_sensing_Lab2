package com.sps.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MotionDetection extends AppCompatActivity implements SensorEventListener{
    private TextView XValue;
    private TextView YValue;
    private TextView ZValue;
    private RadioGroup motion_status_group;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float aX;
    private float aY;
    private float aZ;
    private String label;
    private Button start;
    private Button close_window;
    private Button stop;
    private Button save_to_list;
    private SensorEventListener sensorlistener;
    private FileOutputStream fos=null;
    private List motion_data_result=new ArrayList<>();//储存的数据
    private List first_data=new ArrayList();
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motion_detection);
        try {
            fos=openFileOutput("Motion_data", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        XValue = (TextView) findViewById(R.id.XValue);
        YValue = (TextView) findViewById(R.id.YValue);
        ZValue = (TextView) findViewById(R.id.ZValue);
        start = (Button) findViewById(R.id.start);
        save_to_list=(Button) findViewById(R.id.save_to_List);
        //stop=(Button) findViewById(R.id.stop);
        close_window=(Button) findViewById(R.id.close_motion);
        motion_status_group = (RadioGroup) findViewById(R.id.motion_status);

        motion_status_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton r=(RadioButton)findViewById(i);
                label=r.getText().toString();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(label);
                if(label==null){
                    Toast.makeText(MotionDetection.this
                            ,"Please choose your motion status before detecing the motion"
                            ,Toast.LENGTH_SHORT).show();

                }
                else{
                    //System.out.println("triggering");

                    //mHandler.sendEmptyMessage(1);
                    mHandler.postDelayed(task,10);
                }

            }
        });
        save_to_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(motion_data_result);
                first_data= (List) motion_data_result.get(0);
                System.out.println(motion_data_result.get(0));

                System.out.println(first_data.get(0));//get first X data
                //context=getApplicationContext();
                for(int i=0;i<motion_data_result.size();i++){
                    List one_time_data=(List)motion_data_result.get(i);
                    //for(int j=0;j<one_time_data.size();j++){
                    String txt0=one_time_data.get(0).toString();
                    String txt1=one_time_data.get(1).toString();
                    String txt2=one_time_data.get(2).toString();
                    String label=one_time_data.get(3).toString();
                    writemotiontxt(txt0,txt1,txt2,label);
                }
                //Toast.makeText(MotionDetection.this, "Saved successfull", Toast.LENGTH_LONG).show();
                //writemotiontxt();

            }
        });
        /*
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //sensorManager.unregisterListener(MotionDetection.this);
                XValue.setText("0.0");
                YValue.setText("0.0");
                ZValue.setText("0.0");
                //mHandler.removeCallbacks(task);
                //label=null;

                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

         */
        close_window.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();


                /*
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                 */
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {
        super.onPause();
        //sensorManager.unregisterListener(this);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        XValue.setText("0.0");
        YValue.setText("0.0");
        ZValue.setText("0.0");

        // get the the x,y,z values of the accelerometer
        aX = sensorEvent.values[0];
        aY = sensorEvent.values[1];
        aZ = sensorEvent.values[2];

        // display the current x,y,z accelerometer values
        XValue.setText(Float.toString(aX));
        YValue.setText(Float.toString(aY));
        ZValue.setText(Float.toString(aZ));
        //writemotiontxt(XValue,YValue,ZValue,label);
        List line=new ArrayList();
        line.add(XValue.getText().toString());
        line.add(YValue.getText().toString());
        line.add(ZValue.getText().toString());
        line.add(label);
        motion_data_result.add(line);
        predict_data(XValue, YValue, ZValue);




    }

    private void predict_data(TextView xValue, TextView yValue, TextView zValue) {

    }

    public void writemotiontxt(String XValue, String YValue, String ZValue, String label) {
        //motion_data_result.add();
        context=getApplicationContext();
        String input_string = XValue + " " + YValue + " " + ZValue + " " + label+"\r\n";
        //System.out.println(input_string[1]);
        try {
            if(motion_data_result!=null){
                FileOutputStream fos = context.openFileOutput("motion_data_result", context.MODE_APPEND);
                fos.write(input_string.getBytes());

                fos.flush();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        } finally {
            try {
                fos.close();
                //Toast.makeText(MotionDetection.this, "Saved successfull", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }



        /*
        List line=new ArrayList();
        line.add(XValue.getText().toString());
        line.add(YValue.getText().toString());
        line.add(ZValue.getText().toString());
        line.add(label);
        motion_data_result.add(line);

         */


        /*
        if(motion_data_result!=null){
            for(int i=0;i<motion_data_result.size();i++){
                if(motion_data_result.get(i)==){

                }
            }
        }else{
            motion_data_result.add(line);
        }

         */
            //System.out.println(motion_data_result);

        /*
        try{

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

         */
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    stop();
                    break;
                case 1:
                    //System.out.println("-------------Trigger---------------------");
                    trigger();

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
            System.out.println("running");

            mHandler.sendEmptyMessage(1);
            mHandler.sendEmptyMessageDelayed(0,2*1000);
            //mHandler.postDelayed(null,2*1000);
            //mHandler.sendEmptyMessage(0);
            //mHandler.postDelayed(task1, 2 * 1000);
        }
    };
    /*
    private Runnable task1=new Runnable(){
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);


        }
    };

     */
    public void stop(){
        mHandler.removeCallbacks(task);
        sensorManager.unregisterListener(MotionDetection.this);
        XValue.setText("0.0");
        YValue.setText("0.0");
        ZValue.setText("0.0");

    }
    public void trigger(){
        //mHandler.postDelayed(task,1000);
        //System.out.println("------------sensorManager---------------------");
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // if the default accelerometer exists
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // set accelerometer
            accelerometer = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // register 'this' as a listener that updates values. Each time a sensor value changes,
            // the method 'onSensorChanged()' is called.

        } else {
            // No accelerometer!
        }
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);


    }
}
