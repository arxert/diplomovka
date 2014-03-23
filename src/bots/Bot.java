package bots;

import game.Card;
import game.GameEngine;
import game.State;
import game.Talon;
import utils.Value;

public abstract class Bot {
	
	private int steps = 0;
	protected int ID;
	protected int score;
	
	protected double chips;
	protected double totalStake;
	protected double roundStake;
	
	protected String name;
	
	protected Card c1, c2;
	
	protected Value.state state;

	private GameEngine engine;
	
	public Bot(int id, double chips){
		this.ID = id;
		this.chips = chips;
	}

	public abstract void doAct(double max, State state);
	
	public void act(double max, State state){
		steps = 0;
		if (chips == 0){
			
		} else
			doAct(max, state);
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}
	
	public void setEngine(GameEngine engine){
		this.engine = engine;
	}
	
	public void dealCards(Card c1, Card c2){
		this.c1 = c1;
		this.c2 = c2;
	}
	
	public String toString() {
		return getName();
	}

	public int getID() {
		return ID;
	}
	
	public double getChips(){
		return chips;
	}
	
	public void setState(Value.state state){
		this.state = state;
	}
	
	public Value.state getState(){
		return state;
	}
	
	public Card[] getCards(){
		return new Card[] {c1, c2};
	}
	
	public Card getCard1(){
		return this.c1;
	}
	
	public Card getCard2(){
		return this.c2;
	}
	
	public void setScore(int score){
		this.score = score;
	}
	
	public int getScore(){
		return this.score;
	}
	
	public String getScoreToString(){
		return Value.hands.values()[((score & 0xf00000) >> 0x14) - 6].name();
	}
	
	public void winsChips(double chips){
		this.chips += chips;
	}
	
	public double getTotalStake(){
		return this.totalStake;
	}
	
	public void nullStakes(){
		totalStake = 0;
		roundStake = 0;
	}
	
	public void takeFromTotalStake(double chips){
		this.totalStake -= chips;
	}
	
	public double getRoundStake(){
		return roundStake;
	}
	
	public void newInnerRound(){
		roundStake = 0;
	}
	
	public double payBlinds(int blind){
		if (chips > blind){
			update(blind);
			return blind;
		} else {
			setState(Value.state.allIn);
			update(chips);
			return totalStake;
		}
	}
	
	private boolean stepsAreOK(){
		if (steps != 0){
			System.out.println("only one action is allowed");
			return false;
		}
		return true;
	}
	
	private boolean chipsAreOK(double chips){
		if (chips > this.chips){
			allIn();
			System.out.println(ID + ": you dont have enough chips to do that act, you went all in");
			return false;
		}
		return true;
	}

	public void end(){
		c1 = Talon.getEmptyCard();
		c2 = Talon.getEmptyCard();
		setState(Value.state.left);
		engine.plLeft(ID);
	}
	
	private void update(double chips){
		this.chips -= chips;
		totalStake += chips;
		roundStake += chips;
	}
	
	private boolean passed(double chips){
		return stepsAreOK() && chipsAreOK(chips);
	}
	
	//****** player's actions *****//
	
	public void check(){
		if (!passed(chips))
			return;
		steps += 1;
		setState(Value.state.checked);
		engine.check(ID);
	}

	public void call(double chips){
		if (!passed(chips))
			return;
		steps += 1;
		update(chips);
		setState(Value.state.called);
		engine.call(ID, chips);
	}
	
	public void raise(double chips){
		if (!passed(chips))
			return;
		steps += 1;
		update(chips);
		setState(Value.state.raised);
		engine.raise(ID, chips);
	}
	
	public void fold(){
		if (!passed(chips))
			return;
		steps += 1;
		update(0);
		setState(Value.state.folded);
		engine.fold(ID);
	}
	
	public void allIn(){
//		try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
		if (!passed(chips))
			return;
		steps += 1;
		double temp = chips;
		update(chips);
		setState(Value.state.allIn);
		engine.allIn(ID, temp);
	}
}
