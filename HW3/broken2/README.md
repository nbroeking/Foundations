# CSCI 5828 &mdash; Homework 3 &mdash; Fall 2015

## Broken, version 2

This directory contains the second version of the broken Java program for
Homework 3. The only change is that all of the methods in the
`ProductionLine` class have been annotated with the `synchronized` keyword.
Despite this, the program is still broken.

You can find the source code files in `src/main/java`.

To compile the files:

    ./gradlew build

To run the program:

    bin/runBroken
