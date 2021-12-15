
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.Button;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class GameRoom extends JPanel {
	
	private String RoomID, RoomName, Look, Secret, Password, UserName;
	private JPanel panel;
	private JButton enterBtn;
	private Color bgColor = new Color(0xF6D1CC);
	private Color btnColor = new Color(0xE3E1E0);
	private Color textColor = new Color(0x424242);
	public JLabel makeUser;
	
	public GameRoom(String roomid, String username, String roomname, String look, String secret, String password)  {
		RoomID = roomid;
		UserName = username;
		setPanel(new JPanel());
		getPanel().setBackground(bgColor);
		
		setBounds(100, 100, 380, 156);
		getPanel().setLayout(null);
		
		JLabel nameLabel = new JLabel("");
		nameLabel.setBounds(12, 12, 117, 43);
		nameLabel.setForeground(textColor);
		getPanel().add(nameLabel);
		
		makeUser = new JLabel(UserName);
		makeUser.setForeground(textColor);
		makeUser.setBounds(154, 12, 57, 43);
		getPanel().add(makeUser);
		
		setEnterBtn(new JButton("입장하기"));
		//enterBtn.setBorderPainted(false);
		enterBtn.setBackground(btnColor);
		enterBtn.setForeground(textColor);
		getEnterBtn().setBounds(281, 12, 88, 43);
		getPanel().add(getEnterBtn());
		
		RoomName = roomname;
		nameLabel.setText("[" + RoomName + "]");
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JButton getEnterBtn() {
		return enterBtn;
	}

	public void setEnterBtn(JButton enterBtn) {
		this.enterBtn = enterBtn;
	}
	
	
}