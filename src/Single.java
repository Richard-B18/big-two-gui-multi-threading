/**
 * A subclass of the Hand class and is used to model a hand of Single in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class Single extends Hand{
	/**
	 * A constructor for building a hand of Single with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of single.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of single; otherwise, false.
	 */
	public boolean isValid() {
		if (this.size() == 1)
			return true;
		return false;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "Single".
	 * 
	 * @return The string specifying the type of this hand, which is always "Single".
	 */
	public String getType() {
		return "Single";
	}
}
