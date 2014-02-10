package utils;

import game.*;

import java.util.ArrayList;

public class ListOfPlayers {
	
	private ArrayList<Bot> players = new ArrayList<>();
	
	public Iterable<Bot> getPlayers(){
		return players;
	}
	
	public void filter(String[] pls){
		Bot p = null;
		int i = 0;
		for (String s: pls){
			if ((p = Value.createNewPlayer(s, i)) != null){
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
	
	public void setEngine(GameEngine e){
		for (Bot p: players)
			p.setEngine(e);
	}
}
