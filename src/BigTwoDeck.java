/**
 * A subclass of the Deck class and is used to model a deck of cards used in a Big Two card game.
 * 
 * @author Richard Bryan
 * @version 1.0
 *
 */
public class BigTwoDeck extends Deck{
	/**
	 * A method for initializing a deck of Big Two cards.
	 * This method removes all cards from the deck, create 52 Big Two cards and add them to the deck.
	 */
	public void initialize() {	
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard big2Card = new BigTwoCard(i, j);
				addCard(big2Card);
			}
		}
	}
}
