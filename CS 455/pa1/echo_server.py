#!/usr/bin/env python 

""" 
A simple echo server 
""" 

import socket

#get the port number from user
port_num = int(input("Please enter the port number: "))

#setup server
host = '' 
port = int(port_num)
backlog = 5 
size = 1024 
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog) 
while 1: 
    client, address = s.accept() 
    data = client.recv(size) 
    if data: 
        client.send(data)
    client.close()
