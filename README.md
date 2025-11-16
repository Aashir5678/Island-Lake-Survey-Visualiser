# Island-Lake-Survey-Visualiser
I visualised one of my assignments using Java Swing.

Run `java Main.java` and press space to start the simulation. Press m to randomize the map.

## How islands and lakes are defined
Piece's of land are represented as 1. For two pieces of land to be part of the same island, they must touch at an adjacent side (left, right, top, bottom).

Water is represented by 0. A body of water contains 2 or more 0's touching at an adjacent side or edge

Lakes are bodies of water where every piece of water touching land is diagonally adjacent to either its own lake or land. In other words, a lake can't contain pieces of water which are diagonally
adjacent to a different body of water

## What the colors represent

Once the survey has started, it will look for water that is adjacent to other pieces of water. The blue tiles are water, green is land.
The blinking red tile and the other red tile indicate the two tiles that are being unioned together to form one island / body of water.
At the end of the survey, the solo islands are filled in dark green, and the solo lakes are filled dark blue


