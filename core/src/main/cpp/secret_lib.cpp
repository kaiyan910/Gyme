#include <jni.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>

#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, "NDK", __VA_ARGS__)

const char *app_signature_sha1_debug = "7D5D6015614DC1FB69287BFC15A7F33F86B68E29";

const char HexCode[] = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

static bool is_valid = false;

static void verifySign(JNIEnv *env);

static jobject getApplication(JNIEnv *env) {
    jobject application = NULL;
    jclass activity_thread_clz = env->FindClass("android/app/ActivityThread");
    if (activity_thread_clz != NULL) {
        jmethodID currentApplication = env->GetStaticMethodID(
                activity_thread_clz, "currentApplication", "()Landroid/app/Application;");
        if (currentApplication != NULL) {
            application = env->CallStaticObjectMethod(activity_thread_clz, currentApplication);
        } else {
            LOGE("Cannot find method: currentApplication() in ActivityThread.");
        }
        env->DeleteLocalRef(activity_thread_clz);
    } else {
        LOGE("Cannot find class: android.app.ActivityThread");
    }

    return application;
}

extern "C"
JNIEXPORT
jint JNI_OnLoad(JavaVM *vm, void *res) {

    JNIEnv *env = NULL;

    if (vm->GetEnv((void **) &env, JNI_VERSION_1_4) != JNI_OK) {
        return JNI_ERR;
    }

    verifySign(env);

    return JNI_VERSION_1_4;
}

static void verifySign(JNIEnv *env) {

    //jclass context_class = env->GetObjectClass(context_object);

    // Application object
    jobject application = getApplication(env);
    // Context(ContextWrapper) class
    jclass context_class = env->GetObjectClass(application);

    //context.getPackageManager()
    jmethodID methodId = env->GetMethodID(context_class, "getPackageManager", "()Landroid/content/pm/PackageManager;");
    jobject package_manager_object = env->CallObjectMethod(application, methodId);
    if (package_manager_object == NULL) {
        LOGE("getPackageManager() Failed!");
        return;
    }

    //context.getPackageName()
    methodId = env->GetMethodID(context_class, "getPackageName", "()Ljava/lang/String;");
    jstring package_name_string = (jstring) env->CallObjectMethod(application, methodId);
    if (package_name_string == NULL) {
        LOGE("getPackageName() Failed!");
        return;
    }
    env->DeleteLocalRef(context_class);

    //PackageManager.getPackageInfo(Sting, int)
    jclass pack_manager_class = env->GetObjectClass(package_manager_object);
    methodId = env->GetMethodID(pack_manager_class, "getPackageInfo", "(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;");
    env->DeleteLocalRef(pack_manager_class);
    jobject package_info_object = env->CallObjectMethod(package_manager_object, methodId, package_name_string, 0x40);
    if (package_info_object == NULL) {
        LOGE("getPackageInfo() Failed!");
        return;
    }
    env->DeleteLocalRef(package_manager_object);

    //PackageInfo.signatures[0]
    jclass package_info_class = env->GetObjectClass(package_info_object);
    jfieldID fieldId = env->GetFieldID(package_info_class, "signatures", "[Landroid/content/pm/Signature;");
    env->DeleteLocalRef(package_info_class);
    jobjectArray signature_object_array = (jobjectArray) env->GetObjectField(package_info_object, fieldId);
    if (signature_object_array == NULL) {
        LOGE("PackageInfo.signatures[] is null");
        return;
    }
    jobject signature_object = env->GetObjectArrayElement(signature_object_array, 0);
    env->DeleteLocalRef(package_info_object);

    //Signature.toByteArray()
    jclass signature_class = env->GetObjectClass(signature_object);
    methodId = env->GetMethodID(signature_class, "toByteArray", "()[B");
    env->DeleteLocalRef(signature_class);
    jbyteArray signature_byte = (jbyteArray) env->CallObjectMethod(signature_object, methodId);

    //new ByteArrayInputStream
    jclass byte_array_input_class = env->FindClass("java/io/ByteArrayInputStream");
    methodId = env->GetMethodID(byte_array_input_class, "<init>", "([B)V");
    jobject byte_array_input = env->NewObject(byte_array_input_class, methodId, signature_byte);

    //CertificateFactory.getInstance("X.509")
    jclass certificate_factory_class = env->FindClass("java/security/cert/CertificateFactory");
    methodId = env->GetStaticMethodID(certificate_factory_class, "getInstance", "(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;");
    jstring x_509_jstring = env->NewStringUTF("X.509");
    jobject cert_factory = env->CallStaticObjectMethod(certificate_factory_class, methodId, x_509_jstring);

    //certFactory.generateCertificate(byteIn);
    methodId = env->GetMethodID(certificate_factory_class, "generateCertificate", ("(Ljava/io/InputStream;)Ljava/security/cert/Certificate;"));
    jobject x509_cert = env->CallObjectMethod(cert_factory, methodId, byte_array_input);
    env->DeleteLocalRef(certificate_factory_class);

    //cert.getEncoded()
    jclass x509_cert_class = env->GetObjectClass(x509_cert);
    methodId = env->GetMethodID(x509_cert_class, "getEncoded", "()[B");
    jbyteArray cert_byte = (jbyteArray) env->CallObjectMethod(x509_cert, methodId);
    env->DeleteLocalRef(x509_cert_class);

    //MessageDigest.getInstance("SHA1")
    jclass message_digest_class = env->FindClass("java/security/MessageDigest");
    methodId = env->GetStaticMethodID(message_digest_class, "getInstance", "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    jstring sha1_jstring = env->NewStringUTF("SHA1");
    jobject sha1_digest = env->CallStaticObjectMethod(message_digest_class, methodId, sha1_jstring);

    //sha1.digest (certByte)
    methodId = env->GetMethodID(message_digest_class, "digest", "([B)[B");
    jbyteArray sha1_byte = (jbyteArray) env->CallObjectMethod(sha1_digest, methodId, cert_byte);
    env->DeleteLocalRef(message_digest_class);

    //toHexString
    jsize array_size = env->GetArrayLength(sha1_byte);
    jbyte *sha1 = env->GetByteArrayElements(sha1_byte, NULL);

    char hex_sha[array_size*2+1];

    for (int i = 0; i < array_size; ++i) {
        hex_sha[2*i] = HexCode[((unsigned char)sha1[i])/16];
        hex_sha[2*i+1] = HexCode[((unsigned char)sha1[i])%16];
    }
    hex_sha[array_size * 2] = '\0';

    if (strcmp(hex_sha, app_signature_sha1_debug) == 0) {
        is_valid = true;
    } else {
        LOGE("FBI is watching you.");
    }

    env->DeleteLocalRef(application);

    return;
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_architecture_kotlinmvvm_Secret_lock(JNIEnv *env, jclass type) {
    if (is_valid) {
        return env->NewStringUTF("aSecretKey2");
    } else {
        return env->NewStringUTF("aSecretKey");
    }
}