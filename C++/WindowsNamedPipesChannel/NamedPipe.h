/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   NamedPipe.h
 * Author: dcr
 *
 * Created on February 27, 2018, 8:37 AM
 */

#ifndef NAMEDPIPE_H
#define NAMEDPIPE_H

#include <windows.h>

#include <string>

class NamedPipe {
public:
    NamedPipe();
    virtual ~NamedPipe();

public:
    void create(int dwOpenMode);
    void connect();

    
protected:

    std::string m_pipeName;
    HANDLE m_hNamedPipe;

};

#endif /* NAMEDPIPE_H */

