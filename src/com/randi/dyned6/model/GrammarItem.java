package com.randi.dyned6.model;

import java.util.Vector;

/**
 * A class that represent object of questions in Grammar.
 * @author Randi Waranugraha
 *
 */
public class GrammarItem {

	private String type;
	private String question;
	private String answer;

	private String[] options;

	/**
	 * Creates a new GrammarItem with the given array of string.
	 * @param item Array of string
	 */
	public GrammarItem(String item[]) {
		Vector v = new Vector();
		for(int j = 0; j < item.length; j++){
			if(item[j] != null) {
				v.addElement(item[j]);
			}
		}
		type = (String)v.elementAt(0);
		answer = (String)v.lastElement();
		if("textonly".equals(type)){
			question = (String)v.elementAt(1);
			options = new String[v.size() - 3];
			for(int i = 2; i < v.size() - 1; i++){
				options[i-2] = (String)v.elementAt(i);
			}
		} else if("sentencebuilder".equals(type)){
			options = new String[v.size() - 2];
			for(int i = 1; i < v.size()-1; i++){
				options[i-1] = (String)v.elementAt(i);
			}
		}
	}

	/**
	 * Gets the question type of this GrammarItem object.
	 * @return the type string
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the answer of this GrammarItem object.
	 * @return the answer string
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * Gets the question of this GrammarItem object.
	 * @return the question string
	 */
	public String getQuestion() {
		return question;
	}

	/**
	 * Gets the options with random order of this GrammarItem object
	 * @return the array of options string
	 */
	public String[] getRandomOptions(){
		int repeat = options.length % 2 != 0 ? options.length : options.length - 1;
		for(int j = 0; j < repeat; j++) {
			String[] tempString = new String[options.length];
			int random = Variables.generateRandom(0, options.length);
			tempString[0] = options[random];
			tempString[random] = options[0];
			for(int i = 0; i < options.length; i++) {
				if(i != 0 && i != random){
					tempString[i] = options[i];
				}
			}
			options = tempString;
		}
		return options;
	}
}