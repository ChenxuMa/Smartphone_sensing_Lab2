package com.sps.lab2;
import java.util.ArrayList;
import java.util.IllegalFormatFlagsException;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

//import jdk.tools.jlink.resources.plugins;
public class Parallel_new {//每一个cell对应一个大小为256的概率列表
    public static final Integer cell_size = 256;
    public static final Integer numCells = 11;
    public List<Data> dataset = null;
    public Double[] priorProb = new Double[numCells];
    public int pressCount;
    public int termination_mark;

    //pressCount用于统计按钮次数
    public Parallel_new(String fileName) throws IOException {
        dataset = initDataSet(fileName);


    }

    //每到一个新格子，按一下初始化按钮，初始化先验概率
    public void initial_prior() {
        for (int i = 0; i < numCells; i++) {
            this.priorProb[i] = 1.0 / numCells;
        }
        pressCount = 0;
        termination_mark = 0;
    }

    private List<Data> initDataSet(String fileName) throws IOException {
        List<Data> list = new ArrayList<Data>();

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(Parallel.class.getClassLoader().getResourceAsStream(fileName)));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            Data data = new Data();
            String[] s = line.split(" ");
            // Mile Time ICE对应x,y,z
            for (int i = 0; i < cell_size; i++) {
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
    public int computeLocation(List<Data> list, int strength) {

        double[] currProb = new double[numCells];
        double normalization_value = 0;
        //所有信号幅值等于strength的概率值做全概率加和
        for (int i = 0; i < numCells; i++) {

            normalization_value = normalization_value + this.priorProb[i] * list.get(i).getValue(strength);

        }
        for (int i = 0; i < numCells; i++) {

            currProb[i] = priorProb[i] * list.get(i).getValue(strength) / normalization_value;

        }
        //寻找数组中最大值并记录索引

        double max_prob = currProb[0];
        int max_prob_index = 0;

        for (int i = 1; i < currProb.length; i++) {
            if (max_prob < currProb[i]) {
                max_prob = currProb[i]; //获得最大值
            }
        }

        for (int j = 0; j < currProb.length; j++) {
            if (currProb[j] == max_prob) { //遍历对比
                max_prob_index = j + 1; //输出最大值下标
            }
        }

        //完成一次预测后,用后验概率更新先验概率
        for (int j = 0; j < currProb.length; j++) {
            this.priorProb[j] = currProb[j];
        }
        pressCount++;
        if (pressCount == 5 || max_prob > 0.90) {
            termination_mark = 1;
        }

        return max_prob_index;

    }

    //由列表嵌套的的方式展示矩阵，此处定义Data来储存一个cell的概率向量
    class Data implements Comparable<Data> {

        private List<Double> cell_list = new ArrayList<Double>(cell_size);

        public double getValue(Integer i) {
            return cell_list.get(i);
        }

        public void setValue(Double x) {
            this.cell_list.add(x);
        }

        //todo
        public int compareTo(Data o) {

            return 0;
        }
    }
//存储所用的数据类


    // 在主函数中完成输出
    public static void main(String[] args) throws Exception {
        int strength1 = 90;
        int strength2 = 89;
        int strength3 = 89;
        Parallel_new parallel1 = new Parallel_new("first_feature_pmf.txt");// 相当于建立模型的过程
        Parallel_new parallel2 = new Parallel_new("second_feature_pmf.txt");
        Parallel_new parallel3 = new Parallel_new("third_feature_pmf.txt");
        List<Data> testDataSet1 = parallel1.dataset;
        List<Data> testDataSet2 = parallel2.dataset;
        List<Data> testDataSet3 = parallel3.dataset;
        parallel1.initial_prior();
        parallel2.initial_prior();
        parallel3.initial_prior();//相当于按一次初始化按钮


        int[] voting_vector = {parallel1.computeLocation(testDataSet1, strength1), parallel2.computeLocation(testDataSet2, strength2), parallel3.computeLocation(testDataSet3, strength3)};
        System.out.println(voting_vector[0]);
        System.out.println(voting_vector[1]);
        System.out.println(voting_vector[2]);

        // //实现一个majority voting
        int array2[] = new int[voting_vector.length];
        // //记录voting_vector中每一个元素出现的次数
        for (int i = 0; i < voting_vector.length; i++) {

            array2[i] = 0;
        }
        // //没出现一次，计数加一
        for (int i = 0; i < voting_vector.length; i++) {
            for (int j = 0; j < voting_vector.length; j++) {
                if (voting_vector[i] == voting_vector[j]) {
                    array2[i]++;
                }
            }
        }

        int max = 0;
        int temp = 0;
        for (int i = 0; i < voting_vector.length; i++) {
            //如果存在多个值同时最大的情况，记录第一个出现的
            if (array2[i] > max) {
                max = array2[i];//记录下出现次数最多的数值
                temp = i;//记录下出现次数最多的数的下标
            }
        }
        //找出voting_vector数组中找出出现次数最多的元素，将其赋值给min
        int min = voting_vector[temp];

        System.out.println("Cell" + String.valueOf(min));
        if (parallel1.termination_mark == 1 && parallel2.termination_mark == 1 && parallel3.termination_mark == 1) {
            System.out.println("Convergence has been achieved");
        }


    }
}




