package game;

import utils.ListOfPlayers;

public class GameEngine {
	
	private ListOfPlayers players = new ListOfPlayers();
	private Talon talon = new Talon();
	private ViewEngine viewEngine;
	
	public GameEngine(String[] pls){
		players.filter(pls);
		players.setEngine(this);
		viewEngine = new ViewEngine(this, players);
		for (Bot p: players.getPlayers()){
			dealCards(p, getDealedCard(), getDealedCard());
		}
	}
	
	private Card getDealedCard(){
		return talon.getNextCard();
	}

	private void dealCards(Bot bot, Card c1, Card c2){
		bot.dealCard(c1, c2);
		viewEngine.dealCard(bot.getID(), c1, c2);
	}
	
//	private void dealCard(Bot bot, Card card){
//		bot.dealCard(card);
//		viewEngine.dealCard(bot.getID(), card);
//	}
	
	//****** player's actions *****//
	
	public void fold(int ID){
		viewEngine.fold(ID);
//		gamePanel.setTurn(dealCard());
//		consoleWindow.addLog("Player " + ID + " folded");
	}
	
	public void check(int ID){
		viewEngine.check(ID);
//		players.getPlayer(ID).dealCard(getDealedCard(), getDealedCard());
//		gamePanel.setFlop(dealCard(), dealCard(), dealCard());
	}
	
	public void call(int ID, double chips){
		viewEngine.call(ID, chips);
	}
	
	public void raise(int ID, double chips){
		viewEngine.raise(ID, chips);
	}
}
