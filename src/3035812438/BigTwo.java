import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class BigTwo {
	/**
	 * A constructor for creating a Big Two card game.
	 */
	public BigTwo() {
		for (int i = 0; i < 4; i++) {
			CardGamePlayer player = new CardGamePlayer();
			player.setName("");
			playerlist.add(player);
		}
		
		this.ui = new BigTwoGUI(this);
		this.client = new BigTwoClient(this, this.ui);
	}
	
	// The number of players playing, initialized to 4.
	private int numOfPlayers = 4;
	// The deck of the Big Two Cards.
	private Deck deck;
	// The array list containing all the 4 players.
	private ArrayList<CardGamePlayer> playerlist = new ArrayList<CardGamePlayer>();
	// The array list that will contain all the hands played.
	private ArrayList<Hand> handsOnTable = new ArrayList<Hand>();
	// The index of the current player playing.
	private int currentPlayerIdx;
	// The UI object.
	private BigTwoGUI ui;
	// The Big Two client
	private BigTwoClient client;
	
	/**
	 * A getter method to retrieve the number of players.
	 * 
	 * @return An integer which is the number of players.
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	
	/**
	 * A getter method to retrieve the deck of cards being used.
	 * 
	 * @return A deck object which is the deck of cards being used.
	 */
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * A getter method to retrieve the list of players.
	 * 
	 * @return An ArrayList object of type CardGamePlayer object which is the list of players.
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerlist;
	}
	
	/**
	 * A getter method to retrieve the list of hands played on the table.
	 * 
	 * @return An ArrayList object of type Hand object which is the list of hands.
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}
	
	/**
	 * A getter method to retrieve the index of the current player.
	 * 
	 * @return An integer which is the index of the current player.
	 */
	public int getCurrentPlayerIdx() {
		return this.currentPlayerIdx;
	}
	
	/**
	 * A getter method to retrieve the client object of the local player.
	 * 
	 * @return The cilent object to be retrieved.
	 */
	public BigTwoClient getClient() {
		return this.client;
	}
	
	/**
	 * A method for starting/restarting the game with a given shuffled deck of cards.
	 * 
	 * @param deck A Deck object which is the shuffled deck of cards to start/restart the game with.
	 */
	public void start(Deck deck) {
		this.ui.enable();
		// remove all cards from all players and cards from table
		for (int i = 0; i < this.playerlist.size(); i++) {
			this.playerlist.get(i).removeAllCards();
		}
		
		// remove all cards on table
		while(!this.handsOnTable.isEmpty()) {
			this.handsOnTable.remove(0);
		}
		
		int firstPlayer = -1;
		
		// the distribution part
		for (int i = 0; i < deck.size(); i++) {
			Card card = deck.getCard(i);
			this.playerlist.get(i % 4).addCard(card);
			
			// identifying the 3 of diamonds at the same time
			if (card.getRank() == 2 && card.getSuit() == 0) {
				firstPlayer = i % 4;
			}
		}
		
		// sort cards in players' hands
		for (int i = 0; i < 4; i++) {
			this.playerlist.get(i).sortCardsInHand();;
		}
		
		this.currentPlayerIdx = firstPlayer;
		this.ui.setActivePlayer(firstPlayer);
		
		this.ui.repaint();
		
		this.ui.promptActivePlayer();
	}
	
	/**
	 * A method for making a move by a player with the specified index using the cards specified by the list of indices.
	 * 
	 * @param playerIdx An integer which specifies the index of the current player playing.
	 * @param cardIdx An integer array which is the list of the indices that specifies the cards the current player has.
	 */
	public void makeMove(int playerIdx, int[] cardIdx) {
		this.client.sendMessage(new CardGameMessage(CardGameMessage.MOVE, -1, cardIdx));
	}
	
	/**
	 * A method for checking a move made by a player.
	 * 
	 * @param playerIdx An integer which specifies the index of the current player playing.
	 * @param cardIdx An integer array which is the list of the indices that specifies the cards the current player has.
	 */
	public void checkMove(int playerIdx, int [] cardIdx) {
		// addition to this function, for some conditions of the game,
		// I removed the this.ui.promptActivePlayer in some if conditions, since
		// for the GUI version of the game, the code will only repaint the GUI, if the player has made a valid move
		
		CardGamePlayer player = playerlist.get(playerIdx);
		CardList cards = player.play(cardIdx);
		if (cardIdx != null) {
			cards.sort();
		}
		
		Hand lastPlayed = (this.handsOnTable.isEmpty()) ? null : this.handsOnTable.get(this.handsOnTable.size() - 1);
		
		// cannot pass if the player played the last hand of cards
		// or if he is the first player to play (nothing on table)
		
		if (cardIdx == null) { // if player passes,
			if (handsOnTable.isEmpty() || lastPlayed.getPlayer() == player) {
				this.ui.printMsg("Not a legal move!!!");
			} else {
				this.ui.printMsg("{Pass}");
				// set active player to next since the pass was a legal move
				this.ui.setActivePlayer((playerIdx + 1) % 4);
				this.currentPlayerIdx = (++this.currentPlayerIdx) % 4;
				this.ui.repaint();
				this.ui.promptActivePlayer();
			}
			this.ui.repaint();
			return;
		}
		
		Hand hand = composeHand(player, cards);
		if (hand == null) { // if the player input cannot be made into any hand type
			this.ui.printMsg("Not a legal move!!!");
			return;
		}
	
		// now checks if the player doesn't pass, see if it is a legal move
		if (lastPlayed != null) {
			if (lastPlayed.getPlayer() == player) {
				// if the last hand played is the same as the current active player, means he won the previous "round"
				// can play any legal combinations of card
				String playedCards = "{" + hand.getType() + "} ";
				playedCards += printCards(cards);
				this.ui.printMsg(playedCards);
				
				// since player successfully played the hand, we add to handsOntable
				this.handsOnTable.add(hand);
				
				// remove the cards played from the player's hand
				for (int i = cards.size() - 1; i >= 0; i--) {
					player.getCardsInHand().removeCard(cardIdx[i]);
				}
				
				this.ui.repaint();
				if (this.endOfGame()) { // check for end of game since we are removing cards, have to check if it is empty
					this.printWinningMessage(playerIdx);
					this.ui.repaint();
					this.showWinningDialog(playerIdx);
					this.ui.disable();
					return;
				} else {
					this.ui.setActivePlayer((playerIdx + 1) % 4);
					this.currentPlayerIdx = (++this.currentPlayerIdx) % 4;
					this.ui.repaint();
				}
				this.ui.promptActivePlayer();
				return;
			}
			
			// since there is a return statement above, the code below runs if
			// 1. it is not the very first round (hands on table not empty) and 2. the current active player is not the winner of the round
			// so the player has to play a hand of the same size, that must beat the previously played hand
			if (hand.size() == lastPlayed.size() && hand.beats(lastPlayed)) {
				// copied from the repaint method
				String playedCards = "{" + hand.getType() + "} ";
				playedCards += printCards(cards);
				this.ui.printMsg(playedCards);
				
				// since player successfully played the hand, we add to handsOntable
				this.handsOnTable.add(hand);
				
				// remove the cards played from the player's hand
				for (int i = cards.size() - 1; i >= 0; i--) {
					player.getCardsInHand().removeCard(cardIdx[i]);
				}
				
				this.ui.repaint();
				if (this.endOfGame()) {
					this.printWinningMessage(playerIdx); // checks again if the player won the round, since we removed cards from the player's pile
					this.ui.repaint();
					this.showWinningDialog(playerIdx);
					this.ui.disable();
					return;
				} else {
					this.ui.setActivePlayer((playerIdx + 1) % 4);
					this.currentPlayerIdx = (++this.currentPlayerIdx) % 4;
					this.ui.repaint();
				}
				this.ui.promptActivePlayer();
				return;
			} else {
				this.ui.printMsg("Not a legal move!!!");
				return;
			}
		} else {
			// hands on table is only empty at the very first
			// if first round, 3 of diamonds has to be played
			boolean played = false; // boolean to check if 3 of diamonds is played
			for (int i = 0; i < cards.size(); i++) {
				Card card = cards.getCard(i);
				if (card.getRank() == 2 && card.getSuit() == 0) {
					played = true;
					break;
				}
			}
			
			
			// if 3 of diamonds is in the hand the player played,
			// we don't have to check for any restrictions
			// since the player can play any legal combinations of cards
			// as long as it can be composed as a legal combination
			// here we don't check for anything else since the code below will only run if hand can be composed
			if (played) {
				String playedCards = "{" + hand.getType() + "} ";
				playedCards += printCards(cards);
				this.ui.printMsg(playedCards);
				
				this.handsOnTable.add(hand);
				
				// remove the cards played from the player's hand
				for (int i = cards.size() - 1; i >= 0; i--) {
					player.getCardsInHand().removeCard(cardIdx[i]);
				}
				
				this.ui.repaint();
				
				if (this.endOfGame()) {
					this.printWinningMessage(playerIdx);
					this.ui.repaint();
					this.showWinningDialog(playerIdx);
					this.ui.disable();
					return;
				}
				
				this.ui.setActivePlayer((playerIdx + 1) % 4);
				this.currentPlayerIdx = (++this.currentPlayerIdx) % 4;
				this.ui.repaint();
				this.ui.promptActivePlayer();
				return;
			} else {
				this.ui.printMsg("Not a legal move!!!");
				return;
			}
		}
	}
	
	/**
	 * A method for checking if the game ends.
	 * 
	 * @return A boolean to see if the game ended.
	 */
	public boolean endOfGame() {
		for (int i = 0; i < 4; i++) {
			if (this.playerlist.get(i).getCardsInHand().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	// a new method that I added to print the cards (for assignment 4), since previously I used the
	// CardList.print() method, and it is only for printing in command line
	private String printCards(CardList cards) {
		String output = "";
		for (int i = 0; i < cards.size(); i++) {
			if (i > 0) {
				output += " ";
			}
			output += "[" + cards.getCard(i) + "]";
		}
		return output;
	}
	
	// A new method I added to print the final message, when the game ended or when a player wins.
	private void printWinningMessage(int playerIdx) {
		this.ui.printMsg("Game ends");
		for (int i = 0; i < 4; i++) {
			if (i == playerIdx) {
				this.ui.printMsg("Player " + i + " wins the game.");
			} else {
				this.ui.printMsg("Player " + i + " has " + this.playerlist.get(i).getCardsInHand().size() + " cards in hand.");
			}
		}
	}
	
	// another new method I added to show the message dialog required in assignment 5
	private void showWinningDialog(int playerIdx) {
		String message = "Cards remaining in each hand\n";
		String title;
		for (int i = 0; i < 4; i++) {
			message += this.playerlist.get(i).getCardsInHand().size();
			
			if (i != 3) {
				message += ", ";
			}
		}
		
		if (this.client.getPlayerID() == playerIdx) {
			title = "You win!";
		} else {
			title = "You lose!";
		}
		
		JOptionPane.showMessageDialog(this.ui.getFrame(), message, title, JOptionPane.INFORMATION_MESSAGE);
		this.client.sendMessage(new CardGameMessage(CardGameMessage.READY, -1 , null));
	}
	
	/**
	 * The main method which starts a Big Two card game.
	 * 
	 * @param args Unused.
	 */
	public static void main(String[] args) {
		BigTwo game = new BigTwo();
		
		game.deck = new BigTwoDeck();
		game.deck.shuffle();
	}
	
	/**
	 * A method for returning a valid hand from the specified list of cards of the player.
	 * 
	 * @param player The player, a CardGamePlayer object, the hand belongs to.
	 * @param cards The list of the cards.
	 * @return Returns NULL if no valid hand can be composed from the specified list of cards.
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		int size = cards.size();
		if (size == 5) {
			StraightFlush sf = new StraightFlush(player, cards);
			Quad quad = new Quad(player, cards);
			FullHouse fh = new FullHouse(player, cards);
			Flush flush = new Flush(player, cards);
			Straight straight = new Straight(player, cards);
			
			if (sf.isValid()) {
				return sf;
			} else if (quad.isValid()) {
				return quad;
			} else if (fh.isValid()) {
				return fh;
			} else if (flush.isValid()) {
				return flush;
			} else if (straight.isValid()) {
				return straight;
			}
		} else if (size == 3) {
			Triple triple = new Triple(player, cards);
			if (triple.isValid()) {
				return triple;
			}
		} else if (size == 2) {
			Pair pair = new Pair(player, cards);
			if (pair.isValid()) {
				return pair;
			}
		} else if (size == 1){
			Single single = new Single(player, cards);
			if (single.isValid()) {
				return single;
			}
		}
		return null;
	}
}
