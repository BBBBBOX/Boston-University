# -*- coding: utf-8 -*-
"""
Created on Wed Feb  4 07:37:39 2015

@author: Kobe
"""

import sys

class Interval:
    def __init__ (self, line):
        splitLine = line.split()
        self.start = int(splitLine[0])
        self.finish = int(splitLine[1])
    def __str__ (self):
        return str(self.start)+' '+str(self.finish)

# Read
intervals = [Interval(line) for line in sys.stdin]

solIntervals = []

#sort the data by interval.final and then if they get same finish
#sort them by the interval.start
NewIntervals=sorted(intervals, key = lambda interval: [interval.finish, interval.start])

finish = -100
for interval in NewIntervals:
    if interval.start >= finish:
       nextPick = interval
       solIntervals.append(nextPick)
       finish = nextPick.finish
         

#Print
for interval in solIntervals:
    print (str(interval.start)+" "+str(interval.finish))