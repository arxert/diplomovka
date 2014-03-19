package game;

import utils.Value;
import utils.Value.suit;
import utils.Value.value;

import java.util.Collections;
import java.util.LinkedList;

@SuppressWarnings("unchecked")
public class Talon {

	private LinkedList<Card> allCards = new LinkedList<>();
	private LinkedList<Card> cards = new LinkedList<>();
	private static Card emptyCard = new Card(null, null);
	
	public Talon(){
		createAllCards();
		shuffle();
	}
	
	public Card getNextCard(){
		Card c = cards.poll();
//		cards.add(c);
		return c;
	}
	
	public static Card getEmptyCard(){
		return emptyCard;
	}
	
	public void shuffle(){
		cards = (LinkedList<Card>) allCards.clone();
		Collections.shuffle(cards);
	}
	
	private void createAllCards(){
		allCards.clear();
		for (suit s: Value.suit.values()){
			for (value v: Value.value.values()){
				allCards.push(new Card(v, s));
			}
		}
		cards = (LinkedList<Card>) allCards.clone();
	}
	
	public void removeCard(Card c){
		cards.remove(c);
	}
	
	public Talon getCopyOfTalon(){
		Talon t = new Talon();
		return t;
	}
}
