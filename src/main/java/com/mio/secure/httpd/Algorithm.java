package com.mio.secure.httpd;

import java.io.IOException;

public interface Algorithm {
	/**
     * Do they match?
     * @param hash The hash
     * @param password The password
     * @return TRUE if they match
     * @throws IOException If some error inside
     */
    boolean matches(String hash, String password) throws IOException;
}
