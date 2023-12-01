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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
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
	//왼쪽 좌표 이동
	Action left = new AbstractAction(LEFT) {
		@Override
		public void actionPerformed(ActionEvent e) {
			leftMove();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
		}
	}; 
	//오른쪽 좌표 이동
	private static final String RIGHT = "Right";
	Action right = new AbstractAction(RIGHT) {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	rightMove();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
	    }
	};
	//급속 하강
	public static final String DOWN = "Down";
	Action down = new AbstractAction(DOWN) {
		@Override
		public void actionPerformed(ActionEvent e) {
			steadyDown();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
		}
	};
	//회전
	public static final String ENTER = "Enter";
	Action enter = new AbstractAction(ENTER) {
		@Override
		public void actionPerformed(ActionEvent e) {
			changeDirection();
			ScoreBanner.dispScore.setText(String.valueOf(ScoreBanner.score));
			
		}
	};
	//시작하는 역할
	public static final String SPACE = "Space";
	Action space = new AbstractAction(SPACE) {
		@Override
		public void actionPerformed(ActionEvent e) {
			new Thread() {
				@Override
				public void run() {
					while(gameOver()) {
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
	//객체 배열로 판을 만들었으므로 객체로 받는다.
	final Point[][][] shape = {
			//일자
		{
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1) },
			{ new Point(1, 0), new Point(1, 1), new Point(1, 2), new Point(1, 3) }
		},
			// 역L
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
			// 정사각형
		{
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 0), new Point(1, 1) }
		},
			// 평행
		{
			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1) },
			{ new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) }
		},
			// ㅗ
		{
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(1, 2) },
			{ new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(1, 2) },
			{ new Point(1, 0), new Point(1, 1), new Point(2, 1), new Point(1, 2) }
		},
			// 역평행
		{
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) },
			{ new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1) },
			{ new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(0, 2) }
		}
	};
	//게임판 만들기
	public void makeTetrispad() {
		gamePad = new Color[14][20];
		for(int i = 0; i < 14; i++) {
			for(int j = 0; j < 20; j++) {
				if(i == 0 || j == 19 || i == 13) {
					gamePad[i][j] = Color.cyan;
				}
				else {
					gamePad[i][j] = Color.black;
				}
			}
		}
		makeTetrispieces();
	}
	//조각 만드는 작업
	public void makeTetrispieces() {
		pieceA = new Point(5, 0); 
		selectPieces =r.nextInt(6);
	}
	
	
	public boolean gameOver() {
		boolean b = true;
		for(int i = 1; i <= 12; i++) {
			if(gamePad[i][1] != Color.black) {
				JOptionPane.showMessageDialog(null, "게임 오버");
				b = false;
				break;
			}
		}
		return b;
	}

	@Override
	public void paintComponent(Graphics pc){
		//벽 색칠
		pc.fillRect(0, 0, 26*14, 26*20);
		for (int i = 0; i < 14; i++) {
			for (int j = 0; j < 20; j++) {
				pc.setColor(gamePad[i][j]);
				pc.fillRect(26*i, 26*j, 25, 25);
			}
		}
		spawn(pc);
	}
	//줄 지우는 작업
	public void deleterow(int row) {
		for(int j = row-1; j > 0; j--) {
			for(int i = 1; i <= 12; i++) {
				gamePad[i][j+1] = gamePad[i][j];
			}
		}
	}
	public void fixThePiece() {
		for(Point p : shape[selectPieces][direction]) {
			gamePad[(p.x + pieceA.x/*이동거리 + a위치*/)][(p.y + pieceA.y)] = shapeColor[selectPieces];
			
		}
		lineClear();
		makeTetrispieces();
		gameOver();
	}
	//꽉 찼으면 지우는 작업 + 점수 부여
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
		//변화점 적용
		repaint();
	}
	
	//까만건지 확인
	public boolean checkColor(int x, int y, int direction) {
		for (Point p : shape[selectPieces][direction]) {
			if (gamePad[p.x + x][p.y + y] != Color.BLACK) {
				return true;
			}
		}
		return false;
	}
	//블록생
	private void spawn(Graphics g) {
		g.setColor(shapeColor[selectPieces]);
		for(Point p : shape[selectPieces][direction]) {
			g.fillRect((p.x + pieceA.x) * 26, (p.y + pieceA.y-2) * 26, 25, 25);
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
	//좌 우 이동
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
	//아래로 쭉 하락
	public void steadyDown() {
		if(!checkColor(pieceA.x, pieceA.y + 1, direction)) {
			pieceA.y++;
		}
		else {
			fixThePiece();
		}
		repaint();
	}
	//방향 전환
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