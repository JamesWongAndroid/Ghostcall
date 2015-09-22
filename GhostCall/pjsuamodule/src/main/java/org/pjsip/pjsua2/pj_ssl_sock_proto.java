/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public final class pj_ssl_sock_proto {
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_DEFAULT = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_DEFAULT", pjsua2JNI.PJ_SSL_SOCK_PROTO_DEFAULT_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_SSL2 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_SSL2", pjsua2JNI.PJ_SSL_SOCK_PROTO_SSL2_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_SSL3 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_SSL3", pjsua2JNI.PJ_SSL_SOCK_PROTO_SSL3_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_TLS1 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_TLS1", pjsua2JNI.PJ_SSL_SOCK_PROTO_TLS1_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_TLS1_1 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_TLS1_1", pjsua2JNI.PJ_SSL_SOCK_PROTO_TLS1_1_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_TLS1_2 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_TLS1_2", pjsua2JNI.PJ_SSL_SOCK_PROTO_TLS1_2_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_SSL23 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_SSL23", pjsua2JNI.PJ_SSL_SOCK_PROTO_SSL23_get());
  public final static pj_ssl_sock_proto PJ_SSL_SOCK_PROTO_DTLS1 = new pj_ssl_sock_proto("PJ_SSL_SOCK_PROTO_DTLS1", pjsua2JNI.PJ_SSL_SOCK_PROTO_DTLS1_get());

  public final int swigValue() {
    return swigValue;
  }

  public String toString() {
    return swigName;
  }

  public static pj_ssl_sock_proto swigToEnum(int swigValue) {
    if (swigValue < swigValues.length && swigValue >= 0 && swigValues[swigValue].swigValue == swigValue)
      return swigValues[swigValue];
    for (int i = 0; i < swigValues.length; i++)
      if (swigValues[i].swigValue == swigValue)
        return swigValues[i];
    throw new IllegalArgumentException("No enum " + pj_ssl_sock_proto.class + " with value " + swigValue);
  }

  private pj_ssl_sock_proto(String swigName) {
    this.swigName = swigName;
    this.swigValue = swigNext++;
  }

  private pj_ssl_sock_proto(String swigName, int swigValue) {
    this.swigName = swigName;
    this.swigValue = swigValue;
    swigNext = swigValue+1;
  }

  private pj_ssl_sock_proto(String swigName, pj_ssl_sock_proto swigEnum) {
    this.swigName = swigName;
    this.swigValue = swigEnum.swigValue;
    swigNext = this.swigValue+1;
  }

  private static pj_ssl_sock_proto[] swigValues = { PJ_SSL_SOCK_PROTO_DEFAULT, PJ_SSL_SOCK_PROTO_SSL2, PJ_SSL_SOCK_PROTO_SSL3, PJ_SSL_SOCK_PROTO_TLS1, PJ_SSL_SOCK_PROTO_TLS1_1, PJ_SSL_SOCK_PROTO_TLS1_2, PJ_SSL_SOCK_PROTO_SSL23, PJ_SSL_SOCK_PROTO_DTLS1 };
  private static int swigNext = 0;
  private final int swigValue;
  private final String swigName;
}

