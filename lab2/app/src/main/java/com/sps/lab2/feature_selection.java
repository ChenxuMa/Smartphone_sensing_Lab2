package com.sps.lab2;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class feature_selection {
    public List wifi_list=new ArrayList<>();
    public List Mac_list=new ArrayList<>();
    private List Label_list=new ArrayList<>();

    public List Total_average_list=new ArrayList<>();
    private Map<String, Double> Selected_feature=new HashMap<String, Double>();
    public List result=new ArrayList<>();
    String line=null;
    //private double[][] data_matrix=new double[11][367];
    @RequiresApi(api = Build.VERSION_CODES.N)
    public feature_selection(BufferedReader bufferedReader) throws IOException {
        while((line=bufferedReader.readLine())!=null) {
            String s[]=line.split(" ");
            wifi_list.add(s);


        }
        compute_feature(wifi_list);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void compute_feature(List wifi_list){

        //String[] one_data= (String[]) wifi_list.get(0);
        //System.out.println(wifi_list);
        //System.out.println(one_data[1]);

        for(int i=0;i<wifi_list.size();i++){
            String[] one_data=(String[])wifi_list.get(i);
            if(Mac_list==null){

                Mac_list.add(one_data[0]);

            }
            else{
                //String[] one_data=(String[]) wifi_list.get(i);
                if(!Mac_list.contains(one_data[0])){
                    Mac_list.add(one_data[0]);
                }

            }
            if(Label_list==null){
                Label_list.add(one_data[2]);
            }
            else{
                if(!Label_list.contains(one_data[2])){
                    Label_list.add(one_data[2]);
                }
            }
        }
        for(int i=0;i<Mac_list.size();i++){
            String Mac_address=(String)Mac_list.get(i);
            List Average_result=new ArrayList<>();
            for(int j=0;j<Label_list.size();j++){
                String label=(String)Label_list.get(j);
                Average_result.add(compute_feature_average(wifi_list,label,Mac_address));
            }
            Total_average_list.add(Average_result);
        }

        select_feature(Total_average_list, Label_list, Mac_list);

    }



    private double compute_feature_average(List wifi_list, String label, String Mac){
        List wifi_aptitude=new ArrayList<>();
        double average=0;
        for(int i=0;i<wifi_list.size();i++){
            String[] s=(String[])wifi_list.get(i);
            if(s[0].equals(Mac) & s[2].equals(label)){
                String aptitude_string=s[1];
                wifi_aptitude.add(Double.parseDouble(s[1].substring(0,3)));
            }
        }
        if(wifi_aptitude.size()==0){
            average=0;
        }
        else{
            for(int i=0;i<wifi_aptitude.size();i++){
                average+=(double)wifi_aptitude.get(i);
            }
            average=average/wifi_aptitude.size();
        }
        //System.out.println(wifi_aptitude.size());

        return average;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void select_feature (List total_average_list, List label_list, List Mac_list){
        double variation=0;

        for(int i=0;i<total_average_list.size();i++){
            List data_per_room=(List)total_average_list.get(i);
            variation=compute_variation(data_per_room);
            Selected_feature.put((String)Mac_list.get(i), variation);

        }
        //System.out.println(Selected_feature);
        LinkedHashMap<String, Double> sorted_map=new LinkedHashMap<>();
        Selected_feature.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x->sorted_map.put(x.getKey(),x.getValue()));
        /*
        List<Map.Entry<String, Double>> sort_list=new ArrayList<Map.Entry<String, Double>>(Selected_feature.entrySet());

        Collections.sort(sort_list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> stringDoubleEntry, Map.Entry<String, Double> t1) {
                return (int) (stringDoubleEntry.getValue()-t1.getValue());
            }
        });
        System.out.println(sort_list);

         */
        //System.out.println(sorted_map);
        Integer calculator=0;
        for(Map.Entry<String,Double> entry:sorted_map.entrySet()){
            result.add(entry.getKey());
            calculator++;
            if(calculator==20){
                break;
            }
        }
        //System.out.println(result);


    }
    private double compute_variation(List data_per_room){
        double sum=0;
        double Variation=0;
        for(int i=0;i<data_per_room.size();i++){
            sum+=(double)data_per_room.get(i);
        }
        double average=sum/data_per_room.size();
        for(int i=0;i<data_per_room.size();i++){
            //Variation=(double)data_per_room.get(i)-average;
            Variation+=((double)data_per_room.get(i)-average)*((double)data_per_room.get(i)-average);
        }
        return Variation/data_per_room.size();
    }
}
