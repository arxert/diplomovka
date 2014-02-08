package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import utils.ListOfPlayers;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 700, frameHeight = 450, tabWidth = frameWidth - 100, tabHeight = frameHeight - 100;
	
	private String title = "Game";
	
	private JTabbedPane tbPane = new JTabbedPane();
	
	private GamePanel gamePanel;
	private ConsoleWindow consolPanel;

	public MainFrame(ListOfPlayers players){
		initFrame();
		initPanels();
		initComponents(players);
		addComponents();
	}
	
	
	private void initPanels(){
//		pnlMain.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
//		pnlSouth.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));
	}
	
	private void initComponents(ListOfPlayers pls){
		gamePanel = new GamePanel(pls);
		consolPanel = new ConsoleWindow(pls);
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
		consolPanel.addLog("Player " + ID + " folded");
	}
	
	public void check(int ID){
		
	}
	
}
