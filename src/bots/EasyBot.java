package bots;

import game.Bot;

public class EasyBot extends Bot {

	private static String staticName = "Easy bot";
	
	public EasyBot(int id) {
		super(id);
		setName(getStaticName());
	}

	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void act() {
//		Random r = new Random();
//		if (r.nextBoolean())
//			check();
//		else
//			fold();
		check();
	}
}
