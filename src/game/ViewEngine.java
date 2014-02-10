package game;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import utils.ListOfPlayers;
import view.ConsolePanel;
import view.GamePanel;

public class ViewEngine extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 700, frameHeight = 450, tabWidth = frameWidth - 100, tabHeight = frameHeight - 100;
	
	private String title = "Game";
	
	private JTabbedPane tbPane = new JTabbedPane();
	
	private GamePanel gamePanel;
	private ConsolePanel consolPanel;

	public ViewEngine(GameEngine gE, ListOfPlayers players){
		initFrame();
		initComponents(gE, players);
		addComponents();
	}
	
	private void initComponents(GameEngine gE, ListOfPlayers pls){
		gamePanel = new GamePanel(gE, pls);
		consolPanel = new ConsolePanel(pls);
		tbPane.setPreferredSize(new Dimension(tabWidth, tabHeight));
	}
	
	private void addComponents(){
		tbPane.addTab("Console", consolPanel);
		tbPane.addTab("Game", gamePanel);
		add(tbPane, BorderLayout.CENTER);

		pack();
	}
	
	private void initFrame(){
		setTitle(title);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension(frameWidth, frameHeight));
		setLocation(50, screenSize.height/2 - frameHeight/2);
		setLocation(screenSize.width/2 - frameWidth/2, screenSize.height/2 - frameHeight/2);
		setLayout(new BorderLayout());
		setVisible(true);
		setResizable(false);
	}

	//****** player's actions *****//
	
	public void fold(int ID){
		consolPanel.fold(ID);
		gamePanel.fold(ID);
//		consolPanel.addLog("Player " + ID + " folded");
	}
	
	public void check(int ID){
		consolPanel.check(ID);
		gamePanel.check(ID);
	}
	
	public void raise(int ID, double chips){
		consolPanel.raise(ID, chips);
		gamePanel.raise(ID, chips);
	}
	
	public void call(int ID, double chips){
		consolPanel.call(ID, chips);
		gamePanel.call(ID, chips);
	}

	public void dealCard(int ID, Card c1, Card c2){
		consolPanel.dealedCard(ID, c1, c2);
		gamePanel.dealedCards(ID, c1, c2);
	}
	
}
