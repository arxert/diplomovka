package bots;

import game.Bot;

public class Person extends Bot {

	private static String staticName = "Player";
	
	private boolean run = true;
	
	public Person(int id) {
		super(id);
		setName(getStaticName());
	}
	
	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void act() {
		while(run){
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		run = true;
	}
	
	public void stop(){
		run = false;
	}
}
