package org.pangdoo.duboo.url;

public class URL {
		
	private String scheme;
	
	private String location;
    
	private String path;
    
	private String params;
    
	private String query;
    
	private String fragment;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getFragment() {
		return fragment;
	}

	public void setFragment(String fragment) {
		this.fragment = fragment;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (scheme != null) {
            sb.append(scheme);
            sb.append(':');
        }
        if (location != null) {
            sb.append("//");
            sb.append(location);
        }
        if (path != null) {
            sb.append(path);
        }
        if (params != null) {
            sb.append(';');
            sb.append(params);
        }
        if (query != null) {
            sb.append('?');
            sb.append(query);
        }
        if (fragment != null) {
            sb.append('#');
            sb.append(fragment);
        }
        return sb.toString();
	}
	
	public String baseUrl() {
		StringBuilder sb = new StringBuilder();
		if (scheme != null) {
            sb.append(scheme);
            sb.append(':');
        }
        if (location != null) {
            sb.append("//");
            sb.append(location);
        }
        return sb.toString();
	}
	
	@Override  
    public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj instanceof URL) {
			URL url = (URL) obj;
			if (toString().equals(url.toString())) {
				return true;
			}
		}
		return false;
	}
	
}
