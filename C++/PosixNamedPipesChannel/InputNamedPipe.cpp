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

#include <stdlib.h>

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
    NamedPipe::create();
}

void InputNamedPipe::connect()
{
    m_file = fopen(m_pipeName.c_str(), "rb");
    
    if (m_file == NULL)
    {
        printf("[ERROR] failed to open %s\n", m_pipeName.c_str());
        exit(-1);
    }
}

int InputNamedPipe::read(unsigned char* buffer, int len)
{
    unsigned int nRead = 0;
    unsigned int nTotalRead = 0;
    size_t ret;
    
    do
    {
        nRead = fread(&buffer[nTotalRead], 1, len, m_file);
        
        if (nRead == 0)
        {
//            printf("[ERROR] readfile %d\n", ferror(m_file));
//            exit(0);
            
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