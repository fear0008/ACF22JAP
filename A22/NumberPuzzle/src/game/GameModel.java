/**
 * Filename: 		GameModel.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A12
 * Professor:		Paulo Sousa
 * Date:			2022-10-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the game model in MVC
 */
package game;

/**
 * Class Name: 	GameController
 * Methods: 	getScore, setScore, getSolution, setSolution, getDimension, setDimension, getMode, setMode,
 * 				getType, setType, getMoves, setMoves, increaseMoves
 * Constants:	
 * Purpose:		model for the MVC model in the Number Puzzle game
 * 
 * @author 	Joshua Fearnall
 * @version 2
 * @see 	game
 * @since 	15
 *
 */
public class GameModel 
{
	/** The dimension of the puzzle*/
	private int dimension = 3;
	/** The number of moves the player has taken*/
	private int moves = 0;
	/** The players current score*/
	private int score = 0;
	/** The mode the player has currently selected (Design or Play)*/
	private String gameMode = "Design";
	/** The puzzle type for the game (Number or Text)*/
	private String gameType = "Number";
	/** The default puzzle solution (dimension,type,solution,)*/
	private String solution = "3,Number,1,2,3,4,5,6,7,8,";

	/**
	 * Accessor for the players score
	 * @return The players current score
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * Mutator for the players score
	 * @param score The players new score
	 */
	public void setScore(int score)
	{
		this.score = score;
	}
	
	/**
	 * Returns the current solution (for text) or generates a number solution and returns it
	 * @return The solution for the puzzle in a (dimension,type,solution,) CSV formatted string
	 */
	public String getSolution()
	{
		// generating a solution for a number based game
		if (gameType == "Number")
		{
			String numberSolution = dimension + "," + gameType + ",";
			int numberOfTiles = dimension * dimension;
			
			for (int i = 1; i < numberOfTiles; i++)
			{
				numberSolution += Integer.toString(i) + ",";
			}
			
			return numberSolution;
		}
		
		// returning solution for a character based game
		return solution;
	}
	
	/**
	 * Takes a string from the user and returns a (dimension,type,solution,) CSV formatted string
	 * @param newSolution The solution provided by the user
	 */
	public void setSolution(String newSolution)
	{
		int numberOfTiles = dimension * dimension;
		
		// creating a CSV formatted solution string
		String s = dimension + "," + gameType + ",";
		for (int i = 0; i < numberOfTiles; i++)
		{
			if (i < newSolution.length())
			{
				s += newSolution.charAt(i) + ",";
			}
			else
			{
				s += " ,";
			}
		}
		
		solution = s;
	}
	
	/**
	 * Accessor for board dimension
	 * @return The current board dimension
	 */
	public int getDimension()
	{
		return dimension;
	}
	
	/**
	 * Mutator for board dimension
	 * @param newDimension The new dimension for the board
	 */
	public void setDimension(int newDimension)
	{
		dimension = newDimension;
	}
	
	/**
	 * Accessor for game mode
	 * @return The current game mode (Design or Play)
	 */
	public String getMode()
	{
		return gameMode;
	}
	
	/**
	 * Mutator for game mode
	 * @param newMode The new game mode (Design or Play)
	 */
	public void setMode(String newMode)
	{
		gameMode = newMode;
	}
	
	/**
	 * Accessor for game type
	 * @return The current game type (Number or Text)
	 */
	public String getType()
	{
		return gameType;
	}
	
	/**
	 * Mutator for game type
	 * @param newType The new game type (Number or Text)
	 */
	public void setType(String newType)
	{
		gameType = newType;
	}
	
	/**
	 * Accessor for game moves
	 * @return The current number of moves taken
	 */
	public int getMoves()
	{
		return moves;
	}
	
	/**
	 * Mutator for game moves
	 * @param moves The new number of moves taken
	 */
	public void setMoves(int moves)
	{
		this.moves = moves;
	}
	
	/**
	 * Increases the number of moves taken by 1
	 */
	public void increaseMoves()
	{
		moves++;
	}
}
