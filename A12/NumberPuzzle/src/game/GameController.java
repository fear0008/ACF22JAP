/**
 * Filename: 		GameController.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A12
 * Professor:		Paulo Sousa
 * Date:			2022-09-27
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the Number Puzzle game
 */
package game;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Class Name: 	GameController
 * Methods: 	createButtons, setupBottomBorder
 * Constants:	serialVersionUID
 * Purpose:		Main game controller used for the Number Puzzle game
 * 
 * @author 	Joshua Fearnall
 * @version 1
 * @see 	game
 * @since 	15
 *
 */
public class GameController extends JFrame
{	
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;
	
	/** menu option used to start a new game*/
	private JMenuItem newGameItem;
	/** text field used to enter a solution for a new puzzle*/
	private JTextField editorTextField;
	/** dialog box used to display an about(information) window*/
	private JDialog aboutDialog;
	/** button used to set the solution entered in the text field to the game board*/
	private JButton editorButton;
	/** button using the logo as an image; used to display the about(information) window*/
	private JButton logoButton;
	/** button used to close the about(information) window*/
	private JButton closeButton;
	/** button array containing all buttons on the game board*/
	private JButton buttonGrid[];
	/** used to track the controller object created internally*/
	private Controller controller;
	/** used to track the game controller*/
	private GameController gameController;
	
	
	/**
	 * Class Name: 	Controller
	 * Methods:		shuffleBoard, setSolution, openAbout, actionPerformed
	 * Constants: 	serialVersionUID
	 * Purpose:		Used to control actions triggered in the game window
	 * 
	 * @author 	Joshua Fearnall
	 * @version 1
	 * @see 	game
	 * @since 	15
	 *
	 */
	class Controller implements ActionListener, Serializable
	{
		/**
		 * Serial Version
		 */
		private static final long serialVersionUID = 1L;
		
		/**
		 * Method Name: setSolution
		 * Purpose:		Used to set a given solution to the game board
		 * Algorithm:	Iterates through all buttons, setting their text to the provided characters
		 * 
		 * @param solution The solution for the game board provided by the user
		 */
		private void setSolution(String solution)
		{
			// Iterate through all buttons on the grid, setting their text to a character from the solution
			for (int i = 0; i < buttonGrid.length - 1; i++)
			{
				if (solution.length() > i)
				{
					buttonGrid[i].setText(Character.toString(solution.charAt(i)));
				}
				else
				{
					buttonGrid[i].setText("");
				}
			}
		}
		
		/**
		 * Method Name:	openAbout
		 * Purpose:		Used to open the about(information) window
		 * Algorithm:	Creates and shows a new window with 
		 */
		private void openAbout()
		{
			// creating an modifying the about dialog window
			aboutDialog = new JDialog(gameController, "About", true);
			
			aboutDialog.setSize(600, 600);
			aboutDialog.setResizable(false);
			aboutDialog.setLocationRelativeTo(null);
			aboutDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			aboutDialog.setLayout(new BoxLayout(aboutDialog.getContentPane(), BoxLayout.Y_AXIS));
			
			// image
			JLabel aboutImage = new JLabel();
			aboutImage.setIcon(new ImageIcon(getClass().getClassLoader().getResource("game/images/gameabout.png")));
			aboutImage.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			// button
			closeButton = new JButton("Close");
			closeButton.addActionListener(controller);
			
			// adding controls to the dialog
			aboutDialog.add(aboutImage);
			aboutDialog.add(closeButton);
			
			aboutDialog.setVisible(true);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == newGameItem)
			{
				new NewGameWindow();
				gameController.dispose();
			}
			else if (e.getSource() == editorButton)
			{
				setSolution(editorTextField.getText());
			}
			else if (e.getSource() == logoButton)
			{
				openAbout();
			}
			else if (e.getSource() == closeButton)
			{
				aboutDialog.dispose();
			}
		}
	}

	/**
	 * Method Name: gameController
	 * Purpose:		Default constructor for controller, sets up board with a 3*3 number grid in design mode
	 * Algorithm:
	 */
	public GameController()
	{
		// passing default settings
		this(3, "Number", "Design");
	}
	
	/**
	 * Method Name: gameController
	 * Purpose:		Overloaded constructor for controller, sets up board with a N*N grid of characters or numbers in the given mode
	 * Algorithm:
	 * 
	 * @param dimension The size of the game board
	 * @param gameType The type of game being played (numeric or character)
	 * @param gameMode The game mode. Either design or play.
	 */
	public GameController(int dimension, String gameType, String gameMode)
	{		
		gameController = this;
		controller = new Controller();
		
		// frame settings
		this.setTitle("Number Puzzle - " + gameMode);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new BorderLayout());

		// menu
		JMenuBar menuBar = new JMenuBar();
		JMenu optionsMenu = new JMenu("Options");
		newGameItem = new JMenuItem("New Game");
		newGameItem.addActionListener(controller);
		
		optionsMenu.add(newGameItem);
		menuBar.add(optionsMenu);
		this.setJMenuBar(menuBar);
		
		// top border
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(100, 20));
		this.add(topPanel, BorderLayout.NORTH);
		
		// left border
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(20, 100));
		this.add(leftPanel, BorderLayout.WEST);
		
		// center
		JPanel centerPanel = new JPanel(new GridLayout(dimension, dimension));
		createButtons(dimension, centerPanel);
		this.add(centerPanel, BorderLayout.CENTER);
		
		// right border
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(20, 100));
		this.add(rightPanel, BorderLayout.EAST);
		
		// bottom border
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setPreferredSize(new Dimension(100, 60));
		logoButton = new JButton();
		logoButton.setPreferredSize(new Dimension(140, 50));
		logoButton.setIcon(new ImageIcon(getClass().getClassLoader().getResource("game/images/gamelogo.png")));
		logoButton.addActionListener(controller);
		bottomPanel.add(logoButton);
		setupBottomBorder(gameMode, gameType, bottomPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	/**
	 * Method Name:	createButtons
	 * Purpose:		create a grid of buttons to be used for the game board
	 * Algorithm:	creates dimensionCount^2 buttons and adds them to the game board
	 * 
	 * @param dimensionCount the size of one dimension(x,y) of the board
	 * @param panel the JPanel where the buttons are placed
	 */
	private void createButtons(int dimensionCount, JPanel panel)
	{
		// variables for button counts
		int count = 0;
		int maximumButtons = dimensionCount * dimensionCount;
		
		// button grid array; needed for iteration
		buttonGrid = new JButton[maximumButtons];
		
		// loop to create buttons for the grid
		for (int i = 0; i < dimensionCount; i++)
		{
			for (int j = 0; j < dimensionCount; j++)
			{
				// create a button and add it to the panel
				count++;
				JButton instance;
				if (count < maximumButtons)
				{
					instance = new JButton(Integer.toString(count));
					instance.setFont(new Font("Arial", Font.BOLD, 34));
				}
				else
				{
					instance = new JButton();
					instance.setBackground(Color.BLACK);
					instance.setEnabled(false);
				}

				panel.add(instance);
				
				buttonGrid[count - 1] = instance;
			}
		}
	}
	
	/**
	 * Method Name:	setupBottomBorder
	 * Purpose:		Initialize the bottom border based on the game type and mode
	 * Algorithm:	Create different controls based on the mode and type of game 
	 * the user is playing and add them to the panel
	 * 
	 * @param gameMode the game mode (Design or Play)
	 * @param gameType the game type (Text or Numeric)
	 * @param panel the panel containing the bottom border
	 */
	private void setupBottomBorder(String gameMode, String gameType, JPanel panel)
	{
		if (gameMode == "Design" && gameType == "Text")
		{
			// Enabling settings to allow the user to create new puzzle solutions
			JLabel editorLabel = new JLabel("Solution:");
			editorTextField = new JTextField();
			editorTextField.setPreferredSize(new Dimension(400, 20));
			
			editorButton = new JButton("Set");
			editorButton.addActionListener(controller);
			
			panel.add(editorLabel);
			panel.add(editorTextField);
			panel.add(editorButton);
		}
		else if (gameMode == "Play")
		{
			// Enabling settings to allow the user to view their current statistics while playing the game
			JLabel pointsLabel = new JLabel("Points: 0");
			pointsLabel.setPreferredSize(new Dimension(80, 20));
			
			JLabel movesLabel = new JLabel("Moves: 0");
			movesLabel.setPreferredSize(new Dimension(80, 20));
			
			JLabel timeLabel = new JLabel("Time: 0");
			timeLabel.setPreferredSize(new Dimension(80, 20));
			
			panel.add(pointsLabel);
			panel.add(movesLabel);
			panel.add(timeLabel);
		}
	}
	
	/**
	 * Method Name:	main
	 * Purpose:		used to initialize the game
	 * Algorithm:
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		new GameController();
	}
}
