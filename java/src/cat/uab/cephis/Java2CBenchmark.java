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
package cat.uab.cephis;

import cat.uab.cephis.channel.BidirectionalChannel;
import cat.uab.cephis.channel.JNIChannel;
import cat.uab.cephis.channel.NamedPipesChannel;
import cat.uab.cephis.channel.SocketChannel;
import cat.uab.cephis.util.PerformanceLap;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dcr
 */
public class Java2CBenchmark {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            new Java2CBenchmark().run();
        } catch (IOException ex) {
            Logger.getLogger(Java2CBenchmark.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    /**
     * We create channels and test them for various data sizes
     */
    private void run() throws IOException 
    {
        BidirectionalChannel channel;
        
        channel = new SocketChannel(9017);

        System.out.println("Socket Channel");
        testChannel(channel);
        
        channel = new JNIChannel();
        
        System.out.println("JNI Channel");
        testChannel(channel);
        
        channel = new NamedPipesChannel();
        
        System.out.println("POSIX FIFO Channel");
        testChannel(channel);
    }
    
    private void testChannel(BidirectionalChannel channel) throws IOException 
    {
        int fromSize = 1;
        int toSize = 0x100000;   // 64KB
        
        if (!channel.isSupportedInPlatform())
        {
            System.out.println("[WARNING] Channel not supported in this plaform, skipping...");
            return;
        }
        
        channel.initOtherEndpoint();
        
        PerformanceLap txLap = new PerformanceLap();
        PerformanceLap rxLap = new PerformanceLap();
        PerformanceLap totalLap = new PerformanceLap();
        
        System.out.println("DataSize;TX_Time;RX_Time;RT_Time;");
        System.out.flush();
        
        for (int i=fromSize; i <= toSize; i *= 2)
        {
            
            byte[] data = createArray(i);
            
            totalLap.start();
            
            txLap.start();
            channel.send(data);
            txLap.stop();
            
            rxLap.start();
            channel.receive(data);
            rxLap.stop();
            
            totalLap.stop();
            
            checkArray(data);
            
            System.out.println(i+";"+txLap.ellapsedSeconds()+";"+rxLap.ellapsedSeconds() + ";" + totalLap.ellapsedSeconds() +";");
        }
    }

    private byte[] createArray(int n) 
    {
        byte[] ret = new byte[n];
        for (int i=0; i < n; i++)
            ret[i] = (byte) (i & 0xFF);
        
        return ret;
    }

    private void checkArray(byte[] data) {
        for (int i=0; i  < data.length; i++)
        {
            if (data[i] != (byte)((i+1) & 0xFF) )
            {
                System.err.println("ERROR");
                dumpArray(System.err, data, 0x20);
                System.exit(0);
                //throw new RuntimeException("Processing error in pos " + i + " expected: " + (((i+1) & 0xFF)) + " found " + data[i] );
            }
        }
    }

    private void dumpArray(PrintStream out, byte[] data, int len) 
    {
        out.println("ARRAY:");
        for (int i=0; i < Math.min(data.length, len); i++)
        {
            out.print(String.format("%02X ", data[i]));
            if (i>0 && (i%16 == 0))
                out.println("");
        }
        
        out.println("");
        out.flush();
       
    }
    
}
