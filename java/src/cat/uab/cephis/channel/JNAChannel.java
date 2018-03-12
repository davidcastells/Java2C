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

import com.sun.jna.Native;
import com.sun.jna.Platform;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author dcr
 */
public class JNAChannel implements BidirectionalChannel
{

    static JNAChannelInterface loadLibrary()
    {
        File library = new File("..\\C++\\JNAChannel\\JNAChannel.DLL");
        
        if (!library.exists())
            throw new RuntimeException("Library file not found, current path : " +  new File(".").getAbsolutePath());
            
        return Native.loadLibrary((Platform.isWindows())? library.getAbsolutePath() : "JNAChannel",
            JNAChannelInterface.class);    
    }

    @Override
    public void send(byte[] data) throws IOException
    {
        JNAChannelInterface.INSTANCE.JNAChannel_nativeSend(data, data.length);
    }

    @Override
    public void receive(byte[] data) throws IOException
    {
        JNAChannelInterface.INSTANCE.JNAChannel_nativeReceive(data, data.length);
    }

    @Override
    public void initOtherEndpoint() throws IOException
    {
    }

    @Override
    public boolean isSupportedInPlatform()
    {
        return true;
    }
    
}
