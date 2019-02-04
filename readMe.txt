Project:
Simulate Game of Life
    The Game of Life is set in an infinite two-dimensional grid inhabited by “cells”. Every cell interacts with up to
    eight neighbours, which are the cells that are horizontally, vertically, or diagonally adjacent.
    From an initial seed grid the game "evolves" one iteration at a time. An iteration applies rules to the grid to
    determine its next state. These scenarios are:
    1. No interaction:
        Given a game of life
        When there are no live cells
        Then on the next step there are still no live cells
    2. Underpopulation
       	Given a game of life
       	When a live cell has fewer than two neighbours
       	Then this cell dies
    3. Overcrowding
       	Given a game of life
       	When a live cell has more than three neighbours
       	Then this cell dies
    4. Survival
       	Given a game of life
       	When a live cell has two or three neighbours
       	Then this cell stays alive
    5. Creation of Life
       	Given a game of life
       	When an empty position has exactly three neighbouring cells
       	Then a cell is created in this position
Assumption:
    1. When game start, alive cells exist only on 1/3 centred output window.

Pre-requisites:
    JDK 8 -> Use as is
    For JDK 11, do following:
    1. Open website and download Java SDK w.r.t operating system.
            JavaFX https://gluonhq.com/products/javafx/
    2. Open IntelliJ IDEA
    3. Configure -> Project Defaults -> Project Structure -> Global Libraries
    4. Click "+", -> Java -> Select following:
        From above mentioned download, Unzip folder -> Java-SDk-11-> add all Jar files.
    5. Set a meaningful name. i.e. JavaFX-11
    6. Open project on IntelliJ
    7. Right click project -> Open Module Setting -> Project
        Make sure that Project SDK and Project Language level should be same.
    8. Select Global Libraries -> Right click JavaFX-11 -> Add to modules -> OK ->OK
    9. Open project -> right click src folder -> New -> module-info.java -> add following

       module GOL{
            requires javafx.fxml;
            requires javafx.controls;
            opens sample;
    }

Built with:
    1. IntelliJ IDEA
    2. JDK 8
    3. JavaFX SceneBuilder 2.0


Authors:
    Faiza Mazhar