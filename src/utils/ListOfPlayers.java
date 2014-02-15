package utils;


import game.Bot;
import game.GameEngine;

import java.util.ArrayList;

import utils.Value.state;

public class ListOfPlayers {

	private ArrayList<Bot> players = new ArrayList<>();
	private double chips;
	private int size;
	private Bot temp;
	
	public ListOfPlayers(double initChips){
		chips = initChips;
	}
	
	public Iterable<Bot> getAllPlayers(){
		return players;
	}
	
	public Bot getNextActivePlayer(int ID){
		if ((temp = getNext(ID)).getState() != state.folded)
			return temp;
		else 
			return getNextActivePlayer(temp.getID());
	}
	
	public Bot getNext(int ID){
		for (int i = 0; i < players.size(); i++){
			if (players.get(i).getID() == ID)
				return players.get((i + 1) % size);
		}
		return null;
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
		size = players.size();
	}
	
	public Bot getPlayer(int ID){
		for (Bot p: players){
			if (p.getID() == ID)
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
		return size;
	}
	
	public void removePlayer(int ID){
		players.remove(getPlayer(ID));
		size -= 1;
	}
	
	public void setEngine(GameEngine e){
		for (Bot p: players)
			p.setEngine(e);
	}
}