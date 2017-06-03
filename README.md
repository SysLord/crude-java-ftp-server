# Crude Apache Mina based FTP Server

Apache Mina allows to create an FTP server in Java.

The use case is a situation where an external system only can push data per FTP. Normally an FTP server is needed and the application would poll. (Could be done with Spring Integration). But with this Crude FTP Server we only need to register a listener and get uploaded files directly in our application without an external FTP server.

## Missing pieces

But there are a few things missing in the Apache Mina FTP server:

 * The only filesystem available is a native filesystem. So this is no advantage to a normal application-external FTP.
 * The only usermanagers available are for database or properties file which needs to be a native file.

# This implements

### User Manager
This is an implementation of a very simple DynamicUserManager which just gets a user list.

### Virtual Filesystem
This also implements a very simple virtual ftp filesystem which allows only files and only in user root.

### Upload Done Listener
When a file upload is finished a listener is called with the uploading user and file that was created. Files are not persisted in any other way.

