package game;

import java.util.Arrays;

import computes.Bank;
import computes.Rules;

import utils.ListOfPlayers;
import utils.Value.state;

public class GameEngine {
	
	private Thread game;
	
	private boolean isRunning = true;
	private boolean end = false;
	
	private Talon talon = new Talon();
	
	private ViewEngine viewEngine;
	
	private Bot dealer, bigBlind, smallBlind;
	
	private Card[] deck = new Card[5];
	
	private double initChips = 2000;
	
	private Bank bank;
	
	public ListOfPlayers players = new ListOfPlayers(initChips);
	
	private int rounds = 0;
	private int speed = 0;
	
	public GameEngine(String[] pls){
		players.filter(pls);
		players.setEngine(this);
		viewEngine = new ViewEngine(this, players);
		isRunning = false;
		startGame();
	}
	
	private Card getDealedCard(){
		return talon.getNextCard();
	}

	private void dealCards(Bot bot, Card c1, Card c2){
		bot.dealCards(c1, c2);
		viewEngine.dealCard(bot.getID(), c1, c2);
	}
	
	private void setNoneState(int ID){
		players.getPlayer(ID).setState(state.none);
		viewEngine.setNoneState(ID);
	}
	
	public int getRounds(){
		return rounds;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	/****** player's actions *****/
	
	public void fold(int ID){
		viewEngine.fold(ID);
		viewEngine.setTxtLog("" + bank.getChips());
//		bank.fold(ID);
	}
	
	public void check(int ID){
		viewEngine.check(ID);
		viewEngine.setTxtLog("" + bank.getChips());
	}
	
	public void call(int ID, double chips){
		bank.addChips(chips);
		viewEngine.call(ID, chips);
		viewEngine.setTxtLog("" + bank.getChips());
//		bank.call(ID, chips);
	}
	
	public void raise(int ID, double chips){
		bank.addChips(chips);
		viewEngine.raise(ID, chips);
		viewEngine.setTxtLog("" + bank.getChips());
//		bank.raise(ID, chips);
	}

	public void allIn(int ID, double chips){
		bank.addChips(chips);
		viewEngine.allIn(ID, chips);
		viewEngine.setTxtLog("" + bank.getChips());
//		bank.allIn(ID, chips);
	}
	
	/***** GAME IMPLEMENTATION ******/
	
	public void startGame(){
		game = new Thread(){
			public void run(){
				play();
			}
		};
		game.start();
	}
	
	public void endGame(){
		pauseGame();
		end = true;
	}
	
	public void pauseGame(){
		isRunning = false;
	}
	
	public void continueGame(){
		isRunning = true;
	}
	
	private void play(){
		while (!end) {
			try {Thread.sleep(50);} catch (Exception e1) {e1.printStackTrace();}
			bank = new Bank(players);
			while (isRunning){
				rounds += 1;
				viewEngine.setRounds(rounds);
				initNewRound();
				setDealer();
				setBlinds();
				dealCardsToAllPlayers();
				playRounds();
				if (speed != 0)
					try { Thread.sleep(speed);} catch (Exception e) {e.printStackTrace(); }
				for (Bot b: players.getAllPlayers()){
					b.setScore(0);
					b.nullStakes();
				}
			}
		}
		System.out.println("game ended");
	}
	
	private void setDealer(){
		dealer = players.getActivePlayers().get(0);
	}
	
	private void setBlinds(){
		
	}
	
	private void dealCardsToAllPlayers(){
		for (Bot p: players.getAllPlayers()){
			dealCards(p, getDealedCard(), getDealedCard());
		}
	}

	private void playRounds(){
		botActions();
		for (int i = 0; i <= 2; i++){
//			bank.check();
//			if (!isRunning)
//				return;
			playRound(i);
		}
		if (!isEnoughPlayers())
			return;
		setAllScores();
//		bank.split();
		bank.splitAll();
	}
	
	private void playRound(int i){
		if (!isEnoughPlayers()){
			return;
		}
		clearActions();
		if (i == 0)
			setFlop();
		if (i == 1)
			setTurn();
		if (i == 2)
			setRiver();
		botActions();
	}
	
	private boolean isEnoughPlayers(){
		return players.getActivePlayers().size() > 1;
	}
	
	private void botActions(){
		double max = 0;
		Bot b = dealer;
		int ID = -1;
		if (b.getState().equals(state.folded))
			b = players.getNextActivePlayer(b.getID());
		do {
			viewEngine.isOnMove(b.getID());
			if (speed != 0) try {Thread.sleep(speed);} catch (Exception e) {}
			if (ID == -1)
				ID = b.getID();
			b.act(max);
			if (!b.getState().equals(state.folded)){
				if (b.getRoundStake() > max){
					max = b.getRoundStake();
					ID = b.getID();
				}
			} else {
				if (ID == b.getID())
					ID = -1;
			}
			b = players.getNextActivePlayer(b.getID());
		} while (ID != b.getID());
	}
	
	private void clearActions(){
		for (Bot bot: players.getActivePlayers()){
			bot.newInnerRound();
			setNoneState(bot.getID());
		}
	}
	
	private void initNewRound(){
		talon.shuffle();
		for (Bot bot: players.getAllPlayers()){
			setNoneState(bot.getID());
		}
		viewEngine.newRound();
	}
	
	private void setFlop(){
		for (int i = 0; i < 3; i++){
			deck[i] = talon.getNextCard();
		}
		viewEngine.setFlop(deck[0], deck[1], deck[2]);
	}
	
	private void setTurn(){
		deck[3] = talon.getNextCard();
		viewEngine.setTurn(deck[3]);
	}
	
	private void setRiver(){
		deck[4] = talon.getNextCard();
		viewEngine.setRiver(deck[4]);
	}
	
	private Card[] joinCards(Card[] cards1, Card[] cards2, int size){
		Card[] res;
		res = Arrays.copyOf(cards1, size);
		for (int i = cards1.length; i < size; i++){
			res[i] = new Card(cards2[i - cards1.length].getValue(), cards2[i - cards1.length].getSuit());
		}
		return res;
	}
	
	private int setAllScores(){
//		System.out.println("finding winner");
		Card[] cards;
		int score;
		int max = 0;
		for (Bot bot: players.getActivePlayers()){
			cards = joinCards(deck, bot.getCards(), 7);
			score = Rules.findScore(cards);
			if (score != -1){
				bot.setScore(score);
				if (score >= max){
					max = score;
				}
//				Arrays.sort(cards, Rules.compValues);
//				System.out.print("bot " + bot.getID() + " - " + Value.hands.values()[((score & 0xf00000) >> 0x14) - 6] + " (" + score + ")");
//				for (int i = 0; i < cards.length; i++)
//					System.out.print(", " + cards[i]);
//				System.out.println();
			}
		}
		for (Bot bot: players.getActivePlayers()){
			if (bot.getScore() == max)
				viewEngine.addStats(bot.getCard1(), bot.getCard2());
		}
		return 0;
	}
}
