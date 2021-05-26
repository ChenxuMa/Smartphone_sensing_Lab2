package com.sps.lab2;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//import jdk.tools.jlink.resources.plugins;

public class Serial 
{
    //每一个cell对应一个大小为256的概率列表
    public static final Integer cell_size = 100;
    public static final Integer numCells = 9;
    public List<Data> dataset = null;
    public double[] priorProb = new double[numCells];
    public double max_prob_localization;
    public int max_prob_localization_index;
    public String mac_address;
    //pressCount用于统计按钮次数
    /*
    public Serial(String fileName) throws IOException 
    {
		dataset = initDataSet(fileName);
        
       
	}

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
        for(int i=0;i<numCells;i++)
        {
            this.priorProb[i] = 1.0/numCells;
        }
        mac_address=s;
        
    }

    private List<Data> initDataSet(String fileName, Context applicationContext) throws IOException
    {
		List<Data> list = new ArrayList<Data>();
        AssetManager assetsManager = applicationContext.getAssets();

        InputStream inputStream = assetsManager.open(fileName);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		while ((line = bufferedReader.readLine()) != null) 
        {
			Data data = new Data();
			String[] s = line.split(" ");
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

//返回值变为double型数组,记录这一次更新的后验概率，留给下一次更新当先验概率使用
public double []computeLocation(List<Data> list, int strength) 
{

    double[] currProb = new double[numCells];
    double normalization_value = 0;
    //所有信号幅值等于strength的概率值做全概率加和
    for (int i = 0; i < numCells; i++)
    {   
        
        normalization_value = normalization_value + this.priorProb[i]*list.get(i).getValue(strength);
        
    }
    for (int i = 0; i < numCells; i++) 
    { 
        
        currProb[i] = priorProb[i]*list.get(i).getValue(strength)/normalization_value;
        
    }
    //寻找数组中最大值并记录索引
    
    double max_prob=currProb[0];
    int max_prob_index = 0;

    for(int i=1;i<currProb.length;i++)
    {
    if(max_prob<currProb[i])
    {
        max_prob=currProb[i]; //获得最大值
    }
    }

    for(int j=0;j<currProb.length;j++)
    {
    if(currProb[j] == max_prob)
    { //遍历对比
        max_prob_index = j + 1; //输出最大值下标
    }
    }
    this.max_prob_localization_index = max_prob_index;
    this.max_prob_localization = max_prob;
    //完成一次预测后,返回后验概率，注意：不再用同一个对象的后验概率更新同一个对象的先验概率    
    return currProb; 

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
}



/*
// 在主函数中完成输出
public static void main(String[] args) throws Exception 
{
    int strength1 = 90;
    int strength2 = 89;
    int strength3 = 89;
    //用方差最大的APs对应的表做第一个输入，次强的第二个输入。
    Serial Serial1 = new Serial("first_feature_pmf.txt");// 相当于建立模型的过程
    Serial Serial2 = new Serial("second_feature_pmf.txt");
    Serial Serial3 = new Serial("third_feature_pmf.txt");
    List<Data> testDataSet1 = Serial1.dataset;
    List<Data> testDataSet2 = Serial2.dataset;
    List<Data> testDataSet3 = Serial3.dataset;
    //按一下locate me按钮，第一个表开始贝叶斯滤波过程，讲返回的概率分布向量作为下一个表的先验概率
    Serial1.initial_prior();
    Serial1.computeLocation(testDataSet1, strength1);
    
    if(Serial1.max_prob_localization > 0.9)
    {
        System.out.println("The Convergence has been reached"+"Cell" + String.valueOf(Serial1.max_prob_localization_index));
    }
    else
    {
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

    
    
    
    



}

 */




