package Tetris;
import java.util.*;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.crypto.KeyAgreement;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import javax.swing.JTree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
//keyListener, ActionListener ����
public class gameBoard extends JFrame{
	private static final long serialVersionUID = 1L;

	GameSystem gs = new GameSystem();
	public gameBoard() {
		//setResizable(false);
		setTitle("테트리스");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(175, 175, 380, 645);
		Container con = getContentPane();
		con.setLayout(new BorderLayout(5,5));
		GameSystem gs = new GameSystem();
		gs.makeTetrispad();
		con.add(gs);
		ScoreBanner sc = new ScoreBanner();
		con.add(sc, BorderLayout.SOUTH);
	} 
	
	public static void main(String[] args) {
		gameBoard frame = new gameBoard();
		frame.setVisible(true); 
	}
}
