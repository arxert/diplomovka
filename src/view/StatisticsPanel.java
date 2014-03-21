package view;

import utils.TXTPanel;
import game.Card;

public class StatisticsPanel extends TXTPanel {

	private static final long serialVersionUID = 1L;
	
	public StatisticsPanel(){
		super();
	}
	
	public void statsCards(Card c1, Card c2){
//		addLog("" + c1 + " " + c2);
		addLog("player " + 5 + " was dealed cards " + c1 + ", " + c2);
		System.out.println(c1);
	}
}
