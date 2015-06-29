/*
 * JNIClient2.cpp
 *
 *  Created on: May 4, 2015
 *      Author: andreizimine
 */
#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/un.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <android/log.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <errno.h>

#define  LOG_TAG    "NSocket"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)

int dataLengthRest = 0;
int count = 0;

 extern "C" {
     JNIEXPORT void JNICALL Java_com_helloworld_testjni2_OfflineCppHelpSender_connectToHostJNICPP(JNIEnv * env, jobject obj, jstring port, jstring addr, jstring data, int maxDataLength);
 };

 JNIEXPORT void JNICALL Java_com_helloworld_testjni2_OfflineCppHelpSender_connectToHostJNICPP(
 	JNIEnv *env, jobject obj, jstring port, jstring addr, jstring data, int maxDataLength) {

	int sockfd;
	struct sockaddr_in serv_addr;
	const char *cPort = (*env).GetStringUTFChars(port, NULL);
	const char *cAddr = (*env).GetStringUTFChars(addr, NULL);
	const char *cData = (*env).GetStringUTFChars(data, NULL);
		sockfd = socket(AF_INET, SOCK_STREAM, 0);
	    if(sockfd<0){
	    	__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", strerror(errno));
	    }
	    	bzero(&serv_addr,sizeof(serv_addr));
	    	serv_addr.sin_family = AF_INET;
	    	serv_addr.sin_port = htons(atoi(cPort));
	    	serv_addr.sin_addr.s_addr = inet_addr(cAddr);

	    	if(connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0){
	    		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", strerror(errno));
	    	}

	    	char buf[maxDataLength] = { 0 };
	    	snprintf(buf, maxDataLength, "POST /path/script.cgi HTTP/1.0\r\nContent-Type: application/x-www-form-urlencoded\r\nContent-Length: %d\r\n\r\n%s", strlen(cData), cData);
	    	write(sockfd, buf, strlen (buf) + 1);
	    	close(sockfd);
	}
