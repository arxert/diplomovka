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
import javax.swing.JTextField;

import bots.Person;

import utils.*;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 700, frameHeight = 450, gameWidth = frameWidth;
	
	private Color clBackground = Color.LIGHT_GRAY;

	private ViewWindow[] players = new ViewWindow[9];
	
	private JPanel pnlMain = new JPanel(new BorderLayout());
	private JPanel pnlGame = new JPanel(new BorderLayout());
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	
	private JLabel[] cards = new JLabel[5];
	
	private Card emptyCard = Talon.getEmptyCard();
	
	private JTextField txtLog = new JTextField();
	
	private boolean canRun = true;
	
	public GamePanel(GameEngine gE, ListOfPlayers pls){
		initPanel();
		addComponents(gE, pls);
	}
	
	public void isOnMove(int ID){
		if (canRun)
			getPlayer(ID).isOnMove(true);
	}
	
	public void canRun(boolean yes){
		canRun = yes;
	}
	
	public void newRound(boolean cards){
		if (canRun) {
			if (cards)
				resetCards();
			for (ViewWindow v: players)
				if (v != null)
					v.updateView();
		}
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
		txtLog.setEditable(false);
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
		pnlGame.add(pnlNorth, BorderLayout.NORTH);
		pnlGame.add(pnlCenter, BorderLayout.CENTER);
		pnlGame.add(pnlSouth, BorderLayout.SOUTH);
		pnlMain.add(pnlGame, BorderLayout.CENTER);
		pnlMain.add(txtLog, BorderLayout.SOUTH);
		add(pnlMain, BorderLayout.CENTER);
	}
	
	private ViewWindow getPlayer(int ID){
		for (ViewWindow v: players){
			if (v != null)
				if (v.getID() == ID)
					return v;
		}
		return null;
	}
	
	public void dealedCards(int ID){
		if (canRun)
			getPlayer(ID).updateCards();
	}
	
	public void check(int ID){
		if (canRun){
//			setStatus(ID, state.checked);
			getPlayer(ID).check();
		}
	}
	
	public void fold(int ID){
		if (canRun){
//			setStatus(ID, state.folded);
			getPlayer(ID).fold();
		}
	}
	
	public void raise(int ID, double chips){
		if (canRun){
//			setStatus(ID, state.raised);
			getPlayer(ID).raise(chips);
		}
	}
	
	public void call(int ID, double chips){
		if (canRun){
//			setStatus(ID, state.called);
			getPlayer(ID).call(chips);
		}
	}
	
	public void allIn(int ID, double chips){
		if (canRun){
//			setStatus(ID, state.allIn);
			getPlayer(ID).allIn();
		}
	}
	
	public void blind(int ID, double chips, boolean small){
		if (canRun)
			getPlayer(ID).blind(chips, small);
	}
	
	public void noneState(int ID){
		getPlayer(ID).noneState();
	}
	
	public void setTxtLog(String s){
		txtLog.setText("Bank: " + s);
	}
	
	public void plLeft(int ID){
		getPlayer(ID).left();
	}
	
//	private void setStatus(int ID, state state){
//		getPlayer(ID).setState(state);
//	}
}
