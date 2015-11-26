package com.mio.secure.httpd;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.FileUtils;
/**
 * Clase basada en https://github.com/yegor256/s3auth/blob/master/s3auth-hosts/src/main/java/com/s3auth/hosts/Htpasswd.java
 *
 */
public class Htpasswd {
	private String pathHtpasswdFile;
	
	/**
     * All known algorithms.
     * @see <a href="http://httpd.apache.org/docs/2.2/misc/password_encryptions.html">Algorithms supported by Apache</a>
     */
	private static final Algorithm[] ALGORITHMS = {
        new Md5(),
        new Sha(),
        new UnixCrypt(),
        new PlainText(),
    };

	public Htpasswd(String pathHtpasswdFile) {
		// TODO Auto-generated constructor stub
		this.pathHtpasswdFile = pathHtpasswdFile;
	}
	
	/**
     * Hash matches the password?
     * @param hash The hash to match
     * @param password Password
     * @return TRUE if they match
     * @throws IOException If some error inside
     */
    private static boolean matches(final String hash, final String password)
        throws IOException {
        boolean matches = false;
        for (final Algorithm algo : Htpasswd.ALGORITHMS) {
            if (algo.matches(hash, password)) {
                matches = true;
                break;
            }
        }
        return matches;
    }
    
    /**
    * Get map of users and passwords from the host.
    * @return Map of users
    */
    private ConcurrentMap<String, String> fetch() {
    	ConcurrentMap<String, String> map = new ConcurrentHashMap<String, String>(0);
        final String[] lines = this.content().split("\n");
        for (final String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            final String[] parts = line.trim().split(":", 2);
            if (parts.length != 2) {
                continue;
            }
            map.put(parts[0].trim(), parts[1].trim());
        }
        return map;
    }
    
    /**
     * Can this user login in with this credentials?
     * @param user User name
     * @param password Password
     * @return Yes or no
     * @throws IOException If some error inside
     */
    public boolean authorized(final String user, String password) throws IOException {
            final ConcurrentMap<String, String> users = this.fetch();
            return users.containsKey(user) && Htpasswd.matches(users.get(user), password);
        }
    
    /**
     * Fetch the .htpasswd file, or returns empty string if it's absent.
     * @return Content of .htpasswd file, or empty
     */
    private String content() {
        String content;
        try {
            content = FileUtils.readFileToString(new File(this.pathHtpasswdFile), StandardCharsets.UTF_8);
        } catch (IOException e) {
			// TODO Auto-generated catch block
			content = "";
		}
        
        return content.trim();
    }


}
