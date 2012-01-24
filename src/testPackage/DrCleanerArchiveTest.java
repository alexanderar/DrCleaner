package testPackage;

import static org.junit.Assert.*;


import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipInputStream;

import javax.swing.JList;

import org.junit.Test;
import resources.*;

public class DrCleanerArchiveTest {

	private File testFolder = null;
	private File testFolder2 = null;
	private File archiveDirectory = new File(System.getProperty("user.home") + File.separatorChar + "Archive");

	@Test
	public void testArchiveFiles() {
		
		Random generator = new Random();
		int m_fileNum = generator.nextInt(99);
		//prepare test environment
		testFolder = new File("TestFolder");
		deleteDirectory(testFolder);
		testFolder.mkdir();
		testFolder2 = new File(testFolder.getAbsolutePath() + File.separatorChar + "testFolder2");
		testFolder2.mkdir();
		
		int expectedFilesNumToArchive = createTestFiles(m_fileNum);
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
		File[] arciveDirContentBefore = archiveDirectory.listFiles();
		
		//check results after all	
		
		long archivesNumBefore = arciveDirContentBefore.length;
		
		myRobot mr = new myRobot();
		Thread t = new Thread(mr);
		t.start();
		
		temp.archiveFiles(l);
		
				
		File[] arciveDirContentAfter = archiveDirectory.listFiles();
		long archivesNumAfter = arciveDirContentAfter.length;
		
		boolean test = true;	
		
    	if (archivesNumAfter != archivesNumBefore + 1)
    	{
    		test = false;
    	}
    	//checks that archive was created
    	assertTrue(test);
			File f = arciveDirContentAfter[arciveDirContentAfter.length -1];
		int numOfFilesInArchive = 0;
		try {
			FileInputStream fis = new FileInputStream(f);
			ZipInputStream zis = new ZipInputStream(fis);
			
			while(zis.getNextEntry()!=null)
				numOfFilesInArchive++;
			zis.close();
			fis.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Checks that number of files in created archive is equal to number of files that were supposed to be archived
		assertEquals(expectedFilesNumToArchive, numOfFilesInArchive);
		deleteDirectory(testFolder);
		f.delete();
	}

	public int createTestFiles(int fileCnt)
	{
		Random generator = new Random();
		//Counter counts number of files that are supposed to be found.
		int SupportedFileCnt = 0;
		
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
				SupportedFileCnt++;
				break;
			case 1:
				t = new File(path+File.separatorChar+i+".docx");
				SupportedFileCnt++;
				break;
			case 2:
				t = new File(path+File.separatorChar+i+".xls");
				SupportedFileCnt++;
				break;
			case 3:
				t = new File(path+File.separatorChar+i+".xlsx");
				SupportedFileCnt++;
				break;
			case 4:
				t = new File(path+File.separatorChar+i+".ppt");
				SupportedFileCnt++;
				break;
			case 5:
				t = new File(path+File.separatorChar+i+".pptx");
				SupportedFileCnt++;
				break;
			case 6:
				t = new File(path+File.separatorChar+i+".pdf");
				SupportedFileCnt++;
				break;
			case 7:
				t = new File(path+File.separatorChar+i+".txt");
				break;
			default:
				t = new File(path+File.separatorChar+i+".txt");
			}
			try 
			{
				t.createNewFile();
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
		return SupportedFileCnt;
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
