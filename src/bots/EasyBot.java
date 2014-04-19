package bots;

import game.State;
import helps.Ranks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import computes.HandStrength;

import utils.CardGenerator;
import utils.Value;

public class EasyBot extends Bot {

	private static String staticName = "Easy bot";
	private State state;
	private Integer[] deck;

	private CardGenerator cGen = new CardGenerator();
	private ArrayList<Integer> cards;
	
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
		deck = state.deck;
		max -= roundStake;
//		simpleAct(max);
		call(max);
		manageState();
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
	
	private void removeCardsFromDeck(){
		for (Integer c: deck){
			if (c != null){
				for (int i = cards.size() - 1; i >= 0; i--){
					if (c == cards.get(i)){
						cards.remove(i);
						break;
					}
				}
			}
		}
		for (Integer c: getCards()){
			for (int i = cards.size() - 1; i >= 0; i--){
				if (c == cards.get(i)){
					cards.remove(i);
					break;
				}
			}
		}
	}
	
	private void countEffectiveHandStrength(){
		int[][] hp = new int[3][3];
		int[] hpTot = new int[3];
		Integer[] board = Arrays.copyOf(deck, 5);
		Integer[] opCards = new Integer[2];
		board[3] = getCard1();
		board[4] = getCard2();
		int myRank = Ranks.findScore(board);
		int opRank, index;
		double pPot, nPot;
		int allHp = 0, allHpTot = 0;
		for (int i = 0; i < cards.size(); i++){
			opCards[0] = cards.get(i);
			for (int j = i + 1; j < cards.size(); j++){
				opCards[1] = cards.get(j);
				board[3] = opCards[0];
				board[4] = opCards[1];
				opRank = Ranks.findScore(board);
				if (myRank > opRank)
					index = 0;
				else if (myRank == opRank)
					index = 1;
				else
					index = 2;
				hpTot[index] += 1;
				allHpTot += 1;
				for (int k = 0; k < cards.size() - 1; k++){
					for (int l = k + 1; l < cards.size(); l++){
						if (i != k && i != l && j != k && j != l){
							board[3] = cards.get(k);
							board[4] = cards.get(l);
							myRank = Ranks.findScore(Value.concatCards(board, getCards()));
							opRank = Ranks.findScore(Value.concatCards(board, opCards));
							if (myRank > opRank)
								hp[index][0] += 1;
							else if (myRank == opRank)
								hp[index][1] += 1;
							else
								hp[index][2] += 1;
							allHp += 1;
						}
					}
				}
			}
		}
//		for (int i = 0; i < 3; i++){
//			System.out.print(hpTot[i] + " ");
//		}
//		System.out.println();
//		for (int i = 0; i < 3; i++){
//			for (int j = 0; j < 3; j++){
//				System.out.print(hp[i][j] + " ");
//			}
//			System.out.println();
//		}

		pPot = ((double) (hp[1][0]/2 + hp[2][0] + hp[2][1]/2) / allHp) / ((double) (hpTot[2] + hpTot[1]/2) / allHpTot);
		nPot = ((double) (hp[0][1]/2 + hp[0][2] + hp[1][2]/2) / allHp) / ((double) (hpTot[0] + hpTot[1]/2) / allHpTot);
		
//		pPot = (double) (hp[2][0] + hp[2][1]/2 + hp[1][0]/2) / (hpTot[2] + hpTot[1]);
//		nPot = (double) (hp[0][2] + hp[1][2]/2 + hp[0][1]/2) / (hpTot[0] + hpTot[1]);
//		Ppot = (HP[behind][ahead]+HP[behind][tied]/2+HP[tied][ahead]/2)/(HPTotal[behind]+HPTotal[tied])
//		Npot = (HP[ahead][behind]+HP[tied][behind]/2+HP[ahead][tied]/2)/(HPTotal[ahead]+HPTotal[tied])
		this.pPot = pPot;
		this.nPot = nPot;
//		this.pPot = (double) pPot / (pPot + nPot);
//		this.nPot = 1 - this.pPot;
		System.out.println(this.pPot + " " + this.nPot);
		deck[3] = null;
		deck[4] = null;
	}
	
	private double countHandStrenght(int i, int j){
		int all = 0, good = 0;
		int myRank = Ranks.findScore(Value.concatCards(deck, getCards()));
		int opRank;
		for (int k = 0; k < cards.size(); k++){
			for (int l = k + 1; l < cards.size(); l++){
				if (k != i && k != j && l != i && l != j){
					opRank = Ranks.findScore(Value.concatCards(deck, new Integer[] {cards.get(k), cards.get(l)}));
					if (myRank >= opRank)
						good += 1;
					all += 1;
				}
			}
		}
		return (double) good / all;
	}
	
	private void round1(){
		int all = 0;
		double hs = 0;
		removeCardsFromDeck();
		for (int i = 0; i < cards.size() - 1; i++){
			for (int j = i + 1; j < cards.size(); j++){
				deck[3] = cards.get(i);
				deck[4] = cards.get(j);
				hs += countHandStrenght(i, j);
				all += 1;
			}
		}
		hs = (double) hs / all;
		deck[3] = null;
		deck[4] = null;
//		System.out.println(hs - HandStrength.countHandStrenght(state.deck, getCards(), cards));
		System.out.println(hs);
		
		countEffectiveHandStrength();
		ehs = hs * (1 - nPot) + (1 - hs) * pPot;
		System.out.println(ehs);
		System.out.println("ID = " + ID + ", round = " + state.round + " - " + (double) hs);
	}
	
	private void round2(){
		int all = 0;
		double hs = 0;
		removeCardsFromDeck();
		for (int i = 0; i < cards.size() - 1; i++){
			deck[4] = cards.get(i);
			hs += countHandStrenght(-1, i);
			all += 1;
		}
		deck[4] = null;
		System.out.println("ID = " + ID + ", round = " + state.round + " - " + (double) hs / all);
	}
	
	private void round3(){
		removeCardsFromDeck();
		double hs = countHandStrenght(-1, -1);
		System.out.println("ID = " + ID + ", round = " + state.round + " - " + hs);
	}
}
