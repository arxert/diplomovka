package game;

import computes.Probability;

import view.MenuPanel;

public class Main {

	public static void main(String[] args) {
		new MenuPanel();
		Probability p = Probability.loadProbability();
		p.write();
//		new HandGenerator();
//		Card[] cards = {new Card(value._10, suit.CLUBS), new Card(value._10, suit.DIAMONDS), new Card(value._K, suit.CLUBS), 
//				new Card(value._K, suit.HEARTS), new Card(value._6, suit.SPADES), new Card(value._4, suit.HEARTS), new Card(value._2, suit.CLUBS)};
//		System.out.println(Rules.findScore(cards));
//		Card[] cardss = {new Card(value._10, suit.CLUBS), new Card(value._10, suit.DIAMONDS), new Card(value._K, suit.CLUBS), 
//				new Card(value._K, suit.HEARTS), new Card(value._6, suit.SPADES), new Card(value._7, suit.HEARTS), new Card(value._8, suit.CLUBS)};
//		System.out.println(Rules.findScore(cardss));
	}
}
