package predator_prey_sim;

import util.Helper;

import java.awt.*;
import java.util.ArrayList;
import util.DotPanel;
import java.awt.Color;

public class World {

	private int width, height;
	private Color canavasColor;
	ArrayList<predator> predList = new ArrayList<predator>();
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
		int i = 0;
		while(i < numPredators){
			predator pred = new predator(Helper.nextInt(width), Helper.nextInt(height));
			predList.add(pred);
			i++;
		}
	}
	
	/**
	 * Updates the state of the world for a time step.
	 */
	public void update() {
		// Move predators, prey, etc
		for(predator p: predList){
			p.move();
			p.changeDirection();
		}
	}

	/**
	 * Draw all the predators and prey.
	 */
	public void draw(DotPanel canvas){
		/* Clear the screen */
		PPSim.dp.clear(canavasColor);
		// Draw predators and pray
		for(predator p:predList){
			p.drawPred(canvas);
		}
	}
	//public void addPrey()?
	//public void addPred()?


	// Animal Class 
	abstract class Animal{
		// coordinate points of each animal object
        int x_point; 
		int y_point;

		// An animal object can be a "Square" (Predator)
		// or a "Circle" (Prey)
		String shape; 
		String color;
		int moveDirection;

        public Animal(int x_point, int y_point){
            this.x_point = x_point;
            this.y_point = y_point;
		}
		public abstract void move();
		public abstract void changeDirection();
		public abstract void reproduce();
        public abstract void die();

	}

	// Predator class 
	class predator extends Animal{
		public predator (int x_point, int y_point){
            super(x_point, y_point);
			this.color = "Red";
			this.shape = "Square";
			this.moveDirection = Helper.nextInt(4);
		}

		@Override
		public void move(){
			if(this.x_point == width - 1){
				this.moveDirection = 2;
			}
			if(this.y_point == height - 1){
				this.moveDirection = 1;
			}
			if(this.x_point == 0){
				this.moveDirection = 3;
			}
			if(this.y_point == 0){
				this.moveDirection = 0;
			}


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
				this.moveDirection = Helper.nextInt(4);
			}
		}
		@Override
		public void reproduce(){

		}
		@Override
		public void die(){

		}

		public void drawPred(DotPanel dotPanel){
			dotPanel.drawSquare(this.x_point, this.y_point, Color.RED);
		}
	}



}
