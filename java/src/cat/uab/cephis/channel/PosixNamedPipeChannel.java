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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author dcr
 */
public class PosixNamedPipeChannel implements BidirectionalChannel
{
    private OutputStream fos;
    private InputStream fis;

    /**
     * Since mkfifo creates unidirectional channels, we build 2
     * @todo try Windows Named Pipes
     * @param fifo
     * @throws IOException 
     */
    public PosixNamedPipeChannel() throws IOException 
    {
        // real initialization moved to initOtherEndpoint
        
    }
    
    
    @Override
    public void send(byte[] data) throws IOException
    {
        sendInt(data.length);
        sendByteArray(data);
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
        fos.write(data);
        fos.flush();
    }
    
    @Override
    public void receive(byte[] data) throws IOException
    {
        int len = receiveInt();
        receiveByteArray(data);
        
//        System.out.print("Rx Data:");
//        for (int i=0; i < nRead; i++)
//            System.out.print(String.format("%02X ", (int)(data[i] & 0xFF)));        
//        System.out.println("");
    }
    
    private int receiveInt() throws IOException 
    {
        byte[] b = new byte[4];
        receiveByteArray(b);
        
        int v = (b[0]&0xFF) | (b[1]&0xFF) <<8 | (b[2]&0xFF) << 16 | (b[3]&0xFF) << 24;
        
        return v;
    }
    
    public int receiveByteArray(byte[] data) throws IOException
    {
        int nRead = 0;
        int nToRead = data.length;
        int nOffset = 0;
        
        do
        {
            nRead = fis.read(data, nOffset, nToRead);
            
            if (nRead != -1)
            {
                nToRead -= nRead;
                nOffset += nRead;
            }
        } while (nOffset < data.length);
        
        
        return nOffset;
    }

    @Override
    public void initOtherEndpoint() throws IOException
    {
        // First create the server
        File file = new File("../C++/PosixNamedPipesChannel/dist/Debug/GNU-Linux/posixnamedpipeschannel");
        if (!file.exists())
            throw new RuntimeException("No executable");

//        Runtime.getRuntime().exec(file.getAbsolutePath());
        try
        {
            Thread.sleep(1000);
        } catch (InterruptedException ex)
        {
            Logger.getLogger(PosixNamedPipeChannel.class.getName()).log(Level.SEVERE, null, ex);
        }

        // then connect the pipes
        createPipes();
    }

    @Override
    public boolean isSupportedInPlatform()
    {
        return (OsInfo.isLinux());
    }

    
    

    private void createPipes() throws IOException
    {
        String fifo = "/tmp/fifo_channel";
        String txFifo = fifo + "_tx";
        String rxFifo = fifo + "_rx";

        
        if (OsInfo.isWindows())
        {
////            if (OsInfo.hasCygwin())
////            {
////                tmpDir = new File(OsInfo.getCygwinPath(), "tmp");
////                txFile = new File(tmpDir, txFifo+".lnk");
////                rxFile = new File(tmpDir, rxFifo+".lnk");
////            }
////            else
////                throw new RuntimeException("Not supported");
//            // @todo create named pipe with JNA
//            
//            
//            txFifo = "\\\\.\\pipe\\" + txFifo;
//            rxFifo = "\\\\.\\pipe\\" + rxFifo;
//            
//            OutputNamedPipe onp = new OutputNamedPipe(txFifo);
//            InputNamedPipe inp = new InputNamedPipe(rxFifo);
//            
////            onp.createServer();
////            inp.createServer();
////            onp.waitForClient();
////            inp.waitForClient();
//            onp.createClient();
//            inp.createClient();
//            
//            fos = onp;
//            fis = inp;
        }
        else if (OsInfo.isLinux())
        {
            //OutputNamedPipe onp = new OutputNamedPipe(txFifo);
            //InputNamedPipe inp = new InputNamedPipe(rxFifo);
            
            System.out.println("FOS: " + txFifo);
            System.out.println("FIS: " + rxFifo);
            
            if (!new File(txFifo).exists())
                throw new IOException("" + txFifo + " does not exist");
            if (!new File(rxFifo).exists())
                throw new IOException("" + rxFifo + " does not exist");
                
            fos = new FileOutputStream(txFifo);
            fis = new FileInputStream(rxFifo);

        }   
        
        
    }

    

    
    

    
    
}
