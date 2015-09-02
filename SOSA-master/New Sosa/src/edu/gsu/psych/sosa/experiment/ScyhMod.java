package edu.gsu.psych.sosa.experiment;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public class ScyhMod {
	
	public static String[] setHotKey(){
		
		int  keyNum = Integer.parseInt(JOptionPane.showInputDialog(null, "How many keys you would like to use pressed up to 3 \n If you want no keys pressed please enter 0", "Key Scyn Module",1));
		String[] Keys = new String[keyNum];
		for(int i = 0; i < keyNum; i++){
			Keys[i] = JOptionPane.showInputDialog(null, "Enter Key "+(i+1)+ " \nPlease use All Caps When Entering In Your Key ");
		}
		return Keys;
		//Users inputs a key from 0 to at most 3 (not used by this version)
	}
	
	public static void pressHotKey(String[] keys)throws AWTException{
		try{
		Robot meRobot = new Robot();
		int[] keyP = new int[keys.length];
		for(int i = 0; i < keys.length;i++){
		 keyP[i] = seachedKeyPress(keys[i]);
		}
		if(keyP.length == 1)
		{
			meRobot.keyPress(keyP[0]);
			meRobot.keyRelease(keyP[0]);
		}else if(keyP.length ==2)
		{
			meRobot.keyPress(keyP[0]);
			meRobot.keyPress(keyP[1]);
			meRobot.keyRelease(keyP[0]);
			meRobot.keyRelease(keyP[1]);
		}else if(keyP.length == 3){
			meRobot.keyPress(keyP[0]);
			meRobot.keyPress(keyP[1]);
			meRobot.keyPress(keyP[2]);
			meRobot.keyRelease(keyP[0]);
			meRobot.keyRelease(keyP[1]);
			meRobot.keyRelease(keyP[2]);
		}else{}
		
		}catch(Exception exception)
		{exception.printStackTrace();}
		//Creates an array keyP that depending on the length uses a keyPress/keyRelease from 1-3
		
		
	}
	private static int seachedKeyPress(String keyP)
	{
		if(keyP.equals("A"))
		{return KeyEvent.VK_A;}
		else if(keyP.equals("B"))
		{return KeyEvent.VK_B;}
		else if(keyP.equals("C"))
		{return KeyEvent.VK_C;}
		else if(keyP.equals("D"))
		{return KeyEvent.VK_D;}
		else if(keyP.equals("E"))
		{return KeyEvent.VK_E;}
		else if(keyP.equals("F"))
		{return KeyEvent.VK_F;}
		else if(keyP.equals("G"))
		{return KeyEvent.VK_G;}
		else if(keyP.equals("H"))
		{return KeyEvent.VK_H;}
		else if(keyP.equals("I"))
		{return KeyEvent.VK_I;}
		else if(keyP.equals("J"))
		{return KeyEvent.VK_J;}
		else if(keyP.equals("K"))
		{return KeyEvent.VK_K;}
		else if(keyP.equals("L"))
		{return KeyEvent.VK_L;}
		else if(keyP.equals("M"))
		{return KeyEvent.VK_M;}
		else if(keyP.equals("N"))
		{return KeyEvent.VK_N;}
		else if(keyP.equals("O"))
		{return KeyEvent.VK_O;}
		else if(keyP.equals("P"))
		{return KeyEvent.VK_P;}
		else if(keyP.equals("Q"))
		{return KeyEvent.VK_Q;}
		else if(keyP.equals("R"))
		{return KeyEvent.VK_R;}
		else if(keyP.equals("S"))
		{return KeyEvent.VK_S;}
		else if(keyP.equals("T"))
		{return KeyEvent.VK_T;}
		else if(keyP.equals("U"))
		{return KeyEvent.VK_U;}
		else if(keyP.equals("V"))
		{return KeyEvent.VK_V;}
		else if(keyP.equals("W"))
		{return KeyEvent.VK_W;}
		else if(keyP.equals("X"))
		{return KeyEvent.VK_X;}
		else if(keyP.equals("Y"))
		{return KeyEvent.VK_Y;}
		else if(keyP.equals("Z"))
		{return KeyEvent.VK_Z;}
		else if(keyP.equals("SHIFT"))
		{return KeyEvent.VK_SHIFT;}
		else if(keyP.equals("CONTROL"))
		{return KeyEvent.VK_CONTROL;}
		else if(keyP.equals("ALT"))
		{return KeyEvent.VK_ALT;}
		else if(keyP.equals("1"))
		{return KeyEvent.VK_1;}
		else if(keyP.equals("2"))
		{return KeyEvent.VK_2;}
		else if(keyP.equals("3"))
		{return KeyEvent.VK_3;}
		else if(keyP.equals("4"))
		{return KeyEvent.VK_4;}
		else if(keyP.equals("5"))
		{return KeyEvent.VK_5;}
		else if(keyP.equals("6"))
		{return KeyEvent.VK_6;}
		else if(keyP.equals("7"))
		{return KeyEvent.VK_7;}
		else if(keyP.equals("8"))
		{return KeyEvent.VK_8;}
		else if(keyP.equals("9"))
		{return KeyEvent.VK_9;}
		else if(keyP.equals("0"))
		{return KeyEvent.VK_0;}
		else if(keyP.equals("F1"))
		{return KeyEvent.VK_F1;}
		else if(keyP.equals("F2"))
		{return KeyEvent.VK_F2;}
		else if(keyP.equals("F3"))
		{return KeyEvent.VK_F3;}
		else if(keyP.equals("F4"))
		{return KeyEvent.VK_F4;}
		else if(keyP.equals("F5"))
		{return KeyEvent.VK_F5;}
		else if(keyP.equals("F6"))
		{return KeyEvent.VK_F6;}
		else if(keyP.equals("F7"))
		{return KeyEvent.VK_F7;}
		else if(keyP.equals("F8"))
		{return KeyEvent.VK_F8;}
		else if(keyP.equals("F9"))
		{return KeyEvent.VK_F9;}
		else if(keyP.equals("F10"))
		{return KeyEvent.VK_F10;}
		else if(keyP.equals("F11"))
		{return KeyEvent.VK_F11;}
		else if(keyP.equals("F12"))
		{return KeyEvent.VK_F12;}
		else if(keyP.equals("TAB"))
		{return KeyEvent.VK_TAB;}
		else if(keyP.equals("PRINTSCREEN"))
		{return KeyEvent.VK_PRINTSCREEN;}
		else if(keyP.equals("SPACE"))
		{return KeyEvent.VK_SPACE;}
		else{JOptionPane.showMessageDialog(null, "Key " + keyP + " was not found");
		return 0;}
		//Sets Key values for various Keys
	
	}
}
