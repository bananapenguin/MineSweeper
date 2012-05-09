package mine;
import java.awt.Container;
import java.awt.Image;

import javax.swing.JApplet;
/*
<applet code="mine.MineSweeper.class" width="164" height="207">
</applet>
 */
public class MineSweeper extends JApplet{

	private Container cnt;
	private mainPanel panel;
	private Image buffer;

	public void init(){
		cnt = getContentPane();
		//ダブルバッファ用のイメージバッファを作成
		buffer = createImage(164, 207);
		//メインのパネルを作成
		panel = new mainPanel(buffer);
		cnt.add(panel);
		setSize(164, 207);
	}
}
