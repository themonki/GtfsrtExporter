package com.mio.secure.httpd;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.Md5Crypt;

public final class Md5 implements Algorithm {
	
	/**
     * MD5 pattern.
     */
	private static final Pattern PATTERN =
            Pattern.compile("\\$apr1\\$([^\\$]+)\\$([a-zA-Z0-9/\\.]+=*)");

	public Md5() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(String hash, String password) throws IOException {
		// TODO Auto-generated method stub
		final Matcher matcher = Md5.PATTERN.matcher(hash);
        final boolean matches;
        if (matcher.matches()) {
            final String result = Md5Crypt.apr1Crypt(
                password,
                matcher.group(1)
            );
            matches = hash.equals(result);
        } else {
            matches = false;
        }
        return matches;
	}

}
