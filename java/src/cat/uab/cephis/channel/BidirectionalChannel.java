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

import java.io.IOException;

/**
 * This is a simple bidirectional channel with a C process
 * send and receive are blocking functions
 * 
 * @author dcr
 */
public abstract interface BidirectionalChannel 
{
    public abstract void send(byte[] data) throws IOException;
    public abstract void receive(byte[] data) throws IOException;

    public abstract void initOtherEndpoint() throws IOException;
    public abstract boolean isSupportedInPlatform();
    
}
