Homework3
=========

Authors: Nicolas Broeking, Joshua Rahm
Class: CSCI 5828 - Fall 2015
Turned in by: Both



1.) Question 1
--------------

The broken program doesn't work because the Consumers, Producers, and the
Production Line do not interact with each other in a thread safe manner.  There
is no mutex protection on shared memory thus leading to several thread related issues.

####A. Multiple Producers are producing the same product id.  The Product object
is not thread safe and thus when the product id's are being created there is a
chance that two threads will use the same one.

```Java
public Product() {
    id   = _id; //Set
    name = "Product<" + id + ">";
    _id++; //Updated
    done = false;
}

public void productionDone() {
    done = true;
    _id--;
}
```

These two methods show the mistake with the product ids. When the products are
being created multiple threads can hit the set at the same time. This will give
them each the same product id because there is no mutex stoping the other
threads. The same issue happens with the productionDone method. If one thread
is creating a new product while another one is attempting to call this method it
will cause \_id to have duplicate values.


####B. Some product ids are not being produced 
This problem is related to the above problem. When two threads both get assigned a product with the same id
then the next step is to increment the value. They will both increment the
value at the same time thus making \_id go up two values instead of one.
Essentially for every duplicate product id there will be a missing product id.

####C. Producer and consumer tight loop on their run method 
This is a waist of resources and even though it doesn't lead to any noticeable problems it will cause the computer to waste cpu cycles checking on the status of the queue.
The solution is to use conditional variables to block the thread until that
condition has been satisfied.

####D. Java Memory Barrier
The java environment is designed for optimization, because of this the size of the queue is cached for each of the threads. Because of this the producers will never push the poison onto the queue because they think the queue is full.

```Java
//Producer Main loop
public void run() {
    int count = 0;
    while (count < 20) {   //This will be true forever
      if (queue.size() < 10) { //This will become false and never change
        Product p = new Product();
        String msg = "Producer %d Produced: %s on iteration %d";
        System.out.println(String.format(msg, id, p, count));
        queue.append(p);
        count++; //Will never get called 
      }
    }
    Product p = new Product();
    p.productionDone();
    queue.append(p);
    String msg = "Producer %d is done. Shutting down.";
    System.out.println(String.format(msg, id));
  }


//Consumer Mainloop
while (true) {
      if (queue.size() > 0) {
        Product p = queue.retrieve(); //The producers never know about this action
        if (p.isDone()) {
          String msg = "Consumer %d received done notification. Goodbye.";
          System.out.println(String.format(msg, id));
          return;
        } else {
          products.put(p.id(), p);
          String msg = "Consumer %d Consumed: %s";
          System.out.println(String.format(msg, id, p));
        }
      }

```
The consumers will then never get a poison pill and then will never shut down. They will keep looping checking if the size of the queue changed thus ending in live-lock.


####E. Live-lock, exceptions, and other such queue related issues 
Finally, because we have 10 consumers and 10 producers all trying to access shared memory a lot of different issues will arise. There are so many potential things that could happen that it is impossible to name them all, we will do our best though.

* All operations on the queue will happen in a random order so we can get exceptions because the producer tries to access the next element when the queue is empty. Those statements are not atomic so another thread can swipe the last element in between the check and the retrieve. 

```Java
if (queue.size() > 0) {
   Product p = queue.retrieve(); //The producers never know about this action
```
* The consumers can get live-locked for another reason. Things might still be in the queue but because two consumers could potentially eat the same poison pill thus leaving un-processed things on the queue.

Question 2
----------

This synchronized calls only synchronize the production queue. This is a good start but it isn't enough to create a thread safe program. There are still two things that are wrong with the code. 

####Products static id is not synchronized
This would solve several of the problems that showed up with threads accessing the queue out of order however it would not solve the problems related to duplicated ids and missing ids. This is because the methods where the Product are created

```Java
if (queue.size() < 10) {
        Product p = new Product(); //This still isn't synchronized
        String msg = "Producer %d Produced: %s on iteration %d";
        System.out.println(String.format(msg, id, p, count));
        queue.append(p);
        count++;
      }
```
are not synchronized. They are still able to generate the same number and skip over necessary ones.

####The producers are using the synchronized calls in an unsafe way
The issue lies with the following code.

```Java
while (true) {
      if (queue.size() > 0) { //Thread safe call
        Product p = queue.retrieve(); //Thread safe call
        if (p.isDone()) {
          String msg = "Consumer %d received done notification. Goodbye.";
          System.out.println(String.format(msg, id));
          return;
        } else {
          products.put(p.id(), p);
          String msg = "Consumer %d Consumed: %s";
          System.out.println(String.format(msg, id, p));
        }
      }
    }

```
queue.size() and queue.retrieve are both thread safe calls now however they need to by atomic for the code to work. A situation could arise where there is 1 thing in the queue and a consumer calls queue.size() and gets 1. Then another producer does the same thing and then gets the last thing out of the queue with queue.retrieve. Then the original producer tries to call queue.retrieve() and there is nothing in the queue so it throws an exception and crashes. 

Question 3
----------

Our approach to fix the problem involved turning the ProductionLine class into a blocking queue. Instead of having the producers and consumers busy wait on the insertions and removal we added conditional variables to allow them to wake up only when they are needed. The next step was to add a reentrant lock around only the critical sections of the queue. This way the producers and consumers can still make progress while the other threads are running. The final thing we had to do was to give all producers a lock that they had to block on anytime they created a producer. This prevented the bugs where certain ids would keep getting skipped and duplicated. 

By fixing the code in these three areas we are able to achieve a program where each thread is only blocked when it needs to be and only runs when it has too. The race conditions are all dealt with and the memory barrier issues go away when we start using a mutex.