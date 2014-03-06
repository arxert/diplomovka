package game;

import java.util.ArrayList;

import bots.Bot;

import utils.Value.*;

public class State {

	int round, dealerID;
	int smallBlind, bigBlind;
	double bank;
	Card[] cards;
	ArrayList<Round> rounds = new ArrayList<>();
	ArrayList<Player> players = new ArrayList<>();
	
	class Action {
		double amount;
		int botID;
		state state;
		
		public Action(double amount, int botID, state state){
			this.amount = amount;
			this.botID = botID;
			this.state = state;
		}
	}
	
	class Round {
		ArrayList<Action> actions = new ArrayList<>();		
		int round;
		
		public Round(int round){
			this.round = round;
		}
		
		public void addAction(Action a){
			actions.add(a);
		}
	}
	
	class Player {
		int id;
		double stack;
		double inGame;
		state state;
		
		public Player(int id, double stack, double inGame){
			this.id = id;
			this.stack = stack;
			this.inGame = inGame;
		}
		
		public void setChips(double stack, double inGame){
			this.stack = stack;
			this.inGame = inGame;
		}
	}
	
	public State(int dealerID, int smallBlind){
		this.round = 0;
		this.smallBlind = smallBlind;
		this.bigBlind = smallBlind * 2;
		this.dealerID = dealerID;
	}
	
	public void setBank(double bank){
		this.bank = bank;
	}
	
	public void setRound(int round, Card[] cards){
		this.round = round;
		rounds.add(new Round(round));
		this.cards = cards;
	}
	
	public void addPlayer(Bot bot){
		players.add(new Player(bot.getID(), bot.getChips(), bot.getTotalStake()));
	}
	
	public void addAction(double amount, int botID, state state){
		rounds.get(round).addAction(new Action(amount, botID, state));
		Player p = getPlayer(botID);
		if (p != null) {
			p.setChips(p.stack - amount, p.inGame + amount);
			p.state = state;
		}
	}
	
	private Player getPlayer(int ID){
		for (Player p: players)
			if (p.id == ID)
				return p;
		return null;
	}
	
	public String toString(){
		String res = "dealerID = " + dealerID + ", bank = " + bank + ", smallBlind = " + smallBlind + ", bigBlind = " + bigBlind + "\nplayers: ";
		for (Player p: players){
			res += "player " + p.id + " (" + p.stack + ", " + p.inGame + ", " + p.state + "), ";
		}
		res += "\ncards: ";
		for (Card c: cards){
			res += c + ", ";
		}
		res += "\n";
		for (Round r: rounds){
			res += "round " + r.round + " - actions:\n";
			for (Action a: r.actions)
				res += "player " + a.botID + " " + a.state + " (" + a.amount +  ")\n";
		}
		return res;
	}
}
