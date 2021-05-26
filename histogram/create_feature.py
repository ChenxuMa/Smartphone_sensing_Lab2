from collections import defaultdict
from numpy import *
import numpy as np
label_list=['cell1','cell2','cell3','cell4','cell5','cell6','cell7','cell8','cell9']
summary_list=defaultdict(list)
with open("dataset.txt") as f:
    alist=f.readlines()
    print(alist)

for item in alist:
    astring=item[:-1]
    print(astring)
    feature_list=astring.split(" ")
    print(feature_list)
    if(len(summary_list[feature_list[0]])==0):
        summary_list[feature_list[0]].append(feature_list[2])
    elif(feature_list[2] not in summary_list[feature_list[0]]):
        summary_list[feature_list[0]].append(feature_list[2])
print(len(summary_list))
summary_list_new=defaultdict(list)
for item in alist:
    astring=item[:-1]
    feature_list=astring.split(" ")
    if(len(summary_list_new[feature_list[0]])==0):
        summary_list_new[feature_list[0]].append(int(feature_list[1][:-3]))
    elif(int(feature_list[1][:-3]) not in summary_list_new[feature_list[0]]):
        summary_list_new[feature_list[0]].append(int(feature_list[1][:-3]))
summary_list_average=defaultdict(dict)
for k,v in summary_list_new.items():
    summary_list_average[k]=mean(v)
#print(summary_list_average)


summary_list_new = defaultdict(list)
summary_list_variance=defaultdict(dict)
for k,v in summary_list.items():
    variance_list=[]
    for label in v:
        RSSI_per_room=[]
        for item in alist:
            astring=item[:-1]
            feature_list=astring.split(" ")
            if(feature_list[2]==label and feature_list[0]==k):
                RSSI_per_room.append(abs(int(feature_list[1][:-3])))

        #print(RSSI_per_room)
        #print(RSSI_per_room)
        average=mean(RSSI_per_room)
        summary_list_new[k].append(average)
        variance_list.append(average)
    variance=np.var(variance_list)

    summary_list_variance[k]=variance

print(summary_list)
print(len(summary_list))
print("*****************")
print(summary_list_new)
print(len(summary_list_new))
print(summary_list_variance)
print(len(summary_list_variance))
var_dict=dict(summary_list_variance)
print(var_dict)
sort_dict=dict(sorted(var_dict.items(), key=lambda e:e[1], reverse=True))
print(sort_dict)
sort_feature=[]
calculator=0
for k,v in sort_dict.items():
    if(calculator<15):
        sort_feature.append(k)
    else:
        break
    calculator+=1
print(sort_feature)
with open("selected_wifi15.txt",'a')as f:
    with open("dataset.txt") as f1:
        alist=f1.readlines()
        for item in alist:
            astring=item[:-1]
            feature_list=astring.split(" ")
            for feature in sort_feature:
                if(feature==feature_list[0]):
                    f.write(item)
