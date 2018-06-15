package org.pangdoo.duboo.handler.writer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class InputStreamWriter {
	
	private FileOutputStream fos;
	private FileChannel fileChannel;
	
	private int bufferSize = 1024*1024;
	
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public static InputStreamWriter newInstance(String pathname) {
		return new InputStreamWriter(pathname);
	}
	
	public static InputStreamWriter newInstance(String path, String fileName) {
		return new InputStreamWriter(path, fileName);
	}
	
	private InputStreamWriter(String pathname) {
		try {
			fos = new FileOutputStream(new File(pathname));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private InputStreamWriter(String path, String fileName) {
		try {
			File filePath = new File(path);
			if (!filePath.isDirectory()) {
				filePath.mkdir();
			}
			String pathname = path + "/" + fileName;
			fos = new FileOutputStream(new File(pathname));
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

}
