<div align="center">
  <h1 style="font-family: 'Helvetica', sans-serif;"><span style="color:#AAFF00;">Documentation: Stick Hero</span></h1>
</div>

<div align="center">
  <h6 style="font-family: 'Helvetica', sans-serif;"> © Dhruv Prakash and Abhishek Jha</h6>
</div>

***

#

Stick Hero is a game built using JavaFX, where the player controls the hero that weilds a stick. The objective is to extend the stick to bridge the gap between two platforms.

## How to Play
1. Press MOUSERIGHTCLICK to elongate the stick.
2. Release MOUSERIGHTCLICK to drop the stick.

> Starting file: Controller.java




## Code Structure
1. We have used Singleton Design pattern to only have one stick object.
2. Inheritance has been implemented on the Platform class which inherits Rectangle class.
3. the gamePlay class is the main controller class handling game logic and UI interactions.

The code also successfully handles exceptions where exceptions were possible to be thrown. We also deployed JUnit Testing to test the working of singleton class and the working of getters, setters and various helper functions.


