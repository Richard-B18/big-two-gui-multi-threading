/**
 * A subclass of the Hand class and is used to model a hand of Flush in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class Flush extends Hand{
	/**
	 * A constructor for building a hand of Flush with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of Flush.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of Flush; otherwise, false.
	 */
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		}
		
		int flushSuit = this.getCard(0).getSuit();
		boolean legal = true;
		for (int i = 0; i < 5; i++) {
			if (this.getCard(i).getSuit() != flushSuit) {
				legal = false;
				break;
			}
		}
		return legal;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "Flush".
	 * 
	 * @return The string specifying the type of this hand, which is always "Flush".
	 */
	public String getType() {
		return "Flush";
	}
}
