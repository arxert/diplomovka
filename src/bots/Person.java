package bots;

import game.Bot;

public class Person extends Bot {

	private static String staticName = "Player";
	
	public Person(int id) {
		super(id);
		setName(getStaticName());
	}
	
	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void act() {
		
	}
}
