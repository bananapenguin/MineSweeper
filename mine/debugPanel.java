package mine;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class debugPanel extends JFrame implements ActionListener{
	
	Container cnt;
	mainPanel panel;
	Field field;
	JTextArea ta;
	JButton btn;
	
	public debugPanel(mainPanel panel){
		cnt = getContentPane();
		cnt.setLayout(new BorderLayout());
		this.panel = panel;
		this.field = panel.getField();
		btn = new JButton("更新");
		ta = new JTextArea(30, 30);
		write();
		cnt.add(ta, BorderLayout.CENTER);
		cnt.add(btn, BorderLayout.SOUTH);
		btn.addActionListener(this);
		setSize(150, 400);
		setLocation(250, 0);
		setBackground(Color.pink);
		setVisible(true);
	}
	
	public void write(){
		String bomb = "";
		String state = "";
		for(int i=0; i<9; i++){
			for(int j=0; j<9; j++){
				if(field.isBomb(new Point(j,i))){
					bomb += "1 ";
				}else{
					bomb += "0 ";
				}
				state += field.getState(new Point(j,i))+" ";
			}
			bomb += "\n";
			state += "\n";
		}
		ta.append("bombの配置\n");
		ta.append(bomb);
		ta.append("各セルのstate\n");
		ta.append(state);
	}

	public void actionPerformed(ActionEvent e) {
		ta.setText("");
		write();
		
	}
}
