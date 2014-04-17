package butter.usc.edu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class CarTimer implements Runnable {
	JPanel mapPanel;
	public CarTimer(JPanel mp) {
		super();
		// TODO Auto-generated constructor stub
		mapPanel = mp;
	}

	private void drawingAction() {
		if(ButterGUI.allCarsWrapper.getLock().tryLock()) {
			try {
//				System.out.println("/// Got Lock ///");
		    	mapPanel.updateUI();
				for (int i = 0; i < ButterGUI.allCarsWrapper.allCars.size(); i++) {
					ButterGUI.allCarsWrapper.allCars.get(i).updateSpeed();
				}
				for (int i = 0; i < ButterGUI.allCarsWrapper.allCars.size(); i++) {
					if(ButterGUI.allCarsWrapper.allCars.get(i).point == null) System.out.println("NULL: " + ButterGUI.allCarsWrapper.allCars.get(i));
				}
				mapPanel.repaint();
			} 
			finally {
				ButterGUI.allCarsWrapper.getLock().unlock();
//				System.out.println("// Releasing lock //");
			}
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		final int timeSlice = 125; 
		System.out.println("Starting CarTimer");
		Timer timer = new  Timer (timeSlice, new ActionListener () {
		public void actionPerformed (ActionEvent e) {
			drawingAction();
		}
		});
		timer.start();
	}

}
