package view;

import game.Card;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.ListOfPlayers;

public class OutputPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JPanel pnlBot = new JPanel(new BorderLayout(20, 0));
	
	private JTextArea txtGame = new JTextArea();

	private JScrollPane scrGame = new JScrollPane(txtGame, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	private JButton btnSave = new JButton("Save");
	
	private JCheckBox chckImmediateSave = new JCheckBox("Immediate save");
	
	private JFileChooser flChooser = new JFileChooser("C:/SKOLA/dipl");
	
	private boolean canRun = true;
	private boolean writeToFile = false;
	
	private FileWriter fileWriter;
	private File file = null;
	
	public OutputPanel(ListOfPlayers players){
		initFrame();
		initComponents(players);
		addComponents();
	}
	
	public void end(){
		try {
			if (writeToFile)
				fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void canRun(boolean yes){
		canRun = yes;
	}
	
	private void addLog(String s){
		if (writeToFile){
			try {
				fileWriter.write(s + "\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			txtGame.append(s + "\n");
			txtGame.setCaretPosition(txtGame.getDocument().getLength());
		}
	}
	
	private void initComponents(ListOfPlayers pls){
		scrGame.setViewportView(txtGame);
		txtGame.setEditable(false);
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int returnVal = flChooser.showSaveDialog(txtGame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					if (safeLog(flChooser.getSelectedFile()))
						JOptionPane.showMessageDialog(txtGame, "File was succesfully saved to\n" + flChooser.getSelectedFile().toString());
					else
						JOptionPane.showMessageDialog(txtGame, "File was NOT succesfully saved,\n" + "repeat the action.");
				}
			}
		});
		
		chckImmediateSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (chckImmediateSave.isSelected()){
						int returnVal = flChooser.showSaveDialog(txtGame);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							file = flChooser.getSelectedFile();
							fileWriter = new FileWriter(file);
							writeToFile = true;
							JOptionPane.showMessageDialog(txtGame, "Everything will be saved to file \n" + file.getCanonicalPath());
						} else {
							chckImmediateSave.setSelected(false);
						}
					} else {
						file = null;
						writeToFile = false;
						fileWriter.close();
					}
				} catch (Exception e) {}
			}
		});
	}
	
	private boolean safeLog(File file){
		try {
			txtGame.write(new FileWriter(file));
		} catch (Exception e) {
			System.out.println(e.toString());
			return false;
		}
		return true;
	}
	
	private void addComponents(){
		pnlBot.add(btnSave, BorderLayout.CENTER);
		pnlBot.add(chckImmediateSave, BorderLayout.WEST);
		add(scrGame, BorderLayout.CENTER);
		add(pnlBot, BorderLayout.SOUTH);
	}
	
	private void initFrame(){
		setLayout(new BorderLayout());
		setVisible(true);
	}

	public void dealedCard(int ID, Card c){
		if (canRun)
			addLog("player " + ID + " was dealed card " + c);
	}

	public void dealedCard(int ID, Card c1, Card c2){
		if (canRun)
			addLog("player " + ID + " was dealed cards " + c1 + ", " + c2);
	}
	
	public void check(int ID){
		if (canRun)
			addLog("player " + ID + " checked.");
	}
	
	public void fold(int ID){
		if (canRun)
			addLog("player " + ID + " folded.");
	}
	
	public void raise(int ID, double chips){
		if (canRun)
			addLog("player " + ID + " raised " + chips + ".");
	}
	
	public void call(int ID, double chips){
		if (canRun)
			addLog("player " + ID + " called.");
	}
	
	public void allIn(int ID, double chips){
		if (canRun)
			addLog("player " + ID + " is all in (" + chips + ").");
	}
}
