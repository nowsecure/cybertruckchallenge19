package org.nowsecure.cybertruck.keygenerators;

public class Utils {

    public static String printKey(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append("[ ");
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        sb.append(" ]");
        return sb.toString().toLowerCase();
    }

}
