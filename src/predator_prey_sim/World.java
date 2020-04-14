package predator_prey_sim;

import util.Helper;

import java.awt.*;
import java.util.ArrayList;
import util.DotPanel;
import java.awt.Color;

public class World {

	private int width, height;
	public Color canavasColor;
	// Holds all the predator objects in the world
	ArrayList<predator> predList = new ArrayList<predator>();
	// Holds all the prey objects in the world
	ArrayList<prey> preyList = new ArrayList<prey>();
	// array that holds predator and prey objects based on their location
	Animal [][] worldMap; 
	DotPanel dp;
	/**
	 * Create a new City and fill it with buildings and people.
	 * @param w width of city
	 * @param h height of city
	 * @param numPrey number of prey
	 * @param numPredator number of predators
	 */
	public World(int w, int h, int numPrey, int numPredator) {
		width = w;
		height = h;
		canavasColor = Helper.newRandColor();
		 worldMap = new Animal[h][w];
		// Add Prey and Predators to the world.
		populate(numPrey, numPredator);
	}


	/**
	 * Generates numPrey random prey and numPredator random predators 
	 * distributed throughout the world.
	 * Prey must not be placed outside canavas!
	 *
	 * @param numPrey the number of prey to generate
	 * @param numPredator the number of predators to generate
	 */
	private void populate(int numPrey, int numPredators)
	{
		// Generates numPrey prey and numPredator predators 
		// randomly placed around the world.

		// This loop creates new predator objects, all of which are
		// added in predList
		int i = 0;
		while(i < numPredators){
			int x = Helper.nextInt(width);
			int y = Helper.nextInt(height);
			predator pred = new predator(x, y); // x,y location ON SCREEN
			worldMap[y][x] = pred; // y and x are transposed 
			predList.add(pred); // add each newly initialized predator to predList
			i++;
		}
		// This loop creates new prey objects, all of which are
		// added in predList
		int j = 0;
		while(j < numPrey){
			int x = Helper.nextInt(width);
			int y = Helper.nextInt(height);
			prey prey = new prey(x, y, Helper.newRandColor()); // each prey has a random color
			worldMap[y][x] = prey; // y and x are transposed 
			preyList.add(prey); // add each newly initialized prey to preyList
			j++;
		}
		
	}
	
	/**
	 * Updates the state of the world for a time step.
	 */
	public void update() {
		// Move predators, prey, etc

		// At each time step, each predator in predList 
		// will move to another square, with the possibility of 
		// changing directions, reproducing, or dying
		for(int i = 0; i< predList.size();i++){
			predList.get(i).move();
			predList.get(i).reproduce();
			predList.get(i).die();
		}

		// After a predator moves, it will eat any adjacent prey
		for(int i = 0; i < predList.size(); i++){
			predsEat(predList.get(i).y_point, predList.get(i).x_point); // (y,x) is its location in the 2D array
		}
		
		// Each prey has a chance to move, reproduce, or die
		for(int i = 0; i< preyList.size();i++){
			preyList.get(i).move();
			preyList.get(i).reproduce();
			preyList.get(i).die();
		}

		// After each prey moves, predators get a second opportunity to eat prey that moved
		for(int i = 0; i < predList.size(); i++){
			predsEat(predList.get(i).y_point, predList.get(i).x_point);
		}

		// Each predator scans 15 squares left,right,top,below it to see if there's any
		// prey; this will make sure those predators near a prey do NOT 
		// randomly change in direction when they move again. 
		for(int i = 0; i < predList.size(); i++){
			predScanAround(predList.get(i).y_point, predList.get(i).x_point);
		}

		// Each prey will scan 10 squares left,right,top,below it to see if there's any
		// predators; this will make sure those prey near a pre do NOT 
		// randomly change in direction when they move again. 
		for(int i = 0; i < preyList.size(); i++){
			preyScanAround(preyList.get(i).y_point, preyList.get(i).x_point);
		}

		// both predators and prey will can change the direction they face before
		// they move again (assuming there is no prey nearby in the case for predators,
		// as well as assuming there is no predators nearyby in the case for prey).
		for(int i = 0; i < predList.size(); i++){
			predList.get(i).changeDirection();
		}
		for(int i = 0; i < preyList.size(); i++){
			preyList.get(i).changeDirection();
		}

	}

	/**
	 * Draw all the predators and prey.
	 */
	public void draw(DotPanel canvas){ // takes the canvas the simulation is drawing on
		/* Clear the screen */
		PPSim.dp.clear(canavasColor);
		
		// Draw predators and pray
		for(int i = 0; i< predList.size();i++){
			predList.get(i).drawAnimal(canvas);
		}
		
		for(int i = 0; i< preyList.size();i++){
			preyList.get(i).drawAnimal(canvas);
		}
	}


	/** EXAMPLES ARE INCLUDED IN THE DOCUMENT DESIGN ON README.md
	 * 
	 * checks whether squares above an animal in its corresponding location
	 * in worldMap[][] contains any animal specified by observe. If any matches occur,
	 * the method returns true; otherwise, none of the squares match observe and the method
	 * returns false
	 * 
	 * NOTE: spectator and observe can either only be "pred" or "prey"
	 * @param x position x in the 2D array (an animal's y coordinate in the screen)
	 * @param y position y in the 2D array (an animal's x coordinate in the screen)
	 * @param numSquares how many number of squares to loop over to check if animal is there
	 * @param spectator which animal type is doing the searching
	 * @param observe which animal the spectator is trying to find
	 */

	public boolean SquaresRight(int x, int y, int numSquares, String spectator, String observe){
		// check that any of the numSquares right of worldMap[x][y]
		// is not null, where its type matches the string observe,
		// AND if, within the 2D array, x is at least numSquares squares away from the rightmost border
		// which is the width - 1 column row (y = width-1). 
		// -> we don't want to go out of bounds when checking
		if(worldMap[x][y] != null && worldMap[x][y].type == spectator && (width-1- y >= numSquares)){
			for(int k = 1; k <= numSquares; k++){
				// [y + k] is number of columns after [x][y]
				if(worldMap[x][y + k] != null && worldMap[x][y + k].type == observe){
					// if any of the squares follows the condition, return true
					return true;
				}	
			}
			// When the # of squares right of it does not contain any animals that match
			// observe
			return false;
		}
		// Go here if y is less than numSquares squares away from the rightmost border, and that 
		// worldMap[x][y] has an animal and its type matches the spectator.
		else if (worldMap[x][y] != null && worldMap[x][y].type == spectator) {
			int remDist = width-1- y; // remaining squares until right-border is reached
			for(int k = 1; k <= remDist; k++){
				if(worldMap[x][y + k] != null && worldMap[x][y + k].type == observe){
					return true;
				}
			}
			// When the # of squares on its right does not contain any animals that match
			// observe
			return false;
		}
		return false;
	}

	/**
	 * checks whether squares LEFT of an animal in its corresponding location
	 * in worldMap[][] contains any animal specified by observe. If any matches occur,
	 * the method returns true; otherwise, none of the squares match observe and the method
	 * returns false
	 * 
	 * NOTE: spectator and observe can either only be "pred" or "prey"
	 * @param x position x in the 2D array (an animal's y coordinate in the screen)
	 * @param y position y in the 2D array (an animal's x coordinate in the screen)
	 * @param numSquares how many number of squares to loop over to check if animal is there
	 * @param spectator which animal type is doing the searching
	 * @param observe which animal the spectator is trying to find
	 */

	public boolean SquaresLeft(int x, int y, int numSquares, String spectator, String observe){
		// check that any of the numSquares left of worldMap[x][y]
		// is not null, where its type matches the string observe,
		// AND if, within the 2D array, x is at least numSquares squares away from left-most border
		// which is the 0th column (y  = 0). -> we don't want to go out of bounds when checking
		if(worldMap[x][y] != null && worldMap[x][y].type == spectator && (y >= numSquares)){
			for(int k = 1; k <= numSquares; k++){
				// [y - k] is number of columbs before [x][y]
				if(worldMap[x][y - k] != null && worldMap[x][y - k].type == observe){
					// if any of the squares follows the condition, return true
					return true;
				}	
			}
			// When the # of squares left does not contain any animals that match
			// observe
			return false;
		}
		// Go here if y is less than numSquares squares from the leftmost border, and that 
		// worldMap[x][y] has an animal and its type matches the spectator. 
		else if (worldMap[x][y] != null && worldMap[x][y].type == spectator) {
			int remDist = y; // remaining squares until left-border is reached
			for(int k = 1; k <= remDist; k++){
				if(worldMap[x][y - k] != null && worldMap[x][y - k].type == observe){
					return true;
				}
			}
			// When the # of squares on its left does not contain any animals that match
			// observe
			return false;
		}
		return false;
	}
	
	
	/**
	 * checks whether squares above an animal in its corresponding location
	 * in worldMap[][] contains any animal specified by observe. If any matches occur,
	 * the method returns true; otherwise, none of the squares match observe and the method
	 * returns false
	 * 
	 * NOTE: spectator and observe can either only be "pred" or "prey"
	 * @param x position x in the 2D array (an animal's y coordinate in the screen)
	 * @param y position y in the 2D array (an animal's x coordinate in the screen)
	 * @param numSquares how many number of squares to loop over to check if animal is there
	 * @param spectator which animal type is doing the searching
	 * @param observe which animal the spectator is trying to find
	 */
	
	public boolean SquaresUp(int x, int y, int numSquares, String spectator, String observe){
		// check that any of the numSquares above worldMap[x][y]
		// is not null, where its type matches the string observe,
		// AND if, within the 2D array, x is at least numSquares squares away from top border
		// which is the 0th row (x  = 0). -> we don't want to go out of bounds when checking
		if(worldMap[x][y] != null && worldMap[x][y].type == spectator && (x >= numSquares)){
			for(int k = 1; k <= numSquares; k++){
				// [x - k] is number of rows above [x][y]
				if(worldMap[x - k][y] != null && worldMap[x - k][y].type == observe){
					// if any of the squares follows the condition, return true
					return true;
				}	
			}
			// When the # of squares above does not contain any animals that match
			// observe
			return false;
		}
		// Go here if x is less than numSquares squares from the top border, and that 
		// worldMap[x][y] has an animal and its type matches the spectator. 
		else if (worldMap[x][y] != null && worldMap[x][y].type == spectator) {
			int remDist = x; // remaining squares until top border is reached
			for(int k = 1; k <= remDist; k++){
				if(worldMap[x - k][y] != null && worldMap[x - k][y].type == observe){
					return true;
				}
			}
			// When the # of squares above does not contain any animals that match
			// observe
			return false;
		}
		return false;
	}

	/**
	 * checks whether squares below an animal in its corresponding location
	 * in worldMap[][] contains any animal specified by observe. If any matches occur,
	 * the method returns true; otherwise, none of the squares match observe and the method
	 * returns false
	 * 
	 * NOTE: spectator and observe can either only be "pred" or "prey"
	 * @param x position x in the 2D array (an animal's y coordinate in the screen)
	 * @param y position y in the 2D array (an animal's x coordinate in the screen)
	 * @param numSquares how many number of squares to loop over to check if animal is there
	 * @param spectator which animal type is doing the searching
	 * @param observe which animal the spectator is trying to find
	 */

	public boolean SquaresDown(int x, int y, int numSquares, String spectator, String observe){
		// check that any of the numSquares below worldMap[x][y]
		// is not null, where its type matches the string observe,
		// AND if, within the 2D array, x is at least numSquares squares away from the bottom border
		// which is the 0th row (x  = 0). -> we don't want to go out of bounds when checking
		if(worldMap[x][y] != null && worldMap[x][y].type == spectator && (height - 1 - x >= numSquares)){
			for(int k = 1; k <= numSquares; k++){
				// [x + k] is number of rows below [x][y]
				if(worldMap[x + k][y] != null && worldMap[x + k][y].type == observe){
					return true;
				}	
			}

			// When the # of squares below does not contain any animals that match
			// observe
			return false;
		}
		// Go here if x is less than numSquares squares away from the bottom border 
		// of the 2D array, and that worldMap[x][y] has an animal 
		// and its type matches the spectator. 
		else if (worldMap[x][y] != null && worldMap[x][y].type == spectator) {
			int remDist = height - 1 - x; // remaining squares until bottom border is reached
			for(int k = 1; k <= remDist; k++){
				if(worldMap[x + k][y] != null && worldMap[x + k][y].type == observe){
					return true;
				}
			}
			// When # remDist squares below does not contain any animals that match
			// observe
			return false;
		}
		return false;
	}

	/**
	 * checks whether there is a prey nearby for a predator object located by worldMap[i][j]
	 * The spectator is the "pred" who is checking for "prey" (the observe)
	 * @param x position x in the 2D array (an animal's y coordinate in the screen)
	 * @param y position y in the 2D array (an animal's x coordinate in the screen)
	 */
	public void predScanAround(int i , int j){
		// checking that the object is type "pred" 
		if(worldMap[i][j] != null && worldMap[i][j].type == "pred"){
			// set predator's chasing attribute to false if no prey is 
			// located  in either 4 directions	
			worldMap[i][j].chasing = false; 
			// if there are any prey within 15 squares right of the predator
			if(SquaresRight(i, j, 15, "pred", "prey")){
				worldMap[i][j].moveDirection = 3;
				worldMap[i][j].chasing = true;
			}
			// if there are any prey within 15 squares left of the predator
			if(SquaresLeft(i, j, 15, "pred", "prey")){
				worldMap[i][j].moveDirection = 2;
				worldMap[i][j].chasing = true;
			}
			// if there are any prey within 15 squares above the predator
			if(SquaresUp(i, j, 15, "pred", "prey")){
				worldMap[i][j].moveDirection = 0;
				worldMap[i][j].chasing = true;
			}
			// if there are any prey within 15 squares below the predator
			if(SquaresDown(i, j, 15, "pred", "prey")){
				worldMap[i][j].moveDirection = 1;
				worldMap[i][j].chasing = true;
			}	
		}
	}

	/**
	 * checks whether there is a predator nearby for a prey object located by worldMap[i][j]
	 * The spectator is the "prey" who is checking for "pred" (the observe)
	 * @param x position x in the 2D array (an animal's y coordinate in the screen)
	 * @param y position y in the 2D array (an animal's x coordinate in the screen)
	 */
	public void preyScanAround(int i, int j){
		// checking that the object is type "prey" 
		if(worldMap[i][j] != null && worldMap[i][j].type =="prey"){
			// set prey's chasing attribute to false if no predator is 
			// located  in either 4 directions
			worldMap[i][j].chasing = false;
			// if there are any predators within 10 squares right of the predator
			if(SquaresRight(i, j, 10, "prey", "pred")){
				worldMap[i][j].moveDirection = 2; //move left
				// make sure it's not by left border
				if(j > 0 && worldMap[i][j].type == "prey"){ 
					// move additional step 
					worldMap[i][j].move();
				}
			}
			// if there are any predators within 10 squares left of the predator
			if(SquaresLeft(i, j, 10, "prey", "pred")){
				worldMap[i][j].moveDirection = 3; //move right
				// make sure it's not by the right border
				if(j < width-1 && worldMap[i][j].type == "prey"){
					// move additional step
					worldMap[i][j].move();
				}
			}
			// if there are any predators within 10 squares above of the predator
			if(SquaresUp(i, j, 10, "prey", "pred")){
				worldMap[i][j].moveDirection = 1; // move down
				// make sure it's not by the bottom border
				if(i < height - 1 && worldMap[i][j].type == "prey"){
					// move additonal step
					worldMap[i][j].move();
				}
			}
			// if there are any predators within 10 squares below of the predator
			if(SquaresDown(i, j, 10, "prey", "pred")){
				worldMap[i][j].moveDirection = 0; // move up
				//make sure it's not by the top border
				if(i > 0 && worldMap[i][j].type == "prey"){
					// move additional step
					worldMap[i][j].move();
				}
			}
		}
	}

	/**
	 * First checks where the predator is located within the 2D array, in order to properly
	 * determine which of its adjacent squares should be checked for neighboring prey.
	 * We do this in case a predator is by the boundary of the world, it can only check
	 * certain sides versus when it's completely in the middle to prevent out-of-bounds errors.
	 * @param x position x in the 2D array (the clicked y coordinate in the screen)
	 * @param y position y in the 2D array (the clicked x coordinate in the screen)
	 */
	public void predsEat(int i , int j){
		// check that at position i,j, there is an animal of type "pred"
		if(worldMap[i][j] != null && worldMap[i][j].type =="pred")
		// if predator is on the top-most border of the world
		if(i == 0){		
			// if at the top-left corner
			if(j == 0){
				//look down, right
				adjacent(i, j, "down");		
				adjacent(i, j, "right");
			}
			// if at the top-right corner		
			else if(j == width-1){
				//look left, down
				adjacent(i, j, "left");
				adjacent(i, j, "down");
			}
			// somewhere between the two corners (but still in top-border)
			else{
				//look left down right
				adjacent(i, j, "left");
				adjacent(i, j, "down");
				adjacent(i, j, "right");
			}
		}
		// if predator is on the left-most border of the world
		else if (j == 0){
			// if at bottom-left corner
			if(i == height - 1){
				//look up, right
				adjacent(i, j, "right");
			}
			// somewhere along the left border
			else{
				//look up, right, down
				adjacent(i, j, "up");
				adjacent(i, j, "right");
				adjacent(i, j, "down");
			}
		}
		// if predator is at the bottom border
		else if(i == height - 1){
			// if at bottom-right corner
			if(j == width - 1){
				//look up, left
				adjacent(i, j, "left");
				adjacent(i, j, "up");
			}
			// if somewhere along the bottom border
			else{
				//look left, right, up
				adjacent(i, j, "left");
				adjacent(i, j, "right");
				adjacent(i, j, "up");
			}
		}
		// if somewhere along the right-most border
		else if(j == width -1){
			// look up, down, left
			adjacent(i, j, "up");
			adjacent(i, j, "down");
			adjacent(i, j, "left");
		}
		// if predator is NOT constrained any border and corner
		else{
			// look at all 4 directions
			adjacent(i, j, "up");
			adjacent(i, j, "down");
			adjacent(i, j, "left");
			adjacent(i, j, "right");
		}	
		
	}

	/**
	 * Predator eats the prey at the adjacent square if they exist
	 * @param x position x in the 2D array (the clicked y coordinate in the screen)
	 * @param y position y in the 2D array (the clicked x coordinate in the screen)
	 * @param direction which of the adjacent squares to check that a prey is present 
	 */
	public void adjacent(int x, int y, String direction){
		// if we want to check the right-adjacent square
		if(direction == "right" && worldMap[x][y] != null){
			// ensure that the right square next to the predator is not null
			if(worldMap[x][y].type == "pred" && worldMap[x][y + 1] != null){
				// if the square right to the predator is a prey and does not have the
				// same color as the screen background, eat it
				if(worldMap[x][y + 1].type == "prey" && worldMap[x][y + 1].color != canavasColor){
					((prey)worldMap[x][y + 1]).getEaten();
				}
			}
		}
		// if we want to check the left-adjacent square
		if(direction == "left" && worldMap[x][y] != null){
			// ensure that the left square next to the predator is not null
			if(worldMap[x][y].type == "pred" && worldMap[x][y - 1] != null){
				// if the square left to the predator is a prey and does not have the
				// same color as the screen background, eat it
				if(worldMap[x][y - 1].type == "prey" && worldMap[x][y - 1].color != canavasColor){
					((prey)worldMap[x][y - 1]).getEaten();
				}
			}
		}
		// if we want to check the bottom-adjacent square
		if(direction == "down" && worldMap[x][y] != null){
			// ensure that the square below the predator is not null
			if(worldMap[x][y].type == "pred" && worldMap[x+1][y] != null){
				// if the square below the predator is a prey and does not have the
				// same color as the screen background, eat it
				if(worldMap[x + 1][y].type == "prey" && worldMap[x + 1][y].color != canavasColor){
					((prey)worldMap[x + 1][y]).getEaten();
				}
			}
		}
		// if we want to check the top-adjacent square
		if(direction == "up" && worldMap[x][y] != null){
			// ensure that the square above the predator is not null
			if(worldMap[x][y].type == "pred" && worldMap[x - 1][y] != null){
				// if the square above the predator is a prey and does not have the
				// same color as the screen background, eat it
				if(worldMap[x - 1][y].type == "prey" && direction == "up" && worldMap[x - 1][y].color != canavasColor){
					((prey)worldMap[x - 1][y]).getEaten();
				}
			}
		}
	}

	/**
	 * creates a new predator object and puts it in predList and in worldMap
	 * based on where the user clicks on the screen
	 * @param x position x in the 2D array (the clicked y coordinate in the screen)
	 * @param y position y in the 2D array (the clicked x coordinate in the screen)
	 */
	public void OnClickPrey(int x, int y){
		prey onClickedPrey = new prey(x, y, Helper.newRandColor());
		worldMap[y][x] = onClickedPrey;
		preyList.add(onClickedPrey);
	}

	/**
	 * creates a new prey object and puts it in preyList and in worldMap
	 * whenever the user presses "p" on the keyboard
	 */
	public void onPressedPred(){
		int x = Helper.nextInt(width);
		int y = Helper.nextInt(height);
		predator pred = new predator(x, y); // each prey has a random color
		worldMap[y][x] = pred; 
		predList.add(pred);
	}

	// Animal Class 
	abstract class Animal{
		// coordinate points of each animal object
        int x_point; 
		int y_point;
		// direction to move
		int moveDirection;
		// either "pred" or "prey"
		String type;
		Color color;
		boolean chasing;
        public Animal(int x_point, int y_point){
            this.x_point = x_point;
            this.y_point = y_point;
		}
		// Methods in this class
		public abstract void move();
		public abstract void changeDirection();
		public abstract void reproduce();
		public abstract void die();
		public abstract void drawAnimal(DotPanel dotPanel);

	}

	// Predator class 
	class predator extends Animal{

		// predators created are placed in random coordinates
		// and are randomly assigned a direction to walk to
		public predator (int x_point, int y_point){
            super(x_point, y_point);
			this.moveDirection = Helper.nextInt(4);
			this.type = "pred";
			this.color = Color.RED;
			this.chasing = false; 
		}

		@Override
		public void move(){

			// Ensures that a predator is within the bounds of the world
			if(this.x_point == width - 1){ // if at rightmost 
				this.moveDirection = 2; // make predator move left
			}
			if(this.y_point == height - 1){ // if at very bottom
				this.moveDirection = 0; // make predator go up
			}
			if(this.x_point == 0){ // if at very leftmost
				this.moveDirection = 3; // make predator go right
			}
			if(this.y_point == 0){ // if at very top
				this.moveDirection = 1; // make predator go down
			}
			worldMap[this.y_point][this.x_point] = null;
			// Allow predator to continue moving one square
			// towards its assigned direction
			switch (this.moveDirection){
				// Go up
				case 0:
					this.y_point--;
					worldMap[this.y_point][this.x_point] = this;
					break;
				// Go down
				case 1:
					this.y_point++;
					worldMap[this.y_point][this.x_point] = this;
					break;
				// Go left
				case 2:
					this.x_point--;
					worldMap[this.y_point][this.x_point] = this;
					break;
				// Go right
				case 3:
					this.x_point++;
					worldMap[this.y_point][this.x_point] = this;
					break;
			}
		}

		@Override
		public void changeDirection(){
			// if predator is not chasing a prey
			if (!this.chasing){
				if(Helper.nextDouble() < 0.05){
					// randomly select a new direction 
					this.moveDirection = Helper.nextInt(4);
				}
			}
			
		}

		@Override
		public void reproduce(){
			if(Helper.nextDouble() < 0.0135){
				int i = 0;
				// randomly select how many offspring predators to create
				// maximum 3 new predators can be produced
				int max = Helper.nextInt(4);
				while (i < max){
					int x = Helper.nextInt(width);
					int y = Helper.nextInt(height);
					predator pred = new predator(x, y);
					worldMap[y][x] = pred;
					predList.add(pred);
					i++;
				}
			}
		}

		@Override
		public void die(){
			if(Helper.nextDouble() < 0.011){
				worldMap[this.y_point][this.x_point] = null; // set its current location null
				predList.remove(this); // remove the object from predList if it dies
			}
		}
		@Override
		public void drawAnimal(DotPanel dotPanel){
			// predators are represented as red squares
			dotPanel.drawSquare(this.x_point, this.y_point, this.color);
		}
		
			
	}

	// prey class
	class prey extends Animal{

		public prey (int x_point, int y_point, Color c){
			super(x_point, y_point);
			this.moveDirection = Helper.nextInt(4);
			this.type = "prey";
			this.color = c;
			this.chasing = false;
		}

		@Override
		public void move(){
			// Ensures that a predator is within the bounds of the world
			if(this.x_point == width - 1){ // if at rightmost 
				this.moveDirection = 2; // make predator move left
			}
			if(this.y_point == height - 1){ // if at very bottom 
				this.moveDirection = 0; // make predator go up
			}
			if(this.x_point == 0){ // if at very leftmost
				this.moveDirection = 3; // make predator go right
			}
			if(this.y_point == 0){ // if at very top
				this.moveDirection = 1; // make predator go down
			}

			worldMap[this.y_point][this.x_point] = null;
			// Allow predator to continue moving one square
			// towards its assigned direction
			switch (this.moveDirection){
				// Go up
				case 0:
					this.y_point--;
					worldMap[this.y_point][this.x_point] = this;
					break;
				// Go down
				case 1:
					this.y_point++;
					worldMap[this.y_point][this.x_point] = this;
					break;
				// Go left
				case 2:
					this.x_point--;
					worldMap[this.y_point][this.x_point] = this;
					break;
				// Go right
				case 3:
					this.x_point++;
					worldMap[this.y_point][this.x_point] = this;
					break;
				}
		}

		@Override
		public void changeDirection(){
			// if a predator is not located nearby
			if(!this.chasing){
				if(Helper.nextDouble() < 0.1){
					// randomly select a new direction 
					this.moveDirection = Helper.nextInt(4);
				}
			}
		}

		@Override
		public void reproduce(){
			if(Helper.nextDouble() < 0.03){
				int x = Helper.nextInt(width);
				int y = Helper.nextInt(height);
				Color offSpringColor;
				// chance for the offspring to have a new color
				if (Helper.nextDouble() < 0.1){
					offSpringColor = Helper.newRandColor();
				}
				else{
					// otherwhise, it will have the same color as the parent
					offSpringColor = this.color;
				}
				prey offspring = new prey(x,y, offSpringColor);
				worldMap[y][x] = offspring;
				preyList.add(offspring);
			}
		}

		@Override
		public void die(){
			if(Helper.nextDouble() < 0.0135){
				worldMap[this.y_point][this.x_point] = null;
				preyList.remove(this); // remove the object from preyList if it dies
				}
		}

		@Override
		public void drawAnimal(DotPanel dotPanel){
			dotPanel.drawCircle(this.x_point, this.y_point, this.color);
		}

		// called when a predator object is located adjacent to
		// a prey object
		public void getEaten(){
			worldMap[this.y_point][this.x_point] = null; // make current position in array null
			preyList.remove(this); 
		}
	}
}
