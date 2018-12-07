package org.lowcarbon.soda.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * TODO description
 *
 * @author zhenqilai@kugou.net
 * @since 18-12-7
 */
public class AssetsUtils {

    public static String readFromAssets(Context context, String path) {
        StringBuilder result = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(path);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer, 0, buffer.length)) > 0) {
                result.append(new String(buffer, 0, len));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
