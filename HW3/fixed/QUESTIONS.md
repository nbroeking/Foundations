1.)Using the material that we have covered in Lectures 8, 9, and 10, explain
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
