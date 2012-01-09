package resources;

//import java.awt.BorderLayout;
//import java.awt.EventQueue;

import javax.swing.JList;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
//import java.awt.Dimension;


public class HandlerView extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5501052552433961274L;
	private JButton btnSelectAll;
	private JButton btnClearAll;
	private JButton btnArchive;
	private JButton btnDelete;
	private JButton btnCancel;
	private JList<String> list;
	private JProgressBar progressBar;
	private static MainProgram mainUnit;
    private static DrCleanerView dr;
	
	/**
	 * Launch the application.
	 */
	
	@SuppressWarnings("serial")
	public HandlerView( DrCleanerView d) {
		
		super(null, ModalityType.APPLICATION_MODAL);
		setSize(new Dimension(898, 640));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		getContentPane().setLayout(null);
		
		list = new JList<String>();
		list.setBounds(10, 11, 616, 467);
		list.setSelectionModel(new DefaultListSelectionModel() 
		{    
		@Override          
		    public void setSelectionInterval(int index0, int index1) 
		    {         
		        if(super.isSelectedIndex(index0)) 
		            {             
		                super.removeSelectionInterval(index0, index1);
		            }         
		        else 
		            {            
		                super.addSelectionInterval(index0, index1); 
		            }     
		    } 
		});
		getContentPane().add(list);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 616, 467);
		getContentPane().add(scrollPane);
		
		btnSelectAll = new JButton("Select All");
		btnSelectAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectButtonActionPerformed(e);
			}
		});
		btnSelectAll.setBounds(10, 489, 139, 67);
		getContentPane().add(btnSelectAll);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 567, 860, 35);
		progressBar.setIndeterminate(true);
		UIManager.put("progressBar.repaintInterval", new Integer(50));
		UIManager.put("progressBar.cycleTime", new Integer(500));
		getContentPane().add(progressBar);
		
		btnClearAll = new JButton("Clear All");
		btnClearAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearButtonActionPerformed(e);
			}
		});
		btnClearAll.setBounds(487, 489, 139, 67);
		getContentPane().add(btnClearAll);
		
		btnArchive = new JButton("Archive");
		btnArchive.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				archiveButtonActionPerformed(e);
			}
		});
		btnArchive.setIcon(new ImageIcon(HandlerView.class.getResource("/resources/document-archive-icon.png")));
		btnArchive.setForeground(Color.BLUE);
		btnArchive.setFont(new Font("Teen", Font.BOLD, 16));
		btnArchive.setBounds(636, 11, 234, 110);
		getContentPane().add(btnArchive);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteButtonActionPerformed(e);
			}
		});
		btnDelete.setIcon(new ImageIcon(HandlerView.class.getResource("/resources/Delete-icon.png")));
		btnDelete.setForeground(Color.RED);
		btnDelete.setFont(new Font("Teen", Font.BOLD, 16));
		btnDelete.setBounds(636, 167, 234, 110);
		getContentPane().add(btnDelete);
		
		btnCancel = new JButton("");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelButtonActionPerformed(e);
			}
		});
		btnCancel.setIcon(new ImageIcon(HandlerView.class.getResource("/resources/cancel-icon.png")));
		btnCancel.setBounds(636, 443, 234, 113);
		getContentPane().add(btnCancel);
		dr = d;
		disableButtons();
		initializeMainProgram();	
	}
	
	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
		mainUnit.cancel(true);
	    this.dispose();
	}                                            

	  
	private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             	
		int s = list.getModel().getSize();	    
		list.addSelectionInterval(0, s);	    
	}                                           
	
	
	private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {                                            	
		list.clearSelection();	    
	}                                           
	
	private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             	
		mainUnit.deleteFiles(list);	    
	} 
	
	private void archiveButtonActionPerformed(java.awt.event.ActionEvent evt){
		mainUnit.archiveFiles(list);
	}
	
	private void initializeMainProgram()
    {
        mainUnit = new MainProgram(dr.getNumOfMonth(), dr.getRoot(), this);                        
        mainUnit.set_word(dr.getWordStatus());                            
        mainUnit.set_exel(dr.getExelStatus());                
        mainUnit.set_pdf(dr.getPDFStatus());    
        mainUnit.set_pow(dr.getPowStatus());
        mainUnit.execute();
    }
    
    protected JButton getCancelButton()
    {
        return btnCancel;
    }
    
    protected JList<String> getList()
    {
        return list;
    }
    
    protected JProgressBar getBar()
    {
        return progressBar;
    }
	
	protected final void enableButtons()
    {
        btnArchive.setEnabled(true);
        btnDelete.setEnabled(true);
        btnClearAll.setEnabled(true);
        btnSelectAll.setEnabled(true);
    }
    
    protected final void disableButtons()
    {
        btnArchive.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClearAll.setEnabled(false);
        btnSelectAll.setEnabled(false);
    }
    
    protected JList<String> getJList()
    {
        return list;
    }
}
