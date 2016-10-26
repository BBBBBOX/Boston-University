import sys

class Interval:
    def __init__ (self, line):
        splitLine = line.split()
        self.start = int(splitLine[0])
        self.finish = int(splitLine[1])
    def __str__ (self):
        return str(self.start)+' '+str(self.finish)

intervals = [Interval(line) for line in sys.stdin]

solIntervals=[]
while intervals:
    nextPick=intervals[0]
    earliestEnd=nextPick.finish
    
    for interval in intervals:
        if interval.finish<earliestEnd:
            earliestEnd=interval.finish
            nextPick=interval
            
    solIntervals.append(nextPick)
    intervals.remove(nextPick)
    
    x=0    
    while x<len(intervals):
        interval=intervals[x]
        if interval.start<nextPick.finish:
            intervals.remove(intervals[x])
            x-=1
        x+=1
        
for interval in solIntervals:
    print (str(interval.start)+" "+str(interval.finish))