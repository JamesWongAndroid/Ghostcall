/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package org.pjsip.pjsua2;

public class LogEntry {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected LogEntry(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(LogEntry obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        pjsua2JNI.delete_LogEntry(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public void setLevel(int value) {
    pjsua2JNI.LogEntry_level_set(swigCPtr, this, value);
  }

  public int getLevel() {
    return pjsua2JNI.LogEntry_level_get(swigCPtr, this);
  }

  public void setMsg(String value) {
    pjsua2JNI.LogEntry_msg_set(swigCPtr, this, value);
  }

  public String getMsg() {
    return pjsua2JNI.LogEntry_msg_get(swigCPtr, this);
  }

  public void setThreadId(int value) {
    pjsua2JNI.LogEntry_threadId_set(swigCPtr, this, value);
  }

  public int getThreadId() {
    return pjsua2JNI.LogEntry_threadId_get(swigCPtr, this);
  }

  public void setThreadName(String value) {
    pjsua2JNI.LogEntry_threadName_set(swigCPtr, this, value);
  }

  public String getThreadName() {
    return pjsua2JNI.LogEntry_threadName_get(swigCPtr, this);
  }

  public LogEntry() {
    this(pjsua2JNI.new_LogEntry(), true);
  }

}
