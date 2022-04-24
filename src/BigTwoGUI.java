import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.Border;

/**
 * This class implements the CardGameUI Interface. This class is to build
 * a GUI for the Big Two Card game and handle all user actions.
 * 
 * @author Richard Bryan
 *
 */
public class BigTwoGUI implements CardGameUI{
	// some constant (static & final) variables that I added
	// so that it is much easier to obtain the proper card image filename
	private static final String[] rankString = {"a", "2", "3", "4", "5", "6", "7", "8", "9", "t", "j", "q", "k"};
	private static final String[] suitString = {"d", "c", "h", "s"};
	
	// The Big Two card game associated with this GUI class.
	private BigTwo game;
	
	// A boolean array indicating which cards are being selected.
	private boolean[] selected;
	
	// An integer specifying the index of the active player.
	private int activePlayer;
	
	// The main window of the application for the GUI.
	private JFrame frame;
	
	/* A panel for showing the cards of each player and the cards 
	 played on the table. */
	private JPanel bigTwoPanel;
	
	// A “Play” button for the active player to play the selected cards.
	private JButton playButton;
	
	/* a “Pass” button for the active player to pass his/her turn 
	to the next player.*/
	private JButton passButton;
	
	// A text area for showing the current game status as well as end of game messages.
	private JTextArea msgArea;
	
	// A text area for showing chat messages sent by the players.
	private JTextArea chatArea;
	
	// A text field for players to input chat messages.
	private JTextField chatInput;
	
	/**
	 * The constructor for creating and returning an instance of the BigTwoGUI class.
	 * 
	 * @param game The BigTwo game object associated with this GUI class.
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		
		// creating the frame and its settings
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setTitle("Big Two");
		
		// creating the menu
		this.initializeMenu();
		
		
		// creating the main panel now
		// main panel has two panels, left panel is the bigTwoPanel
		// right panel is the 2 JTextArea and the text field
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		
		JPanel leftPanel = this.initializeLeftPanel();
		JPanel rightPanel = this.initializeRightPanel();
		
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		
		
		this.frame.add(mainPanel);
		
		this.frame.setMinimumSize(new Dimension(1000, 750));
		this.frame.setVisible(true);
		this.disable();
	}
	
	// A method I made to make the constructor less messy
	// This method is to initialize all the menu items
	private void initializeMenu() {
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenu messageMenu = new JMenu("Message");
		
		JMenuItem restartMenuItem = new JMenuItem("Connect");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		restartMenuItem.addActionListener(new RestartMenuItemListener());
		quitMenuItem.addActionListener(new QuitMenuItemListener());
		
		gameMenu.add(restartMenuItem);
		gameMenu.add(quitMenuItem);
		
		JMenuItem clearMenuItem = new JMenuItem("Clear");
		clearMenuItem.addActionListener(new ClearMenuItemListener());
		messageMenu.add(clearMenuItem);
		
		menuBar.add(gameMenu);	
		menuBar.add(messageMenu);
		
		this.frame.setJMenuBar(menuBar);
	}
	
	// another private method that I made to initialize the left panel that is contained by the main panel
	// also returns JPanel object, so that the constructor can know what to add to the main panel, since the main panel is defined in the constructor
	private JPanel initializeLeftPanel() {
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // box layout y-axis
		
		// the big two panel is contained inside the left panel
		bigTwoPanel = new BigTwoPanel();
		
		// the two buttons
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		
		// add the listeners
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		
		// the buttons panel also belongs to the left panel, and it will be at the bottom because of the box layout y-axis
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
		buttonsPanel.setMinimumSize(new Dimension(0, 50));
		buttonsPanel.add(playButton);
		buttonsPanel.add(passButton);
		
		leftPanel.add(bigTwoPanel);
		leftPanel.add(buttonsPanel);
		
		return leftPanel;
	}
	
	// another private method that I made to initialize the right panel that is contained by the main panel
	// also returns JPanel object, so that the constructor can know what to add to the main panel, since the main panel is defined in the constructor
	private JPanel initializeRightPanel() {
		// the right panel contains the two textareas (chatting and message area), along with the chat input at the bottom right
		JPanel rightPanel = new JPanel();
		rightPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

		msgArea = new JTextArea();
		chatArea = new JTextArea();
		
		// make the two text areas not editable
		msgArea.setEditable(false);
		chatArea.setEditable(false);
		
		// set font color for the chat area to be blue
		chatArea.setForeground(Color.BLUE);
		chatArea.setLineWrap(true);
		
		msgArea.setLineWrap(true);
		
		JScrollPane chatScrollPane = new JScrollPane(chatArea);
		JScrollPane msgScrollPane = new JScrollPane(msgArea);
		
		chatScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		msgScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		// set the border with title to indicate the title of the areas
		msgScrollPane.setBorder(BorderFactory.createTitledBorder("Game Status"));
		chatScrollPane.setBorder(BorderFactory.createTitledBorder("Chatting Room"));
		
		
		JPanel chattingPanel = new JPanel();
		chattingPanel.setLayout(new BoxLayout(chattingPanel, BoxLayout.X_AXIS));
		chattingPanel.setMinimumSize(new Dimension(0, 50));
		
		JLabel msgLabel = new JLabel(" Message: ");
		chatInput = new JTextField(20);
		chatInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 18));
		chatInput.addKeyListener(new chatInputKeyListener());
		
		// adding all the components
		chattingPanel.add(msgLabel);
		chattingPanel.add(chatInput);
		
		
		rightPanel.add(msgScrollPane);
		rightPanel.add(chatScrollPane);
		rightPanel.add(chattingPanel);
		
		return rightPanel;
	}
	
	/**
	 * Returns the main frame of the GUI of this class. Used by BigTwo class to create a JOptionPane when the game ends.
	 * 
	 * @return The frame reference to be returned.
	 */
	public JFrame getFrame() {
		return this.frame;
	}
	
	/**
	 * Sets the index of the active player.
	 * 
	 * @param activePlayer An int value representing the index of the active player.
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer > 4) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}
	
	/**
	 * Repaints the GUI.
	 */
	public void repaint() {
		this.bigTwoPanel.repaint();
		this.disable();
		if (this.activePlayer == this.game.getClient().getPlayerID()) {
			this.enable();
		}
	}
	
	/**
	 * Prints the specified string to the message area of the card game user interface.
	 * 
	 * @param msg The string to be printed to the message area of the card game user interface.
	 */
	public void printMsg(String msg) {
		this.msgArea.append(msg + "\n");
	}
	
	/**
	 * Clears the message area of the card game user interface.
	 */
	public void clearMsgArea() {
		this.msgArea.setText("");
	}
	
	/**
	 * Resets the card game user interface.
	 */
	public void reset() {
		
		this.clearMsgArea();
		
		// clears chatArea as well
		this.chatArea.setText("");
		this.enable();
		
		this.game.start(this.game.getDeck());
	}
	
	/**
	 * Enables user interactions. Called when a game starts/restarts.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables user interactions. Called when a game ends.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}

	/**
	 * Prompts active player to select cards and make his/her move.
	 */
	public void promptActivePlayer() {
		this.printMsg(this.game.getPlayerList().get(activePlayer).getName() + "'s turn:");
		
		int activeNumCards = game.getPlayerList().get(activePlayer).getNumOfCards();
		selected = new boolean[activeNumCards];
		for (int j = 0; j < activeNumCards; j++) {
			// reset the selected cards
			selected[j] = false;
		}
		this.repaint();
	}
	
	/**
	 * Prints message to the chatting area panel.
	 * 
	 * @param msg The message to be printed.
	 */
	public void printChat(String msg) {
		int playerIdx = this.activePlayer;
		String output = "Player " + playerIdx + ": " + msg + "\n";
		this.chatArea.append(output);
	}
	
	/**
	 * An inner class that extends JPanel class and implements the MouseListener interface.
	 * This panel will be the panel that shows all the cards & avatars, and also handles events for mouse clicks.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class BigTwoPanel extends JPanel implements MouseListener{
		
		/**
		 * Constructor for the Big Two Game Panel
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		/**
		 * An overriden method inherited from JPanel class to draw the card game table.
		 */
		public void paintComponent(Graphics g) {
			
			// the avatar image name of its corresponding files
			String[] avatars = {"batman.png", "flash.png", "green_lantern.png", "superman.png"};
			
			// remove all components before repainting everything
			this.removeAll();
			g.setColor(new Color(72, 160, 100));
			g.fillRect(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE); // the background of the panel
			
			for (int i = 0; i < 4; i++) {
				int numberOfCards = game.getPlayerList().get(i).getNumOfCards();
				
				
				Image avatar = new ImageIcon("avatars/" + avatars[i]).getImage();
				// remove the avatar if player left the game or there is connection issues
				if (!game.getPlayerList().get(i).getName().equals("")) {
					g.drawImage(avatar, 5, 30 + 135 * i, 80, 95, this);
				}
				
				// setting different colors for active players
				if (i == activePlayer && game.getClient() != null && i == game.getClient().getPlayerID()) {
					// set the "You" to indicate the current active player and make it green color
					g.setColor(Color.GREEN);
					g.drawString("You", 10, 20 + 135 * i);
				} else if (i == activePlayer && game.getClient() != null && i != game.getClient().getPlayerID()) {
					g.setColor(Color.YELLOW);
					g.drawString(game.getPlayerList().get(i).getName(), 10, 20 + 135 * i);
				} else if (i != activePlayer && game.getClient() != null && i == game.getClient().getPlayerID()) {
					g.setColor(Color.BLUE);
					g.drawString("You", 10, 20 + 135 * i);	
				} else {
					g.setColor(Color.BLACK);
					g.drawString(game.getPlayerList().get(i).getName(), 10, 20 + 135 * i);	
				}
				
				// make sure that only local players can see their own card
				if (game.getClient() != null && i == game.getClient().getPlayerID()) {
					CardList cards = game.getPlayerList().get(i).getCardsInHand();
					for (int j = 0; j < numberOfCards; j++) {
						int rank = cards.getCard(j).getRank();
						int suit = cards.getCard(j).getSuit();
						String cardString = rankString[rank] + suitString[suit] + ".gif";
						Image cardImg = new ImageIcon("cards/" + cardString).getImage();
						
						// raise the card if it is selected
						int raised = 0;
						if (selected[j]) {
							raised = -15;
						}
						
						// draw the card in a stacked way
						g.drawImage(cardImg, 90 + 35 * j, raised + 30 + 135 * i, 70, 95, this);
					}
				} else {
					// if not active, set it to the player name instead of "You" and make it black color too
					for (int j = 0; j < numberOfCards; j++) {
						Image backImg = new ImageIcon("cards/b.gif").getImage();
						g.drawImage(backImg, 90 + 35 * j, 30 + 135 * i, 70, 95, this);
					}
				}
				
				// drawing the separator lines
				g.setColor(Color.BLACK);
				g.drawLine(0, (i + 1) * 135, Integer.MAX_VALUE, (i + 1) * 135);
			}
			// adding the last hand played now
			Hand lastPlayed = (game.getHandsOnTable().isEmpty()) ? null : game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
			// if hand not empty
			if (lastPlayed != null) {
				String name = lastPlayed.getPlayer().getName();
				g.setColor(Color.BLACK);
				if (lastPlayed.getPlayer() == game.getPlayerList().get(game.getClient().getPlayerID())) {
					g.setColor(Color.BLUE);
				}
				g.drawString("Played by " + name, 10, 20 + 135 * 4);
				
				for (int i = 0; i < lastPlayed.size(); i++) {
					int rank = lastPlayed.getCard(i).getRank();
					int suit = lastPlayed.getCard(i).getSuit();
					String cardString = rankString[rank] + suitString[suit] + ".gif";
					
					Image img = new ImageIcon("cards/" + cardString).getImage();
					// separate the card by some space instead of stacking them
					g.drawImage(img, 15 + 90 * i, 570, 70, 95, this);
				}
			}
		}
		
		/**
		 * An implemented mouseReleased() method from the MouseListener interface to handle mouse click events.
		 * 
		 * @param e The event to be processed.
		 */
		public void mousePressed(MouseEvent e) {
			int x = e.getX();
			int y = e.getY();

			int numberOfCards = game.getPlayerList().get(activePlayer).getNumOfCards();
			
			// since the last card is viewed as a full card
			// we check for the last card's coordinates separately
			
			// check whether the x detected is at the right spot
			if (x >= 90 + 35 * (numberOfCards - 1) && x < 90 + 35 * (numberOfCards - 1) + 70) {
				// check if the last card is selected, then check if the right y value is clicked
				if (selected[numberOfCards - 1] && y >= 15 + activePlayer * 135 && y < 110 + activePlayer * 135) {
					selected[numberOfCards - 1] = false;
					this.repaint();
					return;
					
				// if the last card is not selected, then check if the y value being clicked is at the right range
				} else if (selected[numberOfCards - 1] == false && y >= 30 + activePlayer * 135 && y < 125 + activePlayer * 135){
					selected[numberOfCards - 1] = true;
					this.repaint();
					return;
				}
			}
			
			// now checking for the rest of the cards starting from the first to the last second card
			for (int i = 0; i < numberOfCards - 1; i++) {
				// checks first if the card to the right is selected, because this affects the areas in which the user is allowed to click to select the current card
				if (selected[i+1]) {
					// if the ith card is not selected, check for the proper x and y range, and this is a bit complex, since we have to account for two "rectangles".
					// hence the or sign is basically to check if the x and y being clicked is in a valid range
					if (selected[i] == false && ((x >= 90 + 35 * i && x < 90 + 35 * (i+1) && y >= 30 + activePlayer * 135 && y < 125 + activePlayer * 135) || (x >= 90 + 35 * (i+1) && x < 90 + 35 * (i + 2) && y >= 110 + activePlayer * 135 && y < 125 + activePlayer * 135))) {
						selected[i] = true;
					// if the ith card is selected, checks for valid range also
					} else if (selected[i] && x >= 90 + 35 * i && x < 90 + 35 * (i+1) && y >= 15 + activePlayer * 135 && y < 110 + activePlayer * 135) {
						selected[i] = false;
					}
				} else {
					// if the card to the right of ith card is not selected/raised
					// checks for valid range of x and y
					if (selected[i] == false && x >= 90 + 35 * i && x < 90 + 35 * (i + 1) && y >= 30 + activePlayer * 135 && y < 125 + activePlayer * 135) {
						selected[i] = true;
						
					// similar explanation, checks for valid x and y range if the current ith card is already selected
					} else if (selected[i] && ((x >= 90 + 35 * i && x < 90 + 35 * (i+1) && y >= 15 + activePlayer * 135 & y < 110 + activePlayer * 135) || (x >= 90 + 35 * (i+1) && x < 90 + 35 * (i + 2) && y >= 15 + 135 * activePlayer && y < 30 + activePlayer * 135))) {
						selected[i] = false;
					}
				}
			}
			// repaints everything
			this.repaint();
		}
		
		public void mouseReleased(MouseEvent e) {}
		public void mouseClicked(MouseEvent e) {}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
	}
	
	/**
	 * An inner class that implements the ActionListener interface and handles the event when the "Play" button is clicked.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class PlayButtonListener implements ActionListener {
		/**
		 * The implemented actionPerformed() method from the ActionListener interface to handle 
		 * button-click events for the “Play” button.
		 * 
		 * @param e The event to be processed.
		 */
		public void actionPerformed(ActionEvent e) {
			int playerIdx = activePlayer;
			int[] cardIdx = null;
			int count = 0;
			// counts the number of cards selected first
			for (int j = 0; j < selected.length; j++) {
				if (selected[j]) {
					count++;
				}
			}
			
			if (count != 0) {
				cardIdx = new int[count];
				count = 0;
				for (int j = 0; j < selected.length; j++) {
					if (selected[j]) {
						cardIdx[count] = j;
						count++;
					}
				}
			} else {
				// does nothing if nothing is played or count is 0
				return;
			}
			
			// this statement is reached only if count is more than 0
			game.makeMove(playerIdx, cardIdx);
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface and handles the event when the "Pass" button is clicked.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class PassButtonListener implements ActionListener {
		/**
		 * The implemented actionPerformed() method from the ActionListener interface to handle 
		 * button-click events for the “Pass” button.
		 * 
		 * @param e The event to be processed.
		 */
		public void actionPerformed(ActionEvent e) {
			int playerIdx = activePlayer;
			int[] cardIdx = null;
			game.makeMove(playerIdx, cardIdx);
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface and handles the event when the “Restart” menu item is clicked.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class RestartMenuItemListener implements ActionListener {
		/**
		 * The implemented actionPerformed() method from the ActionListener interface to handle 
		 * menu-item-click events for the “Restart” menu item.
		 * 
		 * @param e The event to be processed.
		 */
		public void actionPerformed(ActionEvent e) {
			if (!game.getClient().connected()) {
				game.getClient().connect();
			}
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface and handles the event when the “Quit” menu item is clicked.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class QuitMenuItemListener implements ActionListener {
		/**
		 * The implemented actionPerformed() method from the ActionListener interface to handle 
		 * menu-item-click events for the “Quit” menu item.
		 * 
		 * @param e The event to be processed.
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface to handle the event when the "Clear" menu item is clicked.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class ClearMenuItemListener implements ActionListener {
		/**
		 * The implemented actionPerformed() method from the ActionListener interface to handle 
		 * menu-item-click events for the “Clear” menu item.
		 * 
		 * @param e The event to be processed.
		 */
		public void actionPerformed(ActionEvent e) {
			clearMsgArea();
		}
	}
	
	/**
	 * An inner class when anything is being pressed by the keyboard in the chat input.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class chatInputKeyListener implements KeyListener {
		/**
		 * The implemented ketPressed() method from the KeyListener interface to handle keys events.
		 * 
		 * @param e The event to be processed.
		 */
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				String chat = chatInput.getText();
				chatInput.setText("");
				if (!chat.equals("")) {
					game.getClient().sendMessage(new CardGameMessage(CardGameMessage.MSG, -1, chat));
				}
			}
		}
		
		public void keyTyped(KeyEvent e) {}
		public void keyReleased(KeyEvent e) {}
	}
}
