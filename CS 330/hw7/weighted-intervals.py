# -*- coding: utf-8 -*-
"""
Created on Mon Mar 30 03:45:25 2015

@author: Kobe
"""

import sys

class Interval:
	def __init__ (self, line):
		splitLine = line.split()
		self.start = int(splitLine[0])
		self.finish = int(splitLine[1])
		self.value = int(splitLine[2])
	def __str__ (self):
		return str(self.start)+' '+str(self.finish)+' '+str(self.value)
   
intervals = [Interval(line) for line in sys.stdin]
intervals = sorted(intervals, key = lambda interval: interval.finish)
#total number of the intervals
n = len(intervals)
#create a memory list that store the solutions we have calculated
M = [None]* (n+1)
M[0] = 0
#create a value list that store all the values of intervals
v = [0]* n
for i in range(n):
    v[i] = intervals[i].value
#creating a P[i] list
p = [0]*n
m = n
for i in range(1,m):
    for k in range(1,i):
        if intervals[i-k].finish < intervals[i].start:
            p[i] = i-k
            break
    m = m-1

def optimal_Solution(j):
    if j == 0:
        return 0
    elif M[j] != None:
        return M[j]
    else:
        M[j] = max(v[j]+ optimal_Solution(p[j]), optimal_Solution(j-1))
        return M[j]

#return a subset of the interval as solution
solution = []
def Find_Solution(j):
    if j == 0:
        return solution
    else:
        if v[j]+ M[p[j]] >= M[j-1]:
            solution.append(intervals[j])
            Find_Solution(p[j])
        else:
            Find_Solution(j-1)


#run function optimal and return the solution
optimal_Solution(n-1)
Find_Solution(n-1)
for i in solution:
    print(i)