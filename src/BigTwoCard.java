/**
 * A subclass of the Card class and is used to model a card used in a Big Two card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class BigTwoCard extends Card{
	/**
	 * A constructor for building a card with the specified suit and rank.
	 * 
	 * @param suit An integer that specifies the suit of the card, it should be an integer between 0 and 3 inclusive.
	 * @param rank An integer that specifies the rank of the card, it should be an integer between 0 and 12 inclusive.
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * A method for comparing the order of this card with the specified card.
	 * 
	 * @param The specified card to be compared with the card this class contains.
	 * @return Returns a negative integer, zero, or a positive integer when this card is less than, equal to, or greater than the specified card.
	 */
	public int compareTo(Card card) {
		int thisSuit = this.getSuit();
		int thisRank = this.getRank();
		
		int suit = card.getSuit();
		int rank = card.getRank();
		
		
		// the two if blocks below is to add 13 if the rank of the card is 'A' or '2'
		// this is to make comparison easier, and this is safe to do since we are modifying
		// the value of a local variable, not modifying anything that belongs to the class
		if (rank <= 1) {
			rank += 13;
		}
		
		if (thisRank <= 1) {
			thisRank += 13;
		}
		
		if (thisRank < rank) {
			return -1;
		} else if (thisRank > rank) {
			return 1;
		} else {
			// rank is equal now, check suit now
			if (thisSuit == suit) {
				return 0;
			} else if (thisSuit < suit) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
