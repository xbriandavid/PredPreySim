package predator_prey_sim;

import util.Helper;

import java.awt.*;
import java.util.ArrayList;


public class World {

	private int width, height;
	private Color canavasColor;
	
	

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
		
	}
	
	/**
	 * Updates the state of the world for a time step.
	 */
	public void update() {
		// Move predators, prey, etc
	}

	/**
	 * Draw all the predators and prey.
	 */
	public void draw(){
		/* Clear the screen */
		PPSim.dp.clear(canavasColor);
		// Draw predators and pray
	}



}
