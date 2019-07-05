package org.pangdoo.duboo.url;

import java.util.Collection;
import java.util.Set;

/**
 * This interface represents an abstract collector for URL
 * objects.The collector will encapsulate URL into {@link WebURL}.
 * In the whole life cycle of collector,it will be responsible
 * for managing the preservation and use of {@link WebURL}.
 */
public interface URLCollector {
	
	/**
	 * Adds a URL collection,this method will encapsulate
	 * the URL collection into {@link WebURL} set.The
	 * {@link WebURL} set will be saved and returned.
	 * 
	 * @param urls
	 * @return a {@link WebURL} set
	 */
	Set<WebURL> addAll(Collection<String> urls);

	Set<WebURL> addAll(Collection<String> urls, int depth);

	boolean add(String url);

	boolean add(String url, int depth);
	
	/**
	 * @return the size of the collector
	 */
	long size();
	
	/**
	 * Returns {@code true} if the {@link WebURL} collector has one can be used.
	 * 
	 * @return {@code true} if the {@link WebURL} collector has one can be used
	 */
	boolean hasNext();
	
	/**
	 * @return a {@link WebURL} for working
	 */
	WebURL consume();

    /**
     * Making the redirect {@link WebURL}
     * taking the place of the next to work
     * @param url
     * @return
     */
	WebURL redirect(String url);
	
	/**
	 * @return location set
	 */
	Set<String> locations();
	
	/**
	 * This method is for removing the {@link WebURL} from this collector.
	 * 
	 * @param url
	 * @return
	 */
	boolean remove(WebURL url);
	
	/**
	 * This method is for removing {@link WebURL} that meet certain
	 * criteria and belong to this location.
	 * 
	 * @param location
	 * @return
	 */
	int filter(String location);
	
	/**
	 * Put all {@link WebURL} back into the job queue.
	 * 
	 * @return a {@link WebURL} set
	 */
	Set<WebURL> rebuild();
	
	/**
	 * Clear all {@link WebURL} of the collector.
	 */
	void clear();
	
}
