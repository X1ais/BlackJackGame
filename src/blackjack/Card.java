package blackjack;

public class Card {
	
	/*
	 * Fields
	 */
	private String suit;
	private String value;	
	int numValue;
	
	public Card() {}
	
	public Card(String value, String suit) {
		this.value = value;
		this.suit = suit;
	}
	
	public int getValue() {
		if ("jqka".contains(value)) { // J,Q,K,A 
			if (value == "a") {
				return 11;
			}
			return 10;
		}
		return Integer.parseInt(value); // 2-10
	}
	
	public boolean isAce() {
		return value == "a";
	}
	
	public String toString() {
		return value + "-" + suit;
	}
	

}
