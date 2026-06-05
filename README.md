# Stick Hero
This is my implementation of the popular mobile game Stick Hero by Ketchapp. The purpose of this project was to strengthen my foundations using Object Oriented Programming principles.

![Gameplay Demo](assets/stickhero.gif)

The game challenges players to extend a stick of appropriate length to bridge gaps between platforms, collect avocados and acheive the highest possible score.

## Features
- This project efficiently uses OOPs principles such as Encapsulation, Inheritance, Abstraction and Polymorphism to optimize the functioning of the game.
- What is different from the original game? Players can actually track their highest scores via the Leaderboard.
- This project also implements the Singleton Design Pattern as the game requires a single shared object that persists across scene transitions and stores global game data.
- It also includes automated unit tests using JUnit.

## Running the Game
### Clone Repository
```
git clone https://github.com/14Dhruv04/Stick-Hero.git
cd Stick-Hero
```
### Build the Project
```
mvn clean package
```
### Initiate the Game
```
mvn clean javafx:run
```

## Game Instructions
1. Hold **SPACE** to grow the stick.
2. Release **SPACE** to drop the stick. If the length of the stick is enough to reach the next platform, the hero will succesfully land on the next platform else, the hero will fall. 
3. Press **SPACE** to flip the hero while moving in order to collect Avocados.

## Future Improvements
- Add music and implement Sound On/Off option in the Menu.

## Download
Download and extract the attached ZIP file and run: `Stick-Hero.exe`
No separate Java installation is required.

Have a go at it!
