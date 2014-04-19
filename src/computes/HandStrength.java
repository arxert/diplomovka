package computes;

import java.util.ArrayList;

import game.Card;
import helps.Ranks;
import utils.Value;

public class HandStrength {
	
	public static double countHandStrenght(Card[] deck, Card[] myCards, ArrayList<Card> cards){
		int all = 0;
		double hs = 0;
		for (int i = 0; i < cards.size() - 1; i++){
			for (int j = i + 1; j < cards.size(); j++){
				deck[3] = cards.get(i);
				deck[4] = cards.get(j);
				hs += countHS(deck, myCards, cards);
				all += 1;
			}
		}
		return (double) hs / all;
	}
	
	private static double countHS(Card[] deck, Card[] myCards, ArrayList<Card> cards){
		int all = 0, good = 0;
		int opRank;
		int myRank = Ranks.findScore(Value.concatCards(deck, myCards));
		for (int k = 0; k < cards.size(); k++){
			for (int l = k + 1; l < cards.size(); l++){
				opRank = Ranks.findScore(Value.concatCards(deck, new Card[] {cards.get(k), cards.get(l)}));
				if (myRank >= opRank)
					good += 1;
				all += 1;
			}
		}
		return (double) good / all;
	}
}
