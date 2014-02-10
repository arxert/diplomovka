package utils;

import game.Bot;
import bots.*;


public class Value {

	public static enum suit {HEARTS, DIAMONDS, CLUBS, SPADES};
	public static enum value {_2, _3, _4, _5, _6, _7 , _8, _9, _10, _J, _Q, _K, _A};
	public static enum state {checked, folded, raised, called};
	
	public static String[] players = {
		"Empty",
		Person.getStaticName(),
		EasyBot.getStaticName()
	};
	
	public static Bot createNewPlayer(String s, int i){
		if (s.equals(Person.getStaticName()))
			return new Person(i);
		if (s.equals(EasyBot.getStaticName()))
			return new EasyBot(i);
		return null;
	}

}
