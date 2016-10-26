# -*- coding: utf-8 -*-
"""
Created on Sat Feb  7 09:35:50 2015

@author: Kobe
"""

import sys

class IntervalMin:
    def __init__ (self, line):
        splitLine = line.split()
        self.start = int(splitLine[0])
        self.finish = int(splitLine[1])
        self.length = int(splitLine[1] - splitLine[0] + 1)
    def __str__ (self):
        return str(self.start)+' '+str(self.finish)

# Read
intervals = [IntervalMin(line) for line in sys.stdin]

NewIntervals=sorted(intervals, key = lambda length: IntervalMin.length)

tmp = NewIntervals[0]
finish = tmp.finish
start = tmp.start
solution = [tmp]

for interval in NewIntervals:
    finish = interval.finish
    start = interval.start
    solution = [interval]

    x = 0
    while x < len(NewIntervals):
        if NewIntervals[x].finish <= finish or \
            NewIntervals[x].start >= start:
                NewIntervals.remove(NewIntervals[x])
                x -= 1
        x += 1
    
    
for interval in solution:
    print (str(interval.start)+" "+str(interval.finish))
        