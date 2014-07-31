DarkDestiny
===========

A RTS game written in Java


DarkDestiny is a real time strategy game writtem in pure Java without any third-party library except for a JSON parser. 
The game is finished wthin about 9 weeks by Team A1 of University of Birmingham Team Project Module.

Team members:Bowie, Han, Issa, Seffy

The game is based  on the background that a city is occupied by the horrible zombies and the survivors is trying to fight 
with zombies.

The game implements map rendering, sounds, networking, path finding and so on.

Currently, the game can be play in solo mode and operation mode and vesus mode. You can either text to you teammates 
or talk to them via voice chat.

Just download the game and have a look youself.



##How to compile and run the game
System requirement
1. Our game run stable under any os with Java 1.6_65  (Mac 10.9 Mavericks default comes with Java 1.6_65)
2. 1 GB free memory for JVM


Mac 10.9 Mavericks 10.9.0 or above
1. Import DarkDensity under /src to your IDE
     (It is suggested to use MyEclipse or Jetbrains Idea)

2. Change the JRE environment to Java 1.6_65 as there are some new bugs in 1.7

3. Allocate at least 1 GB memory to your JVM
     (i.e.: Add  “-Xms1024m -Xmx1024m“ to your JVM argument)

4. Click “Run” in your IDE, and choose the main class as com.darkdensity.main.Main
* Make sure that you are using Java 1.6_65 and sufficient memory is allocated to your JVM, If you are running on Java1.7, your screen may keep flashing and the control is not stable. This is a bug in Java 1.7 and old version Java
Insufficient memory may cause the game crashed.

For other OS
It is suggested to run our game under Java 1.6_65 environment. We have done some testing on 1.6_45 , 1.7 and 1.8.  However it is not guaranteed that our game runs stable in these version of Java as there are some bugs in Swing and Graphics for those version and cannot be fixed in code level. 
After getting Java 1.6_65, follows the instructions with Mac 10.9 Mavericks 10.9.0 or above

Game Screenshots:
##Game lobby
![Game lobby](https://raw.githubusercontent.com/Han-Jiang/DarkDestiny/master/README/game%20lobby.jpg)   
##Mutiple palyer panel
![Game lobby](https://raw.githubusercontent.com/Han-Jiang/DarkDestiny/master/README/MultiPlayer.jpg)  
##Loading
![](https://raw.githubusercontent.com/Han-Jiang/DarkDestiny/master/README/loading%202.jpg)

##In the game
![In the game](https://raw.githubusercontent.com/Han-Jiang/DarkDestiny/master/README/In%20the%20game.jpg
 "In the game")
![](https://raw.githubusercontent.com/Han-Jiang/DarkDestiny/master/README/chatting.jpg)  
## Zombie Win
![](https://raw.githubusercontent.com/Han-Jiang/DarkDestiny/master/README/Zombie%20Win.jpg)  



To do:
Game UI Refine

2014/07/06 Han Jiang  



