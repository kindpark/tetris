package Tetris;

public class Control extends Thread{
	GameSystem gs = new GameSystem();
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(100);
				gs.steadyDown();
			}catch(InterruptedException e) {
				
			}
		}
	}
}