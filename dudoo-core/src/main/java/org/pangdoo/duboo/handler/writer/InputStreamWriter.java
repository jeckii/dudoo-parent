package org.pangdoo.duboo.handler.writer;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class InputStreamWriter {
	
	private RandomAccessFile file;
	private FileChannel fileChannel;
	
	private int bufferSize = 1024*1024;
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public static InputStreamWriter newInstance(String path, String fileName) {
		return new InputStreamWriter(path, fileName);
	}
	
	private InputStreamWriter(String path, String fileName) {
		try {
			file = new RandomAccessFile(new File(path + File.separator + fileName), "rw");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void write(InputStream inputStream) {
		fileChannel = file.getChannel();
		ByteBuffer byteBuffer = ByteBuffer.allocate(this.bufferSize);
		byte[] bytes = new byte[this.bufferSize];
		try {
			while (inputStream.read(bytes) != -1) {
				byteBuffer.clear();
				byteBuffer.put(bytes);
				byteBuffer.flip();
				fileChannel.write(byteBuffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			if (fileChannel != null) {
				fileChannel.close();
			}
			if (file != null) {
				file.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
