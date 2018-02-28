/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* 
 * File:   JNIChannel.cpp
 * Author: dcr
 *
 * Created on February 9, 2018, 12:55 PM
 */
#ifdef _WIN32
    #include <windows.h>
#endif

#include "cat_uab_cephis_channel_JNIChannel.h"

jbyte* pLocal = NULL;
jsize localSize = 0;

JNIEXPORT void JNICALL Java_cat_uab_cephis_channel_JNIChannel_nativeSend(JNIEnv *env, jobject, jbyteArray a)
{
    jbyte* pa = env->GetByteArrayElements(a, 0);
    jsize na = env->GetArrayLength(a);
    
//    printf("[JNI] na=%d localNa=%d\n", na, localSize);
    
    pLocal = new jbyte[na];
    localSize = na;
    
    for (int i=0; i < na; i++)
        pLocal[i] = pa[i] +1;
    
    env->ReleaseByteArrayElements(a, pa, JNI_ABORT);
}

/*
 * Class:     cat_uab_cephis_channel_JNIChannel
 * Method:    nativeReceive
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_cat_uab_cephis_channel_JNIChannel_nativeReceive(JNIEnv *env, jobject, jbyteArray a)
{
    jbyte* pa = env->GetByteArrayElements(a, 0);
    jsize na = env->GetArrayLength(a);
    
//    printf("[JNI] na=%d localNa=%d\n", na, localSize);
    
    if (na != localSize)
        printf("### ERROR ### different size\n");
    else
    {
        for (int i=0; i < na; i++)
             pa[i] = pLocal[i];
        
        delete pLocal;
        localSize = 0;
    }
    
    env->ReleaseByteArrayElements(a, pa, JNI_COMMIT);
}

