/**
 * Filename: 		Game.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A12
 * Professor:		Paulo Sousa
 * Date:			2022-10-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to initialize the Number Puzzle game
 */
package game;

/**
 * Class Name: 	Game
 * Methods: 	main
 * Constants:	
 * Purpose:		Main entry point for the program
 * 
 * @author 	Joshua Fearnall
 * @version 2
 * @see 	game
 * @since 	15
 *
 */
public class Game 
{
	/**
	 * Method Name:	main
	 * Purpose:		used to initialize the game
	 * Algorithm:
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args)
	{
		GameModel gameModel = new GameModel();
		GameView gameView = new GameView();
		new GameController(gameModel, gameView);
	}
}
