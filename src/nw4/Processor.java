package nw4;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Processor extends Thread {

	private Spider spider;

	public Processor(Spider spider) {
		this.spider = spider;
	}

	@Override
	public synchronized void run() {
		while (true) {
			try {
				URL url = spider.getNextURL();
				if (url == null) {
					return;
				}
				System.out.println(url);

				InputStream is = url.openStream();
				Document doc = Jsoup.parse(is, "UTF-8", url.toString());
				Elements base = doc.getElementsByTag("base");
				Elements links = doc.getElementsByTag("a");
				for (Element link : links) {
					String linkAbsHref = link.attr("abs:href");
					if (!linkAbsHref.isEmpty() && !spider.hasVisited(linkAbsHref)) {
						if (linkAbsHref.substring(0, 7).equals("mailto:")) {
							spider.addMail(linkAbsHref);
						} else {
							URL url2 = new URL(linkAbsHref);
							spider.addURL(url2);
						}
					}
				}
				is.close();
			} catch (FileNotFoundException e) {
				continue;
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}

	}
}
