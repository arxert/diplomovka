package computes;


import java.util.ArrayList;

import bots.Bot;

public class SubBank {

	private double chips;
	private ArrayList<Bot> bots = new ArrayList<>();
	
	public SubBank(double chips){
		this.chips = chips;
	}
	
	public void addChips(double chips){
		this.chips += chips;
	}
	
	public void addPl(Bot b){
		bots.add(b);
	}
	
	public void removePl(Bot b){
		bots.remove(b);
	}

	public void split(){
		int max = 0;
		int count = 0;
		for (Bot b: bots){
			if (b.getScore() == max){
				count += 1;
			}
			if (b.getScore() > max){
				max = b.getScore();
				count = 1;
			}
		}
		for (Bot b: bots){
			if (b.getScore() == max){
				b.winsChips(chips / count);
			}
		}
	}
	
}
