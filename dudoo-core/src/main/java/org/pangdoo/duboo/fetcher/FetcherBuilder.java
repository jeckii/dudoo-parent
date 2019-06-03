package org.pangdoo.duboo.fetcher;

public class FetcherBuilder {

	public static Fetcher custom() {
		return new Fetcher(new Configuration());
	}

	public static Fetcher build(Configuration config) {
		return new Fetcher(config).build();
	}

}
