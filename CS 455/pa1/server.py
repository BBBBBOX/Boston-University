#!/usr/bin/env python 

""" 
A simple echo server 
""" 

import socket 

#setup server
host = '' 
port = 58000
backlog = 5
size = 40000
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM) 
s.bind((host,port)) 
s.listen(backlog)

#connection setup phase
valid_respon = "200 OK: Ready"
invalid_respon = "404 ERROR: Invalid Connnection Setup Message"
client, address = s.accept() 
data = client.recv(size).decode("utf-8")
num_probes = int(data.split(" ")[2])

if data: 
    client.send(valid_respon.encode("utf-8"))
else:
    client.send(invalid_respon.encode("utf-8"))

#Measurement Phase(MP)
for i in range(1, num_probes+1):
    data = client.recv(size).decode("utf-8")
    #while data[-1] != '\n':
    #   data = data + client.recv(size)
    if data:
        client.send(data.encode("utf-8"))


#3) Connection Termination Phase (CTP)
close_ready = "200 OK: Closing Connection"
close_error = "404 ERROR: Invalid Connection"
data = client.recv(size).decode("utf-8")
if data == "t\n":
    client.send(close_ready.encode("utf-8"))
else:
    client.send(close_error.encode("utf-8"))

client.close()

