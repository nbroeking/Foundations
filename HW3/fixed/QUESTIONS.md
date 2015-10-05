Homework3
=========

Authors: Nicolas Broeking, Joshua Rahm
Class: CSCI 5828 - Fall 2015
Turned in by: Both



1.) Question 1
--------------

The broken program doesn't work because the Consumers, Producers, and the
Production Line do not interact with each other in a thread safe manner.  There
is no mutex protection on shared memory thus leading to race conditions. Several problems are

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


####B. Some product ids are not being produced This problem is related to the
above problem. When two threads both get assigned a product with the same id
then the next step is to increment the value. They will both increment the
value at the same time thus making \_id go up two values instead of one.
Essentially for every duplicate product id there will be a missing product id.

####C. Producer and consumer tight loop on their run method This is a waist of
resources and even though it doesn't lead to any noticeable problems it will
cause the computer to waste cpu cycles checking on the status of the queue.
The solution is to use conditional variables to block the thread until that
condition has been satisfied.

####D. Memory Barrier
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

* All operations on the queue will happen in a randomish order so we can get exceptions because the producer tries to access the next element when the queue is empty. Those statements are not atomic so another thread can swipe the last element in between the check and the retrieve. 

```Java
if (queue.size() > 0) {
   Product p = queue.retrieve(); //The producers never know about this action
```
* The consumers can get live-locked for another reason. Things might still be in the queue but because two consumers could potentially eat the same poison pill thus leaving un-processed things on the queue.

Question 2
----------

This sycnronized calls only syncronize the production queue. This is a good start but it isn't enough to create a thread safe program. There are still two things that are wrong with the code. 

####Products static id is not syncronized
This would solve several of the problems that showed up with threads accessing the queue out of order however it would not solve the problems releated to duplicated ids and missing ids. This is because the methods where the Product are created

```Java
if (queue.size() < 10) {
        Product p = new Product(); //This still isn't syncronized
        String msg = "Producer %d Produced: %s on iteration %d";
        System.out.println(String.format(msg, id, p, count));
        queue.append(p);
        count++;
      }
```
are not syncronized. They are still able to generate the same number and skip over nessisary ones.

####The producers are using the syncronized calls in an unsafe way
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

3.3. Now turn your attention to creating an implementation of the program that
functions correctly in the fixed directory. In your answer to this question,
you should discuss the approach you took to fix the problem and get your
version of the program to generate output that is similar to the
example_output.txt file that is included with the repo. While your output will
not match the example_output.txt file exactly, it should have a similar
structure:

The beginning of the output will show the producers starting up, followed by
the consumers, and then their outputs interleaving.  At some point, Producers
will finish their work and announce that they are shutting down.  Consumers
will then start to shut down as well as they encounter the special Products
that indicate that production is done.  All of the producers will eventually
shutdown followed by the last couple of consumers shutting down as well.  The
last part of the file is then a list of product ids from 0 to 199 inclusive.
The monitor may generate output at any time, so it might have a message that
appears in the final list of ids. That is fine, if that happens to you.)
