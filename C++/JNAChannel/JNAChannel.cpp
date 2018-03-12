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

/* 
 * File:   JNIChannel.cpp
 * Author: dcr
 *
 * Created on February 9, 2018, 12:55 PM
 */
#ifdef _WIN32
    #include <windows.h>
#endif

#include <stdio.h>

#if defined(_MSC_VER)
    //  Microsoft 
    #define EXPORT __declspec(dllexport)
    #define IMPORT __declspec(dllimport)
#elif defined(__GNUC__)
    //  GCC
    #define EXPORT __attribute__((visibility("default")))
    #define IMPORT
#else
    //  do nothing and hope for the best?
    #define EXPORT
    #define IMPORT
    #pragma warning Unknown dynamic link import/export semantics.
#endif

unsigned char* pLocal = NULL;
unsigned int localSize = 0;

extern "C"
{
EXPORT void JNAChannel_nativeSend(unsigned char* pa, unsigned int na);
EXPORT void JNAChannel_nativeReceive(unsigned char* pa, unsigned int na);
}

EXPORT void JNAChannel_nativeSend(unsigned char* pa, unsigned int na)
{
    pLocal = new unsigned char[na];
    localSize = na;
    
    for (int i=0; i < na; i++)
        pLocal[i] = pa[i] +1;
}

/*
 * Class:     cat_uab_cephis_channel_JNIChannel
 * Method:    nativeReceive
 * Signature: ([B)V
 */
EXPORT void JNAChannel_nativeReceive(unsigned char* pa, unsigned int na)
{
//    printf("[JNI] na=%d localNa=%d\n", na, localSize);
    
    if (na != localSize)
        printf("### ERROR ### different size\n");
    else
    {
        for (int i=0; i < na; i++)
             pa[i] = pLocal[i];
        
        delete pLocal;
        localSize = 0;
    }    
}

