CSCI 5828 - Spring 2015
Homework 2
Josh Rahm | Nic Broeking
Josh Rahm

//Josh
git init
git remote add origin git@github.com:jrahm/FoundationsHW2.git
git add Readme.md ; git commit -m '0'
git push -u origin master

git add Readme.md ; git commit -m '1'
git add Readme.md ; git commit -m '2'
git add Readme.md ; git commit -m '10'
git push origin master

//Nic
git init 
git remote add origin git@github.com:jrahm/FoundationsHW2.git
git pull origin master
git checkout -b bug-fix
git add Readme.md ; git commit -m '3'
git add Readme.md ; git commit -m '4'
git pull origin master
git mergetool
git commit -m '5'
git add Readme.md ; git commit -m '6'

//Josh
git checkout 2a0fbf3 # checkout 4
git checkout -b bug-fix-experimental
git add Readme.md ; git commit -m '7'
git add Readme.md ; git commit -m '8'
git add Readme.md ; git commit -m '9'

//Nic
git merge bug-fix-experimental
git meregtool
git commit -m '11'
git add Readme.md ; git commit -m '12'
git push origin master

//Josh
git checkout master
git merge bug-fix
git mergetool
git commit -m '13'
git mv Readme.md README.md
git add README.md ; git commit -m '14'
