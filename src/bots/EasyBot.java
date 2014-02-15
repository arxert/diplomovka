package bots;

import java.util.Random;

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
	public void act(double max) {
		max -= getRoundStake();
		if (max == 0){
			Random r = new Random();
			if (r.nextBoolean())
				check();
			else
				raise(20);
		} else {
			Random r = new Random();
			if (r.nextInt(10) < 7)
				call(max);
			else
				raise(max + 25);
		}
//		check();
	}
}
