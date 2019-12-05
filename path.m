clear;
%数据格式设置为long
format long g;
%将csv数据读入到m中
m=csvread('C:\Users\George\Desktop\gps_info.csv',1,0);
ax=m(:,2);
ay=m(:,3);
time=m(:,1);
%将时间戳转换为时间间隔
for i=length(time):-1:2
    time(i)=(time(i)-time(i-1))/1000;
end
time(1)=0;
%绘制出来
plot(ay,ax);
%xlim([104.0826 104.0855])