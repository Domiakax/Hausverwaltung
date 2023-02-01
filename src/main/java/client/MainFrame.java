package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class MainFrame extends JFrame{
	
	private static final String kundenNrString = "kundeNr";;
	
	private final JLabel kundenNr;
	private final JTextField kundenNrEingabe;
	
	
	public MainFrame() {
		Container con = this.getContentPane();
		con.setLayout(new BorderLayout());
		
		JPanel kundenEingabe = new JPanel(new GridLayout(1, 1));
		kundenNr = new JLabel(kundenNrString);
		kundenEingabe.add(kundenNr);
		kundenNrEingabe = new JTextField();
		kundenEingabe.add(kundenNrEingabe);
		this.add(kundenEingabe, BorderLayout.CENTER);
		pack();
	}
	
	public static void main(String[] args) {
		new MainFrame().setVisible(true);
	}

}
