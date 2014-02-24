package com.PSI.irc.client;

import java.io.IOException;

public interface IfSenderModel {

	public abstract void setMsgToSend(String msgToSend) throws IOException;
	
	public abstract void quitServer() throws IOException;

}