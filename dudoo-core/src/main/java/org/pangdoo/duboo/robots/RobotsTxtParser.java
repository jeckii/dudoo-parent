package org.pangdoo.duboo.robots;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pangdoo.duboo.util.StringUtils;

public class RobotsTxtParser {
	
	private byte[] bytes;
	private String charsetName;
	
	public RobotsTxtParser(InputStream input, String charsetName) throws IOException {
		int toIndex = 0;
		int index = 0;
		byte[] temp = new byte[1024];
		while ((index = input.read(temp)) != -1) {
			toIndex = toIndex + index;
			if (bytes == null) {
				bytes = new byte[toIndex];
			} else {
				bytes = Arrays.copyOf(bytes, toIndex);
			}
			System.arraycopy(temp, 0, bytes, toIndex - index, index);
		}
		this.charsetName = charsetName;
	}
	
	public String getContent() {
		try {
			return new String(this.bytes, this.charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] getBytes() {
		return this.bytes;
	}
	
	public Map<String, List<String>> items(String userAgent) {
		if (StringUtils.isBlank(userAgent)) {
			throw new IllegalArgumentException("User agent is null.");
		}
		String content = getContent();
		String[] rows = content.split("\n");
		Map<String, List<String>> items = new HashMap<String, List<String>>();
		boolean ignore = false;
		for (String row : rows) {
			row = row.trim();
			if (row.startsWith("#") || StringUtils.isBlank(row)) {
				continue;
			}
			int separateIndex = row.indexOf(":");
			if (separateIndex == -1) {
				continue;
			}
			String key = row.substring(0, separateIndex).trim().toLowerCase();
			String value = row.substring(separateIndex + 1).trim();
			if ("user-agent".equals(key)) {
				if ("*".equals(value) || userAgent.equals(value)) {
					ignore = false;
				} else {
					ignore = true;
				}
			} else if (ignore && !"disallow".equals(key)
					&& !"allow".equals(key)) {
				ignore = false;
			}
			if (!ignore) {
				List<String> item = items.get(key);
				if (item == null) {
					item = new ArrayList<String>();
				}
				item.add(value);
				items.put(key, item);
			}
		}
		return items;
	}
	
}
