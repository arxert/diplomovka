package game;

import java.util.Arrays;

import bots.Bot;

import computes.Bank;
import computes.Rules;

import utils.ListOfPlayers;
import utils.Value;

public class GameEngine {
	
	private Thread game;
	
	private boolean isRunning = true;
	private boolean end = false;
	
	private Talon talon = new Talon();
	
	private ViewEngine viewEngine;
	
	private int dealer = 0;
	
	private Card[] deck = new Card[5];
	
	private double initChips = 10000;
	
	private Bank bank;
	
	private State gameState;
	
	public ListOfPlayers players = new ListOfPlayers(initChips);
	
	private int rounds = 0;
	private int speed = 0;
//	private int smallBlind = 10, bigBlind = smallBlind * 2;

	private int actBlindIndex = 0;
	private int smallBlinds[] = {25, 50, 100, 250, 500, 1000, 1500, 2000, 4000, 8000};
	private int bigBlind = smallBlinds[actBlindIndex] * 2;
	
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
		players.getPlayer(ID).setState(Value.state.none);
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
		gameState.addAction(0, ID, Value.state.folded);
	}
	
	public void check(int ID){
		viewEngine.check(ID);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(0, ID, Value.state.checked);
	}
	
	public void call(int ID, double chips){
		bank.addChips(chips);
		viewEngine.call(ID, chips);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(chips, ID, Value.state.called);
	}
	
	public void raise(int ID, double chips){
		bank.addChips(chips);
		viewEngine.raise(ID, chips);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(chips, ID, Value.state.raised);
	}

	public void allIn(int ID, double chips){
		bank.addChips(chips);
		viewEngine.allIn(ID, chips);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(chips, ID, Value.state.allIn);
	}
	
	public void plLeft(int ID){
		viewEngine.plLeft(ID);
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
		dealer = players.getActivePlayers().get(0).getID();
		bank = new Bank(players);
		while (!end) {
			try {Thread.sleep(50);} catch (Exception e1) {e1.printStackTrace();}
			while (isRunning){
				rounds += 1;
				viewEngine.setRounds(rounds);
				initNewRound();
				setDealer();
//				setBlinds();
				gameState = new State(dealer, smallBlinds[actBlindIndex], players);
//				gameState = new State(dealer, smallBlind, players);
				playRounds();
				if (speed != 0) try { Thread.sleep(speed);} catch (Exception e) {e.printStackTrace(); }
				viewEngine.newRound(false);
				players.checkChipsAll();
				System.out.println(gameState);
				checkSumPls();
				if (players.getSize() == 1){
					System.out.println("winner is " + players.getAllPlayers().get(0).getName() + players.getAllPlayers().get(0).getID());
					end = true;
					break;
				}
				if (speed != 0) try {Thread.sleep(speed);} catch (Exception e) {}
			}
		}
		System.out.println("game ended");
	}
	
	private void checkSumPls(){
		double res = 0;
		for (Bot b: players.getAllPlayers()){
			res += b.getChips();
		}
		System.out.println(res);
	}
	
	private void setDealer(){
		dealer = players.getNext(dealer).getID();
	}

	// TODO dokoncit blindy
	private void setBlinds(){
		Bot pl = players.getNextActivePlayer(dealer);
//		System.out.println("dealer = " + dealer);
//		System.out.println("next = " + players.getNextActivePlayer(dealer).getID());
//		System.out.println("next next = " + players.getNextActivePlayer(players.getNextActivePlayer(dealer).getID()).getID());
		if (rounds % 150 == 0){
			actBlindIndex += 1;
//			smallBlind *= 2;
			bigBlind = smallBlinds[actBlindIndex] * 2;
			System.out.println("bigBlind = " + bigBlind);
		}
		viewEngine.setTxtLog("Blinds are " + smallBlinds[actBlindIndex] + "/" + bigBlind);
		bank.addChips(pl.payBlinds(smallBlinds[actBlindIndex]));
		bank.addChips(players.getNextActivePlayer(pl.getID()).payBlinds(bigBlind));
	}
	
	private void dealCardsToAllPlayers(){
		for (Bot p: players.getAllPlayers()){
			dealCards(p, getDealedCard(), getDealedCard());
		}
	}

	private void playRounds(){
		for (int i = 0; i <= 3; i++){
			playRound(i);
		}
		setAllScores();
		viewEngine.setTxtLog(bank.splitAll());
	}
	
	private void playRound(int i){
		clearActions();
		if (i == 0)
			setPreflop();
		if (i == 1)
			setFlop();
		if (i == 2)
			setTurn();
		if (i == 3)
			setRiver();
		if (!isEnoughPlayers()){
			return;
		}
		gameState.setRound(i, deck);
		botActions(i);
	}
	
	private boolean isEnoughPlayers(){
		return players.getActivePlayers().size() > 1;
	}
	
	// TODO state
	private void botActions(int round){
		double max = round == 0 ? bigBlind : 0;
		Bot b = players.getNextActivePlayer(players.getNextActivePlayer(players.getNextActivePlayer(dealer).getID()).getID());
		int ID = -1;
		do {
			viewEngine.isOnMove(b.getID());
			if (speed != 0) try {Thread.sleep(speed);} catch (Exception e) {}
			if (ID == -1)
				ID = b.getID();
			gameState.setBank(bank.getChips());
			
//			System.out.println("on move is " + b.getID());
//			System.out.println(gameState);
			
			b.act(max, gameState);
			if (!b.getState().equals(Value.state.folded)){
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
			bot.setScore(0);
			bot.nullStakes();
		}
		deck = new Card[5];
		viewEngine.newRound(true);
	}
	
	private void setPreflop(){
		setBlinds();
		dealCardsToAllPlayers();
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
		int score;
		int max = 0;
		Card[] cards;
		for (Bot bot: players.getActivePlayers()){
			cards = joinCards(deck, bot.getCards(), 7);
			score = Rules.findScore(cards);
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
		// add to log
		for (Bot bot: players.getActivePlayers()){
			if (bot.getScore() == max)
				viewEngine.addStats(bot.getCard1(), bot.getCard2());
		}
		return 0;
	}
}
