package org.nowsecure.cybertruck.keygenerators;

import android.util.Log;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Challenge1 {
    private static final String TAG = "CyberTruckChallenge";


    public Challenge1(){
        this.generateKey();
    }

    protected void generateKey() {
        Log.d(TAG,"KEYLESS CRYPTO [1] - Unlocking carID = 1");

        byte[] dynamicKey = null;
        byte[] in = "CyB3r_tRucK_Ch4113ng3".getBytes();

        try {

            dynamicKey = generateDynamicKey(in);

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidKeySpecException | NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }

        //Log.d(TAG, String.format("KEYLESS CRYPTO [1] - Key: %s", Utils.printKey(dynamicKey)));
    }

    /* Hardcoded key */
    protected byte[] generateDynamicKey(byte[] in) throws NoSuchAlgorithmException, InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException {
        byte[] secret = "s3cr3t$_n3veR_mUst_bE_h4rdc0d3d_m4t3!".getBytes();

        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        DESKeySpec desKeySpec = new DESKeySpec(secret);
        SecretKey skey = keyFactory.generateSecret(desKeySpec);

        Cipher ecipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, skey);
        byte[] out = ecipher.doFinal(in);

        return out;
    }

}
