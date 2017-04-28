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

plt.axis([0, 1000, 0, 1300])
plt.rc('lines', linewidth = 2)

plt.title(r'Tiempo (practica 8)')
plt.xlabel('Threads')
plt.ylabel('Time (ms)')

plt.plot([10,50,100,200,400,600,800,1000],[17,79,143,238,441,621,1058,1105],label='Read 500 pages')

plt.legend()
plt.show()
