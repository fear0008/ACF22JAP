/**
 * Filename: 		GameView.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A32
 * Professor:		Paulo Sousa
 * Date:			2022-11-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the game view in MVC
 */
package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Class Name: 	GameView
 * Methods: 	initializeGame, generateBoard, createNewGameWindow, setupBottomBorder, openSplashScreen, openDialog,
 * 				openAbout, setNewColor, openNewGameWindow, updateTiles, getTileIndex, getEmptyIndex, moveTile, shuffleBoard,
 * 				updateMoves, updateScore, updateTime, calculateScore, disableBoard, saveLayout, loadLayout, showSolution,
 * 				addController, removeController,
 * Constants:	serialVersionUID
 * Purpose:		View for the MVC model in the Number Puzzle game
 * 
 * @author 	Joshua Fearnall
 * @version 3
 * @see 	game
 * @since 	15
 *
 */
public class GameView extends JFrame
{
	/** Serial Version*/
	private static final long serialVersionUID = 1L;
	
	/** menu option used to start a new game*/
	public JMenuItem newGameItem;
	/** button using the logo as an image; used to display the about(information) window*/
	public JButton logoButton;
	/** dialog box used to display an about(information) window*/
	public JDialog aboutDialog;
	/** button used to close the about(information) window*/
	public JButton closeButton = new JButton("Close");
	/** text field used to enter a solution for a new puzzle*/
	public JTextField editorTextField;
	/** button used to set the solution entered in the text field to the game board*/
	public JButton editorButton = new JButton("Set");
	/** Save button in the menu*/
	public JMenuItem saveItem;
	/** Load button in the menu*/
	public JMenuItem loadItem;
	/** Color button in the menu*/
	public JMenuItem colorItem;
	/** Solution button in the menu*/
	public JMenuItem showSolutionItem;
	/** Start button used to begin a new game with the given settings*/
	public JButton startButton = new JButton("Start");
	/** Combo box for game mode selection*/
	public JComboBox<String> modeBox;
	/** Combo box for board size selection*/
	public JComboBox<Integer> dimensionBox;
	/** Combo box for game type selection*/
	public JComboBox<String> typeBox;
	
	/** the game modes a user can select*/
	private String gameModes[] = {"Design", "Play"};
	/** the board sizes a user can select*/
	private Integer dimensions[] = {3,4,5};
	/** the game types a user can select*/
	private String gameTypes[] = {"Number", "Text"};
	/** Label for the players points*/
	private JLabel pointsLabel;
	/** Label for the time remaining*/
	private JLabel timeLabel;
	/** Label for the players moves*/
	private JLabel movesLabel;
	/** Frame for the new game window*/
	private JFrame newGameFrame = new JFrame();
	/** Center panel where the grid exists*/
	private JPanel centerPanel;
	/** button array containing all buttons on the game board*/
	private JButton buttonGrid[];
	/** button array tracking the original layout*/
	private JButton initialButtonLayout[];
	/** Color used to customize the board*/
	private Color color = Color.white;
	/** left side of the main board, used for color customization*/
	private JPanel leftPanel;
	/** right side of the main board, used for color customization*/
	private JPanel rightPanel;
	
	/**
	 * Default constructor used for the view; creates a new game window when initialized
	 */
	public GameView()
	{
		createNewGameWindow();
	}
	
	/**
	 * Initializes the game board before playing the game
	 * @param newDimension The new dimension for the board
	 * @param newMode The new mode for the game
	 * @param newType The new game type
	 * @param newSolution The new solution to set to the board
	 */
	public void initializeGame(int newDimension, String newMode, String newType, String newSolution)
	{
		// reset layout
		this.getContentPane().removeAll();
		this.revalidate();
		this.repaint();
		
		// frame settings
		this.setTitle("Number Puzzle - " + newMode);
		this.setSize(800, 600);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		
		// menu
		JMenuBar menuBar = new JMenuBar();
		JMenu optionsMenu = new JMenu("Options");
		newGameItem = new JMenuItem("New Game", new ImageIcon(getClass().getResource("/images/iconnew.png")));
		saveItem = new JMenuItem("Save", new ImageIcon(getClass().getResource("/images/iconsol.png")));
		loadItem = new JMenuItem("Load", new ImageIcon(getClass().getResource("/images/iconext.png")));
		colorItem = new JMenuItem("Color", new ImageIcon(getClass().getResource("/images/iconcol.png")));
		showSolutionItem = new JMenuItem("Solution", new ImageIcon(getClass().getResource("/images/iconabt.png")));

		optionsMenu.add(newGameItem);
		optionsMenu.add(saveItem);
		optionsMenu.add(loadItem);
		optionsMenu.add(colorItem);
		optionsMenu.add(showSolutionItem);
		
		menuBar.add(optionsMenu);
		this.setJMenuBar(menuBar);
		
		// top border
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(100, 20));
		this.add(topPanel, BorderLayout.NORTH);
		
		// left border
		leftPanel = new JPanel();
		leftPanel.setBackground(color);
		leftPanel.setPreferredSize(new Dimension(20, 100));
		this.add(leftPanel, BorderLayout.WEST);
		
		// center
		// generating board with default settings
		generateBoard(newSolution, newDimension);
		
		// right border
		rightPanel = new JPanel();
		rightPanel.setBackground(color);
		rightPanel.setPreferredSize(new Dimension(20, 100));
		this.add(rightPanel, BorderLayout.EAST);
		
		// bottom border
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setPreferredSize(new Dimension(100, 60));
		logoButton = new JButton();
		logoButton.setPreferredSize(new Dimension(140, 50));
		logoButton.setIcon(new ImageIcon(getClass().getResource("/images/gamelogo.png")));
		
		bottomPanel.add(logoButton);
		setupBottomBorder(newMode, newType, bottomPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		this.setVisible(true);
	}
	
	/**
	 * Generates a new game board for game-play
	 * @param newSolution The new solution used for the board tiles
	 * @param newDimension The new dimension set to the board
	 */
	private void generateBoard(String newSolution, int newDimension)
	{
		StringTokenizer st;
		st = new StringTokenizer(newSolution, ",");
		
		// variables for button counts
		int dimension = newDimension;
		int maximumButtons = dimension * dimension;

		// setting up button grid and secondary grid to track initial settings
		buttonGrid = new JButton[maximumButtons];
		initialButtonLayout = new JButton[maximumButtons];
		
		centerPanel = new JPanel(new GridLayout(dimension, dimension));
		
		// discard type token
		st.nextToken();
		st.nextToken();
		
		// loop to create buttons for the grid
		for (int i = 0; i < maximumButtons; i++)
		{
			// create a button and add it to the panel
			JButton instance;
			if (i < maximumButtons - 1)
			{
				if (st.hasMoreTokens())
				{
					instance = new JButton(st.nextToken());
				}
				else
				{
					instance = new JButton(" ");
				}

				instance.setFont(new Font("Arial", Font.BOLD, 34));
			}
			else
			{
				instance = new JButton("");
				instance.setBackground(Color.BLACK);
				instance.setEnabled(false);
			}
			
			centerPanel.add(instance);
			
			buttonGrid[i] = instance;
			initialButtonLayout[i] = instance;
		}
		
		
		this.add(centerPanel, BorderLayout.CENTER);
		
		// enable the board
		this.setEnabled(true);
		
		// hide the frame when coming from other code
		if (newGameFrame.isVisible())
		{
			newGameFrame.setVisible(false);
		}
	}
	
	/**
	 * Creates and shows the new game window to the user, to allow them to create a new game
	 */
	private void createNewGameWindow()
	{
		// defining new game window
		newGameFrame.setTitle("New Game");
		newGameFrame.setSize(400, 184);
		newGameFrame.setResizable(false);
		newGameFrame.setLocationRelativeTo(null);
		newGameFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		newGameFrame.setLayout(new BoxLayout(newGameFrame.getContentPane(), BoxLayout.Y_AXIS));
		
		// game type settings
		JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		modePanel.setMaximumSize(new Dimension(400, 40));
		JLabel modeLabel = new JLabel("Puzzle Type:");
		modeBox = new JComboBox<>(gameModes);
		
		// board size settings
		JPanel dimensionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		dimensionPanel.setMaximumSize(new Dimension(400, 40));
		JLabel dimensionLabel = new JLabel("Board Size:");
		dimensionBox = new JComboBox<>(dimensions);
		
		// game type settings
		JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		typePanel.setMaximumSize(new Dimension(400, 40));
		JLabel typeLabel = new JLabel("Puzzle Type:");
		typeBox = new JComboBox<>(gameTypes);
		
		// start button
		JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		typePanel.setMaximumSize(new Dimension(400, 40));
		
		// adding all components to various panels
		modePanel.add(modeLabel);
		modePanel.add(modeBox);
		dimensionPanel.add(dimensionLabel);
		dimensionPanel.add(dimensionBox);
		typePanel.add(typeLabel);
		typePanel.add(typeBox);
		startPanel.add(startButton);
		
		// adding panels to the frame
		newGameFrame.add(modePanel);
		newGameFrame.add(dimensionPanel);
		newGameFrame.add(typePanel);
		newGameFrame.add(startPanel);
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
			
			panel.add(editorLabel);
			panel.add(editorTextField);
			panel.add(editorButton);
		}
		else if (gameMode == "Play")
		{
			// Enabling settings to allow the user to view their current statistics while playing the game
			pointsLabel = new JLabel("Points: 0");
			pointsLabel.setPreferredSize(new Dimension(80, 20));
			
			movesLabel = new JLabel("Moves: 0");
			movesLabel.setPreferredSize(new Dimension(80, 20));
			
			timeLabel = new JLabel("Time: 0");
			timeLabel.setPreferredSize(new Dimension(80, 20));
			
			panel.add(pointsLabel);
			panel.add(movesLabel);
			panel.add(timeLabel);
		}
	}
	
	/**
	 * Creates and opens a splash screen for the program before launching and starting the main game
	 */
	public void openSplashScreen()
	{
		// create the new jframe splash screen
		JFrame splashScreen = new JFrame();
		splashScreen.setUndecorated(true);
		
		splashScreen.setSize(500, 500);
		splashScreen.setResizable(false);
		splashScreen.setLocationRelativeTo(null);
		splashScreen.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		splashScreen.setLayout(new BoxLayout(splashScreen.getContentPane(), BoxLayout.Y_AXIS));
		
		// image
		JLabel aboutImage = new JLabel();
		aboutImage.setIcon(new ImageIcon(getClass().getResource("/images/gameabout.png")));
		aboutImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// adding controls to the dialog
		splashScreen.add(aboutImage);
		
		splashScreen.setVisible(true);
		
		try 
		{
			Thread.sleep(3000);
			splashScreen.dispose();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens a dialog message to the user showing information based on the action taken place
	 * @param dialogType The type of dialog to open (finished, lose, or win)
	 */
	public void openDialog(String dialogType)
	{
		// creating an modifying the about dialog window
		aboutDialog = new JDialog(this, "You " + dialogType, true);
		
		aboutDialog.setSize(600, 600);
		aboutDialog.setResizable(false);
		aboutDialog.setLocationRelativeTo(null);
		aboutDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutDialog.setLayout(new BoxLayout(aboutDialog.getContentPane(), BoxLayout.Y_AXIS));
		
		String resource = "/images/";
		
		// select image
		if (dialogType == "finished") {
			resource += "gameend.png";
		}
		else if (dialogType == "lose") {
			resource += "gameerr.png";
		}
		else if (dialogType == "win") {
			resource += "gamewinner.png";
		}
		
		// image
		JLabel aboutImage = new JLabel();
		aboutImage.setIcon(new ImageIcon(getClass().getResource(resource)));
		aboutImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// adding controls to the dialog
		aboutDialog.add(aboutImage);
		aboutDialog.add(closeButton);
		
		aboutDialog.setVisible(true);
	}
	
	/**
	 * Method Name:	openAbout
	 * Purpose:		Used to open the about(information) window
	 * Algorithm:
	 */
	public void openAbout()
	{
		// creating an modifying the about dialog window
		aboutDialog = new JDialog(this, "About", true);
		
		aboutDialog.setSize(600, 600);
		aboutDialog.setResizable(false);
		aboutDialog.setLocationRelativeTo(null);
		aboutDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutDialog.setLayout(new BoxLayout(aboutDialog.getContentPane(), BoxLayout.Y_AXIS));
		
		// image
		JLabel aboutImage = new JLabel();
		aboutImage.setIcon(new ImageIcon(getClass().getResource("/images/gameabout.png")));
		aboutImage.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// adding controls to the dialog
		aboutDialog.add(aboutImage);
		aboutDialog.add(closeButton);
		
		aboutDialog.setVisible(true);
	}
	
	/**
	 * Open a window to allow the user to select a color for the board
	 */
	public void setNewColor()
	{
		Color color = JColorChooser.showDialog(null, "Color Editor", getForeground());
		
		leftPanel.setBackground(color);
		rightPanel.setBackground(color);
		
		this.color = color;
	}
	
	/**
	 * Open a window to allow user to start a new game
	 */
	public void openNewGameWindow()
	{
		this.setEnabled(false);
		
		newGameFrame.setVisible(true);
	}
	
	/**
	 * Update all tiles on the board with text from the new solution
	 * @param newSolution The new layout to be used for the tiles
	 */
	public void updateTiles(String newSolution)
	{
		StringTokenizer st;
		st = new StringTokenizer(newSolution, ",");
		
		// get the dimension from the solution
		int dimension = Integer.parseInt(st.nextToken());
		int boardSize = dimension * dimension;

		// discard type token
		st.nextToken();
		
		// iterate through the board, setting the text for each tile
		for (int i = 0; i < boardSize - 1; i++)
		{
			if (st.hasMoreTokens())
			{
				buttonGrid[i].setText(st.nextToken());
			}
			else
			{
				buttonGrid[i].setText(" ");
			}
		}
	}
	
	/**
	 * Get the index of the requested tile on the game board
	 * @param button The button to find the index of 
	 * @return The index of the requested button in the grid
	 */
	private int getTileIndex(JButton button)
	{
		for (int i = 0; i < buttonGrid.length; i++)
		{
			if (buttonGrid[i] == button)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * Looks for the 'empty' tile in neighboring squares and returns its index
	 * @param buttonIndex The index of the tile requesting
	 * @param dimension The dimension of the board
	 * @return The index of the empty tile; or -1 if it is not a neighbor
	 */
	private int getEmptyIndex(int buttonIndex, int dimension)
	{
		// check neighboring buttons for the empty tile
		if (buttonIndex - 1 >= 0 && buttonGrid[buttonIndex - 1].getText().isEmpty())
		{
			return buttonIndex - 1;
		}
		else if (buttonIndex + 1 <= buttonGrid.length - 1 && buttonGrid[buttonIndex + 1].getText().isEmpty())
		{
			return buttonIndex + 1;
		}
		else if (buttonIndex - dimension >= 0 && buttonGrid[buttonIndex - dimension].getText().isEmpty())
		{
			return buttonIndex - dimension;
		}
		else if (buttonIndex + dimension <= buttonGrid.length - 1 && buttonGrid[buttonIndex + dimension].getText().isEmpty()) 
		{
			return buttonIndex + dimension;
		}
			
		return -1;
	}
	
	/**
	 * Moves a tile on the board
	 * @param sourceObject The tile that is requesting a movement
	 * @param dimension The dimension of the board
	 * @return true if the tile moves; false if it doesn't
	 */
	public boolean moveTile(Object sourceObject, int dimension)
	{
		// cast the source to a button
		JButton button = (JButton)sourceObject;
		
		// get index of source button
		int buttonIndex = getTileIndex(button);
		// check if the source button has the empty space as a neighbor
		int emptyIndex = getEmptyIndex(buttonIndex, dimension);

		// swap button positions
		if (emptyIndex != -1)
		{
			// ensure that the greater value is always the empty index
			if (buttonIndex > emptyIndex)
			{
				int temp = emptyIndex;
				emptyIndex = buttonIndex;
				buttonIndex = temp;
			}
			
			// swapping buttons
			JButton first = buttonGrid[buttonIndex];
			JButton second = buttonGrid[emptyIndex];
			
			centerPanel.remove(first);
			centerPanel.remove(second);
			
			centerPanel.add(second, buttonIndex);
			centerPanel.add(first, emptyIndex);
			
			buttonGrid[buttonIndex] = second;
			buttonGrid[emptyIndex] = first;
			
			this.revalidate();
			this.repaint();
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Shuffles the tiles on the board
	 */
	public void shuffleBoard()
	{
		// shuffle the JButtons
		List<JButton> buttonList = Arrays.asList(buttonGrid);
		Collections.shuffle(buttonList);
		buttonList.toArray(buttonGrid);
		
		// re-add JButtons to layout
		centerPanel.removeAll();
		
		for (int i = 0; i < buttonGrid.length; i++)
		{
			centerPanel.add(buttonGrid[i]);
		}
		
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Updates the moves label with the new move count
	 * @param moves The new move count
	 */
	public void updateMoves(int moves)
	{
		movesLabel.setText("Moves: " + moves);
	}
	
	/**
	 * Updates the score label with the new score
	 * @param score The players new score
	 */
	public void updateScore(int score)
	{
		pointsLabel.setText("Score: " + score);
	}
	
	/**
	 * Updates the time label with the new time
	 * @param time The amount of time remaining
	 */
	public void updateTime(int time)
	{
		timeLabel.setText("Time: " + time);
	}
	
	/**
	 * Calculate the players current score
	 * @param puzzleSolution The solution to match the current board tiles to
	 * @return The number of tiles that are matched (as a score)
	 */
	public int calculateScore(String puzzleSolution)
	{
		StringTokenizer st;
		st = new StringTokenizer(puzzleSolution, ",");
		// get the dimension from the solution, then find number of tiles
		int solutionDimension = Integer.parseInt(st.nextToken());
		int numberOfTiles = solutionDimension * solutionDimension;
		
		int score = 0;
		
		
		// discard type token
		st.nextToken();

		// calculating score
		for (int i = 0; i < numberOfTiles - 1; i++)
		{
			if (st.hasMoreTokens() && buttonGrid[i].getText().equals(st.nextToken()))
			{
				score++;
			}
		}
		
		return score;
	}
	
	/**
	 * Disables all buttons on the game board
	 */
	public void disableBoard()
	{
		for (int i = 0; i < buttonGrid.length; i ++)
		{
			buttonGrid[i].setEnabled(false);
		}
	}
	
	/**
	 * Allows the user to select or create a file to save a layout to
	 * @param solution The layout to save to the file
	 */
	public void saveLayout(String solution)
	{
		// select a file to save to
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save File");
		int selection = fileChooser.showSaveDialog(this);
		
		// write to the selected file
		if (selection == JFileChooser.APPROVE_OPTION)
		{
			try (FileWriter fw = new FileWriter(fileChooser.getSelectedFile()))
			{
				fw.write(solution);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Allows the user to select a file to load a layout from
	 * @return The layout as a string, or "" if a file was not selected
	 */
	public String loadLayout()
	{
		// Has the user select a file to load
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Load File");
		int selection = fileChooser.showOpenDialog(this);
		
		char[] data = new char[100];
		
		// read from a file once selected
		if (selection == JFileChooser.APPROVE_OPTION)
		{
			try (FileReader fr = new FileReader(fileChooser.getSelectedFile()))
			{
				fr.read(data);
				
				return new String(data);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		
		return "";
	}
	
	/**
	 * Reset the board to the proper solution
	 */
	public void showSolution()
	{
		// re-add JButtons to layout
		centerPanel.removeAll();
		
		for (int i = 0; i < buttonGrid.length; i++)
		{
			buttonGrid[i] = initialButtonLayout[i];
			centerPanel.add(buttonGrid[i]);
		}
		
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Add listeners for game view objects to the controller
	 * 
	 * @param controller Action listener used in the GameController
	 */
	public void addController(ActionListener controller)
	{
		newGameItem.addActionListener(controller);
		saveItem.addActionListener(controller);
		loadItem.addActionListener(controller);
		colorItem.addActionListener(controller);
		showSolutionItem.addActionListener(controller);
		
		logoButton.addActionListener(controller);
		
		closeButton.addActionListener(controller);
		
		editorButton.addActionListener(controller);
		
		startButton.addActionListener(controller);
		
		for (int i = 0; i < buttonGrid.length; i++)
		{
			buttonGrid[i].addActionListener(controller);
		}
	}
	
	/**
	 * Removes all listeners in the game view from the controller
	 * 
	 * @param controller Action listener used in the GameController
	 */
	public void removeController(ActionListener controller)
	{
		newGameItem.removeActionListener(controller);
		saveItem.removeActionListener(controller);
		loadItem.removeActionListener(controller);
		colorItem.removeActionListener(controller);
		showSolutionItem.removeActionListener(controller);
		
		logoButton.removeActionListener(controller);
		
		closeButton.removeActionListener(controller);
		
		editorButton.removeActionListener(controller);
		
		startButton.removeActionListener(controller);
		
		for (int i = 0; i < buttonGrid.length; i++)
		{
			buttonGrid[i].removeActionListener(controller);
		}
	}
}
