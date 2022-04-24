/**
 * A subclass of the Hand class and is used to model a hand of Pair in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class Pair extends Hand{
	/**
	 * A constructor for building a hand of Pair with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of Pair.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of Pair; otherwise, false.
	 */
	public boolean isValid() {
		if (this.size() == 2 && this.getCard(0).getRank() == this.getCard(1).getRank())
			return true;
		return false;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "Pair".
	 * 
	 * @return The string specifying the type of this hand, which is always "Pair".
	 */
	public String getType() {
		return "Pair";
	}
}
