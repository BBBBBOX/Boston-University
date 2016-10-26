# -*- coding: utf-8 -*-
"""
Created on Tue Apr 14 09:15:20 2015

@author: Kobe
"""
import sys


#inputs = [line for line in sys.stdin]
delta = int(input())
alpha = int(input())
x = input()
y = input()

#Alignment(X,Y)
m = len(x)
n = len(y)
A = [[None for i in range(m)], [None for j in range(n)]]
'''for i in range(m):
    A[i,0] = i*delta
for j in range(n):
    A[0,j] = j * delta
'''
def opt(i,j):
    if i == 0:
        return j* delta
    elif j == 0:
        return i*delta
    else:
        if A[i][j] != None:
            return A[i][j]
        else:
            A[i][j] = min(alpha + opt(i-1,j-1),\
                        delta + opt(i-1,j),\
                        delta + opt(i,j-1))
a = []
b = []
def Find_Solution(i,j):
    if i == 0 and j == 0:
        a.reverse()
        b.reverse()
        for q in a:
            print(q,end = "")
        print()
        for p in b:
            print (p, end = "")
    else:
        if alpha + A[i-1][j-1] >= A[i][j]:
            a.append(x[i-1])
            b.append(y[j-1])
            Find_Solution(i-1,j-1)
        elif delta + A[i-1][j] >= A[i][j]:
            a.append('-')
            b.append(y[j-1])
            Find_Solution(i-1,j)
        elif delta + A[i][j-1] >= A[i][j]:
            b.append('-')
            a.append(x[i-1])
            Find_Solution(i,j-1)
        else:
            a.append(x[i-1])
            b.append(y[j-1])
            Find_Solution(i-1,j-1)

        
        
        