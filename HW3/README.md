# CSCI 5828 &mdash; Homework 3 &mdash; Fall 2015

In this repository, you will find the two versions of the broken
producer-consumer program that is associated with Homework 3 for
Prof. Anderson's CSCI 5828 class at the University of Colorado
Boulder for the Fall 2015 semester.

Each of the `broken` directories has a README file that explains how
to compile and run the code in that directory.

Your goal is to create a new version of the program that:

* fixes the program so that it runs correctly
  * you may only modify the following files
    1. `Consumer.java`
    2. `Producer.java`
    3. `ProductionLine.java`
  * you may not modify the following files
    1. `Main.java`
    2. `Monitor.java`
    3. `Product.java`

You may certainly add `println` statements to this program to debug it.
But, do not mess with the original `println` statements that appear
in the program and remove any additional `println` statements that
you added to the program **before** you submit it.

* You are done when the following things are true of your new program
  1. The program exits cleanly to the command prompt.
    * That is, all threads shut down cleanly at the end of your program.
  2. Your program's output file finishes by printing out the numbers
     0 to 199 inclusive. There can be no gaps in the numbers that are
     printed out.

This repository contains an example of the output produced by a correctly
fixed version of this program.
