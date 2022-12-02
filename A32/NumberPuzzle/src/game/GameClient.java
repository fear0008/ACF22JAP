/**
 * Filename: 		GameClient.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A32
 * Professor:		Paulo Sousa
 * Date:			2022-11-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the game client in MVC
 */
package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Class Name: 	GameClient
 * Methods: 	start
 * Constants:	serialVersionUID
 * Purpose:		GUI and actions for the numpuz client program
 * 
 * @author 	Joshua Fearnall
 * @version 3
 * @see 	game
 * @since 	15
 *
 */
public class GameClient extends JFrame
{
	/** Serial Version*/
	private static final long serialVersionUID = 1L;
	// interface variables
	/** Button for ending client */
	JButton endButton;
	/** Button for connecting to server */
	JButton connectButton;
	/** Button for starting a new game */
	JButton newGameButton;
	/** Button for sending game to server */
	JButton sendGameButton;
	/** button for receiving game from server */
	JButton receiveGameButton;
	/** button for sending data to server */
	JButton sendDataButton;
	/** button for playing game */
	JButton playButton;
	/** Text area for message log */
	JTextArea logTextArea;
	/** Text are for user name*/
	JTextArea userNameTextArea;
	
	// client variables
	/** socket for the client */
	Socket clientSocket = null;
	/** input stream from the server */
	DataInputStream input = null;
	/** output stream to the server */
	DataOutputStream output = null;
	/** the clients ID assigned from the server */
	int clientId = -1;
	/** true if the client is connected to the server */
	boolean isConnectedToServer = false;
	/** the current game solution */
	String currentSolution = GameConfig.DEFAULT_SOLUTION;
	/** the current game result */
	String gameData = "";
	/** true if the client has started a game */
	boolean isPlayingGame = false;
	
	// MVC variables
	/** game model from the MVC */
	GameModel gameModel;
	
	
	/**
	 * Initializes the GUI for the game client
	 */
	public void start()
	{
		// reset layout
		this.getContentPane().removeAll();
		this.revalidate();
		this.repaint();
		
		// frame settings
		this.setTitle("Number Puzzle - Client");
		this.setSize(600, 500);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// top border
		JPanel topPanel = new JPanel();
		topPanel.setPreferredSize(new Dimension(100, 20));
		this.add(topPanel, BorderLayout.NORTH);
		
		// left border
		JPanel leftPanel = new JPanel();
		leftPanel.setPreferredSize(new Dimension(20, 100));
		this.add(leftPanel, BorderLayout.WEST);
		
		// center
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		
		JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JLabel titleLabel = new JLabel("NUMPUZ CLIENT");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
		
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JLabel userNameLabel = new JLabel("User: ");
		userNameTextArea = new JTextArea(GameConfig.DEFAULT_USER);
		userNameTextArea.setPreferredSize(new Dimension(75, 17));
		JLabel ipLabel = new JLabel("Server: ");
		JTextArea ipTextArea = new JTextArea(GameConfig.DEFAULT_ADDR);
		ipTextArea.setPreferredSize(new Dimension(75, 17));
		JLabel portLabel = new JLabel("Port: ");
		JTextArea portTextArea = new JTextArea(Integer.toString(GameConfig.DEFAULT_PORT));
		portTextArea.setPreferredSize(new Dimension(50, 17));
		
		connectButton = new JButton("Connect");
		endButton = new JButton("End");
		newGameButton = new JButton("New game");
		sendGameButton = new JButton("Send game");
		receiveGameButton = new JButton("Receive game");
		sendDataButton = new JButton("Send data");
		playButton = new JButton("Play");

		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		logTextArea = new JTextArea(10, 35);
		logTextArea.setLineWrap(true);
		logTextArea.setEditable(false);
		
		JScrollPane logScrollPane = new JScrollPane(logTextArea);
		
		// add controls to the title panel
		titlePanel.add(titleLabel);
		
		// add controls to the options panel
		optionsPanel.add(userNameLabel);
		optionsPanel.add(userNameTextArea);
		optionsPanel.add(ipLabel);
		optionsPanel.add(ipTextArea);
		optionsPanel.add(portLabel);
		optionsPanel.add(portTextArea);
		optionsPanel.add(connectButton);
		optionsPanel.add(endButton);
		optionsPanel.add(newGameButton);
		optionsPanel.add(sendGameButton);
		optionsPanel.add(receiveGameButton);
		optionsPanel.add(sendDataButton);
		optionsPanel.add(playButton);
		
		// add controls to the text panel
		textPanel.add(logScrollPane);
		
		// add panels to center panel
		centerPanel.add(titlePanel);
		centerPanel.add(optionsPanel);
		centerPanel.add(textPanel);
		
		// add center panel to frame
		this.add(centerPanel);
		
		// right border
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(20, 100));
		this.add(rightPanel, BorderLayout.EAST);
		
		// bottom border
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setPreferredSize(new Dimension(100, 20));
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		// CLIENT
		Client client = new Client();
		new ClientController(client);
		
		this.setVisible(true);
	}
	
	/**
	 * Class Name: 	ClientController
	 * Methods: 	actionPerformed, addController
	 * Constants:	
	 * Purpose:		Controller for numpuz client actions
	 * 
	 * @author 	Joshua Fearnall
	 * @version 3
	 * @see 	game
	 * @since 	15
	 *
	 */
	class ClientController implements ActionListener
	{
		/** client object for client data and actions */
		Client client;
		
		/**
		 * Client controller constructor
		 * @param client the current client object
		 */
		public ClientController(Client client)
		{
			this.client = client;
			addController();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == connectButton)
			{
				if (!isConnectedToServer)
					client.connectToServer();
			}
			else if (e.getSource() == endButton)
			{
				client.shutdownClient();
			}
			else if (e.getSource() == newGameButton)
			{
				client.createNewGame();
			}
			else if (e.getSource() == sendGameButton)
			{
				client.sendGame();
			}
			else if (e.getSource() == receiveGameButton)
			{
				client.requestGame();
			}
			else if (e.getSource() == sendDataButton)
			{
				client.sendData();
			}
			else if (e.getSource() == playButton)
			{
				client.playGame();
			}
		}
		
		/**
		 * Add listeners to objects
		 */
		private void addController()
		{
			endButton.addActionListener(this);
			connectButton.addActionListener(this);
			newGameButton.addActionListener(this);
			sendGameButton.addActionListener(this);
			receiveGameButton.addActionListener(this);
			sendDataButton.addActionListener(this);
			playButton.addActionListener(this);
		}
	}
	
	/**
	 * Class Name: 	Client
	 * Methods: 	connectToServer, shutdownClient, disconnectFromServer, createNewGame, sendGame, requestGame, sendData,
	 * 				playGame, sendMessageToServer
	 * Constants:	serialVersionUID
	 * Purpose:		Client actions for the numpuz client
	 * 
	 * @author 	Joshua Fearnall
	 * @version 3
	 * @see 	game
	 * @since 	15
	 *
	 */
	class Client
	{
		/**
		 * Connects the client to the server
		 */
		public void connectToServer()
		{
			try
			{
				clientSocket = new Socket(GameConfig.DEFAULT_ADDR, GameConfig.DEFAULT_PORT);
				output = new DataOutputStream(clientSocket.getOutputStream());
				input = new DataInputStream(clientSocket.getInputStream());
				// receive client id
				clientId = input.readInt();
				
				isConnectedToServer = true;
				logTextArea.insert("Connected to server\nID = " + clientId +"\n", logTextArea.getDocument().getLength());
			}
			catch (UnknownHostException uhe) 
			{
	            System.out.println("Unknown Host Exception: " + uhe);
	        } 
			catch (IOException ioe) 
			{
				logTextArea.insert("Server offline...\n", logTextArea.getDocument().getLength());
	        }
		}
		
		/**
		 * Shuts the client down
		 */
		public void shutdownClient()
		{
			if (isConnectedToServer)
			{
				sendMessageToServer(GameConfig.PROTOCOL_END, "");
				disconnectFromServer();
			}

			dispose();
		}
		
		/**
		 * Disconnects the client from the server
		 */
		private void disconnectFromServer()
		{
	        try 
	        {
	            output.close();
	            clientSocket.close();
	        } 
	        catch (IOException ioe) 
	        {
	            System.out.println("IO Exception: " + ioe);
	        }
		}
		
		/**
		 * Generates a new game for the client
		 */
		private void createNewGame()
		{
			// generate random number from 3 to 5
			Random rand = new Random();
			int dimension = rand.ints(3, 6).findAny().getAsInt();
			
			// pass settings for random game as current solution
			switch (dimension)
			{
				case 4:
					currentSolution = "4,Number,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,";
					break;
				case 5:
					currentSolution = "5,Number,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24";
					break;
				case 3:
				default:
					currentSolution = "3,Number,1,2,3,4,5,6,7,8,";
					break;
			}
			
			// log the new solution
			logTextArea.insert("New solution generated: " + currentSolution + "\n", logTextArea.getDocument().getLength());
		}
		
		/**
		 * Sends the current game stored in the client to the server
		 */
		private void sendGame()
		{
			if (isConnectedToServer)
			{
				if (sendMessageToServer(GameConfig.PROTOCOL_SEND_SOLUTION, currentSolution)) {
					// log success
					try {
						String response = input.readUTF();
						logTextArea.insert("Solution sent to server!\n", logTextArea.getDocument().getLength());
						logTextArea.insert(response, logTextArea.getDocument().getLength());
					} catch (IOException e) {
						logTextArea.insert("Connection with the server was interrupted\n", logTextArea.getDocument().getLength());
						isConnectedToServer = false;
					}
				}
			}
			else
			{
				// log failure
				logTextArea.insert("Not connected to server...\n", logTextArea.getDocument().getLength());
			}
		}
		
		/**
		 * requests a game from the server
		 */
		private void requestGame()
		{
			if (isConnectedToServer)
			{
				logTextArea.insert("Requesting solution from server...\n", logTextArea.getDocument().getLength());
				
				if (sendMessageToServer(GameConfig.PROTOCOL_REQUEST_SOLUTION, "")) {
					// Receive solution from server
					try {
						currentSolution = input.readUTF();
						logTextArea.insert("Received solution from server.\nNew solution is: \n" + currentSolution +"\n", logTextArea.getDocument().getLength());
					} catch (IOException e) {
						logTextArea.insert("Connection with the server was interrupted\n", logTextArea.getDocument().getLength());
						isConnectedToServer = false;
					}
				}
			}
			else
			{
				// log failure
				logTextArea.insert("Not connected to server...\n", logTextArea.getDocument().getLength());
			}
		}
		
		/**
		 * sends game data to the server
		 */
		private void sendData()
		{
			if (isConnectedToServer && gameModel != null)
			{
				gameData = userNameTextArea.getText();
				gameData += gameModel.getResults();
				if (sendMessageToServer(GameConfig.PROTOCOL_SEND_DATA, gameData)) {
					// log success
					try {
						String response = input.readUTF();
						logTextArea.insert("Sent game data to server\n", logTextArea.getDocument().getLength());
						logTextArea.insert(response + "\n", logTextArea.getDocument().getLength());
					} catch (IOException e) {
						logTextArea.insert("Connection with the server was interrupted\n", logTextArea.getDocument().getLength());
						isConnectedToServer = false;
					}
				}
			}
			else
			{
				// log failure
				logTextArea.insert("Not connected to server or no game data\n", logTextArea.getDocument().getLength());
			}
		}
		
		/**
		 * starts a new game
		 */
		private void playGame()
		{
			if (!isPlayingGame)
			{
				gameModel = new GameModel();
				GameView gameView = new GameView();
				new GameController(gameModel, gameView, currentSolution);
				isPlayingGame = true;
			}
		}
		
		/**
		 * sends a message to the server
		 * @param protocolId the protocol being used in the message
		 * @param data the data being sent in the message
		 * @return true if the output was sent successfully
		 */
		private boolean sendMessageToServer(String protocolId, String data)
		{
			try {
				output.writeUTF(clientId + GameConfig.PROTOCOL_SEPARATOR + 
								protocolId + GameConfig.PROTOCOL_SEPARATOR +
								data + GameConfig.PROTOCOL_SEPARATOR);
				return true;
			} catch (IOException e) {
				logTextArea.insert("Connection with the server was interrupted\n", logTextArea.getDocument().getLength());
				isConnectedToServer = false;
				return false;
			}
		}
		
	}
}
