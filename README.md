# Pacman

Pacman clone written in Java using javafx. This was a part of Java module at uni, where we had to develop something with graphics. Figured out Pacman
is a perfect candidate :) 

The whole game works as it was intended. Ghosts and Pacman are animated, you have magical dots, which allow you to eat ghosts for 10 seconds,
there are 2 different maps to choose from and the high score is being kept until the game is closed. Of course you can also pause or restart
the game and there is sound too, just for the sake of completeness. There is a .jar file included in the out folder if
you want to play the game, however as you know with the .jar files it could be dangerous so only open it if you trust the source 
(I'm pretty trustworthy!)

With the maps I've decided to go for a tile implementation. Which basically means you draw a pattern of 0123 letters in .txt file which is
later read by the program and puts the correct object in place of its letter. So, 0s are empty spaces, 1s are walls, 2s are normal dots
and 3s are magical dots. That way it's easy to implement new maps if one feels like.

The ghost are pretty stupid - as they hit the wall (or other ghost) they will randomly choose one of the directions (up, down, left, right),
so it's far from being true to the original version of the game. I simply didn't have time to implement the AI, but the framework is there
and this can be done easily, so if you feel like contributing you are more than welcome.

All the assets were drawn by me (feel free to use them as you please), while the sounds have been found online on some free-sounds website
if I remember correctly. 

## Screenshots
Here are some screenshots from the game, in case you are curious how the game looks like:

<img src="(https://github.com/matatusko/Pacman/blob/master/screenshots/pacman_screenshot_00.png" width="100" height="100">
![alt text](https://github.com/matatusko/Pacman/blob/master/screenshots/pacman_screenshot_00.png)
![alt text](https://github.com/matatusko/Pacman/blob/master/screenshots/pacman_screenshot_01.png){:height="50%" width="50%"}
![alt text](https://github.com/matatusko/Pacman/blob/master/screenshots/pacman_screenshot_02.png){:height="50%" width="50%"}
![alt text](https://github.com/matatusko/Pacman/blob/master/screenshots/pacman_screenshot_03.png){:height="50%" width="50%"}
