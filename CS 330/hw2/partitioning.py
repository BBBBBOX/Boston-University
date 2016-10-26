# -*- coding: utf-8 -*-
"""
Created on Wed Feb  4 10:23:03 2015

@author: Kobe
"""

import sys

class Interval:
	def __init__ (self, line):
		splitLine = line.split()
		self.start = int(splitLine[0])
		self.finish = int(splitLine[1])
		self.label = 1
	def __str__ (self):
		return str(self.start)+' '+str(self.finish)+' '+str(self.label)

intervals = [Interval(line) for line in sys.stdin]
intervals.sort(key=lambda x:x.start)


for i in range(len(intervals)):
    interval = intervals[i]
    if i != 0:
       if interval.start < intervals[i-1].finish:
           interval.label += intervals[i-1].label

           

for interval in intervals:
	print(interval)