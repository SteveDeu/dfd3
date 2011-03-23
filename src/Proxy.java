package bilanpack.net.vkscaner;

public class Proxy {
	String info;
	String host;
	int port;
	String login;
	String pswd;
	boolean required;
	
	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	Proxy() {
	}

	Proxy(String info) {
		this.info = info;
		if(info != null){
			required = true;
			parseInputInfo(info);
		}else{
			required = false;
		}
	}

	private void parseInputInfo(String info) throws IllegalArgumentException {
		// info like: "login:pswd@host.com:port"
		try {
			if (info.split("@").length == 2) {
				String ldap = info.split("@")[0];
				login = ldap.split(":")[0];
				pswd = ldap.split(":")[1];

				String hostPort = info.split("@")[1];
				host = hostPort.split(":")[0];
				port = Integer.valueOf(hostPort.split(":")[1]);
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Override
	public String toString() {
		return login + ":" + pswd + "@" + host + ":" + port;
	}
}