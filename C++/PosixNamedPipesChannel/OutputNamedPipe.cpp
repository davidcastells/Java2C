/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   OutputNamedPipe.cpp
 * Author: dcr
 * 
 * Created on February 27, 2018, 7:40 AM
 */

#include "OutputNamedPipe.h"

#include <stdlib.h>

OutputNamedPipe::OutputNamedPipe(const char* name)
{
    m_pipeName = name;
}


OutputNamedPipe::~OutputNamedPipe()
{
}



void OutputNamedPipe::create()
{
    NamedPipe::create();
}

void OutputNamedPipe::connect()
{
    m_file = fopen(m_pipeName.c_str(), "wb");
    
    if (m_file == NULL)
    {
        printf("[ERROR] failed to open %s\n", m_pipeName.c_str());
        exit(-1);
    }
}


void OutputNamedPipe::writeInt(int v)
{
    write((unsigned char*) &v, 4);
}

void OutputNamedPipe::write(unsigned char* buffer, int len)
{
    //unsigned int nWritten;
    unsigned int nTotalWritten = 0;
    
    size_t nWritten = fwrite(buffer, 1, len, m_file);
    
    if (nWritten == 0)
    {
        int err = ferror(m_file);
        
        printf("[ERROR] WriteFile %d\n", err);
        exit(0);
    }
    
    nTotalWritten += nWritten;
    len -= nWritten;
    
    printf("written: %ld nTotalWritten: %d remaining: %d\n", nWritten, nTotalWritten, len);

    
    fflush(m_file);
}