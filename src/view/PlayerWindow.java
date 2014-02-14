package view;

import game.Bot;
import game.GameEngine;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bots.Person;

public class PlayerWindow extends ViewWindow {

	private static final long serialVersionUID = 1L;
	
	private JPanel pnlRight = new JPanel();
	
	private Dimension dimComp = new Dimension(70, 25);

	private JButton btnCheck = new JButton("<html><body>Check</body></html>");
	private JButton btnFold = new JButton("<html><body>Fold</body></html>");
	private JButton btnCall = new JButton("<html><body>Call<br>(0)</body></html>");
	private JButton btnRaise = new JButton("<html><body>Raise<br>(0)</body></html>");
	
	private JSlider sldRaise;
	
	private Person person;
	
	public PlayerWindow(GameEngine gE, Bot pl){
		super(gE, pl);
		person = (Person) pl;
		initComps();
		setRightPanel();
		addPanelRight(pnlRight);
		addPanelBot(sldRaise);
	}
	
	private void initComps(){
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.PAGE_AXIS));
		
		sldRaise = new JSlider(JSlider.HORIZONTAL, 0, 3000, 0);
		sldRaise.setPreferredSize(dimComp);
		sldRaise.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				setBtnRaiseChips();
			}
		});
		
		btnCall.setPreferredSize(dimComp);
		btnFold.setPreferredSize(dimComp);
		btnRaise.setPreferredSize(dimComp);
		btnCheck.setPreferredSize(dimComp);
		
		btnCheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				person.check();
				person.stop();
			}
		});
		
		btnFold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				person.fold();
				person.stop();
			}
		});
		
		btnRaise.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				person.raise(sldRaise.getValue());
				person.stop();
			}
		});
		
		btnCall.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent actionevent) {
				person.call(0);
				person.stop();
			}
		});
	}

	private void setBtnRaiseChips(){
		btnRaise.setText("<html><body>Raise<br>(" + sldRaise.getValue() + ")</body></html>");
	}
	
//	private void setBtnCallChips(double chips){
//		btnCall.setText("<html><body>Call<br>(" + chips + "$)</body></html>");
//	}
	
	private void setRightPanel(){
		pnlRight.add(btnCheck);
		pnlRight.add(btnFold);
		pnlRight.add(btnCall);
		pnlRight.add(btnRaise);		
	}
}
