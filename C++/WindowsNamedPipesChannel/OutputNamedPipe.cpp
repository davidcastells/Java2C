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


OutputNamedPipe::OutputNamedPipe(const char* name)
{
    m_pipeName = name;
}


OutputNamedPipe::~OutputNamedPipe()
{
}



void OutputNamedPipe::create()
{
    NamedPipe::create(PIPE_ACCESS_OUTBOUND);
}


void OutputNamedPipe::writeInt(int v)
{
    write((unsigned char*) &v, 4);
}

void OutputNamedPipe::write(unsigned char* buffer, int len)
{
    unsigned int nWritten;
    unsigned int nTotalWritten = 0;
    
    int ret = WriteFile(m_hNamedPipe, buffer, len, &nWritten, NULL);
    
    if (ret == 0)
    {
        unsigned int err = GetLastError();
        
        printf("[ERROR] WriteFile %d\n", err);
        exit(0);
    }
    
    nTotalWritten += nWritten;
    len -= nWritten;
    
    printf("written: %d nTotalWritten: %d remaining: %d\n", nWritten, nTotalWritten, len);

}