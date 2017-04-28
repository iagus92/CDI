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

plt.axis([0, 1000, 2000, 100000])
plt.rc('lines', linewidth = 2)

plt.title(r'Tiempo practica 9 (500.000 paginas)')
plt.xlabel('Threads')
plt.ylabel('Time (ms)')

plt.plot([3,5,10,30,50,100,300,500,1000],[2141,5179,11739,40737,60830,112970,379488,681447,1439290],label='Notifyall')
plt.plot([3,5,10,30,50,100,300,500,1000],[2356,2406,2442,2578,2648,2643,3187,3287,3617],label='Notify')

plt.legend()
plt.show()
