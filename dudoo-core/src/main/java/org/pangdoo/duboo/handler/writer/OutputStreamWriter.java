package org.pangdoo.duboo.handler.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.pangdoo.duboo.util.LogLogger;

public class OutputStreamWriter {
	
	private LogLogger logger = LogLogger.getLogger(OutputStreamWriter.class);
	
	private FileOutputStream fos;
	private FileChannel fileChannel;
	
	private int bufferSize = 1024*1024;
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public static OutputStreamWriter newInstance(String pathname) {
		return new OutputStreamWriter(pathname);
	}
	
	public static OutputStreamWriter newInstance(String path, String fileName) {
		return new OutputStreamWriter(path, fileName);
	}
	
	private OutputStreamWriter(String pathname) {
		try {
			fos = new FileOutputStream(new File(pathname));
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	private OutputStreamWriter(String path, String fileName) {
		try {
			File filePath = new File(path);
			if (!filePath.isDirectory()) {
				filePath.mkdir();
			}
			String pathname = path + "/" + fileName;
			fos = new FileOutputStream(new File(pathname));
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	public void write(InputStream inputStream) {
		try {
			fileChannel = fos.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(this.bufferSize);
			byte[] bytes = new byte[this.bufferSize];
			fileChannel.force(true);
			int off = -1;
			while ((off = inputStream.read(bytes)) != -1) {
				byteBuffer.clear();
				byteBuffer.put(bytes, 0 , off);
				byteBuffer.flip();
				fileChannel.write(byteBuffer);
			}
		} catch (Exception e) {
			logger.warn(e);
		}
	}
	
	public void close() {
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
