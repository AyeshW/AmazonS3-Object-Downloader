package org.ayesh.samples;

public class DownloadController {

    public static void main(String[] args) {
        DownloadManager downloadManager = new DownloadManager("singerplus");
        downloadManager.download();
    }
}
