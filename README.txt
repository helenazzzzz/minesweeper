=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: hezhangg
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2d Arrays - A 2d arrays was used to store each grid on the minefield. A grid in a certain row
   and column on the minefield is placed in the corresponding row and column in the array. One 
   feature in minesweeper involves searching the adjacent grids of a grid; the array allowed 
   easy searching since adjacent grids would be in the same or adjacent rows and columns. 

  2. File I/O - File I/O was used to read and write the high scores. Since the leaderboard can
    change when more games are played, it's better to store the top scores outside of the Game 
    class in a file. 

  3. Inheritance - Inheritance was used for the different grids that appears onscreen. MineGrid
    and SafeGrid extends the abstract Grid class because they share similar features like the
    flag method and storing coordinates, and differ in how they react to being revealed. 

  4. Recursion - Recursion was used to search through adjacent grids and the adjacent grids'
    adjacent grids and so on. DPS was implemented to search in one direction and then the others.
    

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

Game - Class containing the JFrame and other GUI swing components.

GameBoard - Class containing the minesweeper game logic and Grids

Grid - Abstract Class containing some grid functionalities

MineGrid - Class containing functionalities for a grid containing a mine

SafeGrid - Class containing functionalities for a grid that is safe

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

The implementation for calculating the numbers for SafeGrid and recursion were difficult. 

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

If given the chance, I may split up functionalities in the GameBoard class into more classes,
such as a separate minesweeper class for the game logic. 

========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
