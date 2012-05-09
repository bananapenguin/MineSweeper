package mine;

public class Timer extends Thread {

	private mainPanel panel;
	private int time;
	private boolean start;

	Timer(mainPanel panel){
		this.panel = panel;
		time = 0;
		start = false;
		start();
	}
	
	//タイムをカウントする
	public void run(){
		long startTime,estimatedTime=0;
		try {
			while(true){
				while(start){
					startTime = System.nanoTime();
					time++;
					if(time>999) time=999;
					panel.drawTime(time);
					panel.repaint();
					estimatedTime = System.nanoTime() - startTime;
					sleep(1000-estimatedTime/1000000L);
				}
				//何か処理がないとタイマーがちゃんと動かない
				sleep(0);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//状態をリセット
	void reset(){
		start = false;
		time = 0;
	}

	//タイマーがスタートしているかどうか
	boolean isStart() {
		return start;
	}

	//タイマーをスタートさせる
	void setStart(boolean start) {
		this.start = start;
	}
}
