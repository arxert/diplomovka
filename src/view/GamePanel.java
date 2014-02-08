package view;

import game.Card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bots.Person;

import utils.ListOfPlayers;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel pnlNorth = new JPanel();
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlCenter = new JPanel();
	
	private int frameWidth = 700, frameHeight = 450, gameWidth = frameWidth;
	
	private Color clBackground = Color.LIGHT_GRAY;

	private ViewWindow[] players = new ViewWindow[9];
	
	private JLabel[] cards = new JLabel[5];
	
	public GamePanel(ListOfPlayers pls){
		initPanel();
		addComponents(pls);
	}
	
	public void setFlop(Card c1, Card c2, Card c3){
		cards[0].setIcon(c1.getIcon());
		cards[1].setIcon(c2.getIcon());
		cards[2].setIcon(c3.getIcon());
	}
	
	public void setTurn(Card c){
		cards[3].setIcon(c.getIcon());
	}
	
	public void setRiver(Card c){
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
		pnlCenter.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 30));
		pnlCenter.setBackground(Color.WHITE);
		for (int i = 0; i < cards.length; i++){
			cards[i] = new JLabel();
			cards[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
			pnlCenter.add(cards[i]);
		}
	}
	
	private void addComponents(ListOfPlayers pls){
		int i = 0;
		for (i = 0; i < 4; i++){
			if (pls.getPlayer(i) != null){
				if (pls.getPlayer(i).getName().equals(Person.getStaticName()))
					this.players[i] = new PlayerWindow(pls.getPlayer(i));
				else
					this.players[i] = new BotWindow(pls.getPlayer(i));
				pnlNorth.add(this.players[i]);
			}
		}
		for (; i < 9; i++){
			if (pls.getPlayer(i) != null){
				if (pls.getPlayer(i).getName().equals(Person.getStaticName()))
					this.players[i] = new PlayerWindow(pls.getPlayer(i));
				else
					this.players[i] = new BotWindow(pls.getPlayer(i));
				pnlSouth.add(this.players[i]);
			}
		}
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter, BorderLayout.CENTER);
		add(pnlSouth, BorderLayout.SOUTH);
	}
}
