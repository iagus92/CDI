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

plt.axis([0, 1500, 0, 1800])
plt.rc('lines', linewidth = 2)

plt.title(r'Tiempo (practica 6)')
plt.xlabel('Threads')
plt.ylabel('Time (ms)')

plt.plot([6,100,300,500,750,1000,1300,1500],[1349,1347,1353,1245,1231,1198,1173,1087],label='Ejercicio 4.3(AtomicInteger)')
plt.plot([6,100,300,500,750,1000,1300,1500],[289,297,354,447,424,449,447,462],label='Ejercicio 4.3(LongAdder)')
plt.plot([6,100,300,500,750,1000,1300,1500],[46,28,52,63,76,84,103,106],label='Ejercicio 4.4(LongAdder)')

plt.legend()
plt.show()
