package view;

import game.Bot;
import game.Card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BotWindow extends ViewWindow {

	private static final long serialVersionUID = 1L;
	
	private Bot player;
	
	private JPanel pnlLeft = new JPanel();
	
	private Dimension dimPnl = new Dimension(70, 160);

	private Color clBackground = Color.YELLOW;
	
	private JLabel lblStatus = new JLabel();
	private JLabel c1 = new JLabel();
	private JLabel c2 = new JLabel();
	private JLabel lblName = new JLabel();
	private JLabel lblChips = new JLabel();
	
	public BotWindow(Bot pl){
		super(pl);
		player = pl;
		initPanels();
		initComps();
		addComponents();
	}
	
	public void updateCards(Card c1, Card c2){
		this.c1.setIcon(c1.getIcon());
		this.c2.setIcon(c2.getIcon());
	}
	
	private void initComps(){
		lblName.setText(player.toString());
		c1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		c2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		lblChips.setText(player.getChips() + "$");
		setLblStatus("");
		
	}
	
	private void setLblStatus(String s){
		lblStatus.setText("<html><body>Status:<br>" + s + "</body></html>");
	}
	
	private void initPanels(){
		setLayout(new BorderLayout());
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.PAGE_AXIS));
		pnlLeft.setBackground(clBackground);
		setPreferredSize(dimPnl);
		setBackground(clBackground);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}

	private void addComponents(){
		add(lblName, BorderLayout.NORTH);
		pnlLeft.add(c1);
		pnlLeft.add(c2);
		pnlLeft.add(lblChips);
		pnlLeft.add(lblStatus);
		add(pnlLeft, BorderLayout.WEST);
	}
}
