package com.sps.lab2;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Parallel
{
    //每一个cell对应一个大小为256的概率列表
    public static final Integer cell_size = 100;
    public static final Integer numCells = 9;
    public List<Data> dataset = null;
    public Double[] priorProb=new Double[9];
    public int pressCount;
    public int termination_mark;
    public String mac_address;
    public double max_prob;
    /*
    public Parallel(//String fileName) throws IOException
    //{
        //dataset = initDataSet(fileName);


    //}

     */
    public void readDataset(String fileName, Context applicationContext)throws IOException{
                  //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         System.out.println(fileName);
        dataset = initDataSet(fileName, applicationContext);
//        for(int i=0;i<dataset.size();i++){
//            System.out.println(dataset.get(i).getValue(i));
//        }

    }
    //每到一个新格子，按一下初始化按钮，初始化先验概率
    public void initial_prior(String s)
    {
        mac_address=s;
        for(int i=0;i<numCells;i++)
        {
            this.priorProb[i] = 1.0/numCells;
        }
        pressCount = 0;
        termination_mark = 0;
    }

    private List<Data> initDataSet(String fileName, Context applicationContext) throws IOException
    {
        List<Data> list = new ArrayList<Data>();
        AssetManager assetsManager = applicationContext.getAssets();

        InputStream inputStream = assetsManager.open(fileName);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        /*
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Parallel.class.getClassLoader().getResourceAsStream(fileName)));

         */
        String line = null;
        while ((line = bufferedReader.readLine()) != null)
        {
            Data data = new Data();
            String[] s = line.split(" ");
//            for (int i=0;i<s.length;i++){
//                System.out.println(s[i]);
//            }

            // Mile Time ICE对应x,y,z
            for(int i = 0; i<cell_size; i++)
            {
                data.setValue(Double.parseDouble(s[i]));
            }
            //每次循环结束后，将一个长度为256的列表作为新列表的一个元素
            list.add(data);
        }
        //得到了列表嵌套形式的矩阵
        return list;
    }
    //根据Prior和Sensor Model计算locate数值,贝叶斯滤波器核心
//有两个功能，第一次返回这一次按键的预测值，第二更新成员变量priorProb数组
//strength要求是正数，按0-255的顺序排序
    public int computeLocation(List<Data> list, int strength)
    {

        double[] currProb = new double[numCells];
        double normalization_value = 0;
        DecimalFormat df=new DecimalFormat("0.0000000");
        //所有信号幅值等于strength的概率值做全概率加和
        for (int i = 0; i < numCells; i++)
        {

            normalization_value = normalization_value + priorProb[i]*list.get(i).getValue(strength);


        }
        //double pmf=(Double)list.get(i).getValue(strength);


        for (int i = 0; i < numCells; i++)
        {
            if(normalization_value!=0){

                    currProb[i] = priorProb[i]*list.get(i).getValue(strength)/normalization_value;
                    double computevalue=priorProb[i]*list.get(i).getValue(strength);
                    String normalization_string=df.format(normalization_value);
                    String pmf_string=df.format(computevalue);
                    if(normalization_string.equals(pmf_string)){
                        currProb[i]=0;
                    }



            }
            else if(normalization_value==0){
                currProb[i] = 0;
            }
            //else if(normalization_value)


        }
//        for (int i=0;i<numCells;i++){
//            //System.out.println("&&&&&&&&&&&&&&&&&&");
//            //System.out.println(currProb[i]);
//            //System.out.println(priorProb[i]);
//        }
        //寻找数组中最大值并记录索引

        max_prob=currProb[0];
        int max_prob_index = 0;

        for(int i=1;i<currProb.length;i++)
        {
            if(max_prob<currProb[i])
            {
                max_prob=currProb[i]; //获得最大值
            }
            for(int j=0;j<currProb.length;j++)
            {
                if(currProb[j] == max_prob)
                { //遍历对比
                    max_prob_index = j + 1; //输出最大值下标
                }
            }
        }
        //完成一次预测后,用后验概率更新先验概率
        for(int j=0;j<currProb.length;j++)
        {
            this.priorProb[j] = currProb[j];
        }
        pressCount++;
        if (pressCount == 5 || max_prob > 0.90) {
            termination_mark = 1;
        }

        return max_prob_index;

    }
    //由列表嵌套的的方式展示矩阵，此处定义Data来储存一个cell的概率向量
    class Data implements Comparable<Data>
    {

        private List<Double> cell_list = new ArrayList<Double>(cell_size);

        public double getValue(Integer i) {
            return cell_list.get(i);
        }
        public void setValue(Double x) {
            this.cell_list.add(x);
        }
        //todo
        public int compareTo(Data o)
        {

            return 0;
        }
    }
//存储所用的数据类


/*
    // 在主函数中完成输出
    public static void main(String[] args) throws Exception
    {
        int strength1 = 88;
        int strength2 = 89;
        int strength3 = 92;
        Parallel parallel1 = new Parallel("first_feature_pmf.txt");// 相当于建立模型的过程
        Parallel parallel2 = new Parallel("second_feature_pmf.txt");
        Parallel parallel3 = new Parallel("third_feature_pmf.txt");
        List<Data> testDataSet1 = parallel1.dataset;
        List<Data> testDataSet2 = parallel2.dataset;
        List<Data> testDataSet3 = parallel3.dataset;
        int []voting_vector = {parallel1.computeLocation(testDataSet1, strength1)
        ,parallel2.computeLocation(testDataSet2, strength2)
                ,parallel3.computeLocation(testDataSet3, strength3)};
        //实现一个majority voting
        int array2[]=new int[voting_vector.length];//记录voting_vector中每一个元素出现的次数
        for(int i=0;i<voting_vector.length;i++)
        {

            array2[i]=0;
        }
        for(int i=0;i<voting_vector.length;i++)
        {
            for(int j=0;j<voting_vector.length;j++)
            {
                if(voting_vector[i]==voting_vector[j])
                {
                    array2[i]++;
                }
            }
        }

        int max=0;
        int temp=0;
        for(int i=0;i<voting_vector.length;i++)
        {
            //如果存在多个值同时最大的情况，记录第一个出现的
            if(array2[i]>max)
            {
                max=array2[i];//记录下出现次数最多的数值
                temp=i;//记录下出现次数最多的数的下标
            }
        }
        //找出voting_vector数组中找出出现次数最多的元素，将其赋值给min
        int min = voting_vector[temp];
        System.out.println("Cell" + String.valueOf(min));
    }




 */
}
