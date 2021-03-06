/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   main.cpp
 * Author: dcr
 *
 * Created on February 9, 2018, 11:29 AM
 */

#include <cstdlib>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <sys/types.h>
#include <time.h> 

using namespace std;

int main(int argc, char *argv[])
{
    printf("Java2CBenchmark Test - Socket Channel\n");
    
    int listenfd = 0, connfd = 0;
    struct sockaddr_in serv_addr; 

    int ret;
    
    time_t ticks; 

    listenfd = socket(AF_INET, SOCK_STREAM, 0);
    memset(&serv_addr, '0', sizeof(serv_addr));
    
    int port = 9017;
    
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(port); 

    ret = bind(listenfd, (struct sockaddr*)&serv_addr, sizeof(serv_addr)); 

    if (ret == -1)
    {
        printf("[ERROR] bind failed with error %d\n", errno);
        exit(-1);
    }
    
    ret = listen(listenfd, 10); 

    if (ret == -1)
    {
        printf("[ERROR] listen failed with error %d\n", errno);
        exit(-1);
    }
    
    printf("[INFO] listening on port %d\n", port);
    
    while(1)
    {
        connfd = accept(listenfd, (struct sockaddr*)NULL, NULL); 

        ticks = time(NULL);
        
        int v;
        
        int nRead = read(connfd, &v, 4);
        
        printf("Data Read=%08X\n", v);
        
        unsigned char* buff = new unsigned char[v];
        nRead = 0;
        int ret;
        
        do
        {
            nRead += (ret = read(connfd, &buff[nRead], v-nRead));
        } while (nRead < v && (ret != 0) && (ret != -1));
        
        if (nRead < v)
            printf("ERROR: read %d expected %d\n", nRead, v);
        else
        {
            for (int i=0; i < v; i++)
                buff[i] += 1;

            write(connfd, &v, 4);
            write(connfd, buff, v); 
        }
        
        close(connfd);
        //sleep(1);
     }
}
