package bots;

import game.State;

import java.util.Random;

public class EasyBot extends Bot {

	private static String staticName = "Easy bot";
	
	public EasyBot(int id, double chips) {
		super(id, chips);
		setName(getStaticName());
	}

	public static String getStaticName(){
		return staticName;
	}

	@Override
	public void doAct(double max, State state) {
		max -= roundStake;
//		if (max == 0){
//			Random r = new Random();
//			if (r.nextBoolean())
//				check();
//			else
//				raise(20);
//		} else {
//			Random r = new Random();
//			if (r.nextInt(10) < 8)
//				call(max);
//			else
//				if (r.nextInt(10) < 7)
//					raise(max + 10);
//				else
//					fold();
//		}
		call(max);
//		check();
	}
}
