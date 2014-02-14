package view;

import game.Card;
import game.GameEngine;
import game.Talon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bots.Person;

import utils.*;
import utils.Value.state;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 700, frameHeight = 450, gameWidth = frameWidth;
	
	private Color clBackground = Color.LIGHT_GRAY;

	private ViewWindow[] players = new ViewWindow[9];
	
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	
	private JLabel[] cards = new JLabel[5];
	
	private Card emptyCard = Talon.getEmptyCard();
	
	private boolean canRun = true;
	
	public GamePanel(GameEngine gE, ListOfPlayers pls){
		initPanel();
		addComponents(gE, pls);
	}
	
	public void canRun(boolean yes){
		canRun = yes;
	}
	
	public void setState(int ID, Value.state state){
		if (canRun)
			getPlayer(ID).setState(state);
	}
	
	public void newRound(){
		if (canRun)
			resetCards();
	}
	
	private void resetCards(){
		for (JLabel c: cards)
			c.setIcon(emptyCard.getIcon());
	}
	
	public void setFlop(Card c1, Card c2, Card c3){
		if (canRun) {
			cards[0].setIcon(c1.getIcon());
			cards[1].setIcon(c2.getIcon());
			cards[2].setIcon(c3.getIcon());
		}
	}
	
	public void setTurn(Card c){
		if (canRun)
			cards[3].setIcon(c.getIcon());
	}
	
	public void setRiver(Card c){
		if (canRun)
			cards[4].setIcon(c.getIcon());
	}
	
	private void initPanel(){
		setPreferredSize(new Dimension(gameWidth, frameHeight));
		setBackground(clBackground);
		setLayout(new BorderLayout(10, 0));
		pnlNorth.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		pnlNorth.setBackground(clBackground);
		pnlSouth.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
		pnlSouth.setBackground(clBackground);
		pnlCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
		pnlCenter.setBackground(Color.WHITE);
		for (int i = 0; i < cards.length; i++){
			cards[i] = new JLabel();
			cards[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
			pnlCenter.add(cards[i]);
		}
		resetCards();
	}
	
	private void addComponents(GameEngine gE, ListOfPlayers pls){
		int i = 0;
		for (i = 0; i < 4; i++){
			if (pls.getPlayer(i) != null){
				if (pls.getPlayer(i).getName().equals(Person.getStaticName()))
					this.players[i] = new PlayerWindow(gE, pls.getPlayer(i));
				else
					this.players[i] = new ViewWindow(gE, pls.getPlayer(i));
				pnlNorth.add(this.players[i]);
			}
		}
		for (; i < 9; i++){
			if (pls.getPlayer(i) != null){
				if (pls.getPlayer(i).getName().equals(Person.getStaticName()))
					this.players[i] = new PlayerWindow(gE, pls.getPlayer(i));
				else
					this.players[i] = new ViewWindow(gE, pls.getPlayer(i));
				pnlSouth.add(this.players[i]);
			}
		}
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
	}
	
	private ViewWindow getPlayer(int ID){
		for (ViewWindow v: players){
			if (v != null)
				if (v.getID() == ID)
					return v;
		}
		return null;
	}
	
	public void dealedCards(int ID, Card c1, Card c2){
		if (canRun)
			getPlayer(ID).updateCards(c1, c2);
	}
	
	public void check(int ID){
		if (canRun)
			setStatus(ID, state.checked);
	}
	
	public void fold(int ID){
		if (canRun)
			setStatus(ID, state.folded);
	}
	
	public void raise(int ID, double chips){
		if (canRun)
			setStatus(ID, state.raised);
	}
	
	public void call(int ID, double chips){
		if (canRun)
			setStatus(ID, state.called);
	}
	
	private void setStatus(int ID, state state){
		getPlayer(ID).setState(state);
	}
}
