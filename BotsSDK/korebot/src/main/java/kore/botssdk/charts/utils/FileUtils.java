package kore.botssdk.charts.utils;

import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import kore.botssdk.charts.data.BarEntry;
import kore.botssdk.charts.data.Entry;

public class FileUtils {
    private static final String LOG = "MPChart-FileUtils";

    public FileUtils() {
    }

    public static List<Entry> loadEntriesFromFile(String path) {
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, path);
        ArrayList entries = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            while(true) {
                String line;
                while((line = br.readLine()) != null) {
                    String[] split = line.split("#");
                    if (split.length <= 2) {
                        entries.add(new Entry(Float.parseFloat(split[0]), (float)Integer.parseInt(split[1])));
                    } else {
                        float[] vals = new float[split.length - 1];

                        for(int i = 0; i < vals.length; ++i) {
                            vals[i] = Float.parseFloat(split[i]);
                        }

                        entries.add(new BarEntry((float)Integer.parseInt(split[split.length - 1]), vals));
                    }
                }

                return entries;
            }
        } catch (IOException var9) {
            Log.e("MPChart-FileUtils", var9.toString());
            return entries;
        }
    }

    public static List<Entry> loadEntriesFromAssets(AssetManager am, String path) {
        List<Entry> entries = new ArrayList();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(am.open(path), StandardCharsets.UTF_8));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] split = line.split("#");
                if (split.length <= 2) {
                    entries.add(new Entry(Float.parseFloat(split[1]), Float.parseFloat(split[0])));
                } else {
                    float[] vals = new float[split.length - 1];

                    for(int i = 0; i < vals.length; ++i) {
                        vals[i] = Float.parseFloat(split[i]);
                    }

                    entries.add(new BarEntry((float)Integer.parseInt(split[split.length - 1]), vals));
                }
            }
        } catch (IOException var16) {
            Log.e("MPChart-FileUtils", var16.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var15) {
                    Log.e("MPChart-FileUtils", var15.toString());
                }
            }

        }

        return entries;
    }

    public static void saveToSdCard(List<Entry> entries, String path) {
        File sdcard = Environment.getExternalStorageDirectory();
        File saved = new File(sdcard, path);
        if (!saved.exists()) {
            try {
                saved.createNewFile();
            } catch (IOException var7) {
                Log.e("MPChart-FileUtils", var7.toString());
            }
        }

        try {
            BufferedWriter buf = new BufferedWriter(new FileWriter(saved, true));
            Iterator var5 = entries.iterator();

            while(var5.hasNext()) {
                Entry e = (Entry)var5.next();
                buf.append(e.getY() + "#" + e.getX());
                buf.newLine();
            }

            buf.close();
        } catch (IOException var8) {
            Log.e("MPChart-FileUtils", var8.toString());
        }

    }

    public static List<BarEntry> loadBarEntriesFromAssets(AssetManager am, String path) {
        List<BarEntry> entries = new ArrayList();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(am.open(path), StandardCharsets.UTF_8));

            for(String line = reader.readLine(); line != null; line = reader.readLine()) {
                String[] split = line.split("#");
                entries.add(new BarEntry(Float.parseFloat(split[1]), Float.parseFloat(split[0])));
            }
        } catch (IOException var14) {
            Log.e("MPChart-FileUtils", var14.toString());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException var13) {
                    Log.e("MPChart-FileUtils", var13.toString());
                }
            }

        }

        return entries;
    }
}
