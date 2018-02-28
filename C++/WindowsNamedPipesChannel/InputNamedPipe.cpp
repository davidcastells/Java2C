/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   InputNamedPipe.cpp
 * Author: dcr
 * 
 * Created on February 27, 2018, 7:40 AM
 */

#include "InputNamedPipe.h"

InputNamedPipe::InputNamedPipe(const char* name)
{
    m_pipeName = name;
}


InputNamedPipe::~InputNamedPipe()
{
//    disconnect();
//    close();
}


void InputNamedPipe::create()
{
    NamedPipe::create(PIPE_ACCESS_INBOUND);
}

int InputNamedPipe::read(unsigned char* buffer, int len)
{
    unsigned int nRead = 0;
    unsigned int nTotalRead = 0;
    int ret;
    
    do
    {
        ret = ReadFile(m_hNamedPipe, &buffer[nTotalRead], len, &nRead, NULL);
        
        if (ret == 0)
        {
            int err = GetLastError();
            
            if (err == ERROR_NO_DATA)
            {
                // continue
            }
            else
            {
                printf("[ERROR] readfile %d\n", err);
                exit(0);
            }
        }
        nTotalRead += nRead;
        len -= nRead;
        
        if (nRead != 0)
        printf("read: %d nTotalRead: %d remaining: %d\n", nRead, nTotalRead, len);
    } while (len > 0);
    
    return nTotalRead;
}

int InputNamedPipe::readInt()
{
    int v;
    
    int nread = read((unsigned char*)&v, 4);
    
    return v;
}