/**
 * Filename: 		GameServer.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A32
 * Professor:		Paulo Sousa
 * Date:			2022-11-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to implement the game server in MVC
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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Class Name: 	GameServer
 * Methods: 	start
 * Constants:	
 * Purpose:		GUI and actions for the numpuz server program
 * 
 * @author 	Joshua Fearnall
 * @version 3
 * @see 	game
 * @since 	15
 *
 */
public class GameServer extends JFrame 
{
	/** Serial version */
	private static final long serialVersionUID = 1L;
	// server variables
	/** The socket used by the server */
	ServerSocket serverSocket = null;
	/** The socket used by the client */
	Socket clientSocket = null;
	/** The output stream for the server */
    DataOutputStream output = null;
    /** true if the server is running */
	boolean isServerRunning = false;
	/** The historic number of clients served by this server */
	int totalClients = 0;
	/** The number of clients currently served by the server */
	int currentClients = 0;
	/** The current solution held by the server */
	String currentSolution = GameConfig.DEFAULT_SOLUTION;
	
	// interface variables
	/** Button used to end the server */
	JButton endButton;
	/** Button used to start the server */
	JButton startButton;
	/** Button used to show game results */ 
	JButton resultsButton;
	/** Text area for log messages */
	JTextArea logTextArea;
	/** Checkbox used to finalize the server*/
	JCheckBox finalizeCheckBox;
	
	/**
	 * Initializes the GUI for the game server
	 */
	public void start()
	{
		// reset layout
		this.getContentPane().removeAll();
		this.revalidate();
		this.repaint();
		
		// frame settings
		this.setTitle("Number Puzzle - Server");
		this.setSize(500, 400);
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
		JLabel titleLabel = new JLabel("NUMPUZ SERVER");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
		
		JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JLabel portLabel = new JLabel("Port: ");
		
		JTextField portTextField = new JTextField(Integer.toString(GameConfig.DEFAULT_PORT));
		portTextField.setPreferredSize(new Dimension(50, 17));
		
		startButton = new JButton("Start");
		resultsButton = new JButton("Results");
		finalizeCheckBox = new JCheckBox("Finalize");
		endButton = new JButton("End");

		JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		logTextArea = new JTextArea(10, 35);
		logTextArea.setLineWrap(true);
		logTextArea.setEditable(false);
		
		JScrollPane logScrollPane = new JScrollPane(logTextArea);
		
		// add controls to the title panel
		titlePanel.add(titleLabel);
		
		// add controls to the options panel
		optionsPanel.add(portLabel);
		optionsPanel.add(portTextField);
		optionsPanel.add(startButton);
		optionsPanel.add(resultsButton);
		optionsPanel.add(finalizeCheckBox);
		optionsPanel.add(endButton);
		
		// add controls to the text panel
		textPanel.add(logScrollPane);
		
		centerPanel.add(titlePanel);
		centerPanel.add(optionsPanel);
		centerPanel.add(textPanel);
		
		this.add(centerPanel);
		
		// right border
		JPanel rightPanel = new JPanel();
		rightPanel.setPreferredSize(new Dimension(20, 100));
		this.add(rightPanel, BorderLayout.EAST);
		
		// bottom border
		JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.setPreferredSize(new Dimension(100, 20));
		this.add(bottomPanel, BorderLayout.SOUTH);
		
		Server server = new Server();
		new ServerController(server);
		
		this.setVisible(true);
	}
	
	/**
	 * Class Name: 	ServerController
	 * Methods: 	actionPerformed, addController
	 * Constants:	
	 * Purpose:		Controller used for server actions
	 * 
	 * @author 	Joshua Fearnall
	 * @version 3
	 * @see 	game
	 * @since 	15
	 *
	 */
	class ServerController implements ActionListener
	{
		/** the current server object */
		Server server;
		
		/**
		 * Constructor for the server object
		 * @param server the server currently used
		 */
		public ServerController(Server server)
		{
			this.server = server;
			addController();
		}
		
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource() == startButton)
			{
				if (!isServerRunning)
					server.startServer();
			}
			else if (e.getSource() == endButton)
			{
				server.shutdownServer();
			}
			else if (e.getSource() == resultsButton)
			{
				server.displayResults();
			}
		}
		
		/**
		 * adding action listeners to the JObjects
		 */
		private void addController()
		{
			startButton.addActionListener(this);
			endButton.addActionListener(this);
			resultsButton.addActionListener(this);
		}
	}
	
	/**
	 * Class Name: 	Server
	 * Methods: 	startServer, shutdownServer, displayResults, waitForConnection
	 * Constants:	
	 * Purpose:		Server method implementations
	 * 
	 * @author 	Joshua Fearnall
	 * @version 3
	 * @see 	game
	 * @since 	15
	 *
	 */
	class Server implements Runnable
	{
		/** list of game client threads currently running */
		ArrayList<GameClientThread> clientList = new ArrayList<GameClientThread>(5);
		/** list of game results */
		ArrayList<String> gameResults = new ArrayList<String>(5);
		
		@Override
		public void run() {
	        waitForConnection();
		}
		
		/**
		 * starting the game server 
		 */
		public void startServer()
		{
			logTextArea.insert("Starting server...\n", logTextArea.getDocument().getLength());
	        try 
	        {
	            serverSocket = new ServerSocket(GameConfig.DEFAULT_PORT);
	            logTextArea.insert("Server started\n", logTextArea.getDocument().getLength());
	            isServerRunning = true;
	            Thread serverDaemon = new Thread(this);
	            serverDaemon.start();
	        } 
	        catch (IOException ioe) 
	        {
	            System.out.println("IO Exception: " + ioe);
	        }
		}
		
		/**
		 * shutting the game server down
		 */
		private void shutdownServer()
		{
			try 
			{
				if (isServerRunning)
				{
					isServerRunning = false;
					// close client connections
					for (int i = clientList.size() - 1; i >= 0; i--)
					{
						clientList.get(i).closeIO();
					}
					
					// close server connection
					serverSocket.close();
				}
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			dispose();
		}
		
		/**
		 * displaying game results on the server
		 */
		public void displayResults()
		{
			if (gameResults.size() > 0)
			{
				String results = "";
				for (int i = 0; i < gameResults.size(); i++)
				{
					results += gameResults.get(i) + "\n";
				}
				
				JOptionPane.showMessageDialog(null, results, "Results", JOptionPane.PLAIN_MESSAGE);
			}
			else
			{
				logTextArea.insert("No game data, cannot print game data\n", logTextArea.getDocument().getLength());
			}
		}
		
		/**
		 * waiting for client connections
		 */
	    public void waitForConnection() 
	    {
	        while (isServerRunning) 
	        {
		    	logTextArea.insert("Waiting for client connection...\n", logTextArea.getDocument().getLength());
	            try 
	            {
            		clientSocket = serverSocket.accept();
	            } 
	            catch (IOException ioe) 
	            {
	            	// break out when the server is not running
	            	if (!isServerRunning)
	            		return;
	            	
	                System.out.println("IO Exception: " + ioe);
	            }
	            
	            GameClientThread newThread = new GameClientThread(clientSocket);
	            newThread.start();
	            clientList.add(newThread);
	        }
	    }
	    
		/**
		 * Class Name: 	GameClientThread
		 * Methods: 	clientDisconnected, closeIO, receiveClientSolution, sendServerSolution, receiveClientData
		 * Constants:	serialVersionUID
		 * Purpose:		Threads used to host each game client, and actions for them
		 * 
		 * @author 	Joshua Fearnall
		 * @version 3
		 * @see 	game
		 * @since 	15
		 *
		 */
	    class GameClientThread extends Thread 
	    {
	    	/** the data sent from the client*/
	    	String data = "";
	    	/** input stream to receive data from the client */
	        DataInputStream input = null;
	        /** output stream to send data to the client*/
	        DataOutputStream output = null;
	        /** socket for the thread */
	        Socket threadSocket = null;
	        
	        /**
	         * Constructor for the game client thread
	         * @param clientSocket the socket used by the client
	         */
	        public GameClientThread(Socket clientSocket) 
	        {	
	            this.threadSocket = clientSocket;
	            totalClients++;
	            currentClients++;
	            
	        	try 
	        	{
					input = new DataInputStream(clientSocket.getInputStream());
					output = new DataOutputStream(clientSocket.getOutputStream());
					
					output.writeInt(totalClients);
				} 
	        	catch (IOException e) 
	        	{
					e.printStackTrace();
				}
	        	
	            logTextArea.insert("Client ID: " + totalClients + " connected to server\n", logTextArea.getDocument().getLength());
	        }
	        
	        /**
	         * Called when the client disconnects gracefully from the server
	         * @param clientId The client ID of the caller
	         */
	        private void clientDisconnected(String clientId)
	        {
        		// protocol 0: Client disconnected from server
				logTextArea.insert("Client ID: " + clientId + " disconnected from server\n", logTextArea.getDocument().getLength());
				
				// remove client from list and decrement client count
				clientList.remove(clientList.indexOf(this));
				currentClients--;
				
				// close all IO
				closeIO();
				
				if (currentClients == 0 && finalizeCheckBox.isSelected())
					shutdownServer();
	        }
	        
	        /**
	         * Called when closing IO with the client
	         */
	        public void closeIO()
	        {
				try {
					output.close();
					input.close();
					threadSocket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        
	        /**
	         * Called when receiving a solution from the client
	         * @param clientData the data sent from the client
	         */
	        private void receiveClientSolution(String clientData)
	        {

	        	try {
					output.writeUTF("SERVER: solution received.");
		        	currentSolution = clientData;
		        	logTextArea.insert("Received solution from client\nNew solution is: " + currentSolution + "\n", logTextArea.getDocument().getLength());
				} catch (IOException e) {
					e.printStackTrace();
				}

	        }
	        
	        /**
	         * called when sending a solution to the client
	         * @param clientId The ID of the client to send to
	         */
	        private void sendServerSolution(String clientId)
	        {
	        	try {
					output.writeUTF(currentSolution);
					logTextArea.insert("Sent solution to client: #" + clientId + "\n", logTextArea.getDocument().getLength());
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
	        
	        /**
	         * called when receiving data from the client
	         * @param clientId The ID of the client sending data
	         * @param dataReceived The data sent from the client
	         */
	        private void receiveClientData(String clientId, String dataReceived)
	        {
	        	try {
	        		output.writeUTF("SERVER: client data received");
		        	data = "Player[" + clientId + "]: ";
		        	data += dataReceived;
		        	gameResults.add(data);
		        	logTextArea.insert("Received data from client: #" + clientId + "\n", logTextArea.getDocument().getLength());
	        	} catch (IOException e) {
					e.printStackTrace();
	        	}
	        }
	        
	        public void run() 
	        {
	        	String clientId = "";
	        	String protocolId = "";
	        	String dataReceived = "";
	        	
				StringTokenizer st;
	        	
            	try 
            	{
            		while(!protocolId.equals("0"))
            		{
        				data = input.readUTF();

    					logTextArea.insert(data + "\n", logTextArea.getDocument().getLength());
    					
    					st = new StringTokenizer(data, "#");
						clientId = st.nextToken();
						protocolId = st.nextToken();
						if (st.hasMoreTokens()) {
							dataReceived = st.nextToken();
						}
						
						switch(protocolId)
						{
							case "1":
								receiveClientSolution(dataReceived);
								break;
							case "2":
								sendServerSolution(clientId);
								break;
							case "3":
								receiveClientData(clientId, dataReceived);
								break;
						}
            		}
            		
            		clientDisconnected(clientId);
				} 
    			catch (SocketException e) {
    				logTextArea.insert("Client connection to server ended abruptly\n", logTextArea.getDocument().getLength());
    			}
            	catch (IOException e) 
            	{
					e.printStackTrace();
				}
            	
	        }
	    }
	}
	
}
