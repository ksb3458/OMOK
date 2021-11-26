
// JavaObjClientView.java ObjecStram 기반 Client
//실질적인 채팅 창
import java.awt.Button;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameRoom extends JPanel {
	
	private String RoomID, RoomName, Look, Secret, Password, UserName;
	private JPanel panel;
	private JButton enterBtn;
	public JLabel makeUser;
	
	public GameRoom(String roomid, String username, String roomname, String look, String secret, String password)  {
		RoomID = roomid;
		UserName = username;
		setPanel(new JPanel());
		getPanel().setBackground(SystemColor.info);
		
		setBounds(100, 100, 380, 156);
		getPanel().setLayout(null);
		
		JLabel nameLabel = new JLabel("");
		nameLabel.setBounds(12, 10, 177, 43);
		getPanel().add(nameLabel);
		
		makeUser = new JLabel(UserName);
		makeUser.setBounds(201, 10, 57, 43);
		getPanel().add(makeUser);
		
		setEnterBtn(new JButton("입장하기"));
		getEnterBtn().setBounds(305, 10, 88, 43);
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