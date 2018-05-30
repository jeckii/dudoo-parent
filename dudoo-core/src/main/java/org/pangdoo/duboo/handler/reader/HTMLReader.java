package org.pangdoo.duboo.handler.reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.pangdoo.duboo.exception.FileReaderException;
import org.pangdoo.duboo.util.StringUtils;

public class HTMLReader {
	
	private Document doc;
	
	public HTMLReader() {}
	
	public HTMLReader(InputStream input, String charsetName, String baseUri) throws FileReaderException, IOException {
		if (input == null) {
			throw new FileReaderException("Input is null.");
		}
		doc = Jsoup.parse(input, charsetName, baseUri);
	}
	
	public HTMLReader(String html, String charsetName) throws FileReaderException {
		if (StringUtils.isEmpty(html)) {
			throw new FileReaderException("Html is null.");
		}
		doc = Jsoup.parse(html, charsetName);
	}
	
	public void parseSegment(String bodyHtml, String charsetName) {
		try {
			if (doc == null) {
				doc = Jsoup.parseBodyFragment(bodyHtml, charsetName);
			} else {
				// The Document has been created
				throw new FileReaderException("Document has been created.");
			}
		} catch (FileReaderException e) {
			e.printStackTrace();
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
		for (Node n : nodes) {
			if (n.nodeName().equalsIgnoreCase(nodeName)) {
				list.add(n.outerHtml());
			}
		}
		return list;
	}
	
}