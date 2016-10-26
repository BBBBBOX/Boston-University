# -*- coding: utf-8 -*-
"""
Created on Tue Mar  3 20:22:48 2015

@author: Kobe
"""

'''
Algorithim:
    1. set Max = 0 and Set Min = 0. Delta will store the biggest 
        diffrerence between Max and Min. result will be a tuple that 
        store the Max and Min that have biggest Delta.
    2. If the first value in the list is a, we can initial the following
        values: Max = a, Min = a, Delta = a-a =0, result = (a,a)
    3. Then we go through the rest of the list, for every element b,
        we are going to check:
            1. if b >= Max:
                Then set Max = b, Min= b, Delta = 0, result= (b,b)
            2. if b < Max:
                if b < Min:
                    Then set Min = b
                    tmp = Max-b
                    if tmp > Delta:
                        Delta = tmp
                        reslut = (Max,Min)
                    
    4. After we go through every element, we just return result

Analyze:
    Because we only go through the date once, the running time 
    would just be in Theta(n)
'''
import sys
class melt():
    heights = [int(line) for line in sys.stdin]
    a= heights[0]
    Max = a
    Min = a
    Delta = 0
    result = (a,a)
    heights = heights[1:]
    
    for b in heights:
        if b >= Max:
            Max = b
            Min = b
        else:
            if b < Min:
                Min = b
                tmp = Max - b
                if tmp > Delta:
                    Delta = tmp
                    result = (Max,b)
    print (\
    'the biggest decrease is ' + str(Delta) +\
    ': from '+ str(result[0]) + ' to ' + str(result[1]))