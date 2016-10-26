
import numpy as np
import matplotlib.pyplot as plt

n_groups = 3
Loss = (1857.65, 1723.34, 1650.52)
Corruption = (1857.65, 2536.18, 1887.27)

fig,ax = plt.subplots()
index = np.arange(n_groups)
bar_width = 0.35

opacity = 0.4
rects1 = plt.bar(index, Loss, bar_width,alpha=opacity, color='b',label='Loss')
rects2 = plt.bar(index + bar_width, Corruption, bar_width,alpha=opacity,color='r',label='Corruption')

plt.xlabel("Percentage")
plt.ylabel("RTT")
plt.title("RTT VS. Loss & Corruption")

plt.xticks(index + bar_width, ("0.0", "0.25", "0.50"))
plt.ylim(0,3000)
plt.legend()

plt.tight_layout()
plt.show()
