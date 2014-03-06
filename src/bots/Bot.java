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
			
		}
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

//	public void setChips(double chips){
//		this.chips = chips;
//	}
	
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
			totalStake += blind;
			roundStake += blind;
			chips -= blind;
			return blind;
		} else {
			setState(Value.state.allIn);
			totalStake = chips;
			roundStake = chips;
			chips = 0;
			return totalStake;
		}
	}
	
	private boolean checkSteps(){
		if (steps != 0){
			System.out.println("only one action is allowed");
			return false;
		}
		return true;
	}
	
	private boolean checkChips(double chips){
		if (chips > this.chips){
//			System.out.println("you dont have enough chips to do that act..");
//			steps = 0;
//			allIn();
//			return false;
		}
		return true;
	}

	public void end(){
		c1 = Talon.getEmptyCard();
		c2 = Talon.getEmptyCard();
		setState(Value.state.left);
		engine.plLeft(ID);
	}
	
	//****** player's actions *****//
	
	public void check(){
		if (!checkSteps())
			return;
		steps += 1;
		setState(Value.state.checked);
		engine.check(ID);
	}

	public void call(double chips){
		if (!checkSteps())
			return;
		if (!checkChips(chips))
			return;
		steps += 1;
		this.chips -= chips; 
		totalStake += chips;
		roundStake += chips;
		setState(Value.state.called);
		engine.call(ID, chips);
	}
	
	public void raise(double chips){
		if (!checkSteps())
			return;
		if (!checkChips(chips))
			return;
		steps += 1;
		this.chips -= chips; 
		totalStake += chips;
		roundStake += chips;
		setState(Value.state.raised);
		engine.raise(ID, chips);
	}
	
	public void fold(){
		if (!checkSteps())
			return;
		steps += 1;
		roundStake = 0;
		setState(Value.state.folded);
		engine.fold(ID);
	}
	
	public void allIn(){
		if (!checkSteps())
			return;
		steps += 1;
		totalStake += chips;
		roundStake += chips;
		chips = 0;
		setState(Value.state.allIn);
		engine.allIn(ID, chips);
	}
}
