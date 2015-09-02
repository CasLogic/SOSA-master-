package edu.gsu.psych.sosa.experiment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ardor3d.image.Texture;
import com.ardor3d.math.ColorRGBA;
import com.ardor3d.math.Vector3;
import com.ardor3d.math.type.ReadOnlyColorRGBA;
import com.ardor3d.renderer.Camera;
import com.ardor3d.scenegraph.Node;
import com.ardor3d.util.TextureManager;
import com.ardor3d.util.export.binary.BinaryExporter;
import com.ardor3d.util.export.binary.BinaryImporter;
import com.ardor3d.util.resource.MultiFormatResourceLocator;
import com.ardor3d.util.resource.ResourceSource;

import edu.gsu.psych.sosa.experiment.stimulus.ImageStim;
import edu.gsu.psych.sosa.experiment.stimulus.StimOrder;
import edu.gsu.psych.sosa.experiment.stimulus.Stimulus;
import edu.gsu.psych.sosa.main.SOSAFileHandler;
import edu.gsu.psych.sosa.main.SOSAPoint2D;
import edu.gsu.psych.sosa.main.SOSATexture;

public class Experiment implements Serializable{
	/**
	 * Version 1.0
	 */
	private static final long serialVersionUID = 6196269597116780022L;
	
	private List<Stimulus>	stims	= new ArrayList<Stimulus>();
	private List<StimOrder> orders	= new ArrayList<StimOrder>();
	private int currentOrderIndex = 0;
	
	public String name = "";
	public String version = "";
	
	public transient String subject = "";
	public transient String date	= "";
	private transient String directoryLocation = "";
	private transient ExperimentLog log;
	private transient SOSAPoint2D MIPOrigin;
	
	private transient SOSAUpdatable update;
	
	public ReadOnlyColorRGBA defaultBoardColor = ColorRGBA.WHITE;
	public ReadOnlyColorRGBA defaultBGColor = ColorRGBA.BLACK;

	private transient Texture boardFace = null;
	
	private byte[] boardTextureData = null;
	private SOSATexture texture = null;
	
	public ColorRGBA boardColor = new ColorRGBA(defaultBoardColor);
	public ColorRGBA bgColor = new ColorRGBA(defaultBGColor);
	
	public transient boolean showLabels = true;
	public transient float labelPosition = 1f;		//multiplier for offset
	private static final float maxLabelPosition = 40;	//the maximum position offset
	
	private int windowSizeX = 600;
	private int windowSizeY = 450;
	public boolean isWindowSizeSet = false;
	
	public Experiment(){
		orders.add(new StimOrder(this));
		//Sets the order of the Stimulus 
	}
	
	public void setUpdateInterface(SOSAUpdatable update){
		this.update = update;
		updateOrderList();
		//Updates the Orderlist
	}
	
	/**
	 * Creates a new arrangement of pegs and sets it as active.
	 * Duplicates the last existing order if there is more than one. 
	 */
	public void addPegOrder(){
		addPegOrder(currentOrderIndex);
	}
	
	/**
	 * Creates a new arrangement of pegs and sets it as active.
	 * Duplicates the specified order if there is more than one.
	 * @param duplicateIndex the index of the order to duplicate for this new one. if this is the first created order this value is ignored.
	 */
	public void addPegOrder(int duplicateIndex){
		orders.add(new StimOrder(this));
		currentOrderIndex = orders.size() - 1;		//sets the new order as active
		if(currentOrderIndex > 0 && currentOrderIndex != duplicateIndex)	//if that wasn't the first order to be created and we're not duplicating ourselves
			orders.get(currentOrderIndex).set(orders.get(duplicateIndex));	//make it a duplicate of the specified one
		updateOrderList();
	}
	
	public void removePegOrder(){
		removePegOrder(currentOrderIndex);
		
	}
	
	/**
	 * Will remove the peg order specified if such an order index exists and there are at least 2 orders
	 * @param index the index of the order to remove
	 */
	private void removePegOrder(int index){
		if(index < orders.size() && orders.size() > 1){
			orders.remove(index);
			updateOrderList();
			
		}
	}
	
	private void updateOrderList(){
		if(update != null){
			update.updateOrderDropdown();
			update.updateList();
			//Checks if the status of update status of update
		}
	}
	
	private void updateList(){
		if(update != null)
			update.updateList();
	}
	
	public int getOrderCount(){
		return orders.size();
	}
	
	public int getCurrentOrderIndex(){
		if(currentOrderIndex >= orders.size())		
			currentOrderIndex = orders.size() - 1;
		
		return currentOrderIndex;		
	}
	
	public StimOrder getCurrentOrder(){
		return orders.get(getCurrentOrderIndex());
	}

	public void addPeg() {
		stims.add(new Stimulus());
		stims.get(stims.size() - 1).setLabel("Unlabeled " + stims.size());		//give it a unique name (helpful in setup and testing)
		for (StimOrder order : orders) {
			order.append(stims.size() -1);
		}
		updateList();
	}
	
	public void removePeg(){
		if(!getCurrentOrder().isEmpty())
			removePeg(getCurrentOrder().getCurrentlySelectedIndex());
	}
	
	private void removePeg(int index) {
		int actualIndex = getCurrentOrder().getIndex(index);
		for (StimOrder order : orders)
			order.remove(actualIndex);
		stims.remove(actualIndex);
		updateList();
	}
	
	private void replaceCurrentPeg(Stimulus replacement){
		replacePeg(getCurrentOrder().getIndex(getCurrentPegIndex()), replacement);
	}
	
	private void replacePeg(int index, Stimulus replacement){
		stims.set(index, replacement);
	}

	/**
	 * Takes the current order into account to produce the
	 * proper representative list of stimuli for this experiment
	 * @return a properly ordered collection of {@link edu.gsu.psych.sosa.experiment.stimulus.Stimulus} objects
	 */
	public List<Stimulus> getStimList() {
		List<Integer> indexes = getCurrentOrder().getList();
		List<Stimulus> output = new ArrayList<Stimulus>();
		for (int i = 0; i < indexes.size(); i++)
			output.add(stims.get(indexes.get(i)));
		
		return output;
	}
	
	public List<Stimulus> getStimListUnordered(){
		return stims;
	}

	public Stimulus getPeg(int index) {
		return stims.get(getCurrentOrder().getIndex(index));
	}

	public void setOrder(int selectionIndex) {
		currentOrderIndex = selectionIndex;
		updateList();
	}

	public void movePegUp() {
		getCurrentOrder().moveUp();
		updateList();
		//References stimOrder to moveup
	}
	
	public void movePegDown(){
		getCurrentOrder().moveDown();
		updateList();
		//References stimOrder to movedown
	}

	public void selectPeg(int selectionIndex) {
		getCurrentOrder().select(selectionIndex);
		updateList();
	}
	
	public void selectPeg(Stimulus selection){
		getCurrentOrder().select(selection);
		update.updateCursor(SOSAUpdatable.CURSOR_GRABBING);
		updateList();
	}

	public Stimulus getSelectedPeg() {
		return getCurrentOrder().getCurrentlySelected();
	}
	
	public void setSelectedPeg(Stimulus stim) {
		if(stim != null){
			if(stim instanceof ImageStim)
				convertCurrentStimTo(Stimulus.IMAGE);
			else if(getSelectedPeg() instanceof ImageStim)	//conversion should work in both directions
				convertCurrentStimTo(Stimulus.PEG);
			getCurrentOrder().getCurrentlySelected().setStim(stim);
			updateList();
		}
	}

	public int getCurrentPegIndex() {
		return getCurrentOrder().getCurrentlySelectedIndex();
	}

	/**
	 * Updates the visual elements that pertain to the peg.
	 * Optionally updates the list entry depending on the passed
	 * boolean value.
	 * @param updateListEntry updates the list entry for this peg (should only be necessary when updating the label of the peg)
	 */
	public void updatePegEntry() {
		if(update != null)
			update.updateListActivePeg();	//updates the actual peg line in the list
	}
	
	public void initLabels() {
		showLabels = true;
		labelPosition = 0.6f;
		for(Stimulus stim : stims)
			stim.resetLabelSize();		
	}
	
	/**
	 * Sets the position of the label as a percentage
	 * difference between the position of the peg and the
	 * maximum offset
	 * @param pos value between 0 and 100
	 */
	public void setLablePosition(int pos){
		if(pos >= 0 && pos <= 100)
			labelPosition = (float)pos/(float)100;
	}
	
	/**
	 * Sets the darkness of the label as a percentage
	 * between the given label color (100) and black (0)
	 * @param level value between 0 and 100
	 */
	public void setLabelShade(int level){
		if(level >= 0 && level <= 100){
			float l = (float)level/(float)100;
			for(Stimulus stim : stims)
				stim.darkenLabel(l);
		}
	}
	
	/**
	 * Sets the size of the label as a percentage between
	 * the base size (100) and the minimum size (0)
	 * @param size value between 0 and 100
	 */
	public void setLabelSize(int size){
		if(size >= 0 && size <= 100){
			for(Stimulus stim : stims)
				stim.setLabelSize((float)size/(float)100);
		}
	}
	
	public void setLabelValues(int position, int shade, int size){
		setLablePosition(position);
		setLabelShade(shade);
		setLabelSize(size);
	}
	
	public void updateLabelPositions(Node rootNode, Camera cam){
		if(showLabels){
			for (Stimulus stim : stims) {
				if(rootNode.hasChild(stim.getRender()))
					if(!rootNode.hasChild(stim.getRenderLabel()))
						rootNode.attachChild(stim.getRenderLabel());
				stim.getRenderLabel().setTranslation(cam.getScreenCoordinates(stim.getRender().getWorldTranslation()).addLocal(positionLabel(stim)));				
			}
		}
	}
	
	private Vector3 positionLabel(Stimulus stim){
		return new Vector3(-stim.getRenderLabel().getWidth()/2f, labelPosition * maxLabelPosition, 0);
	}
	
	public void revertAllRenders(){
		for (Stimulus stim : stims)
			stim.resetRender();
	}
	
	public void revertAllLabelRenders(){
		for (Stimulus stim : stims)
			stim.resetLabelRender();
	}
	
	public void setBoardTexture(File file){
		if(file != null){
			try {
				getImageDataFromFile(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			resetBoardTexture();
		}
	}
	
	public void resetBoardTexture() {
		boardTextureData = null;
		boardFace = null;
		texture = null;
	}
	
	public void setBoardTexture(SOSATexture texture) {
		this.texture = texture;
		this.boardTextureData = null;
		getImageDataFromTexture();
	}
	
	private void getImageDataFromFile(File file) throws IOException{
		try {
			MultiFormatResourceLocator mfrl = new MultiFormatResourceLocator(file.getParentFile().toURI(), ".png", ".jpg");
			ResourceSource image = mfrl.locateResource(file.getName());
			boardFace = TextureManager.load(image, Texture.MinificationFilter.Trilinear, true);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//extract the byte array version of this texture (for saving purposes)
		BinaryExporter exporter = new BinaryExporter();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		exporter.save(boardFace, os);
		
		boardTextureData = os.toByteArray();
	}
	
	private void getImageDataFromTexture() {
		boardFace = TextureManager.loadFromImage(texture.getArdorImage(true), Texture.MinificationFilter.Trilinear);
	}
	
	public Texture getBoardFace(){
		if(boardFace == null){
			if(boardTextureData != null){
				BinaryImporter importer = new BinaryImporter();
				try {
					boardFace = (Texture)importer.load(boardTextureData);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if(texture != null) {
				getImageDataFromTexture();
			} 
		}
		return boardFace;
	}

	public void revertBoardColor() {
		boardColor.set(defaultBoardColor);		
	}
	
	public void revertBGColor(){
		bgColor.set(defaultBGColor);
	}
	
	private SimpleDateFormat getDateFormat(){
		return new SimpleDateFormat("MMMM d, yyyy (hh:mm a)");
	}
	
	private ExperimentLog getLog(){
		if(log == null)
			log = new ExperimentLog(this);
		return log;
	}

	public void setLogOutputLocation(String file){
		getLog().setOutputLocation(file);
	}

	public String getLogOutputLocation() {
		return getLog().getOuputLocation();
	}

	public void logMoveOrigin(float x, float y){
		MIPOrigin = new SOSAPoint2D(x, y);
	}
	
	public void logMoveDestination(float x, float y) {
		getLog().addMove(new Move(getSelectedPeg(), MIPOrigin, new SOSAPoint2D(x, y)));
		update.updateCursor(SOSAUpdatable.CURSOR_NORMAL);
	}
	
	public void logInitialPresentation() {
		getLog().logInitialPresentation();
	}
	
	public void logExperimentStart() {
		getLog().logExperimentStart();
	}

	public void writeLog() throws IOException {
		date = getDateFormat().format(new Date());
		SOSAFileHandler.writeFile(getLog().getLog(), getLogOutputLocation());
	}

	public void convertCurrentStimTo(int type) {
		if(getSelectedPeg() == null)
			return;
		if(getSelectedPeg() instanceof ImageStim){
			if(type == Stimulus.IMAGE)
				return;
					//converting to PEG
			Stimulus newPeg = new Stimulus();
			newPeg.convertFrom(getSelectedPeg());
			replaceCurrentPeg(newPeg);
		} else {	//converting to IMAGE
			if(type == Stimulus.PEG)
				return;
			ImageStim newPeg = new ImageStim();
			newPeg.convertFrom(getSelectedPeg());
			replaceCurrentPeg(newPeg);
		}
	}

	public int getWindowSizeX() {
		return windowSizeX;
	}
	
	public int getWindowSizeY() {
		return windowSizeY;
	}
	
	public void setWindowSize(int x, int y){
		isWindowSizeSet = true;
		windowSizeX = x;
		windowSizeY = y;
	}

	public String getWindowSizeText() {
		if(isWindowSizeSet)
			return windowSizeX+"x"+windowSizeY;
		else
			return "not set";
	}
	
	//Set the directory location to save output files to
	public void setDirectoryLocation(String s)
	{
		directoryLocation = s;
	}
	
	//Get the directory that ouput files will save to
	public String getDirectoryLocation()
	{
		return directoryLocation;
	}
}