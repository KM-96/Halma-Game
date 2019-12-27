# Halma Game

## Overview

* In this project, we will play the game of Halma, an adversarial game with some similarities to
checkers. The game uses a 16x16 checkered gameboard. 
* Each player starts with 19 game pieces clustered in diagonally opposite corners of the board. To win the game, a player needs to
transfer all of their pieces from their starting corner to the opposite corner, into the positions
that were initially occupied by the opponent. 
* The agent uses MINIMAX algorithm with ALPHA-BETA pruning in order to generate the best possible move for a given board configuration.

## Setup for two players

Note: We only consider the two-player variant here.
* Simple wooden pawn-style playing pieces, often called "Halma pawns."
* The board consists of a grid of 16×16 squares.
* Each player's camp consists of a cluster of adjacent squares in one corner of the board.
* For two-player games, each player's camp is a cluster of 19 squares. The camps are in
opposite corners.
* Each player has a set of pieces in a distinct color, of the same number as squares in each
camp.
* The game starts with each player's camp filled by pieces of their own color.

For more details refer to [this](https://en.wikipedia.org/wiki/Halma).

## Playing with agents

In this project, our agent is designed to play against another agent or just perform a single move (possibly best) based on the current
configuration of the board

`Single move` <br/> 
* The agent will be given in input.txt a board configuration, a color to play,
and some number of seconds of allowed time to play one move. 
* The agent should return in output.txt the chosen move(s), before the given play time has expired.

`Play against reference agent`
* The agent is also designed to play full games against another agent. There will be a limited
total amount of play time available to for the whole game (e.g., 100 seconds).

`Agent vs agent games`

* A master game playing agent will be implemented by the grading team. This agent will:
  - Create the initial board setup according to the above description.
  - Randomly assign a player color (black or white) to your agent.
  - When playing against the reference minimax, you will get the opening move. Otherwise
who plays first will be chosen randomly.
  - Then, in sequence, until the game is over:
* The master game playing agent will create an input.txt file which lets your agent 
know the current board configuration, which color your agent should play, and
how much total play time your agent has left. 
* The playing time remaining will be updated by subtracting the time taken by your
agent on this move. If time left reaches zero or negative, your agent loses the
game.
* The validity of the move will be checked. If the format of output.txt is incorrect
or your move is invalid according to the rules of the game, your agent loses the
game.
* Your move will be executed by the master game playing agent. This will update
the game board to a new configuration.
* The master game playing agent will check for a game-over condition. If so, the
winning agent will be declared the winner of this game.
* The master game playing agent will then present the updated board to the
opponent agent and let that agent make one move.

`Moving pieces`

* Players cannot make a move that starts outside their own camp and causes one of their
pieces to end up in their own camp.
* If a player has at least one piece left in their own camp, they have to
  - Move a piece out of their camp (i.e. at the end of the whole move the piece ends up
outside of their camp).
  - If that’s not possible, move a piece in their camp further away from the corner of their
own camp ([0,0] or [15,15] respectively).
Only if the player does not have any pieces left in their camp or none of the two alternatives
above are possible are they free to move pieces outside of their camp.
Note: To move “further away”, you should simply move so that you either move further away
horizontally (while not moving closer vertically), or vertically (while not moving closer
horizontally), or both.

## Input and output file formats

`Input`

* First line: A string SINGLE or GAME to let you know whether you are playing a single move
(and can use all of the available time for it) of playing a full game with potentially
many moves (in which case you should strategically decide how to best allocate
your time across moves).
*Second line: A string BLACK or WHITE indicating which color you play. The colors will always be
organized on the board as follows:
(black starts in the top-left corner and white in the bottom-right).
*Third line: A strictly positive floating point number indicating the amount of total play time
remaining for your agent.
*Next 16 lines: Description of the game board, with 16 lines of 16 symbols each:
  - W for a grid cell occupied by a white piece
  - B for a grid cell occupied by a black piece
  - . (a dot) for an empty grid cell
  
`Output`
* 1 or more lines: Describing your move(s). 
* There are two possible types of moves:
  - E FROM_X,FROM_Y TO_X,TO_Y – your agent moves one of your pieces from location
FROM_X, FROM_Y to adjacent empty location TO_X, TO_Y. We will again use zero-based,
horizontal-first, start at the top-left indexing in the board, as in homework 1. So, location
0,0 is the top-left corner of the board; location 15,0 of the top-right corner; location 0,15
is the bottom-left corner, and location 15,15 the bottom-right corner. As explained above,
TO_X,TO_Y should be adjacent to FROM_X,FROM_Y (8-connected) and should be empty.
If you make such a move, you can only make one per turn.
  - J FROM_X,FROM_Y TO_X,TO_Y – your agent moves one of your pieces from location
FROM_X,FROM_Y to empty location TO_X,TO_Y by jumping over a piece in between. You
can make several such jumps using the same piece, as explained above, and should write
out one jump per line in output.txt.
* For example, output.txt may contain: E 11,15 10,15

## Prerequisites

Before you begin, ensure you have met the following requirements:

* You have installed the lastest minor version of java 1.8, both JRE and JDK. You can use [this](https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) for installation.

## Using the experiment

If you are using an IDE like IntelliJ or Eclipse, use the following steps to run the project
* Point the working directory to Logic-Programming-Agent
* Main class: LogicAgent
* JRE: 1.8
* Run the project

If you are running the project on command line, follow these steps:<br/>
* Clone/Download the project to your local machine.
* Navigate to the folder with LogicAgent.java file.
* Run the following commands:
```
javac HalmaGame.java
```

```
java HalmaGame
```

`Note:` Change the relative path to the input.txt file accordingly.


## Code Structure / File description

`/game/Agent.java`

* This is the core of the project where the actual minimax algorithm is implemented to search for the best board confirguration.

`/exceptions/FileException.java`

* Generic exception class for File related exceptions in this project.

`/model/Board.java`

* The Board model contains the current borad configuration (State), time remaining to make a move and the player who has to make the move.

`/model/State.java`

* The State class contains the positions where the white and black pawns are placed on the board, the value of the state and move which has lead to that state.

`/model/Move.java`

* Move class represents a move on the board. It contains the from and to location of the pawn being moved, the move type (Jump or Single) and the hopped positions in case of a jump move.

`/test/MasterAgent.java`

* This is a dummy master agent that runs a Halma game between two different agents.
* The other agents in the test folder are all dummy agents which are used to test the performance of the actual Agent that was developed.

## Contact

If you want to contact me you can reach me at <km69564@usc.edu> or <krishnamanoj14@gmail.com>
