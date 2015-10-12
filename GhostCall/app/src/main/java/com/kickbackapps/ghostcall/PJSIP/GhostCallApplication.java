package com.kickbackapps.ghostcall.PJSIP;

import android.app.Application;

/**
 * Created by Ynott on 10/6/15.
 */
public class GhostCallApplication extends Application {
//    static {
//        System.loadLibrary("pjsua2");
//        System.out.println("Library loaded");
//    }

//    public static Endpoint endpoint = new Endpoint();
//    private EpConfig epConfig = new EpConfig();
//    private TransportConfig sipTpConfig = new TransportConfig();
//
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        try {
//            endpoint.libCreate();
//        } catch (Exception e) {
//            return;
//        }
//        int SIP_PORT = 5080;
//        int LOG_LEVEL = 4;
//        sipTpConfig.setPort(SIP_PORT);
//
//        try {
//            endpoint.libInit(epConfig);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void flushNativeDeinit() {
//        Runtime.getRuntime().gc();
//
//        try {
//            endpoint.libDestroy();
//        } catch (Exception e) {
//        }
//
//        endpoint.delete();
//        endpoint = null;
//    }
}
