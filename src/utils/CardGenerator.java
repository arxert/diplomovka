package utils;

import game.Card;

import java.util.ArrayList;

import utils.Value.suit;
import utils.Value.value;

public class CardGenerator {

	private ArrayList<Card> cards = new ArrayList<Card>();
	
	public CardGenerator(){
		createAllCards();
	}
	
	private void createAllCards(){
		cards.clear();
		for (suit s: Value.suit.values()){
			for (value v: Value.value.values()){
				cards.add(new Card(v, s));
			}
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Card> getCards(){
		return (ArrayList<Card>) cards.clone();
	}
	
	public void checkCards(){
		System.out.println("This is CardGenerator: " + cards.get(0));
	}
	
}
