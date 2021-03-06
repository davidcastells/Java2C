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
package cat.uab.cephis.util;

import java.io.File;


/**
 *
 * @author dcr
 */
public class OsInfo
{
    public static boolean isWindows()
    {
        return getOsName().startsWith("windows");
    }
    
    public static boolean isLinux()
    {
        return getOsName().startsWith("linux");
    }
    
    public static String getOsName()
    {
        return System.getProperty("os.name").toLowerCase();
    }
    
    public static boolean hasCygwin()
    {
        File cygFile = getCygwinPath();
        
        return cygFile.exists();
    }

    public static File getCygwinPath()
    {
        return new File("/cygwin64");
    }
}
