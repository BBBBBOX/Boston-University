#!/usr/bin/env python 

""" 
A simple echo client 
""" 

import socket 
#get host name and port number from user
host_name = input("Please enter the host name")
if host_name == "":
    host_name = 'csa2.bu.edu'
port_number = int(input("Please enter the port number"))
if port_number == 0:
    port_number = 58000
#setup client
host = host_name#the default host name is cas2.bu.edu
port = port_number#the default port number  is 58000
size = 1024 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.connect((host,port))
#sending message to the server
msg = "Hello, world!"
s.send(msg.encode("utf-8")) 
data = s.recv(size).decode("utf-8") 
s.close() 
print ('Received:' + data)
