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

plt.axis([0, 1000, 1950, 3000])
plt.rc('lines', linewidth = 2)

plt.title(r'Tiempo practica 4 (filtro 8, umbral 19000)')
plt.xlabel('Threads')
plt.ylabel('Time (ms)')

plt.plot([6,100,200,350,500,650,850,1000],[2971,2332,1999,2221,2221,2254,2236,2323],label='Pixeles negros')

plt.legend()
plt.show()
