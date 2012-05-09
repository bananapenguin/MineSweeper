package mine;

import java.awt.Point;

public class Field {

	//各セルの状態を示す定数
	public static final int DEFAULT = 0;
	public static final int PUSHED = 1;
	public static final int FLAG = 2;
	public static final int HATENA = 3;
	public static final int HATENA_PUSHED = 4;
	public static final int BOMB_RED = 5;
	public static final int BOMB = 6;
	public static final int BOMB_MISSED = 7;
	public static final int AROUND_1 = 8;
	public static final int AROUND_2 = 9;
	public static final int AROUND_3 = 10;
	public static final int AROUND_4 = 11;
	public static final int AROUND_5 = 12;
	public static final int AROUND_6 = 13;
	public static final int AROUND_7 = 14;
	public static final int AROUND_8 = 15;

	//爆弾の状態を示す配列
	private int[] state = new int[81];
	//爆弾の有る無しを示す配列
	private int[] bomb = new int[81];
	private mainPanel panel;
	private int bombNum;

	public Field(mainPanel panel){
		this.panel = panel;
		reset();
	}
	
	//クリアしたかどうかを返す
	public boolean isClear() {
		int tmp = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				switch (state[i*9+j]) {
				case Field.DEFAULT:
					tmp++;
					break;
				case Field.FLAG:
					tmp++;
					break;
				case Field.HATENA:
					tmp++;
					break;
				default:
					break;
				}
				if (tmp > 10) {
					return false;
				}
			}
		}
		if (tmp == 10) {
			return true;
		}
		return false;
	}
	
	//初期状態にリセットをする
	public void reset(){
		bombNum = 10;
		int tmp, t;

		for (int i = 0; i < 81; i++) {
			state[i] = DEFAULT;
			if (i < 10) bomb[i] = 1;
			else bomb[i] = 0;
		}
		for (int i = 81 - 1; i > 0; i--) {
			t = (int) (Math.random() * i);
			tmp = bomb[i];
			bomb[i] = bomb[t];
			bomb[t] = tmp;
		}
	}

	//失敗してゲームオーバーになったときに
	//爆弾を一斉に表示するメソッド
	public void bombOpen(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				int num = i*9+j;
				if(bomb[num]==1){
					if(state[num]==DEFAULT){
						state[num]=BOMB;
						panel.drawCell(j, i, BOMB);
					}
				}else{
					if(state[num]==FLAG){
						state[num]=BOMB_MISSED;
						panel.drawCell(j, i, BOMB_MISSED);
					}
				}
			}
		}
	}
	
	//ゲームをクリアしたときに
	//一斉に爆弾の位置にフラグを立てる
	public void flagOpen(){
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				int num = i*9+j;
				if(bomb[num]==1){
					if(state[num]==DEFAULT || state[num]==HATENA){
						state[num]=FLAG;
						panel.drawCell(j, i, FLAG);
					}
				}
			}
		}
	}

	//そのセルに爆弾があるかどうか
	public boolean isBomb(Point p){
		if(bomb[p.x+p.y*9]==1) return true;
		else return false;
	}

	//爆弾表示が０のマスを開いたときの処理
	public void zeroOpen(Point p){
		int x = p.x;
		int y = p.y;
		Point tmp = new Point();
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				tmp.setLocation(x+j, y+i);
				if ((y+i>=0 && y+i<=8 && x+j>=0 && x+j<=8)&& !isBomb(tmp)) {
					if (getState(tmp)==DEFAULT) {
						checkBomb(new Point(x+j, y+i));
					}
				}
			}
		}

	}

	//クリックされたセルの処理
	public void checkBomb(Point p) {
		if(getState(p)!=FLAG){
			if(isBomb(p)){
				panel.drawNiko(3);
				panel.drawCell(p, Field.BOMB_RED);
				setState(p, Field.BOMB_RED);
				bombOpen();
				panel.gameOver();
			}else{
				if(aroundBomb(p)==0){
					panel.drawCell(p, Field.PUSHED);
					setState(p, Field.PUSHED);
					zeroOpen(p);
				}else{
					panel.drawCell(p, aroundBomb(p)+7);
					setState(p, aroundBomb(p)+7);
				}
			}
		}
	}

	//そのマスの周りに爆弾がいくつあるか
	public int aroundBomb(Point p){
		int b = 0;
		int x = p.x;
		int y = p.y;
		Point tmp = new Point();
		for (int i=-1; i<=1; i++) {
			for (int j=-1; j<=1; j++) {
				tmp.setLocation(x+j, y+i);
				if ((y+i>=0 && y+i<=8 && x+j>=0 && x+j<=8)&& isBomb(tmp)) {
					b++;
				}
			}
		}
		return b;
	}

	//爆弾の位置を示す。デバッグ用
	public void show(){
		System.out.println("---Show Bomb field---");
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				System.out.print(bomb[j+(i*9)] + " ");
			}
			System.out.println();
		}
	}

	//指定されたセルの状態を返す
	public int getState(Point p){
		return state[p.x+p.y*9];
	}

	//指定されたセルの状態を設定する
	public void setState(Point p, int state){
		this.state[p.x+p.y*9] = state;
	}

	//残りの爆弾の数を返す
	public int getBombNum() {
		return bombNum;
	}

	//残りの爆弾の数を設定する
	public void setBombNum(int bombNum) {
		this.bombNum = bombNum;
	}

}
