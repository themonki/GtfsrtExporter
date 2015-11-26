package com.mio.secure.httpd;

import java.io.IOException;

public final class PlainText implements Algorithm {

	public PlainText() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean matches(String hash, String password) throws IOException {
		// TODO Auto-generated method stub
		return password.equals(hash);
	}

}
