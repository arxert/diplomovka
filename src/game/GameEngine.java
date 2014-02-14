package game;

import java.util.Arrays;

import utils.ListOfPlayers;
import utils.Value;

public class GameEngine {
	
	private Thread game;
	
	private boolean isRunning = true;
	private boolean end = false;
	
	private Talon talon = new Talon();
	
	private ViewEngine viewEngine;
	
	private Bot dealer, bigBlind, smallBlind;
	
	private Card[] deck = new Card[5];
	
	private double initChips = 2000;
	
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
	
	private void setState(int ID, Value.state state){
		players.getPlayer(ID).setState(state);
		viewEngine.setState(ID, state);
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
	}
	
	public void check(int ID){
		viewEngine.check(ID);
	}
	
	public void call(int ID, double chips){
		viewEngine.call(ID, chips);
	}
	
	public void raise(int ID, double chips){
		viewEngine.raise(ID, chips);
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
		stopGame();
		end = true;
	}
	
	public void stopGame(){
		isRunning = false;
	}
	
	public void continueGame(){
		isRunning = true;
	}
	
	private void play(){
		int i = 0;
		while (!end) {
			i += 1;
			while (isRunning){
				rounds += 1;
				viewEngine.setRounds(rounds);
				initNewRound();
				setDealer();
				setBlinds();
				dealCardsToAllPlayers();
				playRounds();
				if (speed != 0)
				try {
					Thread.sleep(speed);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("game ended");
	}
	
	private void setDealer(){
		
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
//			if (!isRunning)
//				return;
			playRound(i);
		}
		findWinner();
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
		for (Bot bot: players.getActivePlayers()){
			if (speed != 0)
				try {
					Thread.sleep(speed);
				} catch (Exception e) {}
			bot.act();
		}
	}
	
	private void clearActions(){
		for (Bot bot: players.getActivePlayers()){
			setState(bot.getID(), Value.state.none);
		}
	}
	
	private void initNewRound(){
		talon.shuffle();
		for (Bot bot: players.getAllPlayers()){
			setState(bot.getID(), Value.state.none);
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
	
	private int findWinner(){
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
//		for (Bot bot: players.getActivePlayers()){
//			if (bot.getScore() == max)
//				viewEngine.addStats(bot.getCard1(), bot.getCard2());
//		}
		return 0;
	}
}
