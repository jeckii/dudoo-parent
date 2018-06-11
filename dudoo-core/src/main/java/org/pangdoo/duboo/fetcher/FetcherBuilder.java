package org.pangdoo.duboo.fetcher;

public class FetcherBuilder {

	public static Fetcher custom() {
		return new Fetcher();
	}

	public static Fetcher build(Configuration config) {
		return new Fetcher(config).build();
	}

}
