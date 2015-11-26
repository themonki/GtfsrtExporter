package com.mio.secure.httpd;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public final class Sha implements Algorithm {
	
	/**
     * SHA1 pattern.
     */
    private static final Pattern PATTERN =
        Pattern.compile("\\{SHA\\}([a-zA-Z0-9/\\+]+=*)");

	public Sha() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(String hash, String password) throws IOException {
		// TODO Auto-generated method stub
		final Matcher matcher = Sha.PATTERN.matcher(hash);
        final boolean matches;
        if (matcher.matches()) {
            final String required = Base64.encodeBase64String(
                DigestUtils.sha1(password)
            );
            matches = matcher.group(1).equals(required);
        } else {
            matches = false;
        }
        return matches;
	}

}
