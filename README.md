# Predator Prey Simulation
### Classes:
***
#### World Class
The  ```World``` Class contains the information of the current state of a simulation, such as how many predators and prey are currently alive in the simulation at one timeframe, as well as where each of the predators and prey are located within the world relative to each other. Several methods in this class check whether a predator or prey is in proximity with each other, and performs other methods as a result of those conditions. 

Important class variables:

* ```int weight, int height```: dimensions of the world.

* ```ArrayList<predator> predList``` holds all the living predator objects in the world at a given timestep.

* ```ArrayList<prey> preyList``` holds all the living prey objects in the world at a given timestep.

* ```Animal worldMap[][]``` is a 2D array that stores animal objects, such as a predator or prey. If a predator is at (80, 30) in the simulation screen, their position in the array is at  ```worldMap[30][80]```. This is because the rows and columns of the canvas for the simulation is transposed with the rows and columns of the 2D array. 

* ```DotPanel dp```: canvas simulation and the world is drawn on.

Methods in this class include:

* ```populate(int x, int y)```: this generates the prey and predator objects in the world and places them on a 2D array based on each of their positions. 

* ```update()```: at each timestep, each of the predators and prey will perform a series of actions:
                 * Predators will move and have a chance to reproduce or die. 
                 * After each of the living predators move, they will eat any prey that's adjacent to them. 
                 * Next, it's then each of the prey's turn to move and have the chance to reproduce or die.
                 * After each of the living preys move, each of the living predators will have a second chance to eat any adjacent prey (makes the simulation more interesting!). 
                 * Both predators and preys will scan, 15 and 10 squares respectively, around them in four directions which will influence their next movement and direction in the next timestep.
                 * Finally, there will be a chance for each of the predators and prey to randomly change their movement direction before the next timestep, assuming that a predator isn't on the hunt chasing a prey, as well as assuming that the prey isn't being chased by a predator and is safe to change movement directions. 

*  ```draw()```: draws each of the invidiual predators and prey after each of them moves, eats, reproduces, etc. 

*  ```predScanAround(int i, int j)```: traverses through the worldMap array which holds each of the predator and prey objects based on their x and y location, and calls ```SquaresRight()```, ```SquaresLeft()```, ```SquaresUp()```, and ```SquaresDown()```, each of which checks if there is a prey object 15 squares away from a predator inside the 2D array. For example, if we wanted to check whether there was a pray object 15 squares left  from a predator object whose location in the 2D array is (50, 30), we would first call ```preyScanAround(50, 30)```, which then calls ```SquaresLeft(50, 30, 15, "pred", "prey")```, where "pred" is spectating or seeing if a "prey" is 15 squares to its left. 

* ```preyScanAround(int i, int j)```: similar to ```predScanAround()``` but checks if there is a predator object 10 squares away from a prey after calling each of directional squares methods. If any of the conditions are true, that prey will move in the opposite direaction (there's a possibility it will be stuck at the boundaries of the simulation since it can't run anywhere else). For example, if we wanted the prey to scan for any predators 10 squares above itself at location ```(50, 30)``` in the 2D array,  ```preyScanAround()``` will call  ```SquaresUp(50, 30, 10, "prey", "pred")```.

* ```SquaresDown/Up/Left/Right(int x, int y, int numSquares, String spectator, String observe)``` determines whether there is another animal object, the observe,  ```numSquares``` away from ```worldMap[i][j]``` the spectator. Down/Up/Left/Right is the direction spectator is looking at. 

* ```predsEat(int i, int j)```: checks that an animial object in ```worldMap[][]``` is a predator and calls ```adjacent(int i, int j, String direction)``` to check if a prey object is located 1 square to its right, left, above, or below it within the 2D array. Before ```adjacent``` is called, there are multiple conditions that check where a predator is located within the simulation. For example, at the top most left corner, a predator may only check 1 square right or below of it. If any of these conditions are true, that particular prey is removed from the simulation, which is done by calling ```getEaten()``` from the prey class. 

* ```adjacent(int i, int j, String direction)```: checks that a predator object at position i,j in ```worldMap[][]``` is adjacted to a prey object, either 1 square above, below, left, or right of it. If a prey object is adjacent to it, that prey object's ```getEaten()``` method is called on itself, which removes itself from the  ```preyList``` arraylist. ***Additionally it checks whether the adjacent prey's color is the same color as the canvas background***.

* ```OnClickPrey(int x, int y)```: a method called from the ```PPSim``` class whenever a user clicks on the canvas/screen. It initializes a new prey object, and takes the  ```x``` and ```y``` coordinates from wherever the mouse clicks the screen. A random color is then assigned to that prey.

* ```onPressedPred()```: a method called from the ```PPSim``` class whenever a user presses 'p' on the keyboard, which produces a new predator on the screen. **This is also an additional UI feature of the simulation**.

***

#### Animal class
The ```Animal``` class is the parent class of both ```predator``` and ```prey```. This is an abstract class with a list of abstract methods. 

Important class variables:

* ```x_point```, ```y_point```, is the geographic location of where the animal object is located within the canvas/simulation. It's location in ```worldMap[][]``` is initialized as ```worldMap[y_point][x_point]``` since the 2D array's rows and columns are transposed with the rows and columns of the canvas itself, so this takes account of the issue. 

* ```int moveDirection``` is the current direction that each animal object is moving towards to. If ```moveDirection``` is ```0```, it is moving up; if ```1```, it is moving down; if ```2```, it is moving left; if ```3```, it is moving right. 

* ```String type```: an animal object has either type ```"pred"``` or ```"prey"``` and this is used so we can traverse ```worldMap[][]``` and check whether ```worldMap[i][j]``` is a predator or a prey type animal object. 

* ```color```: each animal object has a color for which it displays on the canvas. All predators are red, while prey can be any color. 

* ```Boolean chasing```: think of this as an animal's survival mode is "turned on". If a predator sees a prey, it will want to eat that prey, which turns on that predator's survival mode instinct. Likewise, if a prey sees a predator, it would want to run away from it. Whenever either a predator or prey undergoes this experience, it will chase towards or away from the other animal. 

Abstract methods in this class include:

* ```move()```: increments or decrements either the ```x_point``` or ```y_point``` of an animal. 

* ```changeDirection()``` Randomly changes the direction of a predator or prey. 

* ```reproduce()```: creates a new predator (or prey) object. 

* ```die()```: Removes the animal from its corresponding arraylist and the simulation itself. 

* ```drawAnimal(DotPanel dotpanel)```: draws the predator or prey on the canvas when running the simulation. 

***

### Predator Class

The ```predator``` class contains information for each predator object created throughout the duration of a simulation. It is a child class of the ```Animal``` class and overrides its abstract methods. 

Class variables inherited from ```Animal```:

* ```x_point``` and ```y_point``` are randomly assigned when ```populate()``` is called. 

* ```moveDirection``` is randomly assigned using the ```nextInt(int max)``` function from the ```Helper``` class. 

* ```type```  is always  ```"pred"``` for all predator objects created.

* ```color```  is always  ```Color.RED``` for all predator objects 
created. 

* ```chasing``` is set to false when a predator is initialized. It is only changed to ```true``` when a predator sees a prey 15 squares away from it in any direction (and is turned to ```false``` when there is no prey in sight).

Overriden methods:

* ```move()```: this controls how the predator moves, either through incrementing or decrementing its ```x_point``` or ```y_point``` attributes, which then changes its position in ```worldMap[][]```. It first checks whether a predator is by the right most boundary, left most boundary, top most boundary, and bottom most boundary; if it is ever located in any of these areas, its ```moveDirection``` attribute is changed to the opposite direction. *For example*, if a predator is at ```99,10``` (assuming the size of the canvas is 100x100), then the predator is at the right-most of the canvas, but this translates to a poistion in the 2D array as ```worldMap[10][99]```. Since the predator is at the right-most of both the canvas and the 2D array, we would want the predator to move left in order for it to possibly leave the boundaries of the simulation. Thus, its ```moveDirection``` will change to ```2```, indicting to move left. Then, whatever a predator's current value for ```moveDirection``` is, either its ```x_point``` or ```y_point``` is incremented or decremented. 

* ```changeDirection()```: this changes a predator's ```moveDirection``` attribute, **only if a predator's  ```chasing```   attribute is set to false**. Otherwise, there is a 5% chance of a predator changing directions, which is randomly selected using   ```Helper.nextInt(4)```. 

* ```reproduce()```: First iterate through  ```predList``` and there is a 1.35% chance a predator reproduces. Furthermore, each predator randomly produces a new amount of predator offsprings (up to 4). This means that, at each timestep and a predator has the opportunity to reproduce, it could try producing maximum 3 new predator offsprings, or none at all. 

* ```die()```: First iterate through  ```predList``` and there is a 1.1% chance a predator dies at each timestep. Whatever it's current position in ```worldMap[][]``` is set to ```null``` and the object is removed from ```predList```. 

* ```drawAnimal(DotPanel dotPanel)```: draws each of the predators in ```predList``` after iterating through it on the canvas. It is drawn as a red square. 

*** 

### Prey Class 

The ```Prey``` class contains information for each prey object throughout the duration of a simulation. It is a child class of the ```Animal``` class and overrides its abstract methods.

Class variables inherited from ```Animal```:

* ```x_point``` and ```y_point``` are randomly assigned when ```populate()``` is called. 

* ```moveDirection``` is randomly assigned using the ```nextInt(int max)``` function from the ```Helper``` class. 

* ```type```  is always  ```"prey"``` for all prey objects created.

* ```color``` is randomly selected for all predator objects created, which is done by using  ```Helper.newRandomColor()```. 

* ```chasing``` is set to false when an individual prey is initialized. It is only changed to ```true``` whenever a prey object sees another predator 10 squares away from it in any direction (and is turned to ```false``` when there is no predator in sight).

Overriden methods:

* ```move()```: this controls how the prey moves, either through incrementing or decrementing its ```x_point``` or ```y_point``` attributes, which then changes its position in ```worldMap[][]```. It first checks whether a prey is by the right most boundary, left most boundary, top most boundary, and bottom most boundary; if it is ever located in any of these areas, its ```moveDirection``` attribute is changed to the opposite direction. *For example*, if a prey is at ```99,10``` (assuming the size of the canvas is 100x100), then the prey is at the right-most of the canvas, but this translates to a poistion in the 2D array as ```worldMap[10][99]```. Since the prey is at the right-most of both the canvas and the 2D array, we would want the prey to move left in order for it to possibly leave the boundaries of the simulation. Thus, its ```moveDirection``` will change to ```2```, indicting to move left. Then, whatever a prey's current value for ```moveDirection``` is, either its ```x_point``` or ```y_point``` is incremented or decremented. 

* ```changeDirection()```: this changes a prey's ```moveDirection``` attribute, **only if a prey's  ```chasing```   attribute is set to false**. Otherwise, there is a 5% chance of a prey changing directions, which is randomly selected using   ```Helper.nextInt(4)```. 

* ```reproduce()```: First iterate through  ```preyList``` and there is a 2% chance a prey reproduces. Furthermore, each prey produces only 1 offspring. There is also a 1% chance that the color of the offspring will be different than its parent's color.  

* ```die()```: First iterate through  ```preyList``` and there is a 1.35% chance a prey dies at each timestep. It's current position in ```worldMap[][]``` is set to ```null``` and the object is removed from ```preyList```. 

* ```drawAnimal(DotPanel dotPanel)```: draws each of the prey in ```preyList``` after iterating through it and drawing the prey on the canvas. It is drawn as a circle and its color is randomly selected through ```Helper.nextRandomColor()```.

Other methods:

* ```getEaten()```: called when a prey is adjacent to another predator object by 1 square (either to its left, right, above, or below it). It is removed from the ```worldMap[][]``` array based on its location within the simulation. It is then removed from ```preyList```.

***

### PPSim Class

The ```PPSim``` class allows the simulation to be drawn on the canvas, as well as including the UI features of the simulation. For implementing the additional UI functions, we included the methods that were needed to override the methods found from the imported packages. 

For Keyboard events:
* ```keyTyped()```
* ```keyPressed()```
* ```keyReleased()```

For click events:
* ```mouseClicked()```
* ```mousePressed()```
* ```mouseReleased()```
* ```mouseEntered()```
* ```mouseExited()```
* ```mouseDragged()```
* ```mouseMoved()```


### Additional features:
Refer to the ```onPressedPred()``` method found in the ```World``` class. This extra UI feature is added to make predators seem more aggressive such that prey will have a higher chance of being eaten (up to the user of course, but it's honestly more fun when predators eat the prey!)

Additionally, a new rule was implemented such that prey can die naturally, just like predators

Also modified: a prey has a 3% chance of reproducing. 