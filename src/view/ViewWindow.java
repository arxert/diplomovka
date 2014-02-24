package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import game.Card;
import game.GameEngine;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import bots.Bot;

import utils.Value;
import utils.Value.state;

public class ViewWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int ID = -1;
	
	private Bot bot;
	
	private Color clBackground = Color.YELLOW;

	private JPanel pnlLeft = new JPanel();

	private JLabel lblState = new JLabel();
	private JLabel c1 = new JLabel();
	private JLabel c2 = new JLabel();
	private JLabel lblName = new JLabel();
	private JLabel lblChips = new JLabel();
	
	private Dimension dimPnl = new Dimension(95, 165);
	
	public ViewWindow(GameEngine gE, Bot bot){
		this.bot = bot;
		ID = bot.getID();
		init();
		setState(bot.getState());
		initLeft();
		addComponents();
	}
	
	private void initLeft(){
		lblName.setText(bot.toString() + " " + bot.getID());
		c1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		c2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		setLblChips(bot.getChips());
	}
	
	private void setLblStatus(String s){
//		lblStatus.setText("<html><body>Status:<br>" + s + "</body></html>");
		lblState.setText(s);
	}
	
	private void init(){
		setLayout(new BorderLayout());
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.PAGE_AXIS));
		pnlLeft.setBackground(clBackground);
		setBackground(clBackground);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setPreferredSize(dimPnl);
	}
	
	public void isOnMove(boolean yes){
		if (yes){
			setBorder(BorderFactory.createLineBorder(Color.RED, 3));
		}
		else {
			setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		}
	}
	
	public void addPanelRight(JPanel right){
		right.setBackground(clBackground);
		add(right, BorderLayout.EAST);
		dimPnl.setSize(130, dimPnl.height);
		setPreferredSize(dimPnl);
	}

	public void addPanelBot(JSlider sldRaise){
		sldRaise.setBackground(clBackground);
		add(sldRaise, BorderLayout.SOUTH);
	}
	
	private void addComponents(){
		add(lblName, BorderLayout.NORTH);
		pnlLeft.add(c1);
		pnlLeft.add(c2);
		pnlLeft.add(lblState);
		pnlLeft.add(lblChips);
		add(pnlLeft, BorderLayout.WEST);
	}
	
	public void updateCards(Card c1, Card c2){
		this.c1.setIcon(c1.getIcon());
		this.c2.setIcon(c2.getIcon());
	}

	private void setState(Value.state state){
		isOnMove(false);
		if (state.equals(Value.state.folded)){
			setOpaque(false);
			pnlLeft.setOpaque(false);
		}
		else {
			setOpaque(true);
			pnlLeft.setOpaque(true);
		}
	}
	
	private void setLblChips(double chips){
		lblChips.setText(String.valueOf(chips));
	}
	
	public int getID(){
		return ID;
	}

	public void updateView(){
		setLblChips(bot.getChips());
		setLblStatus(bot.getState().name());
	}
	
	//****** player's actions *****//
	
	public void raise(double chips){
		setLblChips(bot.getChips());
		setLblStatus(state.raised.name() + " " + chips);
		setState(state.raised);
		
	}
	
	public void call(double chips){
		setLblChips(bot.getChips());
		setLblStatus(state.called.name() + " " + chips);
		setState(state.called);
	}
	
	public void check(){
		setState(state.checked);
		setLblStatus(state.checked.name());
	}
	
	public void fold(){
		setState(state.folded);
		setLblStatus(state.folded.name());
	}
	
	public void allIn(){
		setState(state.allIn);
		setLblStatus(state.allIn.name());
	}
	
	public void noneState(){
		setState(state.none);
		setLblStatus(state.none.name());
	}
}
