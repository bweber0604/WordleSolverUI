package wordleSolver;

import java.awt.Color;
import java.awt.Font;
import java.awt.TextArea;

import javax.swing.*;


import BreezySwing.GBDialog;

public class HelpWindow extends GBDialog{

	JLabel title = addLabel("How To Use This Solver:", 1, 1, 1, 1);
	JTextArea helpText = addTextArea("1. Type the your word attempts into the left side of the window \n"
			+ "2. Click on the letters depending on the result of the real wordle to the correct color \n"
			+ "3. hit enter \n"
			+ "4. select a word from the list generated and type that into wordle \n"
			+ "5. repeat these steps until you get the answer"
			, 2, 1, 1, 1);
	JButton close = addButton("Close",5,1,1,1);
	
	public HelpWindow(JFrame f) {
		super(f);
		
		helpText.setEditable(false);
		title.setFont(new Font(title.getFont().getName(),Font.BOLD,40));
		title.setBorder(BorderFactory.createLineBorder(Color.black,4));
		
		setSize(700,250);
		setResizable(false);
		setTitle("Wordle Solver Help");
		setVisible(true);
		
	}
	
	public void buttonClicked(JButton btn) {
		if(btn == close) {
			this.dispose();
		}
	}

}
