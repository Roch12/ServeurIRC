package com.PSI.irc;

public interface IfClientServerProtocol {
	public static final String LOGIN_PWD = "#Login?#Pwd?";
	public static final String SEPARATOR="#";
	public static final String KO = "#KO";
	public static final String OK = "#OK";
	public static final String ADD = "#+#";
	public static final String DEL = "#-#";
	public static final String Message = "##";
	public static final String Whispers = "#W#";
	public static final String ErrorUsername = "#USERNAME#";
	public static final String Salon = "#S#";
	public static final String RemoveFromSalon = "#-S#";
	public static final String AddToSalon = "#+S#";
}