This program was class project for Intro to Object-Oriented Programming. It is a simulation that, when run, creates a world inhabited by 'Dudes', 'Wyverns', and 'Fairies'.
Dudes and Wyverns can be spawned on click, where the odds are 50/50 for spawning either. The click 'corrupts' the world, changing the appearance, and spawning either a new Dude or Wyvern.
Dudes goal is to chop down trees and the Wyvern's goal is to 'kill' the Dudes.
Fairies goal is to revive the 'dead' Dudes and trees chopped down by Dudes.
The Dudes and Fairies use the A* pathing strategy to find their next position.
The Wyvern uses a Single-Step strategy to find its next position, to give the Dudes a chance to achieve their goal of cutting trees.
