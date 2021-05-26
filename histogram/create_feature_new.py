from collections import defaultdict
import numpy as np
#import matplotlib.pyplot as plt
room_list=['cell1','cell2','cell3','cell4','cell5','cell6','cell7','cell8','cell9','cell10']
summary_mac=[]
with open ("dataset_new.txt") as f:
    alist=f.readlines()
    for item in alist:
        astring=item[:-1]
        #print(astring)
        feature_list=astring.split(" ")
        for room in room_list:
            if(feature_list[2]==room):
                wifi_aptitude=abs(int(feature_list[1][:-3]))
                if(wifi_aptitude in range(65,90)):
                    file_Name=feature_list[2]+".txt"
                    with open(file_Name,'a') as f1:
                        f1.write(item)
summary=[]

for i in range(1,11):
    file_Name="cell"+str(i)+".txt"
    sort_dict=defaultdict(dict)
    with open(file_Name) as f:
        mac_list = []
        alist=f.readlines()
        for item in alist:
            astring=item[:-1]
            feature_list=astring.split(" ")
            sort_dict[feature_list[0]]=abs(int(feature_list[1][:-3]))
            if(mac_list==[]):
                mac_list.append(feature_list[0])
            elif(mac_list!=[] and feature_list[0] not in mac_list):
                mac_list.append(feature_list[0])
            #print(sort_dict)
        summary.append(sort_dict)
        summary_mac.append(mac_list)
        #print(mac_list)
#print(summary)
print(len(summary_mac))
calculator=1
for summary_per_room in summary_mac:
    file_Name = "cell" + str(calculator) + ".txt"
    for item in summary_per_room:
        average_list = []
        with open(file_Name) as f:
            alist=f.readlines()
            for piece in alist:
                astring = piece[:-1]
                feature_list = astring.split(" ")

                if(item==feature_list[0]):
                    average_list.append(abs(int(feature_list[1][:-3])))
            average_value=np.mean(average_list)
            new_file_name="new_cell"+str(calculator)+".txt"
            with open(new_file_name,'a') as f:
                string_to_write=str(item)+" "+str(average_value)
                string_to_write=string_to_write+"\n"
                f.write(string_to_write)
    calculator+=1
    if(calculator>10):
        break
memory=[]
for i in range(1,11):
    sort_per_cell={}
    per_cell={}
    file_name="new_cell"+str(i)+".txt"
    with open(file_name) as f:
        alist=f.readlines()
        #print(alist)
        for item in alist:
            astring=item[:-1]
            small_list=astring.split(" ")
            #print(small_list)
            #x.append(small_list[0])
            #y.append(small_list[1])
            per_cell[small_list[0]]=small_list[1]
    #print(sort_per_cell)
    #print(per_cell)
    sort_per_cell=dict(sorted(per_cell.items(), key=lambda x:x[1]))
    #print(sort_per_cell)
    iterator=0
    for k,v in sort_per_cell.items():
        if (iterator > 2):
            break
        if(memory==[]):
            memory.append(k)
            iterator+=1
        else:
            if(k not in memory):
                memory.append(k)
                iterator+=1
            else:
                continue

    #plt.plot(x,y)
    #plt.show()
print(memory)
print(len(memory))
feature_string=", ".join(memory)
print(feature_string)
'''
select_feature=[]
for i in range(1,10):
    file_name="cell"+str(i)+".txt"
    sort_dict = defaultdict(dict)
    with open(file_name) as f:
        alist=f.readlines()
        for item in alist:
            astring=item[:-1]
            feature_list=astring.split(" ")
            sort_dict[feature_list[0]]=abs(int(feature_list[1][:-3]))
        #print(sort_dict)
        new_dict=dict(sort_dict)
        #print(new_dict)
        a=dict(sorted(new_dict.items(), key=lambda x: x[1]))
        print(a)
        calculator=0
        for k,v in a.items():
            if(calculator<9):
                select_feature.append(k)
                calculator+=1
            else:
                break
print(select_feature)
print(len(select_feature))
feature_string=", ".join(select_feature)
print(feature_string)
'''

with open("select_wifi27.txt",'a') as f:
    with open("dataset_new.txt") as f1:
        alist=f1.readlines()
        for item in alist:
            astring=item[:-1]
            feature_list=astring.split(" ")
            for feature in memory:
                if(feature==feature_list[0] and abs(int(feature_list[1][:-3])) in range(65,90)):
                    f.write(item)



