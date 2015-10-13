package com.kickbackapps.ghostcall;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.pjsip.pjsua2.Account;
import org.pjsip.pjsua2.AccountConfig;
import org.pjsip.pjsua2.AccountInfo;
import org.pjsip.pjsua2.AudioMedia;
import org.pjsip.pjsua2.AuthCredInfo;
import org.pjsip.pjsua2.Call;
import org.pjsip.pjsua2.CallInfo;
import org.pjsip.pjsua2.CallMediaInfo;
import org.pjsip.pjsua2.CallMediaInfoVector;
import org.pjsip.pjsua2.CallOpParam;
import org.pjsip.pjsua2.Endpoint;
import org.pjsip.pjsua2.EpConfig;
import org.pjsip.pjsua2.Media;
import org.pjsip.pjsua2.OnCallMediaStateParam;
import org.pjsip.pjsua2.OnCallStateParam;
import org.pjsip.pjsua2.TransportConfig;
import org.pjsip.pjsua2.pjmedia_type;
import org.pjsip.pjsua2.pjsip_inv_state;
import org.pjsip.pjsua2.pjsip_transport_type_e;
import org.pjsip.pjsua2.pjsua_call_media_status;

public class TestPjsipScreen extends AppCompatActivity {

    static {
        System.loadLibrary("pjsua2");
        System.out.println("Library loaded");
    }

    Button makeCallButton;
    String host = "sip.ghostcall.in";
    String username = "48244-4824";
    String password = "0a6aea1ee2a4aa53e486dfaa0fcdaafc3be644f46e7d1383acbe8961ca4066bc";
    Account acc;
    Endpoint ep;
    AccountInfo info;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pjsip_screen);

        try {
            ep = new Endpoint();
            ep.libCreate();
            EpConfig epConfig = new EpConfig();
            ep.libInit(epConfig);
            TransportConfig sipTpConfig = new TransportConfig();
            sipTpConfig.setPort(5060);
            ep.transportCreate(pjsip_transport_type_e.PJSIP_TRANSPORT_UDP, sipTpConfig);
            ep.libStart();

            AccountConfig acfg = new AccountConfig();
            acfg.setIdUri("sip:" + username);
            acfg.getRegConfig().setRegistrarUri("sip:"+host);
            AuthCredInfo cred = new AuthCredInfo("plain", "*", username, 0, password);
            acfg.getSipConfig().getAuthCreds().add(cred);
            acc = new Account();
            acc.create(acfg);
            Thread.sleep(1000);


        } catch (Exception e) {
            Log.d("TEST PJSIP ERROR", e.getMessage());
        }


        makeCallButton = (Button) findViewById(R.id.testpjsipButton);
        makeCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                 info = acc.getInfo();
                    Log.d("is reg active: ", Boolean.toString(info.getRegIsActive()));
                    Log.d("reg status: ", info.getRegStatusText());

                } catch (Exception e) {
                    System.out.print(e.getMessage());
                }

                MyCall call = new MyCall(acc, -1);
                CallOpParam prm = new CallOpParam(true);

                try {
                    call.makeCall("sip:627504@sip.ghostcall.in", prm);
                    makeCallButton.setVisibility(View.GONE);
                } catch (Exception e) {
                    call.delete();
                    makeCallButton.setVisibility(View.VISIBLE);
                    Log.d("TEST PJSIP ERROR", e.getMessage());
                }


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            acc.delete();
            ep.libDestroy();
            ep.delete();
        } catch (Exception e) {

        }
    }

    public class MyCall extends Call {

        public MyCall(Account acc, int call_id) {
            super(acc, call_id);
        }

        @Override
        public void onCallState(OnCallStateParam prm) {
            try {
                CallInfo ci = getInfo();
                if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_DISCONNECTED) {
                    this.delete();
                } else if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONNECTING || ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CALLING) {
                    ensureMediaPlayerDeath();
                    mediaPlayer = MediaPlayer.create(TestPjsipScreen.this, R.raw.ring);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } else if (ci.getState() == pjsip_inv_state.PJSIP_INV_STATE_CONFIRMED) {
                    ensureMediaPlayerDeath();
                }
            } catch (Exception e) {
                System.out.print(e.getMessage());
                return;
            }
        }

        private void ensureMediaPlayerDeath() {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        @Override
        public void onCallMediaState(OnCallMediaStateParam prm) {
            CallInfo ci;
            try {
                ci = getInfo();
            } catch (Exception e) {
                System.out.print(e.getMessage());
                return;
            }

            CallMediaInfoVector cmiv = ci.getMedia();
            for (int i = 0; i < cmiv.size(); i++) {
                CallMediaInfo cmi = cmiv.get(i);
                if (cmi.getType() == pjmedia_type.PJMEDIA_TYPE_AUDIO && (cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_ACTIVE ||
                        cmi.getStatus() == pjsua_call_media_status.PJSUA_CALL_MEDIA_REMOTE_HOLD)) {
                    Media m = getMedia(i);
                    AudioMedia am = AudioMedia.typecastFromMedia(m);
                    try {
                        ep.audDevManager().getCaptureDevMedia().startTransmit(am);
                        am.startTransmit(ep.audDevManager().getPlaybackDevMedia());
                    } catch (Exception e) {
                        System.out.print(e.getMessage());
                        continue;
                    }

                }
            }
        }
    }
}
