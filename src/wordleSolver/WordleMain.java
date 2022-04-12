package wordleSolver;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import BreezySwing.GBFrame;

public class WordleMain extends GBFrame {

	
/*How it will work
 * read from a file every possible word that can be entered into wordle and store in a arraylist
 * 	have user input a word
 *  have user input wordle response ('*' = correct spot, '/' = right letter wrong spot, '-' = not in word)
 *  loop through array 
 *  	for every * check input word and words in list for matching letters
 *  		if they matcth add the word to new list
 *  	for every / check if words in new list contain letter in index
 *  		if they dont have letter remove from list
 *  	for every - check if words in latest list contain those letters
 *  		if they have letters in word remove from list
 *  display current list
 *  repeat
 */
	
	JLabel title = addLabel("Wordle Solver: ", 1, 1, 1, 1);
	JLabel prev = addLabel("Previous Guesses: ", 1, 2, 1, 1);
	
	JComboBox type = addComboBox(2, 1, 1, 1);
	
	JTextArea words = addTextArea("", 3, 1, 1, 3);
	
	JLabel word = addLabel("Type Word Here: ", 3, 2, 1, 1);
	JTextField userInput = addTextField("", 3, 3, 1, 1);
	
	JLabel wordRes = addLabel("Type Word Results Here: ", 4, 2, 1, 1);
	JTextField userInputResults = addTextField("", 4, 3, 1, 1);
	
	JButton search = addButton("Search", 5, 3, 1, 1);
	
	JButton reset =addButton("Reset", 5, 4, 1, 1);
	
	ArrayList<String> possibleWords = new ArrayList<String>();
	ArrayList<String> possibleAnswers = new ArrayList<String>();	
	
	String userWord = "";
	String wordResults = "";
	
	boolean wordMaster = false;


	public void buttonClicked(JButton btn) {
		if(btn == search) {
			userWord = userInput.getText().trim();
			wordResults = userInputResults.getText().trim();
			
			prev.setText(prev.getText().concat(userWord + " "));
			
			String fullList = "";
			
			if(wordResults.equals("*****")) {
				messageBox("Correct Word Guessed");
				filterWords();
			}
			
			compareWords();
			for(int i = 0; i < possibleWords.size(); i++) {
				fullList += possibleWords.get(i) + "\n";
				
			}
			words.setText(fullList);
						
			userInput.setText("");
			userInputResults.setText("");
		}else if(btn == reset) {
			prev.setText("");
			filterWords();
		}
	}
	
	public void compareWords() {
		
			int ind = wordResults.indexOf('*');

			//wont be entered if there is no * 
			while(ind != -1) {//repeat while there is a *
				for(int i = 0; i < possibleWords.size(); i++) {
					String s = possibleWords.get(i);
					if(s.charAt(ind) != userWord.charAt(ind)) {
						possibleWords.remove(s);
						i--;//prevent array from skipping elements
					}
					
				}
				
				ind = wordResults.indexOf('*', ind+1);
			}
			
			ind = wordResults.indexOf('/');
			
			while(ind != -1) {
				for(int i = 0; i < possibleWords.size(); i++) {//check every word in array
					String s = possibleWords.get(i);
					if(s.indexOf(userWord.charAt(ind)) == -1) {//-1 means no letter present
						possibleWords.remove(s);
						i--;//stops array element skipping
					}else if(s.charAt(ind) == userWord.charAt(ind)) {
						possibleWords.remove(s);
						i--;
					}
					
				}
				
				ind = wordResults.indexOf('/', ind+1);
			}
			
			
			//System.out.println(nonMutable.toString());
			//breaks with double vowels
			//repeat letters break program
			
			//double letters count only as one instance of word
			//words with one letter of double letters in word
			//program removes all words that dont have the letter
			//then after proceeding to "-" then it remove all the words with the second double letter making the possible word array empty
			ind = wordResults.indexOf('-');
			
			while(ind != -1) {
				for(int i = 0; i < possibleWords.size(); i++) {//check every word in array
					String s = possibleWords.get(i);
					char temp = userWord.charAt(ind);//stores the character that is being checked to be removed
					if(s.indexOf(temp) != -1) {//-1 means no letter present if it is not -1 means it is in word
						int cInd = wordResults.indexOf('/');
						boolean doubleLetter = false;
						
						char l = userWord.charAt(cInd);
						//need a check for double letters from previous sections above
						
						while(cInd != -1) {//loop until there are no more /
							if(s.charAt(cInd) == temp) {// if index of / is the same character as the letter to be removed (presence of double letters) dont remove that letter
								doubleLetter = true;
								break;//double letter found. now dont remove word 
							}
							
							cInd = wordResults.indexOf('/', cInd +1);
						}
						
						
						
//						while(cInd != -1) {
//							if(s.indexOf(userWord.charAt(cInd)) != -1 && userWord.charAt(cInd) == s.charAt(ind)) {// there is a presense of a /
//								//System.out.println(userWord.charAt(cInd) + " " + s.charAt(ind));
//								System.out.println(s);
//								doubleLetter = true;
//								break;
//							}
//							cInd = wordResults.indexOf('/', cInd+1);
//						}
						if(!doubleLetter) {
							//System.out.println(s);
							possibleWords.remove(s);
							i--;//stops array element skipping	
						}
					}
				}
				
				ind = wordResults.indexOf('-', ind+1);
			}
			
		
	}
	
	
	public void resetList() {
		try {
			File words = new File("words.txt");//init file model
			Scanner reader = new Scanner(words);
			possibleWords.clear();
			
			while(reader.hasNextLine()) {//read through every line of array
				String data = reader.nextLine();
				possibleWords.add(data);//add to array
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
	}
	
	public void filterWords() {
		resetList();
		
		if(!wordMaster) {
			try {
				File words = new File("answers.txt");//init file model
				Scanner reader = new Scanner(words);
				possibleAnswers.clear();
				
				while(reader.hasNextLine()) {//read through every line of array
					String data = reader.nextLine();
					possibleAnswers.add(data.toLowerCase());//add to array
				}
				reader.close();
			} catch (FileNotFoundException e) {
				System.out.println("Error");
			}
			
			for(int i = 0; i< possibleWords.size(); i++) {
				if(!possibleAnswers.contains(possibleWords.get(i))) {//get only the words that can be answers
					possibleWords.remove(possibleWords.get(i));
					i--;
				}
			}
		}
		
		
		
	}
	
	
	
	public WordleMain() {
		words.setEditable(false);
		
		type.addItem("Wordle");
		type.addItem("Word Master");
		
		type.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(type.getSelectedItem().equals("Wordle")) {
					wordMaster = false;
					filterWords();
				}else {
					wordMaster = true;
					filterWords();
				}
			}
		});
		
		filterWords();
		
	}
	
	public static void main(String[] args) {
		JFrame f = new WordleMain();
		f.setSize(700,500);
		f.setLocationRelativeTo(null);
		f.setVisible(true);
		f.setResizable(true);
		f.setTitle("Wordle Solver");
	}

}
