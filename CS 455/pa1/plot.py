import matplotlib.pyplot as plt
from numpy import arange
import bisect



plt.xlabel("Message Size (Byte)")
plt.ylabel("TCP Throughput (kbps)")
plt.title("Throughput VS. Message Size")
x1 = [1,100,200,400,800,1000]
y1 = [0.0001400,0.0001581,0.0001474,0.0002287,0.0002693,0.0004064]

x2 = [1024,2048,4096,8192,16384,32768]
y2 = [6866.302,6273.308,11266.47,31441.95,52723.61,79663.50]
plt.plot(x2,y2)

plt.show()
