package com.randi.dyned6.model;

import java.util.Vector;

import com.awan.dyned6.general.Constans;
import com.randi.dyned6.tools.FrameFieldListener;
import com.randi.dyned6.view.manager.AnswerManager;
import com.randi.dyned6.view.manager.AudioAndTextManager;
import com.randi.dyned6.view.manager.ComprehensionReviewManager;
import com.randi.dyned6.view.manager.ComprehensionTextOnlyManager;

/**
 * Controls the view of ComprehensionScreen
 * @author Randi Waranugraha
 *
 */
public class ComprehensionManager {

	private static ComprehensionManager instance = null;
	
	private Vector v;
	private Vector vAnswer;
	private int currentPos;
	private int unitIndex;
	private int lessonIndex;
	
	private FrameFieldListener listener;

	/**
	 * Retrieves the system's ComprehensionManager instance.
	 * 
	 * @return instance of ComprehensionManager 
	 */
	public static ComprehensionManager getInstance() {
		if(instance == null){
			instance = new ComprehensionManager();
		}
		return instance;
	}
	
	private ComprehensionManager() {
		v = new Vector();
		vAnswer = new Vector();
	}

	/**
	 * Set the current Unit and Lesson index of ongoing Comprehension questions.
	 * 
	 * @param unitIndex index of Unit
	 * @param lessonIndex index of Lesson
	 */
	public void setCurrentIndex(int unitIndex, int lessonIndex){
		this.unitIndex = unitIndex;
		this.lessonIndex = lessonIndex;
		
		String[][] rawItem = Constans.comprehensionItem[unitIndex][lessonIndex];
		for(int i = 0; i < rawItem.length; i++){ 
			ComprehensionItem item = new ComprehensionItem(rawItem[i]);
			v.addElement(item);
		}
	}
	
	/**
	 * Sets the listener of this instance.
	 * @param listener
	 */
	public void setListener(FrameFieldListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Decide which next ViewManager to display based on ComprehensionItem question type.
	 */
	public void nextManager(){
		if(listener != null){
			if(currentPos < v.size()){
				ComprehensionItem item = (ComprehensionItem)v.elementAt(currentPos);
				if("textonly".equals(item.getType())){
					ComprehensionTextOnlyManager textOnlyManager = new ComprehensionTextOnlyManager(item);
					listener.onFrameChanged(textOnlyManager);
				} else if("audioandtext".equals(item.getType())){
					AudioAndTextManager audioAndTextManager = new AudioAndTextManager(item);
					listener.onFrameChanged(audioAndTextManager);
				}
			} else {
				ComprehensionReviewManager reviewManager = new ComprehensionReviewManager(vAnswer);
				listener.onFrameChanged(reviewManager);
				listener.changeTitle("Comprehension Feedback");
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
	 * Reset the system's ComprehensionManager instance.
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
	 * Gets the Unit index of this ComprehensionManager instance.
	 * @return the unit index integer
	 */
	public int getUnitIndex() {
		return unitIndex;
	}
	
	/**
	 * Gets the Lesson index of this ComprehensionManager instance.
	 * @return the lesson index integer
	 */
	public int getLessonIndex() {
		return lessonIndex;
	}
}