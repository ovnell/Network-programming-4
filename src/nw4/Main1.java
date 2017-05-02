package nw4;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main1 {

	private static Set<String> visited = new HashSet<String>();

	private static List<String> websites = new ArrayList<String>();
	private static List<String> mails = new ArrayList<String>();

	private static Queue<String> queue = new LinkedList<String>();

	public static void main(String[] args) {
		try {
			URL url = new URL("http://cs.lth.se/eda095/");
			for (int loopCount = 0; loopCount < 10; loopCount++, url = new URL(queue.poll())) {
				InputStream is = url.openStream();
				Document doc = Jsoup.parse(is, "UTF-8", url.toString());
				Elements base = doc.getElementsByTag("base");
				Elements links = doc.getElementsByTag("a");
				for (Element link : links) {
					String linkHref = link.attr("href");
					String linkAbsHref = link.attr("abs:href");
					String linkText = link.text();
					if (!linkAbsHref.isEmpty() && !visited.contains(linkAbsHref)) {
						if (linkAbsHref.substring(0, 7).equals("mailto:")) {
							mails.add(linkAbsHref);
						} else {
							visited.add(linkAbsHref);
							websites.add(linkAbsHref);
							queue.add(linkAbsHref);
							url = new URL(linkAbsHref);
						}
					}
				}
				is.close();
				if (queue.isEmpty()) {
					break;
				}
			}
			System.out.println(websites.size());
			for (String mail : mails) {
				System.out.println(mail);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
