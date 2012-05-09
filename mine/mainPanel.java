package mine;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class mainPanel extends JPanel{

	private Image buffer;
	private Graphics gbuf;
	private Image back, cell, niko, num;
	private Field field;
	private Mouse mouse;
	private Timer timer;
	private boolean gameover = true;

	public mainPanel(Image buffer){
		//イメージを読み込む
		back = new ImageIcon(getClass().getResource("/img/back.png")).getImage();
		cell = new ImageIcon(getClass().getResource("/img/cell.png")).getImage();
		niko = new ImageIcon(getClass().getResource("/img/niko.png")).getImage();
		num = new ImageIcon(getClass().getResource("/img/num.png")).getImage();
		//イメージバッファの登録
		this.buffer = buffer;
		gbuf = this.buffer.getGraphics();
		//バッファに書き込む
		draw();
		//各種メンバの生成
		field = new Field(this);
		mouse = new Mouse(this);
		timer = new Timer(this);
		//リスナに登録
		addMouseListener(mouse);
		addMouseMotionListener(mouse);
		//初期状態にリセット
		reset();
	}

	public Field getField(){
		return field;
	}
	
	//各状態を初期状態にリセット
	void reset(){
		gameover = false;
		field.reset();
		timer.reset();
		draw();
		field.show();
	}
	
	//ゲームがスタートしているかどうかを返す
	boolean isGameStart(){
		if(!timer.isStart()&&!isGameOver())
			return false;
		else 
			return true;
	}
	
	//ゲームをスタートさせる
	void Start(){
		timer.setStart(true);
	}
	
	//ゲームオーバーかどうかを返す
	boolean isGameOver(){
		return gameover;
	}
	
	//ゲームオーバーにする
	void gameOver(){
		gameover = true;
		timer.setStart(false);
	}

	//指定したセルに指定した番号の画像を書き加える
	void drawCell(int x, int y, int n){
		int X = 12 + (x*16);
		int Y = 55 + (y*16);
		gbuf.drawImage(cell, X, Y, X+16, Y+16, 16*n, 0, 16*(n+1), 16, this);
	}
	
	//上記のメソッドと同様だが位置指定にPointクラスを使用する
	void drawCell(Point p, int n){
		int X = 12 + (p.x*16);
		int Y = 55 + (p.y*16);
		gbuf.drawImage(cell, X, Y, X+16, Y+16, 16*n, 0, 16*(n+1), 16, this);
	}

	//ニコマークをバッファに書き込む
	void drawNiko(int n){
		gbuf.drawImage(niko, 72, 16, 96, 40, 24*n, 0, 24*(n+1), 24, this);
	}

	//残りの爆弾をバッファに書き込む
	void drawNum(int n){
		int n1 = n/100;
		int n2 = n%100/10;
		int n3 = n%10;
		if(n<0){
			n1 = 10;
			n2 = (-1*n)%100/10;
			n3 = (-1*n)%10;
		}
		gbuf.drawImage(num, 17, 16, 30, 39, 13*n1, 0, 13*(n1+1), 23, this);
		gbuf.drawImage(num, 30, 16, 43, 39, 13*n2, 0, 13*(n2+1), 23, this);
		gbuf.drawImage(num, 43, 16, 56, 39, 13*n3, 0, 13*(n3+1), 23, this);
	}
	
	//タイマーの時刻をバッファに書き込む
	void drawTime(int time){
		int n1 = time/100;
		int n2 = time%100/10;
		int n3 = time%10;
		gbuf.drawImage(num, 110, 16, 123, 39, 13*n1, 0, 13*(n1+1), 23, this);
		gbuf.drawImage(num, 123, 16, 136, 39, 13*n2, 0, 13*(n2+1), 23, this);
		gbuf.drawImage(num, 136, 16, 149, 39, 13*n3, 0, 13*(n3+1), 23, this);
	}

	//初期状態を書き込む
	private void draw(){
		gbuf.drawImage(back, 0, 0, this);
		drawNiko(0);
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				drawCell(j, i, 0);
			}
		}
		drawNum(10);
		drawTime(0);
	}

	//バッファを表に書き出す
	public void paint(Graphics g){
		g.drawImage(buffer, 0, 0, this);
	}

	//ちらつきを防ぐためのメソッド
	public void update(Graphics g){
		paint(g);
	}
}