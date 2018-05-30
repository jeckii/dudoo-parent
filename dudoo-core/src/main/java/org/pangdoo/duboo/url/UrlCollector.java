package org.pangdoo.duboo.url;

import java.util.Collection;
import java.util.Set;

import org.pangdoo.duboo.exception.NullValueException;

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
	 * @throws NullValueException
	 */
	long size() throws NullValueException;
	
	/**
	 * Returns {@code true} if the {@link WebUrl} collector has one can be used.
	 * 
	 * @return {@code true} if the {@link WebUrl} collector has one can be used
	 */
	boolean hasNext();
	
	/**
	 * @return a {@link WebUrl} for working
	 * @throws NullValueException
	 */
	WebUrl consume() throws NullValueException;
	
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
	 * This method is for removing the {@link WebUrl} that conforms to this
	 * path,and this {@link WebUrl} belongs to the location.
	 * 
	 * @param location The {@link WebUrl} to be removed belongs to this location
	 * @param path
	 * @return
	 */
	int filter(String location, String path);
	
	/**
	 * Put all of {@link WebUrl} in the job queue.
	 * 
	 * @return a {@link WebUrl} set
	 * @throws NullValueException
	 */
	Set<WebUrl> rebuild() throws NullValueException;
	
	/**
	 * Clear all {@link WebUrl} of the collector.
	 */
	void clear();
	
}
