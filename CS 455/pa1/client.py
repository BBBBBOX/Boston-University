#!/usr/bin/env python 

""" 
Pa1 part2, computing RTT and Throughput 
"""
import socket
import time
import sys
#---------------------------------------------------
#Helping function
def make_payload (message_size):
    tmp = ""
    for i in range(message_size):
        tmp += 'a'
    return tmp

def make_message (title, num, payload):
    s = title + " " + str(num) + " " + payload+"\n"
    return s
#----------------------------------------------------
    

#setup TCP connetion
host = 'localhost'
port = 58000 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.connect((host,port))
size = 40000

#connection setup phase
tmp_message = input("input message in CSP format: ")
tmp = tmp_message.strip('\n').split(" ")
#read input
protocol_phase = tmp[0]
measurement_type = tmp[1]
num_probes = int(tmp[2])
message_size = int(tmp[3])
serverDelay = tmp[4]

#send message to server
s.send(tmp_message.encode("utf-8")) 
data = s.recv(size).decode("utf-8")
if data == "200 OK: Ready":
    print ('Received from server:' + data)
else:
    print('Received from server: ' + data)
    s.close()



#Measurement Phase(MP)
total_rtt= 0
total_throughput = 0
for i in range(1,num_probes+1):
    #make a message
    payload = make_payload(message_size)
    msg = make_message('m',i, payload)

    #start time
    start_time = time.clock()
    s.send(msg.encode("utf-8"))
    rev = s.recv(size).decode("utf-8")
    #while rev[-1] != '\n':
    #       rev = rev + s.recv(size)
    #ending time
    end_time = time.clock()
    rtt = end_time - start_time

    #accumulate rtt and throughput
    total_rtt += rtt
    total_throughput += message_size/rtt
    #output 
    print("Received From Server: " + rev)
    print("RTT: " + str(rtt))

ave_rtt = total_rtt / num_probes
ave_throughput = total_throughput / num_probes

if measurement_type == "rtt":
    print("The Average RTT is: " + str(ave_rtt))
elif measurement_type == "tput":
    print("The throughput is: "+ str(ave_throughput))
else:
    print("Error: Type can only be rtt or tput")


#3) Connection Termination Phase (CTP)
msg = "t\n"
s.send(msg.encode("utf-8"))
data = s.recv(size).decode("utf-8")
print("Received from server: "+ data)
print("Client Terminating")
s.close()
