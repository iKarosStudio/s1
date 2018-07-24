package Asgardia.Gui;

import javax.swing.*;
import java.util.*;


import Asgardia.World.Objects.*;
import Asgardia.World.*;

public class GuiMain extends Thread
{
	volatile Asgardia World;
	
	public GuiMain (Asgardia world) {
		World = world;
		
		JFrame f = new JFrame () ;
		
		f.setVisible (true) ;
		f.setSize (400, 300);
		f.setLocation (200, 200);
		f.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE) ;
		//JOptionPane.showMessageDialog (f, "啟動完成");
		
		JLabel ulist = new JLabel () ;
		f.add(ulist);
		JList<String> myList = new JList<String>();
		//f.add (myList);
		myList.setVisible (true);
		
		while (true) {
			List<PcInstance> pcs = World.getAllPc () ;
			
			StringBuffer sb = new StringBuffer ("Player:") ;
			for (PcInstance p : pcs) {
				sb.append (p.Name) ;
				sb.append (" ") ;
			}
			
			String tt = sb.toString () ;
			ulist.setText (tt);
			
			try {
				sleep (1000) ;
			} catch (Exception e) {
				//
			}
		}
	}
}
