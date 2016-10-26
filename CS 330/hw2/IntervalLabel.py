# -*- coding: utf-8 -*-
"""
Created on Wed Feb  4 03:43:13 2015

@author: Kobe
"""

import sys

class Interval:
	def __init__ (self, line):
		splitLine = line.split()
		self.start = int(splitLine[0])
		self.finish = int(splitLine[1])
		self.label = 0
	def __str__ (self):
		return str(self.start)+' '+str(self.finish)+' '+str(self.label)

intervals = [Interval(line) for line in sys.stdin]
intervals.sort(key=lambda x:x.start)
for interval in intervals:
	excluded=[]
	for a in intervals:
		if a==interval:
			break
		elif a.finish>interval.start:
			excluded.append(a.label)
	for x in range (1, len(intervals)+1):
		if not x in excluded:
			interval.label=x
			break
for interval in intervals:
	print interval