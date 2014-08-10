package com.randi.dyned6.model;

import java.util.Vector;

import com.awan.dyned6.general.Constans;
import com.randi.dyned6.tools.FrameFieldListener;
import com.randi.dyned6.view.manager.AnswerManager;
import com.randi.dyned6.view.manager.GrammarReviewManager;
import com.randi.dyned6.view.manager.GrammarTextOnlyManager;
import com.randi.dyned6.view.manager.SentenceBuilderManager;

/**
 * Controls the view of GrammarScreen
 * @author Randi Waranugraha
 *
 */
public class GrammarManager {

	private static GrammarManager instance = null;
	
	private Vector v;
	private Vector vAnswer;
	private int currentPos;
	private int unitIndex;
	private int lessonIndex;

	private FrameFieldListener listener;
	
	private GrammarManager() {
		v = new Vector();
		vAnswer = new Vector();
	}
	
	/**
	 * Retrieves the system's GrammarManager instance.
	 * @return instance of GrammarManager
	 */
	public static GrammarManager getInstance() {
		if(instance == null){
			instance = new GrammarManager();
		}
		return instance;
	}
	
	/**
	 * Set the current Unit and Lesson index of ongoing Grammar questions.
	 * @param unitIndex index of Unit
	 * @param lessonIndex index of Lesson
	 */
	public void setCurrentIndex(int unitIndex, int lessonIndex){
		this.unitIndex = unitIndex;
		this.lessonIndex = lessonIndex;
		
		String[][] rawItem = Constans.grammarItem[unitIndex][lessonIndex];
		for(int i = 0; i < rawItem.length; i++){ 
			GrammarItem item = new GrammarItem(rawItem[i]);
			v.addElement(item);
		}
	}

	/**
	 * Decide which next ViewManager to display based on GrammarItem question type.
	 */
	public void nextManager(){
		if(listener != null){
			if(currentPos < v.size()){
				GrammarItem item = (GrammarItem)v.elementAt(currentPos);
				if("textonly".equals(item.getType())){
					GrammarTextOnlyManager textOnlyManager = new GrammarTextOnlyManager(item);
					listener.onFrameChanged(textOnlyManager);
				} else if("sentencebuilder".equals(item.getType())){
					SentenceBuilderManager sentenceBuilder = new SentenceBuilderManager(item);
					listener.onFrameChanged(sentenceBuilder);
				}
			} else {
				GrammarReviewManager reviewManager = new GrammarReviewManager(vAnswer);
				listener.onFrameChanged(reviewManager);
				listener.changeTitle("Grammar Feedback");
			}
		}
	}

	/**
	 * Adds answer from each Comprehension questions.
	 * 
	 * @param question
	 * @param answer
	 * @param correct 
	 */
	public void addAnswer(String question, String answer, boolean correct){
		AnswerManager answerManager = new AnswerManager(question, answer, correct);
		vAnswer.addElement(answerManager);
		currentPos++;
	}

	/**
	 * Sets the listener of this instance.
	 * @param listener
	 */
	public void setListener(FrameFieldListener listener) {
		this.listener = listener;
	}

	/**
	 * Reset the system's GrammarManager instance.
	 */
	public void reset(){
		currentPos = 0;
		unitIndex = 0;
		lessonIndex = 0;
		listener = null;
		v.removeAllElements();
		vAnswer.removeAllElements();
	}

	/**
	 * Close and clear this ComprehensionManager instance.
	 */
	public static void close(){
		instance = null;
	}

	/**
	 * Try again Grammar questions.
	 */
	public void tryagain(){
		currentPos = 0;
		vAnswer.removeAllElements();
	}

	/**
	 * Gets the Unit index of this GrammarManager instance.
	 * @return the unit index integer
	 */
	public int getUnitIndex() {
		return unitIndex;
	}

	/**
	 * Gets the Lesson index of this GrammarManager instance.
	 * @return the lesson index integer
	 */
	public int getLessonIndex() {
		return lessonIndex;
	}
}