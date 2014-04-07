package com.PSI.irc.client;

import java.io.File;
import java.io.ObjectInputStream.GetField;

import com.PSI.irc.server.ServerToClientThread;

public class Test {

	public static void main(String[] args) {
		String url = "BOP_modif";
		File file = new File("BOP_modif.wav");
		if(file.exists()) System.out.println("exist");
		System.out.println(Test.class.getResource("/user.png"));

	}

}
