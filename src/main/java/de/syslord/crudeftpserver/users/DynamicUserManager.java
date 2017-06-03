package de.syslord.crudeftpserver.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;

public class DynamicUserManager implements UserManager {

	private Map<String, User> users = new HashMap<>();

	public DynamicUserManager(List<FtpServerUser> usersList) {
		this.users = usersList.stream()
			.collect(Collectors.toMap(
					u -> u.getName(),
					u -> u));
	}

	@Override
	public User getUserByName(String username) throws FtpException {
		return users.get(username);
	}

	@Override
	public String[] getAllUserNames() throws FtpException {
		return users.keySet().toArray(new String[] {});
	}

	@Override
	public void delete(String username) throws FtpException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void save(User user) throws FtpException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean doesExist(String username) throws FtpException {
		return users.containsKey(username);
	}

	@Override
	public User authenticate(Authentication authentication) throws AuthenticationFailedException {
		if (authentication != null &&
				UsernamePasswordAuthentication.class.isAssignableFrom(authentication.getClass())) {

			UsernamePasswordAuthentication userAuth = (UsernamePasswordAuthentication) authentication;

			Optional<User> authenticatedUser = users.values().stream()
				.filter(user -> {
					return userAuth.getUsername() != null &&
							userAuth.getPassword() != null &&
							userAuth.getUsername().equals(user.getName()) &&
							userAuth.getPassword().equals(user.getPassword());
				})
				.findFirst();

			return authenticatedUser.orElseThrow(() -> new AuthenticationFailedException());
		}

		throw new AuthenticationFailedException();
	}

	@Override
	public String getAdminName() throws FtpException {
		return "0874567890ß7463568092357809231";
	}

	@Override
	public boolean isAdmin(String username) throws FtpException {
		return false;
	}

}
