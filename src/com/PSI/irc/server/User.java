package com.PSI.irc.server;

public class User {

	private String login;
	private String pwd;
	
	/**
	 * récuperer le login de l'utilisateur
	 * @return String
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * remplacer le login
	 * @param login String
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * récuperer le password
	 * @return String
	 */
	public String getPwd() {
		return pwd;
	}
	
	/**
	 * Changer le password
	 * @param pwd String
	 */
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	/**
	 * Constructeur
	 * @param login String
	 * @param pwd String
	 */
	public User(String login, String pwd) {
		super();
		this.login = login;
		this.pwd = pwd;
	}	
	
}
