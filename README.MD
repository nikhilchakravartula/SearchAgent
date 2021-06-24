# Search Agent

## Objective
* To develop search algorithms that find the optimal path from start state to a goal state.

## Prerequisites
 java 1.8

## Using the experiment

If you are using an IDE like IntelliJ or Eclipse, use the following steps to run the project
* Point the working directory to Search-Agent
* Main class: SearchAgent
* JRE: 1.8
* Run the project

If you are running the project on command line, follow these steps:<br/>
* Clone/Download the project to your local machine.
* Navigate to the folder with SearchAgent.java file.
* Run the following commands:
```
javac SearchAgent.java
```

```
java SearchAgent
```

## Problem Description
* The input of our program includes a topographical map of the land, plus some information
about where our party starts their journey, the intended site our party wants to settle and
some other quantities that control the quality of the solution. 
* The land can be imagined as a
surface in a 3-dimensional space, and a popular way to represent it is by using a mesh-grid. 
* The
M value assigned to each cell will represent how muddy the patch of land is or whether it
contains a rock. At each cell, the wagon can move to each of 8 possible neighbor cells: North,
North-East, East, South-East, South, South-West, West, and North-West. Actions are assumed
to be deterministic and error-free (the wagon will always end up at the intended neighbor cell).
* The wagon cannot go over rocks that are too high, and the wheels are such that, as the land
gets muddier, the wagon slows down. Therefore, the value M in each cell can advise us on
whether we can take that route (in case of rocks) or how much moving into that cell will cost
the settler party in terms of time if they move into it (in case of mud).
## Search for the optimal paths
* Our task is to lead the party of settlers from their start position to the land they aim to reach. 
* If we had the ideal vehicle that can go across any land without a slow-down, usually the shortest geometrical path is defined as the optimal path; however, since our wagon is far from ideal, our objective is to avoid rocks we can’t cross over as well as really muddy areas. 
* Our goal is, roughly, finding the shortest
path among the safe paths. What defines the safety of a path is whether there are rocks we
can’t cross and the muddiness of the cells along that path. 
* Use BFS, A* and UCS search algorithms.
## Contact
If you want to contact me you can reach me at <nchakrav@usc.edu> or <nikhilchakravartula@gmail.com>.
