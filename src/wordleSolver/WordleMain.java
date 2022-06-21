package wordleSolver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import BreezySwing.GBFrame;
import BreezySwing.GBPanel;

public class WordleMain extends GBFrame {

	GBPanel title = addPanel(1, 1, 1, 1);
	GBPanel wordle = addPanel(2, 1, 1, 1);
	GBPanel words = addPanel(1, 2, 1, 2);

	ArrayList<String> possibleAnswers = new ArrayList<String>();

	ArrayList<Character> used = new ArrayList<Character>();

	JLabel titleText = title.addLabel("Wordle Solver", 1, 1, 1, 1);

	//add a button
	JLabel helpButton = title.addLabel("", 1, 2, 1, 1);
	
	JTextArea wordList = words.addTextArea("Words", 1, 1, 1, 1);

	JButton resetBtn = wordle.addButton("Reset", 9, 1, 1, 1);
	JLabel[][] boxes = new JLabel[6][5];

	int row = 0;
	int col = 0;

	String userWord = "";
	String wordResults = "";

	boolean running = true;

	int guessCount = 0;

	final Color BLANK = new Color(255, 255, 255);
	final Color CORRECT = new Color(57, 191, 62);
	final Color SPOT = new Color(255, 243, 105);
	final Color WRONG = new Color(41, 41, 41);
	
	static JFrame f;

	final int[] restrictions = { KeyEvent.VK_ENTER, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_SPACE, 192,
			KeyEvent.VK_BACK_SLASH, KeyEvent.VK_SLASH, KeyEvent.VK_SHIFT, KeyEvent.VK_MULTIPLY, KeyEvent.VK_SUBTRACT,
			KeyEvent.VK_DIVIDE, KeyEvent.VK_ADD, KeyEvent.VK_NUMPAD0, KeyEvent.VK_NUMPAD1, KeyEvent.VK_NUMPAD2, KeyEvent.VK_NUMPAD3,
			KeyEvent.VK_NUMPAD4, KeyEvent.VK_NUMPAD5, KeyEvent.VK_NUMPAD6, KeyEvent.VK_NUMPAD7, KeyEvent.VK_NUMPAD8, KeyEvent.VK_NUMPAD9,
			KeyEvent.VK_DEAD_GRAVE, KeyEvent.VK_PLUS, KeyEvent.VK_ASTERISK, KeyEvent.VK_BRACELEFT,
			KeyEvent.VK_BRACERIGHT, KeyEvent.VK_DECIMAL, KeyEvent.VK_BRACELEFT, KeyEvent.VK_BRACERIGHT,
			KeyEvent.VK_DOLLAR, KeyEvent.VK_EXCLAMATION_MARK, KeyEvent.VK_QUOTE,KeyEvent.VK_SEMICOLON};
	
	
	public boolean isLineValid() {
		
		for(int i = 0; i < 5; i++) {
			if(boxes[row][i].getBackground() == BLANK) {
				messageBox("Select a color for each letter");
				return false;
			}
		}
		
		return true;
	}
	
	// detect if the user input is a input that shouldn't be entered into the boxes
	private boolean contains(int keyCode) {

		for (int i = 0; i < restrictions.length; i++) {
			if (keyCode == restrictions[i]) {// input is a restriction return true		
					return true;
			}
		}

		if(Character.isAlphabetic(keyCode) && !Character.isDigit(keyCode)) {
			return false;
		}

		return true;
	}

	public void buttonClicked(JButton btn) {
		if (btn == resetBtn) {
			row = 0;
			col = 0;
			wordList.grabFocus();
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					boxes[i][j].setText("");
					boxes[i][j].setForeground(Color.black);
					boxes[i][j].setBackground(BLANK);
				}
			}

		}
	}

	public void addLetter(String c) {
		if (col < 5) {
			boxes[row][col].setText(c.toUpperCase());
			boxes[row][col].setBackground(BLANK);
			col++;
		}
	}

	public void addCharacter(char c) {
		if (used.indexOf(c) == -1) // prevent list from adding duplicate letters
			used.add(c);
	}

	public void compareWords() {
		
		int ind = wordResults.indexOf('*');

		while (ind != -1) {// repeat while there is a *

			for (int i = 0; i < possibleAnswers.size(); i++) {
				String s = possibleAnswers.get(i);// word that will be compared
				addCharacter(userWord.charAt(ind));
				if (s.charAt(ind) != userWord.charAt(ind)) {//
					possibleAnswers.remove(s);
					addCharacter(userWord.charAt(ind));// add character to not be removed
					i--;// prevent array from skipping elements
				}
			}

			ind = wordResults.indexOf('*', ind + 1);
		}

		ind = wordResults.indexOf('/');

		
		while (ind != -1) {
			for (int i = 0; i < possibleAnswers.size(); i++) {// check every word in array
				String s = possibleAnswers.get(i);
				addCharacter(userWord.charAt(ind));
				if (s.indexOf(userWord.charAt(ind)) == -1) {// -1 means no letter present

					possibleAnswers.remove(s);
					i--;// stops array element skipping
				} else if (s.charAt(ind) == userWord.charAt(ind)) {// remove the word if the character lies in the same
																	// place as
					possibleAnswers.remove(s);
					i--;
				}
			}
			ind = wordResults.indexOf('/', ind + 1);
		}
		ind = wordResults.indexOf('-');

		while (ind != -1) {
			for (int i = 0; i < possibleAnswers.size(); i++) {// check every word in array
				String s = possibleAnswers.get(i);
				if (s.indexOf(userWord.charAt(ind)) != -1) {// character in word
					if (used.indexOf(userWord.charAt(ind)) == -1) {// letter not in used
						possibleAnswers.remove(s);
						i--;// stops array element skipping
					} else {// letter is in word
							// only remove the word if it is the same position
						if (s.charAt(ind) == userWord.charAt(ind)) {
							possibleAnswers.remove(s);
							i--;
						}
					}
				}
			}

			ind = wordResults.indexOf('-', ind + 1);
		}
				
		guessCount++;
		
		if (wordResults.equals("*****")) {
			messageBox("Word Found in " + guessCount + "Guesses");
			row = 0;
			col = 0;
			for (int i = 0; i < boxes.length; i++) {
				for (int j = 0; j < boxes[i].length; j++) {
					boxes[i][j].setText("");
					boxes[i][j].setBackground(BLANK);
				}
			}
			filterWords();
			wordList.setText("");
		} else {
			String w = "";
			for (String s : possibleAnswers) {
				w += s + "\n";
			}
			wordList.setText(w);
			
		}

	}

	public void filterWords() {

		try {
			File words = new File("answers.txt");// init file model
			Scanner reader = new Scanner(words);
			possibleAnswers.clear();

			while (reader.hasNextLine()) {// read through every line of array
				String data = reader.nextLine();
				possibleAnswers.add(data.toLowerCase());// add to array
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}

		// empty used characters list
		used = new ArrayList<Character>();
		guessCount = 0;
	}

	public WordleMain() {

		wordle.setPreferredSize(new Dimension(200, 400));
		
		wordle.setFocusable(false);
		title.setFocusable(false);
		
		wordList.setEditable(false);
		
		
		title.setBackground(new Color(56, 137, 242));
		title.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		
		
		words.setBackground(Color.white);
		words.setPreferredSize(new Dimension(100, 500));

		titleText.setFont(new Font(titleText.getFont().getName(), Font.BOLD, 60));
		titleText.setForeground(Color.black);
		titleText.setHorizontalAlignment(SwingConstants.CENTER);
		titleText.setVerticalAlignment(SwingConstants.CENTER);

		
		Icon questionMark = new ImageIcon("question_mark_PNG6.png");
		helpButton.setIcon(questionMark);
		helpButton.setPreferredSize(new Dimension(70,70));
		helpButton.setHorizontalAlignment(SwingConstants.CENTER);
		
		helpButton.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				//open a help window
				HelpWindow h = new HelpWindow(f);

			}
		});
		
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				boxes[i][j] = wordle.addLabel("", i + 2, j + 1, 1, 1);
				boxes[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
				boxes[i][j].setOpaque(true);
				boxes[i][j].setBackground(BLANK);
				boxes[i][j].setForeground(Color.black);
				boxes[i][j].setFont(new Font(boxes[i][j].getFont().getFontName(), Font.BOLD, 40));
				boxes[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				boxes[i][j].setPreferredSize(new Dimension(50, 50));
					
				
				//add mouse listener to cycle through green to yellow to gray if there is a letter in it
				
				boxes[i][j].addMouseListener(new MouseListener() {
					
					
					@Override
					public void mouseReleased(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mousePressed(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseExited(MouseEvent e) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void mouseEntered(MouseEvent e) {

					}
					
					@Override
					public void mouseClicked(MouseEvent e) {
						//change background color
						if(!((JLabel) e.getComponent()).getText().isEmpty()) {
							if(e.getComponent().getBackground() == BLANK) {
								e.getComponent().setBackground(WRONG);
								e.getComponent().setForeground(Color.white);
							}else if(e.getComponent().getBackground() == WRONG) {
								e.getComponent().setBackground(SPOT);
							}else if(e.getComponent().getBackground() == SPOT) {
								e.getComponent().setBackground(CORRECT);
							}else{
								e.getComponent().setBackground(WRONG);
							}
						}
					}
				});
			}

		}

		wordList.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {// changes row and will submit current word
					// submit work to then accept the color
					if(col == 5 && isLineValid()) {//only full lines can be entered
						String in = "";
						String res = "";
						for(int i = 0; i < 5;i++) {
							in += boxes[row][i].getText();
							if(boxes[row][i].getBackground() == CORRECT) res += "*";
							else if(boxes[row][i].getBackground() == SPOT) res += "/";
							else if(boxes[row][i].getBackground() == WRONG) res += "-";
						}
						row++;
						col = 0;
						
						userWord = in.toLowerCase().trim();
						wordResults = res.trim();
						compareWords();
					}
					

				} else if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE && col > 0) {
					// remove last letter
					col--;
					boxes[row][col].setText("");
				} else if (!contains(e.getKeyCode())) {// Character.toString(e.getKeyChar())
					addLetter(Character.toString(e.getKeyChar()));
				}

			}
		});

		filterWords();

	}

	public static void main(String[] args) {
		f = new WordleMain();
		f.setSize(800, 600);
		f.setVisible(true);
		f.setResizable(false);
		f.setLocationRelativeTo(null);
		f.setTitle("Wordle Solver");
	}

}