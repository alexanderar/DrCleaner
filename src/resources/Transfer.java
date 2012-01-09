package resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.ProgressListener;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.RequestTokenPair;
import com.dropbox.client2.session.WebAuthSession;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession.WebAuthInfo;


public class Transfer extends SwingWorker<Integer, Object>{
	
	private File fileToSend;
	private static TransferDialog dialogWindow;
	final static private String APP_KEY = "u0dhelag5hvpndw";
    final static private String APP_SECRET = "8x2qyz0nvxktq5s";
    final static private AccessType ACCESS_TYPE = AccessType.APP_FOLDER;
    private DropboxAPI<WebAuthSession> mDBApi;
    
    
    public Transfer(File f, TransferDialog t)
    {
    	fileToSend = f;
    	dialogWindow = t;
    	dialogWindow.getProgressBar().setValue(0);
    }
	
	
	protected Integer doInBackground()
	{
		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
    	WebAuthSession session = new WebAuthSession(appKeys, ACCESS_TYPE);
    	mDBApi = new DropboxAPI<WebAuthSession>(session);
    	try
    	{
    		WebAuthInfo authInfo = mDBApi.getSession().getAuthInfo();
    		String url = authInfo.url;	
    		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) 
            {

                System.err.println( "Desktop doesn't support the browse action (fatal)" );
                System.exit( 1 );
            }
            URI u = new URI (url);
            FileInputStream inputStream = new FileInputStream(fileToSend);
            Integer toContinue = -2;
            JOptionPane.showMessageDialog(null, "You will be redirected to DropBox page. Please login to your accont and allow access to this application",
       				"DrCleaner" ,JOptionPane.INFORMATION_MESSAGE);   
            desktop.browse(u);
            toContinue = JOptionPane.showConfirmDialog(null, "Did You authorize the application?",
       				"DrCleaner" ,JOptionPane.YES_NO_OPTION);         
            while (toContinue == -2)          
            	Thread.sleep(1000);
            if(toContinue == JOptionPane.NO_OPTION)
            	return 1;
            RequestTokenPair r_pair = authInfo.requestTokenPair;
            session.retrieveWebAccessToken(r_pair);
            
            //Check that there is enough available space in users dropbox account in order to upload the file. 
            if(mDBApi.accountInfo().quota < fileToSend.length())
            {
            	JOptionPane.showMessageDialog(null, "You don't have enough available space in your DropBox account",            
            			"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
            	return 1;
            }
            
            
            dialogWindow.setVisible(true);
           
            try
            {
            	ProgressListener listener = new ProgressListener() {
              				
					@Override
					public void onProgress(long bytes, long total) {
						setProgress((int)(bytes * 100/fileToSend.length()));
						dialogWindow.setIcon();						
					}
					@Override
					//checks number of transfered files every 100 milliseconds
					public long progressInterval()
					{
						return 100;
					}
				}; 
				    
				Entry newEntry = mDBApi.putFile("/"+fileToSend.getName(), inputStream,
                       fileToSend.length(), null, listener);				
				inputStream.close();
				setProgress(100);
				
            	JOptionPane.showMessageDialog(null, "The uploaded file's rev is: " + newEntry.rev,
       				"DrCleaner" ,JOptionPane.INFORMATION_MESSAGE);          	
           
            } catch (DropboxUnlinkedException e) {
               
            	// User was unlinked, ask them to link again here.          	
            	JOptionPane.showMessageDialog(null, "User has unlinked fro DropBox account. Please link once again",            
            			"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
            	return 1;
           
            } catch (DropboxException e) {
            	 JOptionPane.showMessageDialog(null, "Something went wrong while uploading.",
         				"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
            	 return 1;
            }   		
    	}
    	catch(DropboxException e)
    	{
			e.printStackTrace();
			return 1;

    	}
    	catch(IOException e)
    	{
			e.printStackTrace();
			return 1;

    	} 
    	catch (URISyntaxException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1;
		} 
    	catch (InterruptedException e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	protected void done() {
		try 
		{
			int result = get();
			if(result == 0)
			{				
				dialogWindow.dispose();
				fileToSend.delete();
			}
			else
				JOptionPane.showMessageDialog(null, "DrCleaner was unable to transfer files to your DropBox account.\nYour files are saved in your Archive directory: " + fileToSend.getAbsolutePath(),
	     				"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
		} 
		catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null, "Interrupted while transfering. Your files are saved in your Archive directory: " + fileToSend.getAbsolutePath(),
     				"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} 
		catch (ExecutionException e) {
			JOptionPane.showMessageDialog(null, "Execution exeption, your files are saved in your Archive directory: " + fileToSend.getAbsolutePath(),
     				"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		catch (SecurityException e)
		{
			JOptionPane.showMessageDialog(null, "Error while deleting backup archive, your files are saved in your Archive directory: " + fileToSend.getAbsolutePath(),
     				"DrCleaner" ,JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		dialogWindow.dispose();		
	}
}
