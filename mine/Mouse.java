package mine;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class Mouse extends MouseAdapter{

	private mainPanel panel;
	private Field field;
	private Point nowCell;

	public Mouse(mainPanel panel){
		this.panel = panel;
		field = panel.getField();
		nowCell = new Point();
	}

	//カーソルが9*9のマスの中にあるかどうか
	private boolean isCellField(Point p){
		if(p.x>=12&&p.x<155&&p.y>=55&&p.y<198){
			return true;
		}else{
			return false;
		}
	}

	//カーソルがニコマークの中にあるかどうか
	private boolean isNikoField(Point p){
		if(p.x>=72&&p.x<95&&p.y>=16&&p.y<39){
			return true;
		}else{
			return false;
		}
	}

	//カーソルがクリックしたセルからドラックして動いていないかどうか
	private boolean isNowCell(Point p){
		Point tmp = getCellPoint(p);
		if(nowCell.equals(tmp)){
			return true;
		}else{
			return false;
		}
	}

	/*
	 * 現在の座標のcellの番号(0,0)〜(8,8)を返す
	 * 座標じゃないので注意
	 * @return cellの番号(0,0)〜(8,8)
	 * */
	private Point getCellPoint(Point p){
		int x = (p.x - 12)/16;
		int y = (p.y - 55)/16;
		return new Point(x, y);
	}

	//マウスを押したとき
	public void mousePressed(MouseEvent e) {
		if(panel.isGameOver()){//ゲームオーバーのとき
			if(isNikoField(e.getPoint()))
				panel.drawNiko(1);
		}else{//ゲーム中のとき
			if(SwingUtilities.isLeftMouseButton(e)){//左クリック
				//Niko
				if(isNikoField(e.getPoint())){
					panel.drawNiko(1);
				}else{
					panel.drawNiko(2);
				}
				//cell
				if(isCellField(e.getPoint())){
					Point p = getCellPoint(e.getPoint());
					nowCell.setLocation(p);
					if(field.getState(p)==Field.DEFAULT){
						panel.drawCell(p, Field.PUSHED);
					}else if(field.getState(p)==Field.HATENA){
						panel.drawCell(p, Field.HATENA_PUSHED);
					}
				}
			}
		}
		panel.repaint();
	}

	//マウスをドラッグしたとき
	public void mouseDragged(MouseEvent e) {
		if(panel.isGameOver()){//ゲームオーバーのとき
			if(isNikoField(e.getPoint())){
				panel.drawNiko(1);
			}else{
				if(field.isClear())
					panel.drawNiko(4);
				else
					panel.drawNiko(3);
			}
		}else{//ゲーム中のとき
			if(SwingUtilities.isLeftMouseButton(e)){//左クリック
				if(isNikoField(e.getPoint())){
					panel.drawNiko(1);
				}else{
					panel.drawNiko(2);
				}

				if(isCellField(e.getPoint())){
					if(!isNowCell(e.getPoint())){
						if(field.getState(nowCell)==Field.DEFAULT){
							panel.drawCell(nowCell, Field.DEFAULT);
						}else if(field.getState(nowCell)==Field.HATENA){
							panel.drawCell(nowCell, Field.HATENA);
						}
						Point p = getCellPoint(e.getPoint());
						nowCell.setLocation(p);
						if(field.getState(nowCell)==Field.DEFAULT){
							panel.drawCell(nowCell, Field.PUSHED);
						}else if(field.getState(nowCell)==Field.HATENA){
							panel.drawCell(nowCell, Field.HATENA_PUSHED);
						}
					}
				}else{
					if(field.getState(nowCell)==Field.DEFAULT){
						panel.drawCell(nowCell, Field.DEFAULT);
					}else if(field.getState(nowCell)==Field.HATENA){
						panel.drawCell(nowCell, Field.HATENA);
					}
				}
			}
		}
		panel.repaint();
	}

	//マウスを放したとき
	public void mouseReleased(MouseEvent e) {
		if(panel.isGameOver()){
			if(isNikoField(e.getPoint())){
				panel.drawNiko(0);
				panel.reset();
			}
		}else{
			if (SwingUtilities.isRightMouseButton(e)) {//右クリック
				Point p = getCellPoint(e.getPoint());
				if(field.getState(p)==Field.DEFAULT){
					field.setState(p, Field.FLAG);
					panel.drawCell(p, Field.FLAG);
					int num = field.getBombNum()-1;
					panel.drawNum(num);
					field.setBombNum(num);
				}else if(field.getState(p)==Field.FLAG){
					field.setState(p, Field.HATENA);
					panel.drawCell(p, Field.HATENA);
					int num = field.getBombNum()+1;
					panel.drawNum(num);
					field.setBombNum(num);
				}else if(field.getState(p)==Field.HATENA){
					field.setState(p, Field.DEFAULT);
					panel.drawCell(p, Field.DEFAULT);
				}
			}else if(SwingUtilities.isLeftMouseButton(e)){//左クリック
				panel.drawNiko(0);
				if(isCellField(e.getPoint())){
					if(!panel.isGameStart())
						panel.Start();
					Point p = getCellPoint(e.getPoint());
					int state = field.getState(p);
					if(state==Field.DEFAULT || state==Field.HATENA)
						field.checkBomb(p);
				}else if(isNikoField(e.getPoint())){
					panel.reset();
				}
			}
			if(field.isClear()){//クリアしたとき
				panel.drawNiko(4);
				field.flagOpen();
				panel.gameOver();
			}
		}
		panel.repaint();
	}
}