package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import utils.ListOfPlayers;

public class ConsoleWindow extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private JTextArea txtGame = new JTextArea();

	private JScrollPane scrGame = new JScrollPane(txtGame, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	private JButton btnSave = new JButton("Save");
	
	private JFileChooser flChooser = new JFileChooser("C:/SKOLA/dipl");
	
	public ConsoleWindow(ListOfPlayers players){
		initFrame();
		initPanels();
		initComponents(players);
		addComponents();
	}
	
	public void addLog(String s){
		txtGame.append(s + "\n");
	}
	
	private void initPanels(){
	}
	
	private void initComponents(ListOfPlayers pls){
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
		add(scrGame, BorderLayout.CENTER);
		add(btnSave, BorderLayout.SOUTH);
	}
	
	private void initFrame(){
		setLayout(new BorderLayout());
		setVisible(true);
	}
}
