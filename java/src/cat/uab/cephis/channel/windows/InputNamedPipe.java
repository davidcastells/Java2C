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
package cat.uab.cephis.channel.windows;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinError;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author dcr
 */
public class InputNamedPipe extends InputStream
{
    private WinNT.HANDLE m_hNamedPipe;
    private final String m_pipeName;

    public InputNamedPipe(String pipeName)
    {
        m_pipeName = pipeName;
        

    }
    
    public void checkError(String message, boolean result) throws IOException 
    {
        if (result) 
            return;
        
        int hr = Kernel32.INSTANCE.GetLastError();
        if (hr == WinError.ERROR_SUCCESS) 
            throw new IOException(message + " failed with unknown reason code");
        else if (hr == WinError.ERROR_PIPE_BUSY)
            throw new IOException("Pipe " + m_pipeName + " Busy");
        else if (hr == WinError.ERROR_INVALID_ACCESS)
            throw new IOException("Invalid Access to Pipe " + m_pipeName );
        else if (hr == WinError.ERROR_BROKEN_PIPE)
            throw new IOException("BrokenPipe " + m_pipeName );
        else 
            throw new IOException(message + " failed: hr=" + hr + " - 0x" + Integer.toHexString(hr) + " pipe " + m_pipeName);
    }

    @Override
    public int read() throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    public int read(byte[] data) throws IOException
    {
        int v = receiveInt();
        int ret = receiveByteArray(data);
        return ret;
    }   

    public int receiveInt() throws IOException
    {
        byte[] b = new byte[4];
        receiveByteArray(b);
        
        int v = (b[0]&0xFF) | (b[1]&0xFF) <<8 | (b[2]&0xFF) << 16 | (b[3]&0xFF) << 24;
        
        return v;
    }    
    
    public int receiveByteArray(byte[] data) throws IOException
    {
        IntByReference lpNumberOfBytesRead = new IntByReference(0);
        boolean ret = Kernel32.INSTANCE.ReadFile(m_hNamedPipe, data, data.length, lpNumberOfBytesRead, null);
        checkError("ReadFile", ret);
        
        return lpNumberOfBytesRead.getValue();
    }

    @Override
    public void close() throws IOException
    {
        boolean ret = Kernel32.INSTANCE.DisconnectNamedPipe(m_hNamedPipe);
        
        checkError("DisconnectNamedPipe", ret);    
        
        ret = Kernel32.INSTANCE.CloseHandle(m_hNamedPipe);
        
        checkError("CloseHandle", ret);    
    }

    public void createServer() throws IOException
    {
        int dwOpenMode = WinBase.PIPE_ACCESS_INBOUND;
        boolean ret;
        
        m_hNamedPipe = Kernel32.INSTANCE.CreateNamedPipe(m_pipeName,
                dwOpenMode,        // dwOpenMode
                WinBase.PIPE_TYPE_BYTE | WinBase.PIPE_READMODE_BYTE | WinBase.PIPE_WAIT,    // dwPipeMode
                1,    // nMaxInstances,
                Byte.MAX_VALUE,    // nOutBufferSize,
                Byte.MAX_VALUE,    // nInBufferSize,
                1000,    // nDefaultTimeOut,
                null);    // lpSecurityAttributes
        
        ret = !WinBase.INVALID_HANDLE_VALUE.equals(m_hNamedPipe);
        checkError("CreateNamedPipe", ret);
    }

    public void waitForClient() throws IOException
    {
        System.out.println("Await client connection");
        boolean ret = Kernel32.INSTANCE.ConnectNamedPipe(m_hNamedPipe, null);
        checkError("ConnectNamedPipe", ret);
        System.out.println("Client connected");
    }

    public void createClient() throws IOException
    {
        int dwOpenMode = WinNT.GENERIC_READ ;
        m_hNamedPipe = Kernel32.INSTANCE.CreateFile(m_pipeName, dwOpenMode, 0, null, WinNT.OPEN_EXISTING, 0, null );
        
        boolean ret = !WinBase.INVALID_HANDLE_VALUE.equals(m_hNamedPipe);
        checkError("CreateFile", ret);
    }
    
    
}
