/**
 * A subclass of the Hand class and is used to model a hand of Quad in a Big Two Card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class Quad extends Hand{
	/**
	 * A constructor for building a hand of Quad with the specified player and list of cards.
	 * 
	 * @param player The player that specifies which hand to build to.
	 * @param cards The list of cards to be built for the specified player.
	 */
	Quad(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * The method to check if this hand is a valid hand of Quad.
	 * 
	 * @return A boolean with a value true if this hand is a valid hand of Quad; otherwise, false.
	 */
	public boolean isValid() {
		if (this.size() != 5) {
			return false;
		}
		
		int start = 0;
		if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
			start = 0;
		} else if (this.getCard(3).getRank() == this.getCard(4).getRank()) {
			start = 1;
		} else {
			return false;
		}
		
		int quadRank = this.getCard(start).getRank();
		boolean legal = true;
		
		for (int i = start; i < start + 4; i++) {
			if (this.getCard(i).getRank() != quadRank) {
				legal = false;
				break;
			}
		}
		return legal;
	}
	
	/**
	 * The method returning a string specifying the type of this hand. In this case, this method will always return "Quad".
	 * 
	 * @return The string specifying the type of this hand, which is always "Quad".
	 */
	public String getType() {
		return "Quad";
	}
}
