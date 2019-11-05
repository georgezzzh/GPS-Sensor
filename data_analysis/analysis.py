import pandas as pd
from math import radians, cos, sin, asin, sqrt
import matplotlib.pyplot as plt
'''
该程序将 csv格式的GPS数据，绘制出路径，与速度，并求出总路径长度
'''


def haversine(lon1, lat1, lon2, lat2):
    # 求两个经纬度之间的距离
    lon1, lat1, lon2, lat2 = map(radians, [lon1, lat1, lon2, lat2])
    dlon = lon2 - lon1
    dlat = lat2 - lat1
    a = sin(dlat/2)**2 + cos(lat1) * cos(lat2) * sin(dlon/2)**2
    c = 2 * asin(sqrt(a))
    r = 6371
    return c * r * 1000


gps_data = pd.read_csv(r"gps_info.csv")
pd.set_option('mode.chained_assignment', None)
time_raw = gps_data['time']
latitude = gps_data['latitude']
longitude = gps_data['longitude']
distance = [0]*(len(latitude)-1)

# 将时间格式化，转换成容易处理的list
time = [value for value in time_raw]
for i in range(0, len(latitude)-2):
    distance[i] = haversine(longitude[i], latitude[i], longitude[i+1], latitude[i+1])
# 消除距离为0的点，距离为0意味着两个gps点是相同的
i = 0
while i < len(distance):
    if distance[i] == 0.0:
        distance.pop(i)
        time.pop(i+1)
    else:
        i += 1
# 将long型的时间换算成时间间隔
timeInterval = [0]*(len(time))
i = len(time)-1
while i > 0:
    timeInterval[i] = time[i]-time[i-1]
    i -= 1
timeInterval.pop(0)
# 求总距离
sum_dist = 0
for tmp in distance:
    sum_dist += tmp
velocity = [0]*(len(distance))
for i in range(1, len(distance)-1):
    velocity[i] = distance[i]/timeInterval[i]*1000
plt.figure(1)
average_v = sum_dist/(time[len(time)-1]-time[0])*1000
plt.title("average velocity is "+str(round(average_v, 2))+" m/s")
plt.ylabel("velocity(m/s)")
plt.xlabel("time(s)")
plt.plot(velocity)
plt.figure(2)
plt.scatter(latitude, longitude, s=0.1)
plt.tick_params(axis='both')
plt.title("total distance is "+str(round(sum_dist, 2))+" m")
plt.show()
