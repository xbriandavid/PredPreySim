package predator_prey_sim;

import util.Helper;

import java.awt.*;
import java.util.ArrayList;
import util.DotPanel;
import java.awt.Color;

public class World {

	private int width, height;
	private Color canavasColor;
	// Holds all the predator objects in the world object
	ArrayList<predator> predList = new ArrayList<predator>();
	Animal [][] worldMap; // used to keep track of predators and prey location
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
		 worldMap = new Animal[w][h];
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
			predator pred = new predator(Helper.nextInt(width), Helper.nextInt(height));
			predList.add(pred);
			i++;
		}

		/*
		int j = 0;
		while(i < numPrey){
			Prey prey = new prey(Helper.nextInt(width), Helper.nextInt(height));
			preyList.add(prey);
			i++;
		}
		*/
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
			predList.get(i).changeDirection();
			predList.get(i).reproduce();
			predList.get(i).die();
		}
	}

	/**
	 * Draw all the predators and prey.
	 */
	public void draw(DotPanel canvas){ // takes the canvas the simulation is drawing on
		/* Clear the screen */
		PPSim.dp.clear(canavasColor);
		// Draw predators and pray
		/*for(predator p:predList){
			p.drawPred(canvas);
		}*/

		for(int i = 0; i< predList.size();i++){
			predList.get(i).drawPred(canvas);
		}
	}


	// Animal Class 
	abstract class Animal{
		// coordinate points of each animal object
        int x_point; 
		int y_point;
		int moveDirection;

        public Animal(int x_point, int y_point){
            this.x_point = x_point;
            this.y_point = y_point;
		}

		// Methods in this class
		public abstract void move();
		public abstract void changeDirection();
		public abstract void reproduce();
		public abstract void die();
		public abstract void drawPred(DotPanel dotPanel);

	}

	// Predator class 
	class predator extends Animal{

		// predators created are placed in random coordinates
		// and are randomly assigned a direction to walk to
		public predator (int x_point, int y_point){
            super(x_point, y_point);
			this.moveDirection = Helper.nextInt(4);
		}

		@Override
		public void move(){

			// Ensures that a predator is within the bounds of the world
			if(this.x_point == width - 1){ // if at rightmost 
				this.moveDirection = 2; // make predator move left
			}
			if(this.y_point == height - 1){ // if at very top 
				this.moveDirection = 1; // make predator go down
			}
			if(this.x_point == 0){ // if at very leftmost
				this.moveDirection = 3; // make predator go right
			}
			if(this.y_point == 0){ // if at very bottom
				this.moveDirection = 0; // make predator go up
			}

			// Allow predator to continue moving one square
			// towards its assigned direction
			switch (this.moveDirection){
				// Go up
				case 0:
					this.y_point++;
					break;
				// Go down
				case 1:
					this.y_point--;
					break;
				// Go left
				case 2:
					this.x_point--;
					break;
				// Go right
				case 3:
					this.x_point++;
					break;
			}
		}

		@Override
		public void changeDirection(){
			if(Helper.nextDouble() < 0.05){
				// randomly select a new direction 
				this.moveDirection = Helper.nextInt(4);
			}
		}

		@Override
		public void reproduce(){
			if(Helper.nextDouble() < 0.01){
				int i = 0;
				// randomly select how many offspring predators to create
				// maximum 3 new predators can be produced
				int max = Helper.nextInt(4);
				while (i < max){
					predator pred = new predator(Helper.nextInt(width), Helper.nextInt(height));
					predList.add(pred);
					i++;
				}
			}
		}

		@Override
		public void die(){
			if(Helper.nextDouble() < 0.012){
				predList.remove(this); // remove the object from predList if it dies
			}
		}
		@Override
		public void drawPred(DotPanel dotPanel){
			// predators are represented as red squares
			dotPanel.drawSquare(this.x_point, this.y_point, Color.RED);
		}		
	}

	// Still need to complete
		/*class prey extends Animal{
			
			public prey (int x_point, int y_point){
				super(x_point, y_point);
				this.moveDirection = Helper.nextInt(4);
			}
		}*/

}
