package com.mio.secure.httpd;

import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.Crypt;

public final class UnixCrypt implements Algorithm {
	/**
     * Unix Crypt pattern.
     */
    private static final Pattern PATTERN =
        Pattern.compile("(\\$[156]\\$)?[a-zA-Z0-9./]+(\\$.*)*");
	public UnixCrypt() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(String hash, String password) throws IOException {
		// TODO Auto-generated method stub
		return PATTERN.matcher(hash).matches()
                && hash.equals(Crypt.crypt(password, hash));
	}

}
