# -*- coding: utf-8 -*-
"""
Created on Tue Feb  3 10:09:29 2015

@author: Kobe
"""

class Name:
	def __init__ (self, first, last):
		self.first = first
		self.last = last
	def __str__ (self):
		return str(self.first)+' '+str(self.last)

names=[Name("hannah", "flynn"), Name("bob", "smith"), Name("jane", "doe"), Name("dan", "adams"), Name("leonid", "reyzin"), Name("jane", "brown")]
sortedByFirstName = sorted(names, key = lambda name: name.first)
sortedByLastName = sorted(names, key = lambda name: name.last)
sortedByLastThenFirst=sorted(names, key = lambda name: [name.last, name.first])

print ("Sorted by first name:")
for name in sortedByFirstName:
	print(name)

print ("Sorted by last name:")
for name in sortedByLastName:
	print(name)

print ("Sorted by last then first name:")
for name in sortedByLastThenFirst:
	print(name)
 
a = [1,2,3,4]
for b in a:
    print(b)