package Tetris;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.SwingConstants;



public class ScoreBanner extends JPanel{
	public static int score = 0;

	public static JLabel dispScore;
	public ScoreBanner() {
		dispScore = new JLabel(String.valueOf(score));
		dispScore.setHorizontalAlignment(SwingConstants.CENTER);
		
		add(dispScore, BorderLayout.SOUTH);
	}
	
}