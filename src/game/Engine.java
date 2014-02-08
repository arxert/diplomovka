package game;

import utils.ListOfPlayers;
import view.MainFrame;

public class Engine {
	
	private ListOfPlayers players = new ListOfPlayers();
	private Talon talon = new Talon();
	private MainFrame mainFrame;
	
	public Engine(String[] pls) {
		players.filter(pls);
		players.setEngine(this);
		mainFrame = new MainFrame(players);
		for (Bot p: players.getPlayers()){
			p.dealCard(dealCard(), dealCard());
		}
	}
	
	public Card dealCard(){
		return talon.getNextCard();
	}
	
	//****** player's actions *****//
	
	public void fold(int ID){
		mainFrame.fold(ID);
//		gamePanel.setTurn(dealCard());
//		consoleWindow.addLog("Player " + ID + " folded");
	}
	
	public void check(int ID){
		players.getPlayer(ID).dealCard(dealCard(), dealCard());
//		gamePanel.setFlop(dealCard(), dealCard(), dealCard());
	}
	
	public void call(int ID){
		
	}
	
	public void raise(int ID, double raise){
		
	}
}
