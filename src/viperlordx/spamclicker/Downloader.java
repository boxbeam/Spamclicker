package viperlordx.spamclicker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {
	public Thread thread;
	public void download(URL website, File destination) {
		thread = new Thread() {
			@Override
			public void run() {
				ReadableByteChannel rbc;
				try {
					rbc = Channels.newChannel(website.openStream());
					FileOutputStream fos = new FileOutputStream(destination.getAbsolutePath());
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	public void waitForEnd() throws InterruptedException {
		thread.join();
	}
}