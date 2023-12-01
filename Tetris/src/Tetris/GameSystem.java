package Tetris;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import java.util.Random;

public class GameSystem extends JPanel{
	private static final long serialVersionUID = -1454276759458774671L;
	int xpos = 0;
	int ypos = 0;
	int direction = 0;
	private static final String LEFT = "Left";
	//���� ��ǥ �̵�
	Action left = new AbstractAction(LEFT) {
		@Override
		public void actionPerformed(ActionEvent e) {
			leftMove();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
		}
	};
	//������ ��ǥ �̵�
	private static final String RIGHT = "Right";
	Action right = new AbstractAction(RIGHT) {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	rightMove();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
	    }
	};
	//�޼� �ϰ�
	public static final String DOWN = "Down";
	Action down = new AbstractAction(DOWN) {
		@Override
		public void actionPerformed(ActionEvent e) {
			steadyDown();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
		}
	};
	//ȸ��
	public static final String ENTER = "Enter";
	Action enter = new AbstractAction(ENTER) {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeDirection();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
			
		}
	};
	//�����ϴ� ����
	public static final String SPACE = "Space";
	Action space = new AbstractAction(SPACE) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread() {
				@Override
				public void run() {
					while(true) {
						try {
							Thread.sleep(500);
							steadyDown();
						}catch(InterruptedException e) {}
					}
				}
			}.start();
		}
	};
	
	Random r = new Random();

	Point pieceA;
	private ArrayList<Integer> nextPieces = new ArrayList<Integer>();
	int selectPieces;
	Color[][] nextPiece;
	Color[][] gamePad;  
	Color[] shapeColor = {Color.blue, Color.red, Color.green, Color.yellow, Color.LIGHT_GRAY, 
			Color.ORANGE, Color.pink};
	//��ü �迭�� ���� ��������Ƿ� ��ü�� �޴´�.
	final Point[][][] shape = {
			//����
		{
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
		},
			// ��L
		{
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 0) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 0) }
		},
			// L
		{
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(2, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(0, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 0) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(2, 0) }
		},
			// ���簢��
		{
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
		},
			// ����
		{
			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
		},
			// ��
		{
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
		},
			// ������
		{
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
		}
	};
	//������ �����
	public void makeTetrispad() {
		gamePad = new Color[14][20];
		for(int i = 0; i < 14; i++) {
			for(int j = 0; j < 20; j++) {
				if(i == 0 || i == 19 || j == 19 || i == 13) {
					gamePad[i][j] = Color.cyan;
				}
				else {
					gamePad[i][j] = Color.black;
				}
			}
		}
		makeTetrispieces();
	}
	
	//���� ����� �۾�
	public void makeTetrispieces() {
		pieceA = new Point(5, 0); 
		selectPieces =r.nextInt(6);
	}
	
	
	public boolean gameOver() {
		int d = 0;
		for(int i = 0; i < 12; i++) {
			if(gamePad[6][i] != Color.black) {
				d++;
				if(d >= 4) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void paintComponent(Graphics pc){
		//�� ��ĥ
		pc.fillRect(0, 0, 26*14, 26*20);
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 20; j++) {
				pc.setColor(gamePad[i][j]);
				pc.fillRect(26*i, 26*j, 25, 25);
			}
		}
		spawn(pc);
	}
	//�� ����� �۾�
	public void deleterow(int row) {
		for(int j = row-1; j > 0; j--) {
			for(int i = 1; i <= 12; i++) {
				gamePad[i][j+1] = gamePad[i][j];
			}
		}
	}
	public void fixThePiece() {
		for(Point p : shape[selectPieces][direction]) {
			gamePad[(p.x + pieceA.x/*�̵��Ÿ� + a��ġ*/)][(p.y + pieceA.y)] = shapeColor[selectPieces];
			
		}
		lineClear();
		makeTetrispieces();
		gameOver();
	}
	//�� á���� ����� �۾� + ���� �ο�
	public void lineClear() {
		int a = 0;
		for(int j = 18; j >= 0; j--) {
			for(int i = 1; i <= 12; i++) {	
				if(gamePad[i][j] != Color.black) {
					a++;
				}
			}
			if(a == 12) {
				deleterow(j);
				j += 1;
				ScoreBanner.score += 10;
				a = 0;
			}
			else {
				a = 0;
			}
		}
		//��ȭ�� ����
		repaint();
	}
	
	//����� Ȯ��
	public boolean checkColor(int x, int y, int direction) {
		for (Point p : shape[selectPieces][direction]) {
			if (gamePad[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}
	//��ϻ�
	private void spawn(Graphics g) {
		g.setColor(shapeColor[selectPieces]);
		for(Point p : shape[selectPieces][direction]) {
			g.fillRect((p.x + pieceA.x) * 26, (p.y + pieceA.y) * 26, 25, 25);
		}
	}

	public void nextPiece(Graphics g) {
		nextPiece = new Color[5][4];
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 4; j++) {
				nextPiece[i][j] = Color.white;
			}
		}
		for(Point p : shape[selectPieces][direction]) {
			g.fillRect(390, 675, 250, 250);
		}
	}
	//�� �� �̵�
	public void rightMove() {
		if(!checkColor(pieceA.x + 1, pieceA.y, direction)) {
			pieceA.x++;
		}
		repaint();
	}
	
	public void leftMove() {
		if(!checkColor(pieceA.x - 1, pieceA.y, direction)) {
			pieceA.x--;
		}
		repaint();
	}
	//�Ʒ��� �� �϶�
	public void steadyDown() {
		if(!checkColor(pieceA.x, pieceA.y + 1, direction)) {
			pieceA.y++;
		}
		else {
			fixThePiece();
		}
		repaint();
	}
	//���� ��ȯ
	public void changeDirection() {
		
		direction++;
		if(direction > 3) {
			direction = direction % 4;
		}
		repaint();
		
	}
	public GameSystem() {
		
		this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
	    this.getActionMap().put(LEFT, left);
	    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
	    this.getActionMap().put(RIGHT, right);
	    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), DOWN);
	    this.getActionMap().put(DOWN, down);
	    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ENTER);
	    this.getActionMap().put(ENTER, enter);
	    this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);
	    this.getActionMap().put(SPACE, space);
	    
	    
	}
}