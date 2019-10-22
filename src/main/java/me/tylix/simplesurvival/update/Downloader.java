package me.tylix.simplesurvival.update;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader implements Runnable {

    private final String name, link;
    private final File out;
    private final boolean silent;

    public Downloader(String name, String link, File out, boolean silent) {
        this.name = name;
        this.link = link;
        this.out = out;
        this.silent = silent;
        if (!silent)
            System.out.println("Downloading " + this.name + "...");
    }

    @Override
    public void run() {
        try {
            URL url = new URL(this.link);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(this.out);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = bufferedInputStream.read(buffer, 0, 1024)) > 0)
                bufferedOutputStream.write(buffer, 0, read);
            bufferedOutputStream.close();
            bufferedInputStream.close();
            if (!this.silent)
                System.out.println("Download of " + this.name + " complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}