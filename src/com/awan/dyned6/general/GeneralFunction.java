package com.awan.dyned6.general;
import java.io.IOException;import javax.microedition.io.Connector;import javax.microedition.io.file.FileConnection;
public class GeneralFunction {	
    public static boolean checkIsFileExists(String path){
    	boolean finalReturn = false;

    	try {
			FileConnection fileConnection = (FileConnection)Connector.open(path);
			finalReturn = fileConnection.exists();
		} catch (IOException e) {			// TODO Auto-generated catch block			e.printStackTrace();		}
    	return finalReturn;
    }
    public static boolean createDirectory(final String newDirectory) {
        // First step make sure temp directory is there
        FileConnection fconn = null;
        boolean returnFlag = false;
        try {
            fconn = (FileConnection) Connector.open(newDirectory, Connector.READ_WRITE);
            if ( !fconn.exists() ) {
                fconn.mkdir();  // create the folder/file if it doesn't exist
            }
            fconn.close();
            fconn = null;
            returnFlag = true;
        } catch (Exception e) {
            String errorMessage = "Error creating directory: " + newDirectory + '\n' + e.toString();            System.out.println("message " + errorMessage);            returnFlag = false;
        } finally {
            try {
                if ( fconn != null ) {
                    fconn.close();
                    fconn = null;
                }
            } catch (Exception e) {
            }
        }
        return returnFlag;
    }
}