#from xgboost import XGBClassifier
from collections import defaultdict
import pandas as pd
import csv
import os
import numpy as np
from manuf import manuf

signal_strength=[]
row=["wifi"]
room_list=['cell1','cell2','cell3','cell4','cell5','cell6','cell7','cell8','cell9','cell10']

for i in range(60,91):
    row.append(str(i))
row_string=" ".join(row)
print(row_string)




#5:6c:ce:a4, ac:22:05:79:e8:5a, 20:e8:82:f0:55:9b, 88:f8:72:8e:4a:a1, e4:c3:2a:e7:81:78, 28:d1:27:8b:17:6d, ac:22:05:5e:61:bb, 38:43:7d:36:6a:0d
#feature_string=
mac_string="c8:54:4b:93:d1:01, ac:22:05:8b:d2:59, ae:22:15:8b:d2:59, ca:54:4b:b3:d1:02, c8:54:4b:93:d1:02, ac:22:05:8b:d2:13, ca:54:4b:b2:43:62, c8:54:4b:92:43:62, c8:54:4b:92:43:61, 44:fe:3b:74:48:87, 50:7e:5d:7c:b3:32, 98:0d:67:3e:39:61, 9c:6f:52:15:d2:79, ac:22:05:16:f1:d5, ae:22:15:16:f1:d5, 5c:a8:6a:0a:d1:2c, 00:5f:67:78:1e:1e, f0:9f:c2:fd:23:9d, ac:22:05:16:f1:c7, 9c:6f:52:15:d2:7a, b4:75:0e:5d:b0:d5, 8a:ac:c0:9e:6a:a1, 88:ac:c0:be:6a:a1, c8:00:84:84:d5:3a, c8:00:84:84:d5:3e, c8:00:84:84:d5:3d, 8a:ac:c0:be:6a:a1, 8a:ac:c0:ae:6a:a1"
mac_list=mac_string.split(", ")
print(mac_string)
actual_mac_list=[]
'''
p=manuf.MacParser(update=True)
for item in mac_list:
    #print(type(p.get_manuf(item)))
    result=""
    type_variable=type(p.get_manuf(item))
    result=p.get_manuf(item)
    if(result is None):
        print("This is an None")
    elif result:
        actual_mac_list.append(item)
print(actual_mac_list)
print(len(actual_mac_list))
actual_string=", ".join(actual_mac_list)
print(actual_string)

'''

'''
mac_list=['54:fa:3e:6a:32:bc',
          '74:42:7f:07:99:ba',
          '56:67:11:0c:2e:9d',
          '54:67:51:0c:2e:9d',
          '58:ef:68:c7:1a:36',
          'ac:22:05:e1:be:c1',
          '7c:8b:ca:b6:df:74',
          '90:5c:44:cf:fa:dd',
          '8c:e0:81:67:3a:e1',
          'ac:22:05:22:2a:94',
          '34:2c:c4:da:3f:df',
          'cc:32:e5:7c:58:f2',
          '5a:d3:43:ee:c0:d1',
          '74:31:70:c9:69:2f',
          '8c:a6:df:9a:60:df',
          '18:35:d1:bf:a1:df',
          'be:f8:cc:46:f5:7b',
          '92:5c:14:cf:fa:dd',
          '38:43:7d:6a:93:ec',
          'ae:22:05:79:e7:5d',
          'd8:47:32:66:7e:0f',
          'e4:c3:2a:e7:81:77',
          '54:67:51:0c:2e:89',
          '4c:1b:86:91:9b:de',
          '2a:35:d1:a9:6a:79',
          '68:ff:7b:69:ee:ba',
          '2c:b2:1a:96:4f:0a',
          '2e:ee:52:82:be:d3',
          '88:f8:72:8e:4a:a5',
          'b0:95:75:6c:ce:a4',
          'ea:47:32:51:f0:f1',
          'd8:47:32:51:f0:f1',
          '88:f8:72:8e:4a:a0']

'''






'''
mac_list=['7c:39:53:a6:ff:51',
          '1c:3a:de:cb:29:3e',
          '9c:6f:52:35:5e:66',
          'f0:2f:74:40:da:08',
          '20:e8:82:f0:55:9c',
          '08:26:97:9b:8d:62',
          '54:fa:3e:9f:1f:2c',
          '96:9a:4a:f3:f0:7f',
          '18:35:d1:bf:a1:df',
          'bc:a5:11:ad:06:63',
          '5a:d3:43:93:13:a1',
          '56:83:3a:3b:21:c2',
          '2e:ee:52:82:be:d3',
          '1c:74:0d:38:3e:03',
          '70:2e:22:8a:e3:4e',
          '92:5c:14:d2:25:b9',
          '54:83:3a:3b:21:c2',
          '54:fa:3e:46:d9:47',
          '44:a5:6e:a4:30:0a',
          '90:5c:44:d0:40:82',
          '54:67:51:0c:2e:89',
          '80:7d:14:30:d1:6c',
          '20:e8:82:f0:55:9b',
          '90:9a:4a:f3:ef:ea',
          '1c:3b:f3:89:25:6b',
          'a0:ec:80:4a:c0:db',
          '68:02:b8:05:b2:8c',
          '54:fa:3e:6a:32:bc',
          '18:83:bf:6b:dd:08',
          '22:3b:f3:89:25:6b']
          '''




def split_feature(feature_number:int):
    for item in alist:
        string_list = item.split();
        #print(string_list)
        for i in range(0, feature_number):
            if (string_list[0] == mac_list[i]):
                file_str = str(i) + "_mac.txt"
                with open(file_str, 'a') as f:
                    f.write(item)

def pmf_original(feature_number:int, rssi_number:int):
    for i in range(0, feature_number):
        pmf_list = defaultdict(dict)
        data_dict1 = defaultdict(list)
        first_datalist = []
        data_list = []
        read_file_name = str(i) + "_mac.txt"
        pmf_file_name = str(i) + "_feature_pmf.txt"
        with open(read_file_name) as f:
            blist = f.readlines()
        # print(blist)

        for items in blist:
            string_list = items.split()

            data_dict1[string_list[2]].append(abs(int(string_list[1][:3])))
        #print(data_dict1)
        # print(data_dict)
        for k, v in data_dict1.items():
            aptitude_dict = defaultdict(int)
            # print(k)
            # print(v)
            for i in range(0, rssi_number):
                if (i in v):
                    for value in v:
                        if (i == value):
                            aptitude_dict[i] += 1
                else:
                    aptitude_dict[i] = 0
            pmf_list[k] = dict(aptitude_dict)
        # print(pmf_list)

        for k, v in pmf_list.items():
            #print(k)
            #print(dict(v))
            data_list.append(k)
        for rooms in room_list:
            s = ""
            if (rooms not in data_list):

                for i in range(0, rssi_number):
                    s = s + "0.0" + " "
                s += "\n"
                with open(pmf_file_name, 'a') as f:
                    f.write(s)
            else:
                sum = 0
                for key, value in dict(pmf_list[rooms]).items():
                    sum += value

                for key, value in dict(pmf_list[rooms]).items():
                    s = s + str(value / sum) + " "
                s += "\n"
                with open(pmf_file_name, 'a') as f:
                    f.write(s)
def pmf_new(dir:str,feature_number:int, rssi_number:int):
    for i in range(0, feature_number):
        file_name = str(i) + "_mac.txt"
        with open(file_name) as f:
            list1 = f.readlines()
            for item in list1:
                signal_level = item.split(' ')[1][1:3]
                num_item = int(float(signal_level))
                if (signal_strength == []):
                    signal_strength.append(num_item)
                elif (num_item not in signal_strength):
                    signal_strength.append(num_item)
    signal_strength.sort()

    for i in range(0, feature_number):
        file_name = str(i) + "_feature_pmf.txt"
        with open(file_name)as f:
            list1 = f.readlines()
            print(list1)
            cell_number=1
            for item in list1:
                # print(item)
                # print(list1.index(item))

                pmf_per_room = item.split(" ")
                #print(pmf_per_room)
                calculator = 0
                slide_window = []
                for item1 in pmf_per_room:

                    begin = signal_strength[0]
                    end = signal_strength[-1]
                    if (calculator in range(begin - 3, end + 4)):
                        slide_window.append(float(item1))
                    calculator += 1
                # print(slide_window)
                sum = 0

                not_zero_caculator = 0
                for item in slide_window:
                    if (item != 0.0):
                        not_zero_caculator += 1
                        sum += item * 0.1
                        # item=item*0.9
                        slide_window[slide_window.index(item)] = str(item * 0.9)
                average = sum / (len(slide_window) - not_zero_caculator)
                for item in slide_window:
                    if (item == 0.0):
                        # item=average
                        slide_window[slide_window.index(item)] = str(average)
                # print(slide_window)
                calculator = 0
                slide_window_pointer = 0
                for item in pmf_per_room:
                    if (calculator in range(begin - 3, end + 4)):
                        pmf_per_room[calculator] = slide_window[slide_window_pointer]
                        slide_window_pointer += 1
                    calculator += 1
                # print(pmf_per_room)
                new_item = ",".join(pmf_per_room[60:91])
                # print(new_item)
                #print(new_item)

                new_file_name = dir+"/"+ str(i) + "_feature_pmf_new.txt"
                title="cell"+str(cell_number)
                with open(new_file_name, 'a')as f:
                    f.write(title+" "+new_item+"\n")
                cell_number+=1
            # print(list1)
def mkdir(dir:str):
    folder=os.path.exists(dir)
    if not folder:
        os.makedirs(dir)
    else:
        print("dir has been created")
if __name__=='__main__':
    dir='E:/Pycharm/histogram/pmf_new'
    mkdir(dir)
    with open('select_wifi27.txt') as f:
        alist=f.readlines()
        feature_number=len(mac_list)

        for i in range(0,feature_number+1):
            new_file_name = dir + "/" + str(i) + "_feature_pmf_new.txt"
            with open(new_file_name,'a') as f:
                f.write(row_string+"\n")




        print(feature_number)
        rssi_number=101
        split_feature(feature_number)
        pmf_original(feature_number,rssi_number)
        pmf_new(dir,feature_number,rssi_number)





'''
for item in alist:
    string_list=item.split();
    print(string_list)
    for i in range(0,20):
        if(string_list[0]==mac_list[i]):
            file_str=str(i)+"_mac.txt"
            with open(file_str,'a') as f:
                f.write(item)
'''


'''
for i in range(0,20):
    pmf_list = defaultdict(dict)
    data_dict1 = defaultdict(list)
    first_datalist = []
    data_list=[]
    read_file_name=str(i)+"_mac.txt"
    pmf_file_name=str(i)+"_feature_pmf.txt"
    with open(read_file_name) as f:
        blist=f.readlines()
    #print(blist)






    for items in blist:

        string_list=items.split()

        data_dict1[string_list[2]].append(abs(int(string_list[1][:3])))
    print(data_dict1)
    #print(data_dict)
    for k,v in data_dict1.items():
        aptitude_dict=defaultdict(int)
        #print(k)
        #print(v)
        for i in range(0,100):
            if(i in v):
                for value in v:
                    if(i==value):
                        aptitude_dict[i]+=1
            else:
                aptitude_dict[i]=0
        pmf_list[k]=dict(aptitude_dict)
    #print(pmf_list)

    for k, v in pmf_list.items():
        print(k)
        print(dict(v))
        data_list.append(k)
    for rooms in room_list:
        s = ""
        if (rooms not in data_list):

            for i in range(1, 101):
                s = s + "0.0" + " "
            s += "\n"
            with open(pmf_file_name, 'a') as f:
                f.write(s)
        else:
            sum = 0
            for key, value in dict(pmf_list[rooms]).items():
                sum += value
                
            for key, value in dict(pmf_list[rooms]).items():
                s = s + str(value / sum) + " "
            s += "\n"
            with open(pmf_file_name, 'a') as f:
                f.write(s)
                
for i in range(0,20):
    file_name=str(i)+"_mac.txt"
    with open(file_name) as f:
        list1=f.readlines()
        for item in list1:
            signal_level=item.split(' ')[1][1:3]
            num_item=int(float(signal_level))
            if(signal_strength==[]):
               signal_strength.append(num_item)
            elif(num_item not in signal_strength):
                signal_strength.append(num_item)
signal_strength.sort()

for i in range(0,20):
    file_name=str(i)+"_feature_pmf.txt"
    with open(file_name)as f:
        list1=f.readlines()
        #print(list1)
        for item in list1:
            #print(item)
            #print(list1.index(item))
            pmf_per_room=item.split(" ")
            #print(pmf_per_room)
            calculator=0
            slide_window = []
            for item1 in pmf_per_room:

                begin=signal_strength[0]
                end=signal_strength[-1]
                if(calculator in range (begin-3,end+4)):
                    slide_window.append(float(item1))
                calculator += 1
            #print(slide_window)
            sum=0

            not_zero_caculator=0
            for item in slide_window:
                if(item !=0.0):
                    not_zero_caculator+=1
                    sum+=item*0.1
                    #item=item*0.9
                    slide_window[slide_window.index(item)]=str(item*0.9)
            average=sum/(len(slide_window)-not_zero_caculator)
            for item in slide_window:
                if(item ==0.0):
                    #item=average
                    slide_window[slide_window.index(item)]=str(average)
            #print(slide_window)
            calculator=0
            slide_window_pointer=0
            for item in pmf_per_room:
                if(calculator in range (begin-3,end+4)):
                    pmf_per_room[calculator]=slide_window[slide_window_pointer]
                    slide_window_pointer+=1
                calculator+=1
            #print(pmf_per_room)
            new_item=" ".join(pmf_per_room)
            #print(new_item)
            print(new_item)
            new_file_name=str(i)+"_feature_pmf_new.txt"
            with open(new_file_name,'a')as f:
                f.write(new_item)
        #print(list1)









if(signal_strength==[]):
    signal_strength.append(row[2])
elif(row[2] not in signal_strength):
    signal_strength.append(row[2])



#print(signal_strength)



    for i in range(0,4):
        for k in data_dict1:
            aptitude_dict = defaultdict(int)
            for i in range(0,256):
                if(i in data_dict1[k]):
                    for value in data_dict1[k]:
                        if(i==value):
                            aptitude_dict[i]+=1
                else:
                    aptitude_dict[i]=0
            #print(aptitude_dict)

            pmf_list[k]=dict(aptitude_dict)
    print(pmf_list)
    for k,v in pmf_list.items():
        list=[]
        for key,value in v.items():
            if(value!=0):
                list.append(value)




    data_list = []
    for k,v in pmf_list.items():


        print(k)
        print(dict(v))
        data_list.append(k)
    for rooms in room_list:
        s=""
        if (rooms not in data_list):

            for i in range(0,256):
                s=s+"0.0"+" "
            s+="\n"
            with open(pmf_file_name,'a') as f:
                f.write(s)
        else:
            sum=0
            for key,value in dict(pmf_list[rooms]).items():
                sum+=value
            for key,value in dict(pmf_list[rooms]).items():
                s=s+str(value/sum)+" "
            s+="\n"
            with open(pmf_file_name,'a') as f:
                f.write(s)
'''
