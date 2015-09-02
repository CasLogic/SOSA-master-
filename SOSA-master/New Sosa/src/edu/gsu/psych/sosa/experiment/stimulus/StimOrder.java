package edu.gsu.psych.sosa.experiment.stimulus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.gsu.psych.sosa.experiment.Experiment;

public class StimOrder implements Serializable{
	
	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 2612362013119119973L;
	
	private Experiment parent;	
	private LinkedList<Integer> indexes = new LinkedList<Integer>();
	private int currentlySelected = 0;
	/**sets parent to new experiment **/
	public StimOrder(Experiment e) {
		parent = e;
	}
	/**adds i into indexes **/
	public void append(int i) {
		indexes.add(i);		
	}
	/**returns indexes **/
	public List<Integer> getList() {
		return indexes;
	}
	/**orders the list **/
	public List<Stimulus> getOrderedList(){
		List<Stimulus> temp = parent.getStimListUnordered();
		List<Stimulus> output = new ArrayList<Stimulus>(temp.size());
		for (Integer i : indexes)
			output.add(temp.get(i));
		return output;
	}
	/**returns element at index position **/
	public int getIndex(int index) {
		return indexes.get(index);
	}
	/**checks to see if indexex is empty **/
	public boolean isEmpty(){
		if(indexes != null && indexes.size() > 0)
			return false;
		return true;
	}
	/**adds all elements of stimOrder to the front of indexes **/
	public void set(StimOrder stimOrder) {
		this.indexes.addAll(stimOrder.getList());
	}
	/**remove element at index **/
	public void remove(int index) {
		indexes.removeFirstOccurrence(index);
		for(Integer i : indexes)
			if(i > index)
				indexes.set(indexes.indexOf(i), --i);
	}
	/**move element forward on the list **/
	public void moveUp() {
		if(currentlySelected > 0){
			indexes.add(currentlySelected -1, indexes.remove(currentlySelected));
			--currentlySelected;
		}
	}
	/**move element backward on the list **/
	public void moveDown(){
		if(currentlySelected < indexes.size() -1){
			indexes.add(currentlySelected +1, indexes.remove(currentlySelected));
			++currentlySelected;
		}
	}
	/** select stimulus at element **/
	public Stimulus select(int index){
		if (index > -1 && index < indexes.size())
			currentlySelected = index;
		return getCurrentlySelected();
	}
	/**select stimulus **/
	public void select(Stimulus stim){
		int x = parent.getStimList().indexOf(stim);
		if(x > -1)
			currentlySelected = x;
	}
	/**returns the current element **/
	public Stimulus getCurrentlySelected() {
		if(indexes.size() < 1)
			return null;
		if(currentlySelected >= indexes.size())
			currentlySelected = indexes.size() -1;
		return parent.getStimListUnordered().get(indexes.get(currentlySelected));
	}
	/**returns list **/
	public String toString(){
		return getOrderedList().toString();
	}
	/**returns selected element position **/
	public int getCurrentlySelectedIndex() {
		return currentlySelected;
	}
}
