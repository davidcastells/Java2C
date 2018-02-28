/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   NamedPipe.cpp
 * Author: dcr
 * 
 * Created on February 27, 2018, 8:37 AM
 */

#include "NamedPipe.h"

NamedPipe::NamedPipe()
{
}


NamedPipe::~NamedPipe()
{
}

void NamedPipe::create(int dwOpenMode)
{
    m_hNamedPipe = CreateNamedPipe(m_pipeName.c_str(),
            dwOpenMode,        // dwOpenMode
            PIPE_TYPE_BYTE | PIPE_READMODE_BYTE | PIPE_NOWAIT,    // dwPipeMode
            1,    // nMaxInstances,
            0x10000000,    // nOutBufferSize,
            0x10000000,    // nInBufferSize,
            1000,    // nDefaultTimeOut,
            NULL);    // lpSecurityAttributes
        
    if (m_hNamedPipe == INVALID_HANDLE_VALUE)
    {
        printf("[ERROR] Failed to open pipe %s\n", m_pipeName.c_str());
        exit(-1);
    }
}

void NamedPipe::connect()
{
    printf("[Client] Await client connection to pipe %s\n", m_pipeName.c_str());
    int ret = 0;
    bool loop = true;
    
    do 
    {
        ret = ConnectNamedPipe(m_hNamedPipe, NULL);

        if (ret == 0)
        {
            int err = GetLastError();
            
            if (err == ERROR_PIPE_CONNECTED)
            {
                // Successfully connected
                loop = false;
            }
            else if (err != ERROR_PIPE_LISTENING)
            {
                printf("[ERROR] Failed to connect to pipe %s error %d\n", m_pipeName.c_str(), err);
                exit(-1);
            }
        }    
    } while (loop);
    
    printf("[Client] client connected to pipe %s\n", m_pipeName.c_str());
}

