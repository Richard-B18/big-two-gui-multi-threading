/**
 * A subclass of the Hand class and is used to model a hand of Full House in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class FullHouse extends Hand{
	/**
	 * A constructor for building a hand of Full House with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	FullHouse(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of Full House.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of Full House; otherwise, false.
	 */
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		}
		
		if (this.getCard(1).getRank() == this.getCard(2).getRank()) {
			if (!(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank())) {
				return false;
			}
			
			// also check if first and last is different rank
			if (!(this.getCard(3).getRank() == this.getCard(4).getRank() && this.getCard(0).getRank() != this.getCard(4).getRank())) {
				return false;
			}
		} else if (this.getCard(2).getRank() == this.getCard(3).getRank()) {
			if (!(this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank())) {
				return false;
			}
			
			// also check if first and last is different rank
			if (!(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(0).getRank() != this.getCard(4).getRank())) {
				return false;
			}
		} else {
			return false;
		}
		return true;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "FullHouse".
	 * 
	 * @return The string specifying the type of this hand, which is always "FullHouse".
	 */
	public String getType() {
		return "FullHouse";
	}
}
