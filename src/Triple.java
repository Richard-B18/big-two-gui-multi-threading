/**
 * A subclass of the Hand class and is used to model a hand of Triple in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class Triple extends Hand{
	/**
	 * A constructor for building a hand of Triple with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	Triple(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of Triple.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of Triple; otherwise, false.
	 */
	public boolean isValid() {
		boolean sameRank = this.getCard(0).getRank() == this.getCard(1).getRank();
		sameRank = sameRank && (this.getCard(1).getRank() == this.getCard(2).getRank());
		
		// verify one more time (even though it's unnecessary) 
		sameRank = sameRank && (this.getCard(0).getRank() == this.getCard(2).getRank());
		if (this.size() == 3 && sameRank) {
			return true;
		}
		return false;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "Triple".
	 * 
	 * @return The string specifying the type of this hand, which is always "Triple".
	 */
	public String getType() {
		return "Triple";
	}
}
