package game;

import view.ViewWindow;

public abstract class Bot {
	
	private int ID;
	private String name;
	private GameEngine engine;
	private Card c1, c2;
//	private ViewWindow window;
	private double chips;

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
	
	public void dealCard(Card c1, Card c2){
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

	//****** player's actions *****//
	
	public void check(){
		engine.check(ID);
	}

	public void call(double chips){
		engine.call(ID, chips);
	}
	
	public void raise(double chips){
		engine.raise(ID, chips);
	}
	
	public void fold(){
		engine.fold(ID);
	}
}
