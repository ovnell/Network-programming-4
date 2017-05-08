package nw4;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Spider {
	private Set<String> visited = new HashSet<String>();
	private List<String> traversedURLs = new ArrayList<String>();
	private LinkedList<URL> remainingURLs = new LinkedList<URL>();
	private List<String> mails = new ArrayList<String>();

	private int nbrVisited = 0;
	private int nbrToVisit;
	private List<Thread> threads = new ArrayList<Thread>();

	public Spider(int visit) {
		nbrToVisit = visit;
		for (int i = 0; i < 10; i++) {	// 10 trådar.
			Thread t = new Processor(this);
			threads.add(t);
			t.start();
		}
	}

	public synchronized URL getNextURL() {
		if (nbrVisited == nbrToVisit) {
			return null;
		}
		while (remainingURLs.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		URL url = remainingURLs.removeFirst();
		traversedURLs.add(url.toString());
		visited.add(url.toString());
		nbrVisited++;
		return url;
	}

	public synchronized void addURL(URL url) {
		remainingURLs.add(url);
		notifyAll();
	}

	public boolean hasVisited(String url) {
		return visited.contains(url);
	}

	public void addMail(String mail) {
		mails.add(mail);
	}
	
	public String getVisitedSites() {
		StringBuilder sb = new StringBuilder();
		for (String s : traversedURLs) {
			sb.append(s + "\n");
		}
		return sb.toString();
	}
	
	public String getMails() {
		StringBuilder sb = new StringBuilder();
		for (String s : mails) {
			sb.append(s + "\n");
		}
		return sb.toString();
	}

}
