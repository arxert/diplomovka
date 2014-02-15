package computes;

import java.util.ArrayList;
import java.util.Collections;

import game.Bot;

import utils.ListOfPlayers;
import utils.Value;
import utils.Value.state;

public class Bank {

	private ListOfPlayers bots;
//	private ArrayList<SubBank> subBanks = new ArrayList<>();
	private double chips = 0;
	private double maxScore = 0;
	
	public Bank(ListOfPlayers bots){
		this.bots = bots;
//		subBanks.add(new SubBank(0));
	}
	
	public void addChips(double chips){
		this.chips += chips;
	}
	
	public double getChips(){
		return chips;
	}
	
	public void splitAll(){
		System.out.println("splitting");
		double split = 0;
		double temp = 0;
		double min = 0;
		ArrayList<Bot> winners = new ArrayList<>();
		while (chips > 0){
			maxScore = 0;
			split = 0;
			temp = 0;
			winners.clear();
			for (Bot bot: bots.getActivePlayers()){
				if (bot.getScore() > maxScore){
					maxScore = bot.getScore();
					winners.clear();
					winners.add(bot);
				} else
					if (bot.getScore() == maxScore){
						winners.add(bot);
					}
			}
			Collections.sort(winners, Value.stakeComparator);
			System.out.println("winners: ");
			for (Bot b: winners){
				System.out.println(b.getID());
			}
			for (Bot b: winners){
				min = b.getTotalStake();
				for (Bot others: bots.getAllPlayers()){
					temp = Math.min(min, others.getTotalStake());
					others.takeFromTotalStake(temp);
					split += temp;
//					System.out.println("others: " + others.getID() + ", temp = " + temp);
				}
				for (Bot win: winners){
					if (win.getState() != state.folded)
						win.winsChips(split / winners.size());
				}
				System.out.println("chips = " + chips + ", split = " + split);
				chips -= split;
				b.setState(state.folded);
			}
		}
	}
	
	
//	public void fold(int ID){
//		for (SubBank b: subBanks)
//			b.removePl(bots.getPlayer(ID));
//	}
//	
//	public void raise(int ID, double chips){
//		subBanks.get(subBanks.size()).addChips(chips);
//	}
//	
//	public void call(int ID, double chips){
//		subBanks.get(subBanks.size()).addChips(chips);
//	}
//	
//	public void allIn(int ID, double chips){
//		
//	}
//	
//	public void split(){
//		for (SubBank b: subBanks)
//			b.split();
//	}
}
