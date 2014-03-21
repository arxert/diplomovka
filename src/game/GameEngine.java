package game;

import java.util.Arrays;

import bots.Bot;

import computes.Bank;
import computes.Rules;

import utils.ListOfPlayers;
import utils.Value;
import utils.Value.state;

public class GameEngine {
	
	private Thread game;
	
	private boolean isRunning = true;
	private boolean end = false;
	
	private Talon talon = new Talon();
	
	private ViewEngine viewEngine;
	
	private int dealer = 0;
	
	private Card[] deck = new Card[5];
	
	private double initChips = 50000;
	
	private Bank bank;
	
	private State gameState;
	
	public ListOfPlayers players = new ListOfPlayers(initChips);
	
	private int rounds = 0;
	private int speed = 0;
//	private int smallBlind = 10, bigBlind = smallBlind * 2;

	private int actBlindIndex = 0;
	private int smallBlinds[] = {25, 50, 100, 250, 500, 1000, 1500, 2000, 5000, 10000, 15000, 25000, 50000};
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
		viewEngine.setAction(ID, state.none, 0);
	}
	
	public int getRounds(){
		return rounds;
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	/****** player's actions *****/
	
	public void fold(int ID){
		viewEngine.setAction(ID, state.folded, 0);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(0, ID, Value.state.folded);
	}
	
	public void check(int ID){
		viewEngine.setAction(ID, state.checked, 0);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(0, ID, Value.state.checked);
	}
	
	public void call(int ID, double chips){
		bank.addChips(chips);
		viewEngine.setAction(ID, state.called, chips);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(chips, ID, Value.state.called);
	}
	
	public void raise(int ID, double chips){
		bank.addChips(chips);
		viewEngine.setAction(ID, state.raised, chips);
		viewEngine.setTxtLog("" + bank.getChips());
		gameState.addAction(chips, ID, Value.state.raised);
	}

	public void allIn(int ID, double chips){
		bank.addChips(chips);
		viewEngine.setAction(ID, state.allIn, chips);
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
				playRounds();
				if (speed != 0) try { Thread.sleep(speed);} catch (Exception e) {e.printStackTrace(); }
				viewEngine.newRound(false);
				players.checkChipsAll();
//				System.out.println(gameState);
//				if (checkSumPls() != 5 * initChips){
//					System.out.println(gameState);
//					System.out.println(checkSumPls());
//					return;
//				}
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
	
	private double checkSumPls(){
		double res = 0;
		for (Bot b: players.getAllPlayers()){
			res += b.getChips();
		}
		return res;
	}
	
	private void setDealer(){
		dealer = players.getNext(dealer).getID();
	}

	private void setBlinds(){
		Bot pl = players.getNextActivePlayer(dealer);
		double sb, bb;
		if (rounds % 150 == 0){
			actBlindIndex += 1;
			bigBlind = smallBlinds[actBlindIndex] * 2;
			System.out.println("bigBlind = " + bigBlind);
		}
		viewEngine.setTxtLog("Blinds are " + smallBlinds[actBlindIndex] + "/" + bigBlind);
		
		sb = pl.payBlinds(smallBlinds[actBlindIndex]);
		bank.addChips(sb);
		gameState.addAction(sb, pl.getID(), Value.state.smallBlind);
		viewEngine.setAction(pl.getID(), state.smallBlind, smallBlinds[actBlindIndex]);
		
		pl = players.getNextActivePlayer(pl.getID());
		bb = pl.payBlinds(bigBlind);
		bank.addChips(bb);
		gameState.addAction(bb, pl.getID(), Value.state.bigBlind);
		viewEngine.setAction(pl.getID(), state.bigBlind, bigBlind);
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
		double max = 0;
		if (round == 0){
			setBlinds();
			max = bigBlind;
		}
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
			if (players.getActivePlayers().size() == 1)
				return;
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
