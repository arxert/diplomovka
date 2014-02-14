package utils;


import game.Bot;
import game.GameEngine;

import java.util.ArrayList;

public class ListOfPlayers {

	private ArrayList<Bot> players = new ArrayList<>();
	private double chips;
	
	public ListOfPlayers(double initChips){
		chips = initChips;
	}
	
	public Iterable<Bot> getAllPlayers(){
		return players;
	}
	
	public ArrayList<Bot> getActivePlayers(){
		ArrayList<Bot> res = new ArrayList<>();
		for (Bot bot: players){
			if (!bot.getState().equals(Value.state.folded))
				res.add(bot);
		}
		return res;
	}

	public void filter(String[] pls){
		Bot p = null;
		int i = 0;
		for (String s: pls){
			if ((p = Value.createNewPlayer(s, i)) != null){
				p.setState(Value.state.none);
				p.setChips(chips);
				players.add(p);
			}
			i += 1;
		}
	}
	
	public Bot getPlayer(int id){
		for (Bot p: players){
			if (p.getID() == id)
				return p;
		}
		return null;
	}
	
	public Bot getPlayer(String name){
		for (Bot p: players){
			if (name.equals(p.toString()))
				return p;
		}
		return null;
	}
	
	public int getSize(){
		return players.size();
	}
	
	public void removePlayer(int ID){
		players.remove(getPlayer(ID));
	}
	
	public void setEngine(GameEngine e){
		for (Bot p: players)
			p.setEngine(e);
	}
}
