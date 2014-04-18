package bots;

import game.Card;
import game.State;
import helps.Ranks;

import java.util.ArrayList;
import java.util.Random;

import utils.CardGenerator;
import utils.Value;

public class EasyBot extends Bot {

	private static String staticName = "Easy bot";
	private State state;

	private CardGenerator cGen = new CardGenerator();
	private ArrayList<Card> cards;
	
	public EasyBot(int id, double chips) {
		super(id, chips);
		setName(getStaticName());
	}

	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void doAct(double max, State state) {
		this.state = state;
		max -= roundStake;
		simpleAct(max);
//		call(max);
//		manageState();
	}
	
	private void simpleAct(double max){
		if (max == 0){
			Random r = new Random();
			if (r.nextBoolean())
				check();
			else
				raise(20);
		} else {
			Random r = new Random();
			if (r.nextInt(10) < 8)
				call(max);
			else
				if (r.nextInt(10) < 7)
					raise(max + 10);
				else
					fold();
		}
	}
	
	private void manageState(){
		if (state.round == 0)
			cards = cGen.getCards();
		if (state.round == 1)
			round1();
		if (state.round == 2)
			round2();
		if (state.round == 3)
			round3();
	}
	
	private void round1(){
		int sMe = 0, sOther = 0, all = 0, good = 0;
		for (Card c: state.deck){
			if (c != null){
				for (int i = cards.size() - 1; i >= 0; i--){
					if (c.getHash() == cards.get(i).getHash()){
						cards.remove(i);
						break;
					}
				}
			}
		}
		for (int i = 0; i < cards.size() - 1; i++){
			for (int j = i + 1; j < cards.size(); j++){
				for (int k = 0; k < cards.size(); k++){
					for (int l = k + 1; l < cards.size(); l++){
						if (i != j && i != l && i != k && j != l && j != k){
							state.deck[3] = cards.get(i);
							state.deck[4] = cards.get(j);
							all += 1;
							sMe = Ranks.findScore(Value.concatCards(state.deck, getCards()));
							sOther = Ranks.findScore(Value.concatCards(state.deck, new Card[] {cards.get(k), cards.get(l)}));
							if (sMe >= sOther){
								good += 1;
							}
						}
					}
				}
			}
		}
		System.out.println("ID = " + ID + ", round = " + state.round + " - " + (double) good / all);
	}
	
	private void round2(){
		int sMe = 0, sOther = 0, all = 0, good = 0;
		for (Card c: state.deck){
			if (c != null){
				for (int i = cards.size() - 1; i >= 0; i--){
					if (c.getHash() == cards.get(i).getHash()){
						cards.remove(i);
						break;
					}
				}
			}
		}
		for (int i = 0; i < cards.size() - 1; i++){
			state.deck[4] = cards.get(i);
			for (int k = 0; k < cards.size(); k++){
				for (int l = k + 1; l < cards.size(); l++){
					if (i != l && i != k && k != l){
						all += 1;
						sMe = Ranks.findScore(Value.concatCards(state.deck, getCards()));
						sOther = Ranks.findScore(Value.concatCards(state.deck, new Card[] {cards.get(k), cards.get(l)}));
						if (sMe >= sOther)
							good += 1;
					}
				}
			}
		}
		System.out.println("ID = " + ID + ", round = " + state.round + " - " + (double) good / all);
	}
	
	private void round3(){
		int sMe = 0, sOther = 0, all = 0, good = 0;
		for (Card c: state.deck){
			for (int i = cards.size() - 1; i >= 0; i--){
				if (c.getHash() == cards.get(i).getHash()){
					cards.remove(i);
					break;
				}
			}
		}
		for (int k = 0; k < cards.size() - 1; k++){
			for (int l = k + 1; l < cards.size(); l++){
				if (k != l){
					all += 1;
					sMe = Ranks.findScore(Value.concatCards(state.deck, getCards()));
					sOther = Ranks.findScore(Value.concatCards(state.deck, new Card[] {cards.get(k), cards.get(l)}));
					if (sMe >= sOther)
						good += 1;
				}
			}
		}
		System.out.println("ID = " + ID + ", round = " + state.round + " - " + (double) good / all);
	}
}
