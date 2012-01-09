package resources;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.File;

import javax.swing.JProgressBar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import java.awt.Color;

public class TransferDialog extends JDialog {
	
	private final static JProgressBar progressBar = new JProgressBar();
	private static final long serialVersionUID = 5754737025462078912L;
	private final JPanel contentPanel = new JPanel();
	private final JLabel[] animation = new JLabel[4];
	private final static JLabel animation0 = new JLabel("");
	private final static JLabel animation1 = new JLabel("");
	private final static JLabel animation2 = new JLabel("");
	private final static JLabel animation3 = new JLabel("");
	private final JLabel doctorIcon = new JLabel("");
	private final JLabel dropBoxIcon = new JLabel("");
	private static int animCounter = -1;
	
	/**
	 * Create the dialog.
	 */
	public TransferDialog(File f) {
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 741, 371);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);		
		JLabel transfering = new JLabel("Transfering files to Dropbox \r\nPLEASE WAIT and don't close the program!");
		transfering.setBounds(86, 11, 584, 99);
		transfering.setFont(new Font("Times New Roman", Font.BOLD, 18));
		contentPanel.add(transfering);
		progressBar.setBounds(10, 294, 708, 38);
		progressBar.setStringPainted(true);
		contentPanel.add(progressBar);
		doctorIcon.setIcon(new ImageIcon(TransferDialog.class.getResource("123.jpg")));
		doctorIcon.setBounds(21, 88, 160, 177);
		
		contentPanel.add(doctorIcon);
		dropBoxIcon.setIcon(new ImageIcon(TransferDialog.class.getResource("Networking.png")));
		dropBoxIcon.setBounds(526, 77, 128, 206);
		
		contentPanel.add(dropBoxIcon);
		animation0.setIcon(new ImageIcon(TransferDialog.class.getResource("Documents1.png")));
		animation0.setBounds(176, 134, 76, 109);
		animation[0] = animation0;
		animation[0].setVisible(false);
		contentPanel.add(animation0);

		animation1.setIcon(new ImageIcon(TransferDialog.class.getResource("Documents2.png")));
		animation1.setBounds(256, 88, 76, 109);
		animation[1] = animation1;
		animation[1].setVisible(false);
		contentPanel.add(animation1);
		
		animation2.setIcon(new ImageIcon(TransferDialog.class.getResource("Documents3.png")));
		animation2.setBounds(354, 77, 76, 109);
		animation[2] = animation2;
		animation[2].setVisible(false);
		contentPanel.add(animation2);
		
		animation3.setIcon(new ImageIcon(TransferDialog.class.getResource("Documents4.png")));
		animation3.setBounds(440, 99, 76, 109);
		animation[3] = animation3;
		animation[3].setVisible(false);
		contentPanel.add(animation3);
		Transfer transfer = new Transfer(f , this);
		transfer.addPropertyChangeListener(new PropertyChangeListener() {			
			@Override
			public void propertyChange(PropertyChangeEvent ev) {
				if(ev.getPropertyName().equals("progress"))
				{
					int newValue = (Integer)ev.getNewValue();
					progressBar.setValue(newValue);
				}
				
			}
		});
		transfer.execute();		
	}
	protected JProgressBar getProgressBar()
	{
		return progressBar;
	}
	
	protected void setPBar(int n, boolean done)
	{
		progressBar.setValue(n);
		done = true;
	}
	protected void setIcon()
	{
		if(animCounter>-1)
		{
			animation[(int)(animCounter % 4)].setVisible(false);
		}
		animCounter++;
		animation[(int)(animCounter % 4)].setVisible(true);		
	}
}
