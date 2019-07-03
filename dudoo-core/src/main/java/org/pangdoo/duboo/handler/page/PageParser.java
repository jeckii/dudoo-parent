package org.pangdoo.duboo.handler.page;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.pangdoo.duboo.util.LogLogger;
import org.pangdoo.duboo.util.StringUtils;

public class PageParser {
	
	private final static LogLogger logger = LogLogger.getLogger(PageParser.class);
	
	public static PageParser newInstance() {
		return new PageParser();
	}
	
	public static PageParser newInstance(InputStream input, String charsetName, String baseUri) {
		return new PageParser(input, charsetName, baseUri);
	}
	
	public static PageParser newInstance(String html, String charsetName) {
		return new PageParser(html, charsetName);
	}
	
	private Document doc;
	
	private PageParser() {}
	
	private PageParser(InputStream input, String charsetName, String baseUri) {
		try {
			if (input == null) {
				throw new NoPageContentException("Input is null.");
			}
		} catch (NoPageContentException e) {
			logger.warn(e);
		}
		try {
			doc = Jsoup.parse(input, charsetName, baseUri);
		} catch (IOException e) {
			logger.warn(e);
		}
	}
	
	private PageParser(String html, String charsetName) {
		try {
			if (StringUtils.isEmpty(html)) {
				throw new NoPageContentException("Html is null.");
			}
		} catch (NoPageContentException e) {
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
				throw new PageDocumentExistException("Document has been created.");
			}
		} catch (PageDocumentExistException e) {
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
	
	public List<Node> segmentNodes(Node node, String nodeName) {
		List<Node> nodes = node.childNodes();
		List<Node> list = new ArrayList<Node>(nodes.size());
		for (Node n : nodes) {
			if (n.nodeName().equalsIgnoreCase(nodeName)) {
				list.add(n);
			}
		}
		return list;
	}
	
	public List<Node> segmentAllNodes(Node node, String nodeName) {
		List<Node> nodes = new ArrayList<Node>();
		segmentAllNodes(node, nodeName, nodes);
		return nodes; 
	}
	
	private void segmentAllNodes(Node node, String nodeName, List<Node> nodes) {
		if (node.childNodeSize() > 0) {
			List<Node> childNodes = node.childNodes();
			for (Node childNode : childNodes) {
				if (nodeName.equalsIgnoreCase(childNode.nodeName())) {
					nodes.add(childNode);
				}
				if (childNode.childNodeSize() > 0) {
					segmentAllNodes(childNode, nodeName, nodes);
				}
			}
		}
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
	
	public void clearBody() {
		Element body = doc.body();
		Element html = body.parent();
		body.remove();
		html.append("<body></body>");		
	}
	
	public void append(String html) {
		doc.body().append(html);
	}

}
