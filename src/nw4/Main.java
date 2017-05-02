package nw4;

import java.net.URL;

public class Main {

	public static void main(String[] args) {
		try {
			Spider spider = new Spider(10);
			spider.addURL(new URL("http://cs.lth.se/eda095/"));
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
