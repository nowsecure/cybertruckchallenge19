package org.nowsecure.cybertruck.keygenerators;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Challenge2 {
    private static final String TAG = "CyberTruckChallenge";


    public Challenge2(Context context){
        byte[] in = "uncr4ck4ble_k3yle$$".getBytes();
        byte[] key =  this.readKeyFromAssets(context);
        byte[] secret = null;

        try {

            secret = this.generateDynamicKey(in, key);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException |InvalidKeyException e ) {
            e.printStackTrace();
        }
    }

    protected byte[] readKeyFromAssets(Context context) {
        Log.d(TAG,"KEYLESS CRYPTO [2] - Unlocking carID = 2");

        StringBuilder stringBuffer = new StringBuilder();
        InputStream is = null;
        try {
            is = context.getAssets().open("ch2.key");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str = null;
        while (true) {
            try {
                if (!((str = br.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            stringBuffer.append(str);
        }

        //Log.d(TAG, String.format("KEYLESS CRYPTO [2] - Key: %s", stringBuffer.toString()));
        return stringBuffer.toString().getBytes();
    }

    /* Keys stored in plaintext at rest */
    protected byte[] generateDynamicKey(byte[] input, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {

        SecretKeySpec keySpec = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);

        byte[] out = cipher.doFinal(input);

        //Log.d(TAG, String.format("KEYLESS CRYPTO [2] - Key: %s", Utils.printKey(out)));

        return out;
    }

}
