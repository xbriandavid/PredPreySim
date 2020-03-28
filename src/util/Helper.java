package util;

import java.util.Random;
import java.awt.Color;

public class Helper {

	private static Random rand = null;


	/**
	 * Use the singleton design pattern to store
	 * the random number generator. You'll learn
	 * more about this later.
	 * @return
	 */
	private static Random getRand() {
		if(rand == null) {
			rand = new Random();
		}
		return rand;
	}
	/**
	 * Set the seed used by the random number generator.
	 * @param seed
	 */
	public static void setSeed(long seed) {
		rand = new Random(seed);
	}
	/**
	 * Get a random integer r, such that 0<= r < max.
	 */
	public static int nextInt(int max) {
		return getRand().nextInt(max);
	}
	/**
	 * Return a random double between 0 (inclusive) and 1 (exclusive).
	 */
	public static double nextDouble() {
		return getRand().nextDouble();
	}
	
	/**
	 * Return a random color.
	 */
	public static Color newRandColor(){
		return new Color(nextInt(255),nextInt(255),nextInt(255));
	}
}
