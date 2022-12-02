/**
 * Filename: 		GameController.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A32
 * Professor:		Paulo Sousa
 * Date:			2022-11-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the game controller in MVC
 */
package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

/**
 * Class Name: 	GameController
 * Methods: 	startGame, startTimer
 * Constants:	
 * Purpose:		Controller for the MVC model in the Number Puzzle game
 * 
 * @author 	Joshua Fearnall
 * @version 3
 * @see 	game
 * @since 	15
 *
 */
public class GameController
{	
	/** Controller used for MVC model*/
	private Controller controller;
	/** Model used for MVC model*/
	private GameModel gameModel;
	/** View used for MVC model*/
	private GameView gameView;
	
	/** Time in seconds; used with the timer object*/
	private int seconds = 0;
	/** Timer used for timing various tasks*/
	Timer timer = new Timer();
	/** Timer Task, used in conjunction with the timer*/
	TimerTask timerTask;
	
	/**
	 * Method Name: GameController
	 * Purpose:		Constructor for controller, initializes gameview settings
	 * Algorithm:
	 * 
	 * @param gameModel The model used for MVC
	 * @param gameView The view used for MVC
	 * @param newSolution The default solution used in the game
	 */
	public GameController(GameModel gameModel, GameView gameView, String newSolution)
	{
		// initializing model and view variables
		this.gameModel = gameModel;
		this.gameView = gameView;
		
		gameView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if (!newSolution.equals(""))
		{
			gameModel.setSolution(newSolution);
		}
		
		// instantiating a controller
		controller = new Controller();
		
		// initializing game
		gameView.openSplashScreen();
		gameView.initializeGame(gameModel.getDimension(), gameModel.getMode(), gameModel.getType(), gameModel.getSolution());
		gameView.addController(controller);
		
		if (gameModel.getMode() == "Play")
		{
			startGame();
		}
	}
	
	/**
	 * Class Name: 	Controller
	 * Methods:		actionPerformed
	 * Constants: 	serialVersionUID
	 * Purpose:		Used to control actions triggered in the game window
	 * 
	 * @author 	Joshua Fearnall
	 * @version 2
	 * @see 	game
	 * @since 	15
	 *
	 */
	class Controller implements ActionListener, Serializable
	{
		/** Serial Version*/
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() == gameView.newGameItem)
			{
				// triggered when the user selects the new game menu option
				gameView.openNewGameWindow();
			}
			else if (e.getSource() == gameView.editorButton)
			{
				// triggered when the user sets a new text setting 
				gameModel.setSolution(gameView.editorTextField.getText());
				gameView.updateTiles(gameModel.getSolution());
			}
			else if (e.getSource() == gameView.logoButton)
			{
				// triggered when the user clicks on the logo button
				gameView.openAbout();
			}
			else if (e.getSource() == gameView.closeButton)
			{
				// triggered when the user presses the close button on the about dialog
				gameView.aboutDialog.dispose();
			}
			else if (e.getSource() == gameView.startButton)
			{
				// triggered when the user presses the start game button on the new game panel
				gameModel.setDimension((int)gameView.dimensionBox.getSelectedItem());
				gameModel.setType((String)gameView.typeBox.getSelectedItem());
				gameModel.setMode((String)gameView.modeBox.getSelectedItem());
				
				startGame();
			}
			else if (e.getSource() == gameView.colorItem)
			{
				// triggered when the user selects the color menu item
				gameView.setNewColor();
			}
			else if (e.getSource() == gameView.saveItem)
			{
				// triggered when the user selects the save menu item
				gameView.saveLayout(gameModel.getSolution());
			}
			else if (e.getSource() == gameView.loadItem)
			{
				// triggered when the user selects the load menu item
				String layout = gameView.loadLayout();
				
				try 
				{
					// when the user selects a valid file to load
					if (layout != "")
					{
						// use tokenizer
						StringTokenizer st;
						st = new StringTokenizer(layout, ",");
						
						// set dimension
						gameModel.setDimension(Integer.parseInt(st.nextToken()));
						
						// set type
						gameModel.setType(st.nextToken());
						
						// set solution
						String newSolution = "";
						while (st.hasMoreTokens())
						{
							newSolution += st.nextToken();
						}
						
						gameModel.setSolution(newSolution);
						
						// set mode
						gameModel.setMode("Play");
						
						// start game
						startGame();
					}
				}
				catch(Exception ex)
				{
					System.err.println("File not found / invalid file");
				}

			}
			else if (e.getSource() == gameView.showSolutionItem)
			{
				// stop timer
				if (timerTask != null)
				{
					timerTask.cancel();
				}

				// open dialog
				gameView.openDialog("finished");
				// record results
				gameModel.setResults(", points: " + gameModel.getScore() + ", time: " + seconds);
				// show the solution
				gameView.showSolution();
				// prevent further actions on the board
				gameView.disableBoard();
			}
			else 
			{
				if (gameModel.getMode() == "Play")
				{
					// trying to move a tile
					boolean tileMoved = gameView.moveTile(e.getSource(), gameModel.getDimension());
					
					// when a tile is moved
					if (tileMoved)
					{
						// increase move count
						gameModel.increaseMoves();
						gameView.updateMoves(gameModel.getMoves());
						
						// check and update current score
						int currentScore = gameView.calculateScore(gameModel.getSolution());
						gameModel.setScore(currentScore);
						gameView.updateScore(currentScore);
						
						// get the number of tiles on the board
						int numberOfTiles = gameModel.getDimension();
						numberOfTiles = (numberOfTiles * numberOfTiles) - 1;
						
						// check if the player has won the game
						if (currentScore == numberOfTiles)
						{
							// stop timer
							if (timerTask != null)
							{
								timerTask.cancel();
							}

							// show winner
							gameView.openDialog("win");
							gameModel.setResults("points: " + gameModel.getScore() + ", time: " + seconds);
							// prevent further actions on the board
							gameView.disableBoard();
						}
					}
				}
			}
		}
	}
	
	/**
	 * Initializes a game board when a new game is started.
	 */
	public void startGame()
	{
		// remove/add event controllers and initialize board
		gameView.removeController(controller);
		gameView.initializeGame(gameModel.getDimension(), gameModel.getMode(), gameModel.getType(), gameModel.getSolution());
		gameView.addController(controller);
		
		// setup board and game settings when entering play mode
		if (gameModel.getMode() == "Play")
		{
			gameView.shuffleBoard();
			
			gameModel.setMoves(0);
			
			int startingScore = gameView.calculateScore(gameModel.getSolution());
			gameView.updateScore(startingScore);
			gameModel.setScore(startingScore);
			
			seconds = 0;
			seconds = (gameModel.getDimension() * gameModel.getDimension()) * 5;
			startTimer();
		}
	}
	
	/**
	 * Starts a timer used to track time during splash screen and gameplay
	 */
	private void startTimer() 
	{
		// cancel timer when one is already running
		if (timerTask != null) 
		{
			timerTask.cancel();
		}

		// Timer task
		timerTask = new TimerTask() 
		{
			public void run() 
			{
				// decrement seconds
				seconds--;

				// Update your interface
				gameView.updateTime(seconds);
				
				// check if the player has lost the game
				if (seconds <= 0)
				{
					// stop timer
					timerTask.cancel();
					// show loser
					gameView.openDialog("lose");
					gameModel.setResults("points: " + gameModel.getScore() + ", time: " + seconds);
					// prevent actions on board
					gameView.disableBoard();
				}
			}
		};
		try 
		{
			timer.scheduleAtFixedRate(timerTask, 0, 1000);
		} catch(Exception e) 
		{
			e.printStackTrace();
		}
	}
}
