JAVAPROJECT=/home/dcr/Java2CBenchmark/java
JDK=/usr/java/jdk1.8.0_151
JDK_OS=linux
CC=gcc
CPP=g++
CXX=g++
LD=g++

CFLAGS=-g -std=c++11 -I/usr/local/include -I$(JDK)/include -I$(JDK)/include/$(JDK_OS) -m64 -O3
CPPFLAGS=-g -std=c++11  -I/usr/local/include -I$(JDK)/include -I$(JDK)/include/$(JDK_OS) -m64 -O3
LDFLAGS=-fno-stack-protector -shared -fPIC -static-libstdc++ -Wl,--no-as-needed -lstdc++

all: cat_uab_cephis_channel_JNIChannel.h JNIChannel.so
	
JNIChannel.so: JNIChannel.cpp
	gcc $(CPPFLAGS) $(LDFLAGS) JNIChannel.cpp -o JNIChannel.so

	
cat_uab_cephis_channel_JNIChannel.h:
	javah -cp $(JAVAPROJECT)/dist/Java2CBenchmark.jar cat.uab.cephis.channel.JNIChannel
	



clean:
	rm -fr *.so
	rm -fr cat_uab_cephis_channel_JNIChannel.h:
	
	
#install:
#	cp $(TARGET) /usr/local/lib/

