package org.pangdoo.duboo.handler.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.pangdoo.duboo.exception.ReaderException;
import org.pangdoo.duboo.util.LogLogger;
import org.pangdoo.duboo.util.StringUtils;

public class HTMLReader {
	
	private LogLogger logger = LogLogger.getLogger(HTMLReader.class);
	
	public static HTMLReader newInstance() {
		return new HTMLReader();
	}
	
	public static HTMLReader newInstance(InputStream input, String charsetName, String baseUri) {
		return new HTMLReader(input, charsetName, baseUri);
	}
	
	public static HTMLReader newInstance(String html, String charsetName) {
		return new HTMLReader(html, charsetName);
	}
	
	private Document doc;
	
	private HTMLReader() {}
	
	private HTMLReader(InputStream input, String charsetName, String baseUri) {
		try {
			if (input == null) {
				throw new ReaderException("Input is null.");
			}
		} catch (ReaderException e) {
			logger.warn(e);
		}
		try {
			doc = Jsoup.parse(input, charsetName, baseUri);
		} catch (IOException e) {
			logger.warn(e);
		}
	}
	
	private HTMLReader(String html, String charsetName) {
		try {
			if (StringUtils.isEmpty(html)) {
				throw new ReaderException("Html is null.");
			}
		} catch (ReaderException e) {
			logger.warn(e);
		}
		doc = Jsoup.parse(html, charsetName);
	}
	
	public void parseSegment(String bodyHtml, String charsetName) {
		try {
			if (doc == null) {
				doc = Jsoup.parseBodyFragment(bodyHtml, charsetName);
			} else {
				// The Document has been created
				throw new ReaderException("Document has been created.");
			}
		} catch (ReaderException e) {
			logger.warn(e);
		}
	}
	
	public Document getDocument() {
		return doc;
	}
	
	public Node html() {
		List<Node> nodes = doc.childNodes();
		for (Node node : nodes) {
			if (node.nodeName().equalsIgnoreCase("html")) {
				return node;
			}
		}
		return null;
	}
	
	public Node body() {
		return doc.body();
	}
	
	public List<String> textSegments(Node node) {
		List<Node> nodes = node.childNodes();
		List<String> list = new ArrayList<String>(nodes.size());
		for (Node n : nodes) {
			if (n.nodeName().equalsIgnoreCase("#text")) {
				list.add(n.outerHtml());
			}
		}
		return list;
	}
	
	public List<Node> segmentsNode(Node node, String nodeName) {
		List<Node> nodes = node.childNodes();
		List<Node> list = new ArrayList<Node>(nodes.size());
		for (Node n : nodes) {
			if (n.nodeName().equalsIgnoreCase(nodeName)) {
				list.add(n);
			}
		}
		return list;
	}
	
	public List<String> segments(Node node, String nodeName) {
		List<Node> nodes = node.childNodes();
		List<String> list = new ArrayList<String>(nodes.size());
		StringBuilder sb = new StringBuilder();
		for (Node n : nodes) {
			if (n.nodeName().equalsIgnoreCase(nodeName)) {
				List<String> texts = textSegments(n);
				for(String text : texts) {
					sb.append(text);
				}
				list.add(sb.toString());
				sb.delete(0, sb.length());
			}
		}
		return list;
	}
	
}