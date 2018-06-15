package org.pangdoo.duboo.url;

import java.util.Collection;
import java.util.Set;

import org.pangdoo.duboo.exception.NullException;

/**
 * This interface represents an abstract collector for URL
 * objects.The collector will encapsulate URL into {@link WebUrl}.
 * In the whole life cycle of collector,it will be responsible
 * for managing the preservation and use of {@link WebUrl}.
 */
public interface UrlCollector {
	
	/**
	 * Adds a URL collection,this method will encapsulate
	 * the URL collection into {@link WebUrl} set.The
	 * {@link WebUrl} set will be saved and returned.
	 * 
	 * @param urls
	 * @param depth
	 * @return a {@link WebUrl} set
	 */
	Set<WebUrl> add(Collection<String> urls, int depth);
	
	/**
	 * @return the size of the collector
	 * @throws NullException
	 */
	long size() throws NullException;
	
	/**
	 * Returns {@code true} if the {@link WebUrl} collector has one can be used.
	 * 
	 * @return {@code true} if the {@link WebUrl} collector has one can be used
	 */
	boolean hasNext();
	
	/**
	 * @return a {@link WebUrl} for working
	 * @throws NullException
	 */
	WebUrl consume() throws NullException;
	
	/**
	 * Making the redirect {@link WebUrl} 
	 * taking the place of the next to work
	 * 
	 * @param url
	 * @return a {@link WebUrl}
	 */
	WebUrl moveToRedirect(String url);
	
	/**
	 * @return location set
	 */
	Set<String> locations();
	
	/**
	 * This method is for removing the {@link WebUrl} from this collector.
	 * 
	 * @param url
	 * @return
	 */
	boolean remove(WebUrl url);
	
	/**
	 * This method is for removing {@link WebUrl} that meet certain
	 * criteria and belong to this location.
	 * 
	 * @param location
	 * @return
	 */
	int filter(String location);
	
	/**
	 * Put all {@link WebUrl} back into the job queue.
	 * 
	 * @return a {@link WebUrl} set
	 * @throws NullException
	 */
	Set<WebUrl> rebuild() throws NullException;
	
	/**
	 * Clear all {@link WebUrl} of the collector.
	 */
	void clear();
	
}
