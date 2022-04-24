/**
 * A subclass of the CardList class and is used to model a hand of cards.
 * It has a private instance variable for storing the player who plays this hand.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
abstract class Hand extends CardList{
	/**
	 * A constructor for building a hand with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) {
			int rank = cards.getCard(i).getRank();
			int suit = cards.getCard(i).getSuit();
			BigTwoCard card = new BigTwoCard(suit, rank);
			this.addCard(card);
		}
		this.sort();
	}
	
	// The player that plays this hand.
	private CardGamePlayer player;
	
	/**
	 * A method for retrieving the player of this hand.
	 * 
	 * @return 
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
	}
	
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return A card object which is the top card of this hand (an object that will be created using this class).
	 */
	public Card getTopCard() {
		if (this.getType() == "Single") {
			return this.getCard(0);
		} else if (this.getType() == "Pair") {
			return this.getCard(1);
		} else if (this.getType() == "Triple") {
			return this.getCard(2);
		} else if (this.getType() == "Straight") {
			return this.getCard(4);
		} else if (this.getType() == "Flush") {
			return this.getCard(4);
		} else if (this.getType() == "FullHouse") {
			if (this.getCard(1).getRank() == this.getCard(2).getRank()) {
				return this.getCard(2);
			} else if (this.getCard(2).getRank() == this.getCard(3).getRank()) {
				return this.getCard(4);
			} else {
				return null;
			}
		} else if (this.getType() == "Quad") {
			if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
				return this.getCard(3);
			} else if (this.getCard(3).getRank() == this.getCard(4).getRank()) {
				return this.getCard(4);
			} else {
				return null;
			}
		} else if (this.getType() == "StraightFlush") {
			return this.getCard(4);
		} else {
			return null;
		}
	}
	
	/**
	 * A method for checking if this hand beats a specified hand (the parameter).
	 * 
	 * @param hand The specified hand to be compared with this hand.
	 * @return A boolean which tells if this hand beats the specified hand.
	 */
	public boolean beats(Hand hand) {		
		// After the if-statement above
		// It will make sure that the code below only runs if
		// This hand's type and the specified type is the same, and the specified type is valid
		
		Card thisTopCard = this.getTopCard();
		Card handTopCard = hand.getTopCard();
		// we don't check based on the top card, if the hands are size of 5
		// we check their "hierarchy" first
		if (hand.size() == 5) {
			if (this.getType() != hand.getType()) {
				String hierarchy[] = {"Straight", "Flush", "FullHouse", "Quad", "StraightFlush"};
				int thisRanking = -1;
				int handRanking = -1;
				for (int i = 0; i < 5; i++) {
					if (this.getType() == hierarchy[i]) {
						thisRanking = i;
					}
					
					if (hand.getType() == hierarchy[i]) {
						handRanking = i;
					}
				}
				
				if (thisRanking > handRanking) {
					return true;
				} else {
					return false;
				}
			}
		}
		
		// exception for flush
		// we check for suit first. instead of rank of top card
		if (this.getType() == "Flush") {
			int thisSuit = thisTopCard.getSuit();
			int handSuit = handTopCard.getSuit();
			
			if (thisSuit > handSuit) {
				return true;
			} else if (thisSuit < handSuit) {
				return false;
			}
		}
		
		if (thisTopCard.compareTo(handTopCard) > 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * The abstract method checking if this hand is a valid hand.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand; otherwise, false.
	 */
	abstract boolean isValid();
	
	/**
	 * The abstract method returning a string specifying the type of this hand.
	 * 
	 * @return The string specifying the type of this hand.
	 */
	abstract String getType();
	
	
}
