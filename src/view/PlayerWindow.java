package view;

import game.Bot;
import game.Card;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bots.Person;

public class PlayerWindow extends ViewWindow {

	private static final long serialVersionUID = 1L;
	
	private Bot player;
	
	private boolean enabled = false;
	
	private JPanel pnlLeft = new JPanel();
	private JPanel pnlRight = new JPanel();
	
	private Dimension dimPnl = new Dimension(130, 160);
	private Dimension dimComp = new Dimension(70, 25);

	private Color clBackground = Color.YELLOW;
	
	private JLabel c1 = new JLabel();
	private JLabel c2 = new JLabel();
	private JLabel lblName = new JLabel();
	private JLabel lblChips = new JLabel();
	private JButton btnCheck = new JButton("<html><body>Check</body></html>");
	private JButton btnFold = new JButton("<html><body>Fold</body></html>");
	private JButton btnCall = new JButton("<html><body>Call<br>(0$)</body></html>");
	private JButton btnRaise = new JButton("<html><body>Raise<br>(0$)</body></html>");
	private JSlider sldRaise;
	
	public PlayerWindow(Bot pl){
		super(pl);
		player = pl;
		if (player.getName() == Person.getStaticName())
			enabled = true;
		init();
		initComps();
		addComponents(enabled);
	}
	
	public void updateCards(Card c1, Card c2){
		this.c1.setIcon(c1.getIcon());
		this.c2.setIcon(c2.getIcon());
	}
	
	private void initComps(){
		c1.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		c2.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		lblChips.setText(player.getChips() + "$");
		sldRaise = new JSlider(JSlider.HORIZONTAL, 0, 3000, 0);
		sldRaise.setPreferredSize(dimComp);
		sldRaise.setBackground(clBackground);
		sldRaise.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent arg0) {
				setBtnRaiseChips();
			}
		});
		btnCall.setPreferredSize(dimComp);
		btnFold.setPreferredSize(dimComp);
		btnRaise.setPreferredSize(dimComp);
		btnCheck.setPreferredSize(dimComp);
		
		btnCheck.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player.check();
			}
		});
		
		btnFold.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player.fold();
			}
		});
	}
	
	private void setBtnRaiseChips(){
		btnRaise.setText("<html><body>Raise<br>(" + sldRaise.getValue() + "$)</body></html>");
	}
	
//	private void setBtnCallChips(double chips){
//		btnCall.setText("<html><body>Call<br>(" + chips + "$)</body></html>");
//	}
	
	private void init(){
		lblName.setText(player.toString());
		setLayout(new BorderLayout());
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.PAGE_AXIS));
		pnlRight.setBackground(clBackground);
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.PAGE_AXIS));
		pnlLeft.setBackground(clBackground);
		setPreferredSize(dimPnl);
		setBackground(clBackground);
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
	}

	private void addComponents(boolean enabled){

//		btnCall.setForeground(Color.BLUE);
//		btnCheck.setForeground(Color.BLUE);
//		btnFold.setForeground(Color.BLUE);
//		btnRaise.setForeground(Color.BLUE);
		if (!enabled){
			btnCall.setBackground(Color.BLACK);
			btnCheck.setBackground(Color.BLACK);
			btnFold.setBackground(Color.BLACK);
			btnRaise.setBackground(Color.BLACK);
		}
		
		btnCheck.setEnabled(enabled);
		btnFold.setEnabled(enabled);
		btnRaise.setEnabled(enabled);
		btnCall.setEnabled(enabled);
		sldRaise.setEnabled(enabled);
		
		add(lblName, BorderLayout.NORTH);
		pnlLeft.add(c1);
		pnlLeft.add(c2);
		pnlLeft.add(lblChips);
		pnlRight.add(btnCheck);
		pnlRight.add(btnFold);
		pnlRight.add(btnCall);
		pnlRight.add(btnRaise);
		add(sldRaise, BorderLayout.SOUTH);
		add(pnlLeft, BorderLayout.WEST);
		add(pnlRight, BorderLayout.EAST);
	}
}
