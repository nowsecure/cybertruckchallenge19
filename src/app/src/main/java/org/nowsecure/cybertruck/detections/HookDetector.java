package org.nowsecure.cybertruck.detections;

import android.os.Process;
import android.util.Log;
import java.net.Socket;

import 	android.os.StrictMode;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class HookDetector  {

    private static final String classname = HookDetector.class.getSimpleName();

    private static final String TAG = "CyberTruckChallenge";
    //private static int FRIDA_DEFAULT_PORT = 27042;

    //private boolean isHooked;

    public HookDetector(){
        //isHooked = false;
    }

    public boolean isFridaServerInDevice() {
        if (new File("/data/local/tmp/frida-server").exists() ||
                new File("/data/local/tmp/re.frida.server").exists() ||
                new File("/sdcard/re.frida.server").exists() ||
                new File("/sdcard/frida-server").exists()) {
            Log.d(TAG,"TAMPERPROOF [0] - Hooking detector trigger due to frida-server was found in /data/local/tmp/");
            return true;
        }
        return false;
    }

//    public void run() {
//        //Log.d(TAG,"FRIDA TEST [0] - Frida server located in /data/local/tmp/");
//
//
////        Log.d(TAG,"FRIDA TEST [9] - Frida server scanning (Java)");
////        try{
////            this.isFridaServerRunning();
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//    }


//    public boolean isFridaServerRunning() throws IOException {
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        boolean bool = false;
//        int port;
//
//        for (port=0; port < 65535; port++) {
//            bool = false;
//            try{
//                Socket socket = new Socket("127.0.0.1", port);
//                try {
//                    socket.setSoTimeout(10);
//                    socket.getOutputStream().write(0);
//                    socket.getOutputStream().write(("AUTH\r\n").getBytes("UTF-8"));
//                    byte[] response = new byte[6];
//                    if(socket.getInputStream().read(response) == 6) {
//                        if(new String(response, "UTF-8").equals("REJECT")) {
//                            socket.close();
//                            Log.d(TAG,"FRIDA DETECTED [9] - Frida server is running (Java) in port: " + port);
//                            if (port == FRIDA_DEFAULT_PORT) {
//                                Log.d(TAG,"FRIDA DETECTED [9] - Frida server is running (Java) in default port: " + port);
//                            }
//                            return true;
//                        }
//                        socket.close();
//                    }
//                    else {
//                        socket.close();
//                    }
//                } catch (Exception e) {
//                    //e.printStackTrace();
//                }
//            } catch (Exception e) {
//                //e.printStackTrace();
//            }
//        }
//        return bool;
//    }

}
