package org.pangdoo.duboo.url.collector;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.pangdoo.duboo.url.*;
import org.pangdoo.duboo.util.LogLogger;
import org.pangdoo.duboo.util.StringUtils;
import org.pangdoo.duboo.robots.RobotsCache;

public final class DefaultURLCollector implements URLCollector {

    private final static LogLogger logger = LogLogger.getLogger(DefaultURLCollector.class);

    public DefaultURLCollector() {
    }

    public static DefaultURLCollector custom() {
        return new DefaultURLCollector();
    }

    public DefaultURLCollector url(String url) {
        this.add(url);
        return this;
    }

    public DefaultURLCollector url(String url, int depth) {
        this.add(url, depth);
        return this;
    }

    public DefaultURLCollector urls(Collection<String> urls) {
        this.addAll(urls);
        return this;
    }

    public DefaultURLCollector urls(Collection<String> urls, int depth) {
        this.addAll(urls, depth);
        return this;
    }

    public DefaultURLCollector(Collection<String> urls, int depth) {
        addAll(urls, depth);
    }

    public DefaultURLCollector(Collection<String> urls) {
        this(urls, 1);
    }

    private Set<WebURL> webUrlCollection = new HashSet<WebURL>();

    private WebURL nextUrl;

    private Set<WebURL> remains = new HashSet<WebURL>();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    private final Map<String, WebURL> redirectMap = new LinkedHashMap<String, WebURL>();

    private WebURL setNextUrl(WebURL nextUrl) {
        removeRemains(nextUrl);
        return this.nextUrl = nextUrl;
    }

    private boolean removeRemains(WebURL url) {
        return this.remains.remove(url);
    }

    @Override
    public Set<WebURL> addAll(Collection<String> urls) {
        return addAll(urls, 1);
    }

    @Override
    public Set<WebURL> addAll(Collection<String> urls, int depth) {
        lock.writeLock().lock();
        try {
            Set<WebURL> temp = new HashSet<WebURL>(urls.size());
            WebURL webUrl;
            for (String url : urls) {
                webUrl = new WebURL(url, depth);
                this.webUrlCollection.add(webUrl);
                this.remains.add(webUrl);
                temp.add(webUrl);
            }
            return temp;
        } catch (Exception e) {
            logger.warn(e);
        } finally {
            lock.writeLock().unlock();
        }
        return null;
    }

    @Override
    public boolean add(String url) {
        return add(url, 1);
    }

    private WebURL addUrl(String url, int depth) {
        WebURL webUrl = new WebURL(url, depth);
        this.webUrlCollection.add(webUrl);
        this.remains.add(webUrl);
        return webUrl;
    }

    @Override
    public boolean add(String url, int depth) {
        lock.writeLock().lock();
        try {
            return addUrl(url, depth) != null;
        } finally {
            lock.writeLock().unlock();
        }
    }


    @Override
    public WebURL consume() {
        lock.writeLock().lock();
        try {
            if (this.remains.size() == 0) {
                return null;
            }
            return setNextUrl(this.remains.iterator().next());
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public WebURL redirect(final String url) {
        lock.writeLock().lock();
        try {
            WebURL next = this.nextUrl;
            if (next == null) {
                throw new URLNullErrorException("Next URL is null");
            }
            WebURL nextUrl = addUrl(url, next.getDepth());
            if (nextUrl != null) {
                this.redirectMap.put(next.getOrigin(), nextUrl);
                this.webUrlCollection.remove(next);
                return setNextUrl(nextUrl);
            }
            return null;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public long size() {
        lock.readLock().lock();
        try {
            return this.webUrlCollection.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean hasNext() {
        lock.readLock().lock();
        try {
            if (this.remains.isEmpty()) {
                return false;
            }
            return true;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<String> locations() {
        lock.readLock().lock();
        try {
            Set<WebURL> webUrls = this.webUrlCollection;
            Set<String> locations = new HashSet<String>(webUrls.size());
            for (WebURL webURL : webUrls) {
                locations.add(webURL.getLocation());
            }
            return locations;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean remove(WebURL url) {
        lock.writeLock().lock();
        try {
            this.webUrlCollection.remove(url);
            return removeRemains(url);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int filter(String location) {
        lock.writeLock().lock();
        try {
            if (StringUtils.isBlank(location)) {
                throw new IllegalArgumentException("The domain is null.");
            }
            int count = 0;
            List<String> disallows = RobotsCache.get(location).getDisallow();
            for (String disallow : disallows) {
                for (WebURL url : this.webUrlCollection) {
                    if (location.equals(url.getLocation())
                            && StringUtils.pattern(url.getPath(), URLResolver.pathPattern(disallow))) {
                        if (this.webUrlCollection.remove(url)) {
                            count++;
                        }
                    }
                }
            }
            return count;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Set<WebURL> rebuild() {
        lock.writeLock().lock();
        try {
            this.remains.addAll(this.webUrlCollection);
            return this.remains;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            this.webUrlCollection.clear();
            this.remains.clear();
            if (this.nextUrl != null) {
                this.nextUrl = null;
            }
            this.redirectMap.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
