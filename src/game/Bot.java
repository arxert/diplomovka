package game;

import utils.Value;
import view.ViewWindow;

public abstract class Bot {
	
	private int ID;
	private String name;
	private GameEngine engine;
	private Card c1, c2;
//	private ViewWindow window;
	private double chips;
	private Value.state state;
	private int score;

	public abstract void act();
	
	public Bot(int id){
		this.ID = id;
	}
	
	public String getName(){
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setEngine(GameEngine engine){
		this.engine = engine;
	}
	
	public void dealCard(Card c){
		if (c1 == null)
			c1 = c;
		else 
			if (c2 == null)
				c2 = c;
	}
	
	public void dealCards(Card c1, Card c2){
		this.c1 = c1;
		this.c2 = c2;
//		window.updateCards(c1, c2);
	}
	
	public void registerView(ViewWindow window) {
//		this.window = window;
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

	public void setChips(double chips){
		this.chips = chips;
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

	//****** player's actions *****//
	
	public void check(){
		setState(Value.state.checked);
		engine.check(ID);
	}

	public void call(double chips){
		setState(Value.state.called);
		engine.call(ID, chips);
	}
	
	public void raise(double chips){
		setState(Value.state.raised);
		engine.raise(ID, chips);
	}
	
	public void fold(){
		setState(Value.state.folded);
		engine.fold(ID);
	}
}
