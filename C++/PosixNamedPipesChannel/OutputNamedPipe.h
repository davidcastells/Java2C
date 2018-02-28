/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   OutputNamedPipe.h
 * Author: dcr
 *
 * Created on February 27, 2018, 7:40 AM
 */

#ifndef OUTPUTNAMEDPIPE_H
#define OUTPUTNAMEDPIPE_H

#include "NamedPipe.h"




class OutputNamedPipe : public NamedPipe
{
public:
    OutputNamedPipe(const char* name);
    virtual ~OutputNamedPipe();
    
public:
    void create();
    virtual void connect();
    void writeInt(int v);    
    void write(unsigned char* buffer, int len);
};

#endif /* OUTPUTNAMEDPIPE_H */

