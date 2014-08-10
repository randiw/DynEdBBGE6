package com.randi.dyned6.tools;

/*
 * ConnectionManager.java
 *
 * © Research In Motion Limited, 2006
 * Confidential and proprietary.
 */

import java.io.*;
import javax.microedition.io.*;

import net.rim.device.api.servicebook.*;
import net.rim.device.api.system.*;
import net.rim.device.api.ui.component.*;

/**
 * The ConnectionManager provides the capability of parsing through
 * all of the service books (and service records) on the handheld.  
 * Once the parsing operation is complete, the ConnectionManager provides
 * two main methods for accessing information around which service books
 * should be used for connectivity.
 */
public class ConnectionManager implements GlobalEventListener, CoverageStatusListener
{
    private static final long ID = 0x1431cf6271d3b1edL; // ConnectionManager.ID
    private static String IPPP = "IPPP";                // Static instance of the IPPP string so we don't create it every time.
    
    private static ConnectionManager _manager;      // Static instance of the ConnectionManager.
    
    private boolean _mdsSupport;                    // Boolean representing whether MDS is supported.
    private boolean _bisSupport;                    // Boolean representing whether BIS-B is supported.
    private boolean _wapSupport;                    // Boolean representing whether WAP is supported.
    
    /**
     * The constructor for this class which simply parses the service books.
     */
    private ConnectionManager() 
    {
        //parseServiceBooks();
    	setCoverage();
    }
    
    /**
     * Returns an instance of the ConnectionManager.  This currently
     * only leverages providing a static instance (one per process) but could 
     * easily be changed to provide a singleton instance for the whole system.
     * @return an instance of the ConnectionManager.
     */
    public static ConnectionManager getInstance()
    {
        if( _manager == null ) {
            _manager = new ConnectionManager();
        }
        return _manager;
    }
    
    /**
     * Returns the Connection object specified by the name (e.g. HttpConnection) using the
     * appropriate transport mechanism (MDS, BIS-B, TCP) depending on what service books
     * are currently supported on the handheld and using a priority scale in the following order:
     * <code> 
     *      MDS
     *      BIS-B
     *      WAP - To be supported in the future
     *      HTTP over Direct TCP
     * </code>
     * This method does NOT check for the name to ensure that HTTP is being requested and as such
     * it may not work if you request a socket connection over the BIS-B transport protocol.  
     */
    public Connection getConnection( String name, int mode, boolean timeouts ) throws IOException
    {
        if( _mdsSupport ) {
            // MDS Transport
            name = name.concat( ";deviceside=false" );
        } else if( _bisSupport ) {
            // BIS-B Transport
            name = name.concat( ";deviceside=false;ConnectionType=mds-public" );
        } else if( _wapSupport ) {
            // TODO
        } else {
            // HTTP over Direct TCP
            name = name.concat( ";deviceside=true" );
        }
        
        return Connector.open( name, mode, timeouts );
    }
    
    /**
     * Returns a string representing the type of connection that would be chosen when using getConnection.
     * @return a string representing the type of connection that would be chosen when using getConnection.
     */
    public String getConnectionType()
    {
        if( _mdsSupport ) {
            // MDS Transport
            return "MDS";
        } else if( _bisSupport ) {
            // BIS-B Transport
            return "BIS-B";
        } else if( _wapSupport ) {
            // WAP Transport
            return "WAP";
        } else {
            // HTTP over Direct TCP
            return "Direct TCP";
        }
    }
    
    /**
     * This method uses the CoverageInfo API to determine what coverage is available on the device.
     * CoverageInfo is available as of 4.2.0, but until 4.2.2, Coverage_MDS is shown as available 
     * when only BIS_B Coverage is actually available on the device.   
     */
    private void setCoverage() {
    	if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_MDS)) {
       		_mdsSupport = true;
    	}
    	if (CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B)) {
    		_bisSupport = true;
    	}
    }
    
    /**
     * This method handles changes in Coverage through the CoverageStatusListener interface.
     * CoverageStatusListener works with CoverageInfo and is available with 4.2.0 
     */
    public void coverageStatusChanged(int newCoverage) {
    	if ((newCoverage & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS) {
        	_mdsSupport = true;
    	}
    	if ((newCoverage & CoverageInfo.COVERAGE_BIS_B) == CoverageInfo.COVERAGE_BIS_B) {
    		_bisSupport = true;
    	}
    }
    

    /**
     * This method provides the functionality of actually parsing 
     * through the service books on the handheld and determining
     * which traffic routes are available based on that information.
     * Before 4.2.0, this method is necessary to determine coverage.
     */
    private void parseServiceBooks()
    {
        // Add in our new items by scrolling through the ServiceBook API.
        ServiceBook sb = ServiceBook.getSB();
        ServiceRecord[] records = sb.findRecordsByCid( IPPP );      // The IPPP service represents the data channel for MDS and BIS-B
        if( records == null ) {
            return;
        }
        
        int numRecords = records.length;
        for( int i = 0; i < numRecords; i++ ) {
            ServiceRecord myRecord = records[i];
            String name = myRecord.getName();       // Technically, not needed but nice for debugging.
            String uid = myRecord.getUid();         // Technically, not needed but nice for debugging.

            // First of all, the CID itself should be equal to IPPP if this is going to be an IPPP service book.
            if( myRecord.isValid() && !myRecord.isDisabled() ) {
                // Now we need to determine if the service book is Desktop or BIS.  One could check against the
                // name but that is unreliable.  The best mechanism is to leverage the security of the service
                // book to determine the security of the channel.
                int encryptionMode = myRecord.getEncryptionMode();
                if( encryptionMode == ServiceRecord.ENCRYPT_RIM ) {
                    _mdsSupport = true;
                } else {
                    _bisSupport = true;
                }
            }
        }
    }
    
    ////////////////////////////////////////////////////////////
    /// GlobalEventListener Interface Implementation         ///
    ////////////////////////////////////////////////////////////
    
    /**
     * Invoked when the specified global event occured. 
     * The eventOccurred method provides two object parameters and two integer parameters for supplying details about the event itself. The developer determines how the parameters will be used. 
     * 
     * For example, if the event corresponded to sending or receiving a mail message, the object0 parameter might specify the mail message itself, while the data0 parameter might specify the identification details of the message, such as an address value.
     * 
     * @param guid - The GUID of the event.
     * @param data0 - Integer value specifying information associated with the event.
     * @param data1 - Integer value specifying information associated with the event.
     * @param object0 - Object specifying information associated with the event.
     * @param object1 - Object specifying information associated with the event.
     */
    public void eventOccurred(long guid, int data0, int data1, Object object0, Object object1)
    {
        if( guid == ServiceBook.GUID_SB_ADDED ||
            guid == ServiceBook.GUID_SB_CHANGED ||
            guid == ServiceBook.GUID_SB_OTA_SWITCH ||
            guid == ServiceBook.GUID_SB_OTA_UPDATE ||
            // This item was added to the JDE in v4.1.  If compiling in that version uncomment this line
            // and otherwise leave it out.
            // guid == ServiceBook.GUID_SB_POLICY_CHANGED ||
            guid == ServiceBook.GUID_SB_REMOVED ) {
                Dialog.inform( "Service Book Global Event Received" );
                parseServiceBooks();
        }
    }
}
