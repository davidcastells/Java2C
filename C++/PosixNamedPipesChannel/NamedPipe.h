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

#include <stdio.h>

#include <string>

class NamedPipe {
public:
    NamedPipe();
    virtual ~NamedPipe();

public:
    void create();
    virtual void connect() = 0;

    
protected:

    std::string m_pipeName;
    FILE* m_file;

};

#endif /* NAMEDPIPE_H */

