/**
 * A subclass of the Hand class and is used to model a hand of Straight Flush in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class StraightFlush extends Hand{
	/**
	 * A constructor for building a hand of Straight Flush with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of Straight Flush.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of Straight Flush; otherwise, false.
	 */
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		}
		
		int suit = this.getCard(0).getSuit();
		for (int i = 0; i < 4; i++) {
			
			// A temporary int variable to account for 'A' and '2',
			 // and make its rank "higher" than 'K'
			int rank1 = this.getCard(i).getRank();
			int rank2 = this.getCard(i + 1).getRank();
			
			if (rank1 <= 1) {
				rank1 += 13;
			}
			
			if (rank2 <= 1) {
				rank2 += 13;
			}
			
			if (rank2 - rank1 != 1 || this.getCard(i + 1).getSuit() != suit) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "StraightFlush".
	 * 
	 * @return The string specifying the type of this hand, which is always "StraightFlush".
	 */
	public String getType() {
		return "StraightFlush";
	}
}
