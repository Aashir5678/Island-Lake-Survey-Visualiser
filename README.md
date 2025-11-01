# Island-Lake-Survey-Visualiser
I visualised one of my assignments using Java Swing.

Run `java Main.java.` and press space to start the simulation. Press m to randomize the map.

## How islands and lakes are defined
Piece's of land are represented as 1. For two pieces of land to be part of the same island, they must touch at an adjacent side (left, right, top, bottom).

Water is represented by 0. A body of water contains 2 or more 0's touching at an adjacent side or edge

Lakes are bodies of water encompassed on all 4 sides and 4 corners by land.

