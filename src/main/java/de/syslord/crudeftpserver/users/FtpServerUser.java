package de.syslord.crudeftpserver.users;

import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.ConcurrentLoginPermission;
import org.apache.ftpserver.usermanager.impl.TransferRatePermission;
import org.apache.ftpserver.usermanager.impl.WritePermission;

public class FtpServerUser extends BaseUser {

	public FtpServerUser(String name, String password, boolean mayWrite, int maxConcurrentLogins) {
		setName(name);
		setPassword(password);
		setEnabled(true);
		setHomeDirectory("");

		List<Authority> permissions = new ArrayList<>();

		if (mayWrite) {
			permissions.add(new WritePermission());
		}

		permissions.add(new ConcurrentLoginPermission(maxConcurrentLogins, maxConcurrentLogins));
		permissions.add(new TransferRatePermission(0, 0));

		setAuthorities(permissions);
	}

}
