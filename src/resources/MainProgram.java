package resources;


import java.io.*;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
//import java.util.Random;
//import java.util.Vector;
import javax.swing.SwingWorker;
import java.util.concurrent.ExecutionException;
//import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;



/**
 *
 * @author Alexander Artyomov
 */
public class MainProgram extends SwingWorker<String[], String> {    	
    private int _numOfMonth;
    private boolean _word,_exel,_pdf,_pow;
    private File _source;
    private ArrayList<File> _listOfFiles;
    private JList<String> list;
    //private final JButton cancelButton;
    private final JProgressBar progress;
    private final HandlerView handlerFrame;
            
    public MainProgram(int num, File source, HandlerView hr)
    {
        _listOfFiles = new ArrayList<File>();
        _numOfMonth = num;	
        _source = source;
        /*TODO if added only for running UnitTest - should be without if*/
        if(hr != null){       
        	list = hr.getList();
        	progress = hr.getBar();       
        	handlerFrame = hr;
        }
        else
        {
        	list = null;
        	progress = null;
        	handlerFrame = null;
        }
        
        
    }

    public int get_numOfMonth() {
        return _numOfMonth;
    }
    
    protected ArrayList<File> getListOfFiles()
    {
        if(!_listOfFiles.isEmpty())    
            return _listOfFiles;
        return null;
    }

    public void set_numOfMonth(int numOfMonth) {
        _numOfMonth = numOfMonth;
    }

    public boolean is_word() {
        return _word;
    }

    public void set_word(boolean word) {
        _word = word;
    }

    public boolean is_exel() {
        return _exel;	
    }

    public void set_exel(boolean exel) {
        _exel = exel;
    }

    public boolean is_pdf() {
        return _pdf;	
    }

    public void set_pdf(boolean pdf) {
        _pdf = pdf;	
    }

    public boolean is_pow() {
        return _pow;	
    }

    public void set_pow(boolean pow) {
        _pow = pow;	
    }

    public File get_source() {
        return _source;	
    }

    public void set_source(File source) {
        _source = source;	
    }
   
    @SuppressWarnings({ "unchecked", "serial", "rawtypes" })
	protected String[] doInBackground()
    {    
        handlerFrame.disableButtons();
        list.setModel(new javax.swing.AbstractListModel() {  
            String [] strings = {"Searching..."};
            public int getSize() {return 1; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        if(isCancelled())
        {
            list.setModel(new javax.swing.AbstractListModel() {             
                String [] strings = {"Search operation was canceled"};            
                public int getSize() {return 1; }            
                public Object getElementAt(int i) { return strings[i]; }
        });
            return fileList();
        }
        else
        {
            list.setEnabled(false);      
            searchDirectory(_source);        
            return fileList();
        }
        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked", "serial" })
	@Override
    protected void done()
    {  
        progress.setIndeterminate(false);
        if(!isCancelled())
        {
	        try               
	        {    
	            if(!_listOfFiles.isEmpty())
	            {
	                handlerFrame.enableButtons();                        
	                list.setModel(new javax.swing.AbstractListModel() {            
	                    String [] strings = get();                                
	                    public int getSize() { return strings.length; }                                
	                    public Object getElementAt(int i) { return strings[i]; }                                
	                }); 
	                list.setEnabled(true);
	            }
	            else
	            {
	                list.setModel(new javax.swing.AbstractListModel() {                  
	                   String [] strings = {"No Files Were Found"};                
	                   public int getSize() {return 1; }            
	                   public Object getElementAt(int i) { return strings[i]; }});               
	                JOptionPane.showMessageDialog(handlerFrame,                                                         
	                        "No Files Were Found", "DrCleaner",                                                                           
	                        JOptionPane.INFORMATION_MESSAGE); 
	                handlerFrame.dispose();
	            }
	        }                
	        catch(InterruptedException ex)                
	        {        
	            JOptionPane.showMessageDialog(null,             
	                    "Interupted while waiting for results", "Error",                                        
	                    JOptionPane.ERROR_MESSAGE);                
	        }                       
	        catch(ExecutionException ex)                
	        {        
	            JOptionPane.showMessageDialog(null,             
	                    "Error encountered while performing search", "Error",                                        
	                    JOptionPane.ERROR_MESSAGE);                    
	        } 
        }
        else
        	handlerFrame.dispose();
    }
    
    /* TODO Should be private changed to public in order to perform a Unit test */
    public void searchDirectory(File source)
    {
        File[] listOfFiles = source.listFiles();
         
         if(listOfFiles != null){
             for (int i = 0; i < listOfFiles.length; i++)  
             {
                 if (listOfFiles[i].isFile())   
                 {  
                     if(isFileSearched(listOfFiles[i]))
                         _listOfFiles.add(listOfFiles[i]); 
                 }
                 else
                 {
                     if(listOfFiles[i].isDirectory() && !listOfFiles[i].isHidden())
                         searchDirectory(listOfFiles[i]);
                 }
             }
         }
    }
    
    //Returns true if current file is in right type and 
    //wasn't used more than _numOfMonth
    private boolean isFileSearched(File file)
    {
        Path p = file.toPath();   
        long cutoff = System.currentTimeMillis() - ((long)_numOfMonth * 30 * 24 * 60
                * 60 * 1000);  
        try
        {    
            FileTime time = (FileTime)Files.getAttribute(p,            
                    "lastAccessTime" , LinkOption.NOFOLLOW_LINKS);                    
            long lastAccessed = time.toMillis();
            if(lastAccessed<=cutoff && file.isHidden() == false)
            {       
                if(_word)      
                    if( file.getName().endsWith(".doc")||file.getName().endsWith(".docx")     
                            ||file.getName().endsWith(".DOC")||file.getName().endsWith(".DOCX"))
                        return true;
                if(_exel)      
                    if( file.getName().endsWith(".xls")||file.getName().endsWith(".xlsx")     
                            ||file.getName().endsWith(".XLS")||file.getName().endsWith(".XLSX"))
                        return true;
                if(_pow)      
                    if( file.getName().endsWith(".ppt")||file.getName().endsWith(".pptx")     
                            ||file.getName().endsWith(".PPT")||file.getName().endsWith(".PPTX"))
                        return true;
                if(_pdf)      
                    if( file.getName().endsWith(".pdf")||file.getName().endsWith(".PDF"))
                        return true;
            }
        }
        catch (IOException e)
        {
            return false;
        }
        return false;
    }
    
    public String [] fileList()
    {
        String [] str = new String[_listOfFiles.size()];
        for(int i = 0;i<_listOfFiles.size();i++)
            str[i] = _listOfFiles.get(i).getAbsolutePath();
        return str;
    }
    
	public JList<String> deleteFiles(JList<String> l)
    {
        //TODO
        return null;
    }
}