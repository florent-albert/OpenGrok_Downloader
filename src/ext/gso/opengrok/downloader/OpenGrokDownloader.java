package ext.gso.opengrok.downloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class OpenGrokDownloader 
{
	private static Properties properties = new Properties();
	private static String opengrokURL;
	
	public static void main(String[] args) throws FileNotFoundException, IOException
	{
		properties.load(new FileInputStream(new File("codebase/grokdownloader.properties")));
		String urlToDownload = "";
		opengrokURL = properties.getProperty("opengrok.baseUrl");
		String product = properties.getProperty("opengrok.product");
		urlToDownload = opengrokURL + "xref/"+ product + "/";
		String component = properties.getProperty("opengrok.component");
		if(!component.equals("all")){
			urlToDownload +=  component;
			recursiveBrowse(urlToDownload, product, component + "/");
		}
		else{
			recursiveBrowse(urlToDownload, product, "");
		}
	}
	
	private static void recursiveBrowse(String currentUrl, String product, String startingComponent) throws IOException
	{
		String breadCrumb = startingComponent;
		String browsableUrl = currentUrl + "/";
		Document page = Jsoup.connect(browsableUrl).get();
		Element dirlist = page.select("table#dirlist").first();
		System.out.println("Currently browsing path: "+browsableUrl);
		Iterator<Element> dirIterator = dirlist.select("tr").iterator();
		while(dirIterator.hasNext())
		{
			Element tr = dirIterator.next();
			Element a = tr.select("a").first();
			if(a != null)
			{
				String href = a.attr("href");
				if(!href.equals(".."))
				{
					if(href.equals("lost+found") || href.startsWith("CD_") || href.endsWith("_IA") || href.endsWith("Installer")){
						continue;
					}
					if(href.endsWith("/"))
					{
						recursiveBrowse(browsableUrl+href, product, breadCrumb+href);
					}
					else//reached technical level
					{
						String downloadUrl = opengrokURL + "raw/" + product + "/" + breadCrumb + "/" + href;
						URL url = new URL(downloadUrl); 
						String tDir = properties.getProperty("opengrok.downloadLocation"); 
						String path = tDir + "/" + product + "/"+ breadCrumb + "/"+href; 
						File file = new File(path); 
						FileUtils.copyURLToFile(url, file);
					}
				}
			}
		}
	}
	
}
