Homework3
=========

Authors: Nicolas Broeking, Joshua Rahm
Class: CSCI 5828 - Fall 2015
Turned in by: Both



1.) Using the material that we have covered in Lectures 8, 9, and 10, explain
why the broken program doesn't work. What concurrency-related problems are
occuring in this program? If you see the program end in livelock, then describe
what is happening with the threads. Why can't they make progress? If you see
the program end in another way, such as getting to the point where it prints
out the product ids but doesn't include all of them, explain why you think that
happened. Note: If you start to add println statements to the Producers and
Consumers, you may actually altar the behavior of the program! If you observe
this, then also include a discussion on why that happens as well. In your
answer, if you want to include snippets of code and/or output to explain what
you are seeing, then do so. Use all of Markdown's capabilities to display what
you need to explain the concurrency-related problems that you are observing.

The broken program doesn't work because the Consumers, Producers, and the
Production Line do not interact with each other in a thread safe manner.  There
is no mutex protection on shared memory thus leading to race conditions. Several problems are

####1. Multiple Producers are producing the same product id.  The Product object
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
is creatng a new product while another one is attempting to call this method it
will cause \_id to have duplicate values.


####2. Some product ids are not being produced This problem is releated to the
above problem. When two threads both get assigned a product with the same id
then the next step is to increment the value. They will both increment the
value at the same time thus making \_id go up two values instead of one.
Essentially for every duplicate product id there will be a missing product id.

####3. Producer and consumer tight loop on their run method This is a waist of
resources and even though it doesn't lead to any noticible problems it will
cause the computer to waste cpu cycles checking on the status of the queue.
The solution is to use conditional variables to block the thread untill that
condition has been satisified..

####4. Livelock, exceptions, and other such queue related issues Finally,
because we have 10 consumers and 10 producers all trying to syncronize on the
queue we will run into many issues.

2.) Now switch your attention to the broken2 program. The only difference
between the two programs are the synchronized keywords on the methods contained
in ProductionLine.java. For this question, explain why this approach to fixing
the program failed. Why is it that synchronizing these methods is not enough.
What interactions between the threads are still occuring that cause the program
to not be able to produce the correct output? Again, you may use snippets of
code and/or output to illustrate your points. The only requirement for this
question is that you focus exclusively on issues related to why this particular
approach fails to solve the problem. In other words, your answer to this
question should be different than your answer to the question above where you
are discussing the program and its concurrency problems in general

3.3. Now turn your attention to creating an implementation of the program that
functions correctly in the fixed directory. In your answer to this question,
you should discuss the approach you took to fix the problem and get your
version of the program to generate output that is similar to the
example_output.txt file that is included with the repo. While your output will
not match the example_output.txt file exactly, it should have a simliar
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
