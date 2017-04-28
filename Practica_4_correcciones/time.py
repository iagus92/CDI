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

plt.axis([0, 1000, 2000, 4000])
plt.rc('lines', linewidth = 2)

plt.title(r'Tiempo practica 4 (filtro 8)')
plt.xlabel('Threads')
plt.ylabel('Time (ms)')

plt.plot([6,100,200,350,500,650,850,1000],[3331,2387,2168,2317,2266,2316,2259,2508],label='Columnas')
plt.plot([6,100,200,350,500,650,850,1000],[2985,2504,2233,2249,2349,2248,2498,2512],label='Filas')
plt.plot([6,100,200,350,500,650,850,1000],[3155,2286,2274,2378,2448,2245,2416,2511],label='Bandas verticales')
plt.plot([6,100,200,350,500,650,850,1000],[3145,2403,2349,2501,2200,2375,2611,2540],label='Bandas horizontales')

plt.legend()
plt.show()
