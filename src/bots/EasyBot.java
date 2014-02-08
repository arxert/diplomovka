package bots;

import game.Bot;

public class EasyBot extends Bot {

	private static String staticName = "Easy bot";
	
	public EasyBot(int id) {
		super(id);
		setName(getStaticName());
	}

//	@Override
//	public void setName() {
//		name = staticName;
//	}

	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void act() {
		
	}
}
