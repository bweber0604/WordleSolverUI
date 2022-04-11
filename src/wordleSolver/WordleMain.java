package wordleSolver;
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
	
	JTextArea words = addTextArea("", 2, 1, 1, 3);
	
	JLabel word = addLabel("Type Word Here: ", 2, 2, 1, 1);
	JTextField userInput = addTextField("", 2, 3, 1, 1);
	
	JLabel wordRes = addLabel("Type Word Results Here: ", 3, 2, 1, 1);
	JTextField userInputResults = addTextField("", 3, 3, 1, 1);
	
	JButton search = addButton("Search", 4, 3, 1, 1);
	
	JButton reset =addButton("Reset", 4, 4, 1, 1);
	
	ArrayList<String> possibleWords = new ArrayList<String>();
		
	String userWord = "";
	String wordResults = "";
	
	//boolean running = true;
	
	
	//breaks with double vowels
	
	public void buttonClicked(JButton btn) {
		if(btn == search) {
			userWord = userInput.getText().trim();
			wordResults = userInputResults.getText().trim();
			
			prev.setText(prev.getText() + userWord);
			
			String fullList = "";
			
			compareWords();
			for(String s : possibleWords) {
				fullList += s + "\n";
			}
			
			words.setText(fullList);
			
			userInput.setText("");
			userInputResults.setText("");
		}else if(btn == reset) {
			resetList();
		}
	}
	
	
	public void getInputs() {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your word: ");
		userWord = sc.nextLine();
		System.out.println("Enter wordle results: ");
		wordResults = sc.nextLine();
	}
	
	public void compareWords() {
		
		
		//trees /*/--
		
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
			
			ind = wordResults.indexOf('-');
			
			while(ind != -1) {
				for(int i = 0; i < possibleWords.size(); i++) {//check every word in array
					String s = possibleWords.get(i);
					if(s.indexOf(userWord.charAt(ind)) != -1) {//-1 means no letter present if it is not -1 means it is in word
						possibleWords.remove(s);
						i--;//stops array element skipping
					}
				}
				
				ind = wordResults.indexOf('-', ind+1);
			}
			
		
	}
	
	
	public void resetList() {
		try {
			File words = new File("words.txt");//init file model
			Scanner reader = new Scanner(words);
			
			while(reader.hasNextLine()) {//read through every line of array
				String data = reader.nextLine();
				possibleWords.add(data);//add to array
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
	}
	
	public WordleMain() {
		words.setEditable(false);
		
		try {
			File words = new File("words.txt");//init file model
			Scanner reader = new Scanner(words);
			
			while(reader.hasNextLine()) {//read through every line of array
				String data = reader.nextLine();
				possibleWords.add(data);//add to array
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Error");
		}
		
		
//		while(running) {
//			getInputs();
//			compareWords();
//			for(String s : possibleWords) {
//				System.out.println(s);
//			}
//		}
		
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
