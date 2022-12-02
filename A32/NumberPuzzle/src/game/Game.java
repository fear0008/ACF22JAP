/**
 * Filename: 		Game.java
 * Identification:	[Joshua Fearnall, 041019251]
 * Course:			CST 8221 - JAP, Lab Section: 302
 * Assignment:		A32
 * Professor:		Paulo Sousa
 * Date:			2022-11-28
 * Compiler:		Eclipse IDE for Java Developers - Version: 2022-06 (Java 17.0.4.1)
 * Purpose:			Class used to initialize the Number Puzzle game
 */
package game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Class Name: 	Game
 * Methods: 	main
 * Constants:	
 * Purpose:		Main entry point for the program
 * 
 * @author 	Joshua Fearnall
 * @version 3
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
		LauncherController controller = new LauncherController();
		controller.initializeGui();
	}
	
	/**
	 * Controller for the game launcher
	 */
	static class LauncherController implements ActionListener
	{
		/** JButton used for start button */
		JButton startButton;
		/** Combobox used to select type to launch*/
		JComboBox<String> launcherComboBox;
		
		/**
		 * Initialize the GUI for the numpuz launcher
		 */
		public void initializeGui()
		{
			JFrame frame = new JFrame();
			String[] launcherOptions = {"Client", "Server", "MVC"};
			
			
			// reset layout
			frame.getContentPane().removeAll();
			frame.revalidate();
			frame.repaint();
			
			// frame settings
			frame.setTitle("Number Puzzle - Server");
			frame.setSize(500, 400);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
			frame.setLayout(new BorderLayout());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			// top border
			JPanel topPanel = new JPanel();
			topPanel.setPreferredSize(new Dimension(100, 20));
			frame.add(topPanel, BorderLayout.NORTH);
			
			// left border
			JPanel leftPanel = new JPanel();
			leftPanel.setPreferredSize(new Dimension(20, 100));
			frame.add(leftPanel, BorderLayout.WEST);
			
			// center
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
			
			JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			JLabel titleLabel = new JLabel("NUMPUZ LAUNCHER");
			titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
			
			JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			
			launcherComboBox = new JComboBox<>(launcherOptions);
			startButton = new JButton("Start");

			// add controls to the title panel
			titlePanel.add(titleLabel);
			
			// add controls to the options panel
			optionsPanel.add(launcherComboBox);
			optionsPanel.add(startButton);
			
			// add panels to center panel
			centerPanel.add(titlePanel);
			centerPanel.add(optionsPanel);
			
			// add center panel to frame
			frame.add(centerPanel);
			
			// right border
			JPanel rightPanel = new JPanel();
			rightPanel.setPreferredSize(new Dimension(20, 100));
			frame.add(rightPanel, BorderLayout.EAST);
			
			// bottom border
			JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			bottomPanel.setPreferredSize(new Dimension(100, 20));
			frame.add(bottomPanel, BorderLayout.SOUTH);
			
			addController();
			frame.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == startButton)
			{
				String item = (String) launcherComboBox.getSelectedItem();
				
				if (item == "Client")
				{
					GameClient gameClient = new GameClient();
					gameClient.start();
				}
				else if (item == "Server")
				{
					GameServer gameServer = new GameServer();
					gameServer.start();
				}
				else if (item == "MVC")
				{
					GameModel gameModel = new GameModel();
					GameView gameView = new GameView();
					new GameController(gameModel, gameView, "");
				}
			}
		}
		
		/**
		 * Add controller to button
		 */
		private void addController()
		{
			startButton.addActionListener(this);
		}
	}
}
