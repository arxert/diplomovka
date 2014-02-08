package view;

import game.Bot;
import game.Card;

import javax.swing.JPanel;

public abstract class ViewWindow extends JPanel {

	private static final long serialVersionUID = 1L;

	public abstract void updateCards(Card c1, Card c2);
		
	public ViewWindow(Bot pl){
		pl.registerView(this);
	}
}
