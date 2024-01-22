# Queueing theory
Queueing theory is the mathematical study of waiting lines or queues.
We use queueing theory in our software development to analyze and optimize our practices and processes, such as our customer service responsiveness, project management kanban planning, inter-process communication message queues, and DevOps continuous deployment pipelines.
## Contents
<h4>1- Introduction</h4>
<h4>2- Queue types and service types</h4>
<h4>3- Description the project</h4>

## Introduction
We use queueing theory in our projects for many purposes:
<h5>1- Customer service responsiveness</h5>
<h5>2- Project management kanban planning</h5>
<h5>3- Inter-process communication message queues</h5>
<h5>4- DevOps continuous deployment pipelines</h5>

## Queue types and service types
Queue types and service types describe how the queue chooses which items to process.
<h5>1- First In First Out (FIFO): serve the customer who has been waiting for the longest time.</h5>
<h5>2- Last In First Out (LIFO): serve the customer who has been waiting for the shortest time.</h5>
<h5>3- Priority: serve customers based on their priority level; these levels could be based on status, urgency, payment, etc.</h5>
<h5>4- Shortest Job First (SJF): serve the customer who needs the smallest amount of service.</h5>
<h5>5- Longest Job First (LJF): serve the customer who needs the largest amount of service.</h5>
<h6>6- Time Sharing: serve everyone at the same time; service capacity is distributed evenly among everyone waiting.</h6>

## Description
<h4>There are four types of service </h4>
<h5>1- M/M/1   means that Poisson Arrivals, exponentially distributed service times, one-only servers, and infinite capacity buffer.</h5>
<h5>2- M/M/C   means that Poisson Arrivals, exponentially distributed service times, c identical servers, and infinite capacity buffer.</h5>
<h5>3- M/M/1/K means that Poisson Arrivals, exponentially distributed service times, one-only servers, and finite capacity buffer with size k.</h5> 
<h5>4- M/M/C/K means that Poisson Arrivals, exponentially distributed service times, c identical servers, and finite capacity buffer with size k.</h5>

You can choose your type of service by writing on the console after that you should insert data such as arrival rate, service rate, and the number of simulations and if you choose M/M/C you should insert the number of servers and capacity if you choose any one of M/M/1/k or M/M/C/K.

##### After choosing your system and insert requiring data the program will show you a simulation diagram about how the system works, 
###### 1- L:  Expected number of customers in the system.
###### 2- Lq: Expected number of customers in the queue.
###### 3- W:  Expected waiting time in the system.
###### 4- Wq: Expected waiting time in the queue.

<img src="https://github.com/nourElbassuny/Queue-Models/assets/146573118/749a7770-d1ef-4ce9-b44a-90c7501bcf73">
<img src="https://github.com/nourElbassuny/Queue-Models/assets/146573118/da0d0699-0804-44b7-a965-2d160c9512a1" width=800>


