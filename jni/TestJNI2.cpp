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
     JNIEXPORT bool JNICALL Java_com_helloworld_testjni2_JNISender_connectToHostJNICPP(JNIEnv * env, jobject obj, jstring port, jstring addr, jstring data, int maxDataLength);
 };

 JNIEXPORT bool JNICALL Java_com_helloworld_testjni2_JNISender_connectToHostJNICPP(
 	JNIEnv *env, jobject obj, jstring port, jstring addr, jstring data, int maxDataLength) {
	int sockfd;
	bool offlineJNI;
	struct sockaddr_in serv_addr;
	const char *cPort = (*env).GetStringUTFChars(port, NULL);
	const char *cAddr = (*env).GetStringUTFChars(addr, NULL);
	const char *cData = (*env).GetStringUTFChars(data, NULL);
		sockfd = socket(AF_INET, SOCK_STREAM, 0);
	    if(sockfd<0){
	    	__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", strerror(errno));
	    	jclass cls = env->GetObjectClass(obj);
	    	jfieldID fid;
	    	jstring jstr;
	    	const char *str;
	    	jboolean connectCppException;

		  	fid = env->GetStaticFieldID(cls, "connectCppException", "Z");
		  	connectCppException = env->GetStaticBooleanField(cls, fid);
	    	env->SetStaticBooleanField(cls, fid, true);
	    	return false;
	    }
	    	bzero(&serv_addr,sizeof(serv_addr));
	    	serv_addr.sin_family = AF_INET;
	    	serv_addr.sin_port = htons(atoi(cPort));
	    	serv_addr.sin_addr.s_addr = inet_addr(cAddr);

	    	if(connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0){
	    		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", strerror(errno));
		    	jclass cls = env->GetObjectClass(obj);
		    	jfieldID fid;
		    	jstring jstr;
		    	const char *str;
		    	jboolean connectCppException;

			  	fid = env->GetStaticFieldID(cls, "connectCppException", "Z");
			  	connectCppException = env->GetStaticBooleanField(cls, fid);
		    	env->SetStaticBooleanField(cls, fid, true);
		    	return false;
	    	}

	    	char buf[maxDataLength];
	    	snprintf(buf, maxDataLength, "POST /path/script.cgi HTTP/1.0\r\nContent-Type: application/x-www-form-urlencoded\r\nContent-Length: %d\r\n\r\n%s", strlen(cData), cData);
	    	write(sockfd, buf, strlen (buf) + 1);
	    	close(sockfd);
	    	return true;
 	 }
