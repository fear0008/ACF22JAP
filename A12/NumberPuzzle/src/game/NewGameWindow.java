/**
 * Filename: 		NewGameWindow.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A12
 * Professor:		Paulo Sousa
 * Date:			2022-09-27
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to create a new game window for the Number Puzzle game from the options menu
 */
package game;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class Name: 	NewGameWindow
 * Methods: 	actionPerformed
 * Constants: 	serialVerionUID
 * Purpose: 	Opens a window where the user can select settings for their next game
 * 
 * @author 	Joshua Fearnall
 * @version 1
 * @see 	game
 * @since 	15
 *
 */
public class NewGameWindow extends JFrame implements ActionListener
{
	/**
	 * Serial Version
	 */
	private static final long serialVersionUID = 1L;
	
	// variables needed for start button action
	/** Start button used to begin a new game with the given settings*/
	private JButton startButton;		
	/** Combo box for game mode selection*/
	private JComboBox<String> modeBox;
	/** Combo box for board size selection*/
	private JComboBox<Integer> dimensionBox;
	/** Combo box for game type selection*/
	private JComboBox<String> typeBox;
	
	// constants used for game settings
	/** the game modes a user can select*/
	private String gameModes[] = {"Design", "Play"};
	/** the board sizes a user can select*/
	private Integer dimensions[] = {3,4,5};
	/** the game types a user can select*/
	private String gameTypes[] = {"Number", "Text"};
	
	/**
	 * Method Name: NewGameWindow
	 * Purpose:		The default constructor used for the NewGameWindow
	 * Algorithm:	Creates and modifies each control for the window, then makes it visible
	 */
	public NewGameWindow()
	{
		// defining new game window
		this.setTitle("New Game");
		this.setSize(400, 184);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
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
		startButton = new JButton("Start");
		startButton.addActionListener(this);
		
		// adding all components to various panels
		modePanel.add(modeLabel);
		modePanel.add(modeBox);
		dimensionPanel.add(dimensionLabel);
		dimensionPanel.add(dimensionBox);
		typePanel.add(typeLabel);
		typePanel.add(typeBox);
		startPanel.add(startButton);
		
		// adding panels to the frame
		this.add(modePanel);
		this.add(dimensionPanel);
		this.add(typePanel);
		this.add(startPanel);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{

		if (e.getSource() == startButton)
		{		
			// action when start button is pressed, starts a new game with the given settings
			int boardSize = (int)dimensionBox.getSelectedItem();
			String gameType = (String)typeBox.getSelectedItem();
			String gameMode = (String)modeBox.getSelectedItem();
			
			new GameController(boardSize, gameType, gameMode);
			this.dispose();
		}
	}
}
