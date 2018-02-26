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
import java.io.IOException;

/**
 *
 * @author dcr
 */
public class JNIChannel implements BidirectionalChannel
{
    static
    {
        File currentDir = new File(System.getProperty("user.dir"));
        
        if (OsInfo.isLinux())
            System.load(new File(currentDir.getParentFile(), "/C++/JNIChannel/JNIChannel.so").getAbsolutePath());
        else
            System.load(new File(currentDir.getParentFile(), "/C++/JNIChannel/JNIChannel.DLL").getAbsolutePath());
    }

    @Override
    public void send(byte[] data) throws IOException 
    {
        nativeSend(data);
    }

    @Override
    public void receive(byte[] data) throws IOException 
    {
        nativeReceive(data);
    }

    private native void nativeSend(byte[] data);
    private native void nativeReceive(byte[] data);

    @Override
    public void initOtherEndpoint() throws IOException
    {
        // JNI does not need to do anythin to initialize the other endpoint
    }

    @Override
    public boolean isSupportedInPlatform()
    {
        return true;
    }
    
}
