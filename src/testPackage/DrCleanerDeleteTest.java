
package testPackage;

import static org.junit.Assert.*;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.swing.JList;

import org.junit.Test;

import resources.DrCleanerView;
import resources.HandlerView;
import resources.MainProgram;

public class DrCleanerDeleteTest
{
	private File testFolder = null;
	private File testFolder2 = null;

	@Test
	public void test() {
		int m_fileNum = 99;
		//prepare test environment
		testFolder = new File("TestFolder");
		deleteDirectory(testFolder);
		testFolder.mkdir();
		testFolder2 = new File(testFolder.getAbsolutePath() + File.separatorChar + "testFolder2");
		testFolder2.mkdir();
		
		int expectedFilesNumAfterDeletion = createTestFiles(m_fileNum);
		//test delete from application
		MainProgram temp = new MainProgram(0, testFolder, new HandlerView(new DrCleanerView()));
		temp.set_exel(true);
		temp.set_pdf(true);
		temp.set_pow(true);
		temp.set_word(true);	
		temp.searchDirectory(testFolder);
		String[] lisOfFiles =  temp.fileList();
		
		JList<String> l = new JList<String>(lisOfFiles);
		int listSize = l.getModel().getSize();	    
		l.addSelectionInterval(0, listSize);
		
		//check results after all
	
		myRobot mr = new myRobot();
		Thread t = new Thread(mr);
		t.start();
		
		temp.deleteFiles(l);
	
		long fileNumAfterdeletion = testFolder.listFiles().length + testFolder2.listFiles().length - 1;
		
		assertEquals(expectedFilesNumAfterDeletion, fileNumAfterdeletion, 0);

		deleteDirectory(testFolder);
	}

	public int createTestFiles(int fileCnt)
	{
		Random generator = new Random();
		//Counter counts number of files that is not supposed to be found - wrong type;
		int notSupportedFileCnt = 0;
		
		for(int i = 0 ; i < fileCnt; i++)
		{
			int type = generator.nextInt(8);
			int where =  generator.nextInt(2);
			String path;
			if(where == 0)
				path = testFolder.getAbsolutePath();
			else
				path = testFolder2.getAbsolutePath();
			File t;
			switch (type) {
			case 0:
				t = new File(path+File.separatorChar+i+".doc");
				break;
			case 1:
				t = new File(path+File.separatorChar+i+".docx");
				break;
			case 2:
				t = new File(path+File.separatorChar+i+".xls");
				break;
			case 3:
				t = new File(path+File.separatorChar+i+".xlsx");
				break;
			case 4:
				t = new File(path+File.separatorChar+i+".ppt");
				break;
			case 5:
				t = new File(path+File.separatorChar+i+".pptx");
				break;
			case 6:
				t = new File(path+File.separatorChar+i+".pdf");
				break;
			case 7:
				t = new File(path+File.separatorChar+i+".txt");
				notSupportedFileCnt++;
				break;
			default:
				t = new File(path+File.separatorChar+i+".txt");
				notSupportedFileCnt++;
			}
			try 
			{
				t.createNewFile();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return notSupportedFileCnt;
	}
	
	static public void deleteDirectory(File path) 
	{
		if (path == null)
			return;
		if (path.exists())
		{
			for(File f : path.listFiles())
			{
				if(f.isDirectory()) 
				{
					deleteDirectory(f);
					f.delete();
				}
			    else
			    {
			    	f.delete();
			    }
			}
			path.delete();
		}
	}
	
	public class myRobot extends Thread
	{
		public void run()
		{
			try
			{
				Robot r = new Robot();
				r.delay(2000);
				for (int i = 0; i < 4; i++)
				{
					r.keyPress(KeyEvent.VK_ENTER);
					r.delay(1000);
					r.keyRelease(KeyEvent.VK_ENTER);
				}
			}
			catch (Exception e)
			{
			}
		}
	}
}
