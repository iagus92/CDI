"""
=============================
Demo of the errorbar function
=============================

This exhibits the most basic use of the error bar method.
In this case, constant values are provided for the error
in both the x- and y-directions.
"""

import numpy as np
import matplotlib.pyplot as plt

# example data

plt.axis([0, 585, 0, 11])

plt.title(r'Tiempo')
plt.ylabel('Thread')
plt.xlabel('Time (ms)')

plt.plot([1,365], [1,1],label="Thread 1")
plt.plot([1,478], [2,2])
plt.plot([2,570], [3,3])
plt.plot([10,494], [4,4])
plt.plot([38,545], [5,5])
plt.plot([34,505], [6,6])
plt.plot([54,500], [7,7])
plt.plot([58,585], [8,8])
plt.plot([74,558], [9,9])
plt.plot([90,573], [10,10])

plt.show()
