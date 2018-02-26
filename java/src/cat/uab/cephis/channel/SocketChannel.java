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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Socket Channel implements the transmision by using a client
 * socket in Java that connects to a server port in C/C++ on localhost
 *  Both sides must know the port number and must be available (no other 
 *  applications in the system use that port).
 * 
 * @author dcr
 */
public class SocketChannel implements BidirectionalChannel
{

    private final int port;
    private Socket socket;
    private OutputStream os;
    private InputStream is;
    
    public SocketChannel(int port)
    {
        this.port = port;
    }

    @Override
    public void send(byte[] data) throws IOException
    {
        connect();
        
        
        int v = data.length;
        
        sendInt(v);
        os.write(data);
        os.flush();
    }

    @Override
    public void receive(byte[] data) throws IOException
    {
        
        int readFromSocket = 0;
        int senderDataLen = 0;        // data len transmitted by sender
                
        // receive the length in little endian order
        for (int i=0; i < 4; i++)
        {
            senderDataLen |= (is.read() & 0xFF) << (8*i);
        }
        
        if (senderDataLen != data.length)

            throw new RuntimeException("Wire protocol informed of " + senderDataLen + " bytes but expecting " + data.length);
        
        do
        {
            readFromSocket += is.read(data, readFromSocket, (data.length-readFromSocket));
        } while (readFromSocket < data.length);
        
        disconnect();
    }

    private void connect() throws IOException 
    {
        this.socket = new Socket("127.0.0.1", port);
        os = socket.getOutputStream();
        is = socket.getInputStream();
    }

    private void disconnect() throws IOException 
    {
        is.close();
        os.close();
        is = null;
        os = null;
        socket.close();
        socket = null;
        
        
    }

    private void sendInt(int v) throws IOException 
    {
        byte[] buf = new byte[4];
        
        // send the length in little endian order
        for (int i=0; i < 4; i++)
        {
            buf[i] = (byte)(v & 0xFF);
            v >>= 8;
        }

        os.write(buf);
    }

    @Override
    public void initOtherEndpoint() throws IOException
    {
        // @todo check it it is running first
        
        // Init the server , is it is not running
        if (OsInfo.isWindows())
            Runtime.getRuntime().exec("../C++/SocketChannel/dist/Debug/Cygwin-Windows/socketchannel.exe");
    }

    @Override
    public boolean isSupportedInPlatform()
    {
        return true;
    }
}
