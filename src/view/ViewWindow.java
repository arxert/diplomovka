package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import game.Bot;
import game.Card;
import game.GameEngine;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import utils.Value;

public class ViewWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int ID = -1;
	
	private GameEngine gameEngine;

	private Color clBackground = Color.YELLOW;

	private JPanel pnlLeft = new JPanel();

	private JLabel lblStatus = new JLabel();
	private JLabel c1 = new JLabel();
	private JLabel c2 = new JLabel();
	private JLabel lblName = new JLabel();
	private JLabel lblChips = new JLabel();

	private Dimension dimPnl = new Dimension(55, 160);
		
	public ViewWindow(GameEngine gE, Bot pl){
		this.ID = pl.getID();
		this.gameEngine = gE;
		init();
		setLblChips(500000);
		initLeft(pl);
		addComponents();
//		pl.registerView(this);
	}
	
	private void initLeft(Bot pl){
		lblName.setText(pl.toString());
		c1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		c2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		lblChips.setText(String.valueOf(pl.getChips()));
		setLblStatus(" ");
	}
	
	private void setLblStatus(String s){
//		lblStatus.setText("<html><body>Status:<br>" + s + "</body></html>");
		lblStatus.setText(s);
	}
	
	private void init(){
		setLayout(new BorderLayout());
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.PAGE_AXIS));
		pnlLeft.setBackground(clBackground);
		setBackground(clBackground);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
		setPreferredSize(dimPnl);
	}

	public void addPanelRight(JPanel right){
		right.setBackground(clBackground);
		add(right, BorderLayout.EAST);
		setPreferredSize(new Dimension(130, dimPnl.height));
	}

	public void addPanelBot(JSlider sldRaise){
		sldRaise.setBackground(clBackground);
		add(sldRaise, BorderLayout.SOUTH);
	}
	
	private void addComponents(){
		add(lblName, BorderLayout.NORTH);
		pnlLeft.add(c1);
		pnlLeft.add(c2);
		pnlLeft.add(lblStatus);
		pnlLeft.add(lblChips);
		add(pnlLeft, BorderLayout.WEST);
	}

	public void updateCards(Card c1, Card c2){
		this.c1.setIcon(c1.getIcon());
		this.c2.setIcon(c2.getIcon());
	}

	public void setStatus(Value.state state){
		setLblStatus(state.name());
	}
	
	private void setLblChips(double chips){
		lblChips.setText(String.valueOf(chips));
	}
	
	public void check(){
		gameEngine.check(ID);
	}
	
	public void fold(){
		gameEngine.fold(ID);
	}
	
	public void raise(double chips){
		gameEngine.raise(ID, chips);
	}
	
	public void call(double chips){
		gameEngine.call(ID, chips);
	}
	
//	public void check(int ID){
//		
//	}
//	
//	public void fold(int ID){
//		
//	}
//	
	public int getID(){
		return ID;
	}
}
