package br.com.jasf.microservicechallenge.utils;

public class Util {
	public static boolean delay(int millisenconds) {
		try {
			Thread.sleep(millisenconds);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}
}
