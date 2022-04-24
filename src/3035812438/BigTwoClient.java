import java.io.*;
import java.net.*;
import javax.swing.*;

public class BigTwoClient implements NetworkGame{
	
	/**
	 * A constructor for creating a Big Two client.
	 * @param game A reference to a BigTwo object associated with this client.
	 * @param gui A reference to a BigTwoGUI object associated the BigTwo object.
	 */
	public BigTwoClient(BigTwo game, BigTwoGUI gui) {
		// creating a pop-up window to input player name
		this.playerName = JOptionPane.showInputDialog("Please input your name: ");
		while (this.playerName == null || this.playerName.equals("")) {
			this.playerName = JOptionPane.showInputDialog("Please input your name: ");
		}
		
		this.game = game;
		this.connect();
		this.gui = gui;
	}
	
	// A BigTwo object for the Big Two card game.
	private BigTwo game;
	
	// A BigTwoGUI object for the Big Two card game.
	private BigTwoGUI gui;
	
	// A socket connection to the game server.
	private Socket sock;
	
	// An ObjectOutputStream for sending messages to the server.
	private ObjectOutputStream oos;
	
	// An integer specifying the playerID (i.e., index) of the local player.
	private int playerID;
	
	// A string specifying the name of the local player.
	private String playerName;
	
	// A string specifying the IP address of the game server.
	private String serverIP;
	
	// An integer specifying the TCP port of the game server.
	private int serverPort;
	
	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID() {
		return this.playerID;
	}

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName() {
		return this.playerName;
	}

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName the name of the local player
	 */
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP() {
		return this.serverIP;
	}

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP the IP address of the server
	 */
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort the TCP port of the server
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Makes a network connection to the server.
	 */
	public void connect() {
		try {
			setServerPort(2396);
			setServerIP("127.0.0.1");
			
			sock = new Socket(getServerIP(), getServerPort());
			
			//create an ObjectOutputStream for sending messages to the game server
			oos = new ObjectOutputStream(sock.getOutputStream());
			
			Thread myThread = new Thread(new ServerHandler());
			myThread.start();
			
//			this.sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.playerName))
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message the specified message received from the server
	 */
	public void parseMessage(GameMessage message) {
		boolean thisPlayer = false;
		if (message.getPlayerID() == this.playerID) {
			thisPlayer = true;
		}
		
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			// setting the player id of the local player
			this.setPlayerID(message.getPlayerID());
			
			// setting the player names in the player list in the game class
			String[] playerNames = (String[]) message.getData();
			for(int i = 0; i < 4; i++) {
				if (playerNames[i] != null) {
					this.game.getPlayerList().get(i).setName(playerNames[i]);
				}
			}
			this.game.getPlayerList().get(this.playerID).setName(this.playerName);
			
			this.sendMessage(new CardGameMessage(CardGameMessage.JOIN, -1, this.getPlayerName()));
		} else if (message.getType() == CardGameMessage.JOIN) {
			if (message.getPlayerID() == this.playerID) {
				this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			} else {
				String newPlayerName = (String) message.getData();
				this.game.getPlayerList().get(message.getPlayerID()).setName(newPlayerName);
			}
			
			this.gui.repaint();
		}  else if (message.getType() == CardGameMessage.FULL) {
			this.gui.printMsg("Server is full. Cannot join the game.");
		}  else if (message.getType() == CardGameMessage.QUIT) {
			// remove the player by setting the name to empty
			this.game.getPlayerList().get(message.getPlayerID()).setName("");
			
			this.gui.disable();
			
			if (!game.endOfGame()) {
				this.sendMessage(new CardGameMessage(CardGameMessage.READY, -1, null));
			}
		}  else if (message.getType() == CardGameMessage.READY) {
			this.gui.printMsg(this.game.getPlayerList().get(message.getPlayerID()).getName() + " is ready.");
		}  else if (message.getType() == CardGameMessage.START) {
			BigTwoDeck deck = (BigTwoDeck) message.getData();
			this.game.start(deck);
		}  else if (message.getType() == CardGameMessage.MOVE) {
			this.game.checkMove(message.getPlayerID(), (int []) message.getData());
		}  else if (message.getType() == CardGameMessage.MSG) {
			this.gui.printChat((String) message.getData());
		} 
	}

	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message) {
		try {
			// send the message to the server
			oos.writeObject(message);
		} catch(Exception ex) {
			gui.printMsg("Error sending message! Please try again!");
			ex.printStackTrace();
		}
	}
	
	/**
	 * An inner class that implements the Runnable interface.
	 * 
	 * @author Richard Bryan
	 *
	 */
	public class ServerHandler implements Runnable {
		
		private ObjectInputStream ois;
		public ServerHandler() {
			try {
				ois = new ObjectInputStream(sock.getInputStream());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		public void run() {
			CardGameMessage message;
			
			try {
				//wait for message sent from server
				while((message = (CardGameMessage) ois.readObject()) != null) {
					//parse the message
					parseMessage(message);
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * A new function I added that returns true if the socket in this client is connected to the server.
	 * 
	 * @return The boolean to be returned.
	 */
	public boolean connected() {
		return this.sock.isConnected();
	}
}
