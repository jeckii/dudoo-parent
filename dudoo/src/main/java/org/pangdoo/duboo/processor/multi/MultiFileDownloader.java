package org.pangdoo.duboo.processor.multi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.pangdoo.duboo.util.LogLogger;

public class MultiFileDownloader {
	
	private final static LogLogger logger = LogLogger.getLogger(MultiFileDownloader.class);
	
	private FileOutputStream fos;

	private FileChannel fileChannel;

	private static final int DEFAULT_BUFFER_SIZE = 1 << 20;

	private static final int MINIMUM_BUFFER_SIZE = 1 << 10;

	public static MultiFileDownloader downloader(String path) {
		return new MultiFileDownloader(path);
	}
	
	public static MultiFileDownloader downloader(String path, String fileName) {
		return new MultiFileDownloader(path, fileName);
	}
	
	private MultiFileDownloader(String path) {
		try {
			fos = new FileOutputStream(new File(path));
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	private MultiFileDownloader(String path, String fileName) {
		try {
			File filePath = new File(path);
			if (!filePath.isDirectory()) {
				filePath.mkdir();
			}
			fos = new FileOutputStream(new File(path + File.separatorChar + fileName));
		} catch (Exception e) {
			logger.warn(e);
		}
	}

	public void download(InputStream input) {
		download(input, -1);
	}

	public void download(InputStream input, int bufferSize) {
		try {
			fileChannel = fos.getChannel();
			if (bufferSize < MINIMUM_BUFFER_SIZE) {
				bufferSize = DEFAULT_BUFFER_SIZE;
			}
			ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);
			byte[] bytes = new byte[bufferSize];
			fileChannel.force(true);
			int off = -1;
			while ((off = input.read(bytes)) != -1) {
				byteBuffer.clear();
				byteBuffer.put(bytes, 0 , off);
				byteBuffer.flip();
				fileChannel.write(byteBuffer);
			}
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	public void shutdown() {
		try {
			if (fileChannel != null) {
				fileChannel.close();
			}
			if (fos != null) {
				fos.close();
			}
		} catch (Exception e) {
			logger.warn(e);
		}
	}

}
