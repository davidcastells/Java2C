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

import cat.uab.cephis.channel.windows.InputNamedPipe;
import cat.uab.cephis.channel.windows.OutputNamedPipe;
import cat.uab.cephis.util.OsInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



/**
 *
 * @author dcr
 */
public class WindowsNamedPipeChannel implements BidirectionalChannel
{
    private OutputStream fos;
    private InputStream fis;

    /**
     * Since mkfifo creates unidirectional channels, we build 2
     * @todo try Windows Named Pipes
     * @param fifo
     * @throws IOException 
     */
    public WindowsNamedPipeChannel() throws IOException 
    {
        // real initialization moved to initOtherEndpoint
        
    }
    
    
    @Override
    public void send(byte[] data) throws IOException
    {
        fos.write(data);
    }

    @Override
    public void receive(byte[] data) throws IOException
    {
        int nRead = fis.read(data);
        
        if (nRead < data.length)
            throw new IOException("Could not read all data");
        
//        System.out.print("Rx Data:");
//        for (int i=0; i < nRead; i++)
//            System.out.print(String.format("%02X ", (int)(data[i] & 0xFF)));        
//        System.out.println("");
    }

    @Override
    public void initOtherEndpoint() throws IOException
    {
        createPipes();
        
//        if (OsInfo.isWindows())
//            Runtime.getRuntime().exec("../C++/NamedPipesChannel/dist/Debug/Cygwin-Windows/namedpipeschannel.exe");
    }

    @Override
    public boolean isSupportedInPlatform()
    {
        return (OsInfo.isWindows());
    }

    
    

    private void createPipes() throws IOException
    {
        String fifo = "fifo_channel";
        String txFifo = fifo + "_tx";
        String rxFifo = fifo + "_rx";

        
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
            
            
            txFifo = "\\\\.\\pipe\\" + txFifo;
            rxFifo = "\\\\.\\pipe\\" + rxFifo;
            
            OutputNamedPipe onp = new OutputNamedPipe(txFifo);
            InputNamedPipe inp = new InputNamedPipe(rxFifo);
            
//            onp.createServer();
//            inp.createServer();
//            onp.waitForClient();
//            inp.waitForClient();
            onp.createClient();
            inp.createClient();
            
            fos = onp;
            fis = inp;
        }
        else if (OsInfo.isLinux())
        {
            File tmpDir = new File("/tmp");
            File txFile = new File(tmpDir, txFifo);
            File rxFile = new File(tmpDir, rxFifo);
            
            if (!txFile.exists())
                Runtime.getRuntime().exec("mkfifo /tmp/"+txFifo);
            if (!rxFile.exists())
                Runtime.getRuntime().exec("mkfifo /tmp/"+rxFifo);

            
            fos = new FileOutputStream(txFile);
            fis = new FileInputStream(rxFile);

        }   
        
        
    }
    
}
