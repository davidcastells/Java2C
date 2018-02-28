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
import java.io.OutputStream;

/**
 *
 * @author dcr
 */
public class OutputNamedPipe extends OutputStream
{
    private WinNT.HANDLE m_hNamedPipe;
    private final String m_pipeName;

    public OutputNamedPipe(String pipeName)
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
        else 
            throw new IOException(message + " failed: hr=" + hr + " - 0x" + Integer.toHexString(hr));
    }
    
    @Override
    public void write(int i) throws IOException
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void write(byte[] data) throws IOException
    {
        int v = data.length;
        
        sendInt(v);
        sendByteArray(data);
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
        int dwOpenMode = WinBase.PIPE_ACCESS_OUTBOUND;
        
        m_hNamedPipe=Kernel32.INSTANCE.CreateNamedPipe(m_pipeName,
                dwOpenMode,        // dwOpenMode
                WinBase.PIPE_TYPE_BYTE | WinBase.PIPE_READMODE_BYTE | WinBase.PIPE_WAIT,    // dwPipeMode
                1,    // nMaxInstances,
                Byte.MAX_VALUE,    // nOutBufferSize,
                Byte.MAX_VALUE,    // nInBufferSize,
                1000,    // nDefaultTimeOut,
                null);    // lpSecurityAttributes
        
        boolean ret = !WinBase.INVALID_HANDLE_VALUE.equals(m_hNamedPipe);
        checkError("CreateNamedPipe", ret);
    }
    
    public void createClient() throws IOException
    {
        int dwOpenMode = WinNT.GENERIC_WRITE ;
        m_hNamedPipe = Kernel32.INSTANCE.CreateFile(m_pipeName, dwOpenMode, 0, null, WinNT.OPEN_EXISTING, 0, null );
        
        boolean ret = !WinBase.INVALID_HANDLE_VALUE.equals(m_hNamedPipe);
        checkError("CreateFile", ret);
    }

    public void waitForClient() throws IOException
    {
        System.out.println("Await client connection");
        boolean ret = Kernel32.INSTANCE.ConnectNamedPipe(m_hNamedPipe, null);
        checkError("ConnectNamedPipe", ret);
        System.out.println("Client connected");
    }

    private void sendInt(int v) throws IOException
    {
        byte[] b = new byte[4];
        
        // buffer in little endian, less significant bytes first
        for (int i=0; i < 4; i++)
        {
            b[i] = (byte) (v & 0xFF);
            v = v >> 8;
        }
        
        sendByteArray(b);
    }

    private void sendByteArray(byte[] data) throws IOException
    {
        IntByReference lpNumberOfBytesWritten = new IntByReference(0);
        boolean ret = Kernel32.INSTANCE.WriteFile(m_hNamedPipe, data, data.length, lpNumberOfBytesWritten, null);
        checkError("WriteFile", ret);
        
        if (lpNumberOfBytesWritten.getValue() != data.length)
            throw new IOException("Only " + lpNumberOfBytesWritten.getValue() + " written from " + data.length);
    }
    
    
}
