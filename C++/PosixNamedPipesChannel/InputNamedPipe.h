/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   InputNamedPipe.h
 * Author: dcr
 *
 * Created on February 27, 2018, 7:40 AM
 */

#ifndef INPUTNAMEDPIPE_H
#define INPUTNAMEDPIPE_H

#include "NamedPipe.h"


class InputNamedPipe : public NamedPipe
{
public:
    InputNamedPipe(const char* name);
    virtual ~InputNamedPipe();
    
public:
    void create();
    virtual void connect();
    int readInt();
    int read(unsigned char* buffer, int len);

};

#endif /* INPUTNAMEDPIPE_H */

