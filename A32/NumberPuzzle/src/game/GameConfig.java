/**
 * Filename: 		GameConfig.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A32
 * Professor:		Paulo Sousa
 * Date:			2022-11-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the game config in MVC
 */
package game;

/**
 * Class Name: 	GameConfig
 * Methods: 	
 * Constants:	PROTOCOL_SEPARATOR, PROTOCOL_END, PROTOCOL_SEND_SOLUTION, PROTOCOL_REQUEST_SOLUTION, PROTOCOL_SEND_DATA,
 * Purpose:		Constant data for the numpuz client and server programs
 * 
 * @author 	Joshua Fearnall
 * @version 3
 * @see 	game
 * @since 	15
 *
 */
public class GameConfig 
{
	/** Separates data in the protocol */
	static final String PROTOCOL_SEPARATOR = "#";
	/** Protocol sent when client is disconnecting from the server*/
	static final String PROTOCOL_END = "0";
	/** Sent when the client is sending a new solution */
	static final String PROTOCOL_SEND_SOLUTION = "1";
	/** Sent when the client is requesting a new solution */
	static final String PROTOCOL_REQUEST_SOLUTION = "2";
	/** Sent when the client is sending game results */
	static final String PROTOCOL_SEND_DATA = "3";
	
	/** The default user name for the client */
	static String DEFAULT_USER = "Paulo";
	/** The default address for the server */
	static String DEFAULT_ADDR = "localhost";
	/** The default port for the server*/
	static int DEFAULT_PORT = 12345;
	
	/** The default solution for the game*/
	static String DEFAULT_SOLUTION = "3,Number,1,2,3,4,5,6,7,8,";
}
