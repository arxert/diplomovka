package stats;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class Statistics extends JPanel {

	private static final long serialVersionUID = 1L;

	public void loadFile(){
		try {
			JFileChooser chooser=new  JFileChooser();
	        int returnVal = chooser.showOpenDialog(null);
	        if(returnVal == JFileChooser.APPROVE_OPTION) {
	        	File f = chooser.getSelectedFile();
	        	Scanner scanner = new Scanner(f);
	        	String line = scanner.next();
	        	while(line != null){
	        		System.out.println(line);
	        	}
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
