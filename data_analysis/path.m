clear;
m=csvread('C:\Users\George\Desktop\accelerate.csv',1,0);
timeInterval=0.1;
ax=m(:,2);
ay=m(:,3);
t=0.1*ones(length(ax),1);
vx=ones(length(ax),1);
vx(1)=0;
for i=2:length(ax)
    vx(i)=vx(i-1)+ax(i)*timeInterval;
end
vy=ones(length(ay),1);
vy(1)=0;
for i=2:length(ay)
    vy(i)=vy(i-1)+ay(i)*timeInterval;
end

dx=ones(length(vx),1);
dx(1)=0;
dy=ones(length(vx),1);
dy(1)=0;
for i=2:length(vx)
    dx(i)=dx(i-1)+vx(i)*timeInterval;
end
for i=2:length(vy)
    dy(i)=dy(i-1)+vy(i)*timeInterval;
end
disp(dx);
disp(dy)

plot(dx,dy)
axis([-10 30 -40 20])