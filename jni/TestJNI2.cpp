#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/un.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <fcntl.h>
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
//, jstring port, jstring addr, jstring data, int maxDataLength
 extern "C" {
     JNIEXPORT jstring JNICALL Java_com_helloworld_testjni2_SelectAndShare_connectToHostJNICPP(JNIEnv * env, jobject obj, jstring fileName);
 };

 JNIEXPORT jstring JNICALL Java_com_helloworld_testjni2_SelectAndShare_connectToHostJNICPP(
 	JNIEnv *env, jobject obj, jstring fileName) {
	/*int sockfd;
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
	    	close(sockfd);*/

	 	/*FILE *ptr_file;
	 	const char *name = (*env).GetStringUTFChars(fileName, NULL);
	 	//struct stat st;
	 	//stat(name, &st);
	 	//int size = st.st_size;
	 	//fseek(ptr_file, 0L, SEEK_END);
	    //int sz=ftell(ptr_file);
	    //fseek(ptr_file, 0L, SEEK_SET);
	 	struct stat stbuf;
	 	char buf[1000] = {0};
	 	char bufFinal[1000] = {0};
	 	ptr_file =fopen(name,"r");
	 	int size = 0;
	 	if (!ptr_file)
	 		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", "fel");

	 	while (fgets(buf,1000, ptr_file) != NULL){
	 		//size_t destination_size = sizeof (buf);
	 		if(size == 0)
	 			size = strlen(buf);
	 		strncat(bufFinal, buf, size + size);
	 		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", buf);
	 		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %i", size);

	 	}
	 	//__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %i", file_size);
	 	fclose(ptr_file);

	 	jstring jstrBuf = (*env).NewStringUTF(bufFinal);*/
	 	char buf[28] = {0};
	 	char bufFinal[1000] = {0};
	 	ssize_t bytes_read;
	 	int fd;
	 	int sizeOfBuf = 1000;
	 	int size = 0;
	 	const char *name = (*env).GetStringUTFChars(fileName, NULL);
	 	if ((fd = open(name, O_RDONLY)) == -1)
	 	{
	 		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", "File is not readable");
	 	}
	 	// Hantera fel? -1
	 	jbyteArray firstMacArray = (*env).NewByteArray(1000);
	 	//jbyte *bytes = env->GetByteArrayElements( firstMacArray, 0);
	 	while((bytes_read = read(fd, buf, 28)) > 0){
	 		//int convert = static_cast<int>(bytes_read);
	 		//strncat(bufFinal, buf, 27);

	 		/*int i = 0;
	 		for ( i = 0; i < bytes_read; i++ )
		 	{
		 	    if ( buf[i] == '\n' )
		 	    {
		 	        buf[i] = ' ';
		 	        //break;
		 	    }
		 	}*/
	 		//strncpy(bufFinal, buf, 28);
	 		strncat(bufFinal, buf, 28);
	 		//bytes = buf;
	 		//bufFinal[strcspn(bufFinal, "\r\n")] = 0;
	 		//strncpy(bufFinal, buf, bytes_read);
	 		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %i", bytes_read);
	 		__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", buf);
 	 	}
	 	//(*env).SetByteArrayRegion (firstMacArray, 0, 1000, reinterpret_cast<jbyte*>(bufFinal));
	 	//env->SetByteArrayRegion(firstMacArray, 0, 1000, bytes );
	 	//env->ReleaseByteArrayElements( firstMacArray, bytes, 0 );
	 	//bufFinal[strcspn(bufFinal, "\r\n")] = 0;
	 	//bufFinal[strcspn(bufFinal, "\r\n")] = {0};
	 	//string strBuf = (*env).NewString(bufFinal);

	 	jstring jstrBuf = (*env).NewStringUTF(bufFinal);

	 	close(fd);

	 	/*char buf[CHUNK];
	 	FILE *file;
	 	size_t nread;
	 	const char *name = (*env).GetStringUTFChars(fileName, NULL);
	 	file = fopen(name, "r");
	 	if (file) {
	 		while ((nread = fread(buf, 1, sizeof buf, file)) > 0){
	 			fwrite(buf, 1, nread, stdout);

	 		}
	 		if (ferror(file)) {

	 		}
	 		fclose(file);
	 	}
	 	__android_log_print(ANDROID_LOG_DEBUG, "LOG_TAG", "Log catch %s", buf);*/

	 	//free(jstrBuf);
	    return jstrBuf;
 	 }
