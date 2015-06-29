/*
 * JNIClient.cpp
 *
 *  Created on: May 3, 2015
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
     JNIEXPORT void JNICALL Java_com_helloworld_testjni2_TempFolder_accessFields(JNIEnv * env, jobject obj);
 };

 JNIEXPORT void JNICALL Java_com_helloworld_testjni2_TempFolder_accessFields(
 	JNIEnv *env, jobject obj) {
	  jclass cls = env->GetObjectClass(obj);
	  jfieldID fid;
	  jstring jstr;
	  const char *str;
	  jboolean connectCppException;

	  printf("In C:\n");

	  fid = env->GetStaticFieldID(cls, "connectCppException", "Z");
	  if (fid == 0) {
	    return;
	  }
	  connectCppException = env->GetStaticBooleanField(cls, fid);
	  //printf("  FieldAccess.si = %%Z", connectException);
	  env->SetStaticBooleanField(cls, fid, true);

	  /*fid = env->GetFieldID(cls, "z", "Ljava/lang/String;");
	  if (fid == 0) {
	    return;
	  }
	  jstr = (jstring) env->GetObjectField(obj, fid);
	  str = env->GetStringUTFChars(jstr, 0);
	  printf("  c.s = \"%s\"\n", str);
	  env->ReleaseStringUTFChars(jstr, str);

	  jstr = env->NewStringUTF("123");
	  env->SetObjectField(obj, fid, jstr);*/
 }


