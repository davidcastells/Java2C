/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.c
 * Author: dcr
 *
 * Created on February 27, 2018, 7:38 AM
 */

#include <stdio.h>
#include <stdlib.h>

#include "InputNamedPipe.h"
#include "OutputNamedPipe.h"

/*
 * 
 */
int main(int argc, char** argv)
{
    printf("[INFO] Posix Named Pipes Server\n");
    std::string fifo = "fifo_channel";
    std::string txFifo = "/tmp/" + fifo + "_tx";
    std::string rxFifo = "/tmp/" + fifo + "_rx";

    InputNamedPipe inPipe(txFifo.c_str());
    OutputNamedPipe outPipe(rxFifo.c_str());

    inPipe.create();
    outPipe.create();
    inPipe.connect();
    outPipe.connect();
    
    while (1)
    {
        int num = inPipe.readInt();
        
        printf("RX size: %d\n", num);
        
        unsigned char buffer[num];
        
        int nRead = inPipe.read(buffer, num);
        
        
        for (int i=0; i < num; i++)
            buffer[i] += 1;

        outPipe.writeInt(num);
        outPipe.write(buffer, num);
        
//        printf("TxData: ");
//        
//        for (int i=0; i < num; i++)
//            printf("%02X ", buffer[i]);
//        
//        printf("\n");
    }
    
    return (EXIT_SUCCESS);
}

