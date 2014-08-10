package com.randi.dyned6.model;

/**
 * A class that represent object of question in Comprehension.
 * 
 * @author Randi Waranugraha
 *
 */
public class ComprehensionItem {

	private String type;
	private String question;
	private String answer;
	
	private String[] options;

	/**
	 * Creates a new ComprehensionItem with the given array of string.
	 * 
	 * @param item Array of string
	 */
	public ComprehensionItem(String[] item) {
		type = item[0];
		question = item[1];
		answer = item[5];
		
		options = new String[item.length - 3];
		for(int i = 2; i < item.length-1; i++){
			options[i-2] = item[i];
		}
	}
	
	/**
	 * Gets the question type of this ComprehensionItem object.
	 * @return the type string
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Gets the question of this ComprehensionItem object.
	 * @return the question string
	 */
	public String getQuestion() {
		return question;
	}
	
	/**
	 * Gets the answer of this ComprehensionItem object.
	 * @return the answer string
	 */
	public String getAnswer() {
		return answer;
	}
	
	/**
	 * Gets the options with random order of this ComprehensionItem object
	 * @return the array of options string
	 */
	public String[] getRandomOptions(){
		for(int j = 0; j < options.length; j++) {
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