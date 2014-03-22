package computes;

import java.util.ArrayList;
import java.util.Collections;

import bots.Bot;

import utils.ListOfPlayers;
import utils.Value;
import utils.Value.state;

public class Bank {

	private ListOfPlayers bots;
	private double chips = 0;
	private double maxScore = 0;
	
	public Bank(ListOfPlayers bots){
		this.bots = bots;
	}
	
	public void addChips(double chips){
		this.chips += chips;
	}
	
	public double getChips(){
		return chips;
	}
	
	public String splitAll(){
		String result = "Bank = " + chips + ", winners: ";
		double min, temp;
		double split = 0;
		int score, count = 0;
		double res = 0;
		ArrayList<Bot> winners = new ArrayList<>(bots.getActivePlayers());
		Collections.sort(winners, Value.handComparator);
		for (Bot b: winners){
			if (chips == 0)
				return result;
			count = 0;
			min = b.getTotalStake();
			score = b.getScore();
			for (Bot others: bots.getAllPlayers()){
				if (others.getScore() == score){
					count += 1;
				}
				temp = Math.min(min, others.getTotalStake());
				others.takeFromTotalStake(temp);
				split += temp;
			}
			if (count > 1)
				try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			if (count == 0){
				System.out.println("something is wrong with bank split...");
				System.out.println(chips);
			} else {
				res = split/count;
				chips -= res;
				b.winsChips(res);
				split -= (res);
				result += ", " + b.getName() + b.getID() + " - " + res + " (" + b.getScoreToString() + ")";
				b.setScore(0);
			}
		}
		return result;
	}
	
	// TODO change completely
	public String splitAlll(){
		double split = 0;
		double temp = 0;
		double min = 0;
		int count = 0;
		String result = "Bank = " + chips + ", winners: ";
		ArrayList<Bot> winners = new ArrayList<>();
		while (chips > 0){
//			System.out.println(chips);
			maxScore = 0;
			split = 0;
			temp = 0;
			winners.clear();
			for (Bot bot: bots.getActivePlayers()){
//				System.out.println(bot.getID() + ", " + bot.getTotalStake());
				if (bot.getScore() > maxScore){
					maxScore = bot.getScore();
					winners.clear();
					winners.add(bot);
				} else
					if (bot.getScore() == maxScore){
						winners.add(bot);
					}
			}
//			if (winners.size() > 1){
//				try {Thread.sleep(2000);} catch (Exception e) {	e.printStackTrace();}
//			}
//			if (Value.hands.values()[((winners.get(0).getScore() & 0xf00000) >> 0x14) - 6] == Value.hands.fullHouse){
//				try {Thread.sleep(2000);} catch (Exception e) {	e.printStackTrace();}
//			}
			Collections.sort(winners, Value.stakeComparator);
			count = winners.size();
			for (Bot b: winners){
				min = b.getTotalStake();
				for (Bot others: bots.getAllPlayers()){
					temp = Math.min(min, others.getTotalStake());
					others.takeFromTotalStake(temp);
					split += temp;
//					System.out.println("others: " + others.getID() + ", temp = " + temp);
				}
				for (Bot win: winners){
					if (win.getState() != state.none){
						win.winsChips(split / count);
						result += ", " + win.getName() + win.getID() + " - " + split / count + " (" + Value.hands.values()[((win.getScore() & 0xf00000) >> 0x14) - 6] + ")";
					}
				}
//				System.out.println("chips = " + chips + ", split = " + split);
				count -= 1;
				chips -= split;
				split = 0;
				b.setState(state.none);
			}
//			checkSumPls();
		}
		return result;
	}
	

	private void checkSumPls(){
		double res = 0;
		for (Bot b: bots.getAllPlayers()){
			res += b.getChips();
		}
		System.out.println(res);
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
