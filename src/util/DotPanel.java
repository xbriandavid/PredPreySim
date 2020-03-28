package util;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class DotPanel extends javax.swing.JPanel {

	// ignore this.
	private static final long serialVersionUID = -2909151487015547533L;

	// pre-defined colors
    public static final Color BLACK      = Color.BLACK;
    public static final Color BLUE       = Color.BLUE;
    public static final Color CYAN       = Color.CYAN;
    public static final Color DARK_GRAY  = Color.DARK_GRAY;
    public static final Color GRAY       = Color.GRAY;
    public static final Color GREEN      = Color.GREEN;
    public static final Color LIGHT_GRAY = Color.LIGHT_GRAY;
    public static final Color MAGENTA    = Color.MAGENTA;
    public static final Color ORANGE     = Color.ORANGE;
    public static final Color PINK       = Color.PINK;
    public static final Color RED        = Color.RED;
    public static final Color WHITE      = Color.WHITE;
    public static final Color YELLOW     = Color.YELLOW;

    // references needed for graphics
	private static BufferedImage offscreenImage;
	private static Graphics2D offscreen;

	/** Size of a "pixel" on the screen */
	private int dotSize;

	/** size of the panel in true screen pixels */
	private int width;
	private int height;

	/** Active drawing color */
	private Color penColor;

	/** Automatically refresh screen after pixel calls? */
	private boolean autoShow;

	public void setAutoShow(boolean b) {
		autoShow = b;
	}

	/** Returns the number of dots wide that the panel is */
	public int getWidthInDots() {
		return width / dotSize;
	}

	/** Returns the number of dots tall that the panel is */
	public int getHeightInDots() {
		return height / dotSize;
	}

	public int getDotSize() {
		return dotSize;
	}


	public void setPenColor(Color penColor) {
		this.penColor = penColor;
		offscreen.setColor(penColor);
	}

	/** Force the panel to a particular size */
	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}



	/**
	 * Construct a new DotPanel.
	 * @param w width of the panel in dots (NOT pixels)
	 * @param h height of the panel in dots (NOT pixels)
	 * @param dotSize the size of a dot IN pixels
	 */
	public DotPanel(int w, int h, int dotSize) {
		this.dotSize = dotSize;
		width = w * dotSize;
		height = h * dotSize;
		autoShow = false;
	}


	/** Initialize the DotPanel graphics.  DO NOT CALL this until
	 *  you have added the panel to a frame and called pack()
	 */
	public void init() {
		width = this.getWidth();
		height = this.getHeight();

		if(width ==0 || height == 0) {
			System.out.println("ERROR: You tried calling init() to soon!  Make sure you add the panel to a frame and call pack() first!");
			System.exit(-1);
		}
		offscreenImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);
		offscreen = offscreenImage.createGraphics();
	}

	public void paintComponent(Graphics g) {
		g.drawImage(offscreenImage, 0, 0, null);
	}

	/** Draw a "square dot" on the screen.
	 * 
	 * */
	public void drawSquare(int x, int y, Color c) {
		setPenColor(c);
		offscreen.fillRect(x* dotSize  , y * dotSize, 2*dotSize, 2*dotSize);
		if(autoShow) {
			repaintAndSleep(0);
		}
	}
	
	/** Draw a "circle dot" on the screen.
	 
	 * */
	public void drawCircle(int x, int y, Color c) {
		setPenColor(c);
		offscreen.fillOval(x * dotSize, y * dotSize, 2*dotSize, 2*dotSize);
		if(autoShow) {
			repaintAndSleep(0);
		}
	}

	public  void clear() {
		clear(BLACK);
	}
	public  void clear(Color color) {
		offscreen.setColor(color);
		offscreen.fillRect(0, 0, width, height);
		offscreen.setColor(penColor);
	}

	/**
	 * Draw the current frame to the screen, then sleep for the specified delay
	 * @param delay is in milliseconds
	 */
	@SuppressWarnings("static-access")
	public void repaintAndSleep(int delay) {
		this.repaint();
		try {
			Thread.currentThread().sleep(delay);
		} catch (InterruptedException e) {
			System.out.println("Error sleeping");
		}
	}


}

