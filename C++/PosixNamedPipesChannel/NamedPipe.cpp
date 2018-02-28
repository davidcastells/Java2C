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

#include <stdlib.h>

NamedPipe::NamedPipe()
{
}


NamedPipe::~NamedPipe()
{
}

void NamedPipe::create()
{
    std::string cmd = "mkfifo " + m_pipeName;
    
    printf("[INFO] Executing %s\n", cmd.c_str());
    
    system(cmd.c_str());
}



