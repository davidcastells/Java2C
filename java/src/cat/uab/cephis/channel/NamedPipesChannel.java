/*
 * Copyright (C) 2018 Universitat Autonoma de Barcelona - David Castells-Rufas <david.castells@uab.cat>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cat.uab.cephis.channel;

import cat.uab.cephis.util.OsInfo;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 *
 * @author dcr
 */
public class NamedPipesChannel implements BidirectionalChannel
{
    private FileOutputStream fos;
    private FileInputStream fis;

    /**
     * Since mkfifo creates unidirectional channels, we build 2
     * @todo try Windows Named Pipes
     * @param fifo
     * @throws IOException 
     */
    public NamedPipesChannel() throws IOException 
    {
        
        
    }
    
    
    @Override
    public void send(byte[] data) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void receive(byte[] data) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initOtherEndpoint() throws IOException
    {
        createPipes();
        
        if (OsInfo.isWindows())
            Runtime.getRuntime().exec("../C++/PosixFIFOChannel/dist/Debug/Cygwin-Windows/posixfifochannel.exe");
    }

    @Override
    public boolean isSupportedInPlatform()
    {
        if (OsInfo.isWindows())
            return false;
        
        return true;
    }

    private void createNamePipeInWindows(String pipeName, boolean tx)
    {
        int dwOpenMode = (tx)? WinBase.PIPE_ACCESS_OUTBOUND : WinBase.PIPE_ACCESS_INBOUND;
        
        HANDLE    hNamedPipe=Kernel32.INSTANCE.CreateNamedPipe(pipeName,
                dwOpenMode,        // dwOpenMode
                WinBase.PIPE_TYPE_BYTE | WinBase.PIPE_READMODE_BYTE | WinBase.PIPE_WAIT,    // dwPipeMode
                1,    // nMaxInstances,
                Byte.MAX_VALUE,    // nOutBufferSize,
                Byte.MAX_VALUE,    // nInBufferSize,
                1000,    // nDefaultTimeOut,
                null);    // lpSecurityAttributes
        assertCallSucceeded("CreateNamedPipe", !WinBase.INVALID_HANDLE_VALUE.equals(hNamedPipe));
    }

    public static final void assertCallSucceeded(String message, boolean result) {
        if (result) {
            return;
        }

        int hr = Kernel32.INSTANCE.GetLastError();
        if (hr == WinError.ERROR_SUCCESS) {
            fail(message + " failed with unknown reason code");
        } else {
            fail(message + " failed: hr=" + hr + " - 0x" + Integer.toHexString(hr));
        }
    }
    
    private static void fail(String string)
    {
        throw new RuntimeException(string);
    }

    private void createPipes() throws IOException
    {
        String fifo = "fifo_channel";
        String txFifo = fifo + "_tx";
        String rxFifo = fifo + "_rx";
        File txFile = null;
        File rxFile = null;

        
        if (OsInfo.isWindows())
        {
//            if (OsInfo.hasCygwin())
//            {
//                tmpDir = new File(OsInfo.getCygwinPath(), "tmp");
//                txFile = new File(tmpDir, txFifo+".lnk");
//                rxFile = new File(tmpDir, rxFifo+".lnk");
//            }
//            else
//                throw new RuntimeException("Not supported");
            // @todo create named pipe with JNA
            
            
            txFile = new File("\\\\.\\pipe\\" + txFifo);
            rxFile = new File("\\\\.\\pipe\\" + rxFifo);
            
            createNamePipeInWindows("\\\\.\\pipe\\" + txFifo, true);
            createNamePipeInWindows("\\\\.\\pipe\\" + rxFifo, false);
        }
        else if (OsInfo.isLinux())
        {
            File tmpDir = new File("/tmp");
            txFile = new File(tmpDir, txFifo);
            rxFile = new File(tmpDir, rxFifo);
            
            if (!txFile.exists())
                Runtime.getRuntime().exec("mkfifo /tmp/"+txFifo);
            if (!rxFile.exists())
                Runtime.getRuntime().exec("mkfifo /tmp/"+rxFifo);
        }   
        
        fos = new FileOutputStream(txFile);
        fis = new FileInputStream(rxFile);
        
    }
    
}
