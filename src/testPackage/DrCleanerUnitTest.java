package testPackage;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import java.util.Random;

import resources.MainProgram;

//import resources.MainProgram;

public class DrCleanerUnitTest {

	/*
	 * In the test we will create a test folder with random number of files(no more than 50) that suit user's 
	 * searching parameters. We will run the test and check that searching algorithm finds all of those files
	 * */
	@Test
	public void testSearchingAlgorithm() {
		
		//Major test folder
		File testFolder = new File("TestFolder");
		deleteDirectory(testFolder);
		testFolder.mkdir();
		//Inner test Folder
		File testFolder2 = new File(testFolder.getAbsolutePath() +File.separatorChar + "testFolder2");
		testFolder2.mkdir();
		Random generator = new Random();
		int numOffiles = generator.nextInt(50);
		//Counter counts number of files that is not supposed to be found - wrong type;
		int counter = 0;
		for(int i = 0 ; i< numOffiles; i++)
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
				counter ++;
				break;
			default:
				t = new File(path+File.separatorChar+i+".txt");
				counter ++;
			}
			try {
				t.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} 
		int expectedNumOfFiles = numOffiles - counter;
		MainProgram temp = new MainProgram(0, testFolder, null);
		temp.set_exel(true);
		temp.set_pdf(true);
		temp.set_pow(true);
		temp.set_word(true);	
		temp.searchDirectory(testFolder);	
		String[] lisOfFiles =  temp.fileList();
		boolean test = true;
		for(String s: lisOfFiles)
		{
			if(s.endsWith(".txt"))
				test = false;
			break;
		}
		assertEquals(expectedNumOfFiles, lisOfFiles.length, 0);
		assertTrue(test);
		deleteDirectory(testFolder);
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
}
