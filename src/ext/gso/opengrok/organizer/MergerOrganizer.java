package ext.gso.opengrok.organizer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class MergerOrganizer
{
	private static String TARGET_PATH="C:/temp/athens2";
	private static String SOURCE_PATH;
	
	public static void main(String[] args) throws Exception
	{
		File sourceRoot = new File("C:/Temp/athens");
		File targetRoot = new File("C:/Temp/athens2");
		
		ArrayList<File> sourceFiles = new ArrayList<File>();
		recursiveBrowse(sourceRoot, sourceFiles);
		for(File origFile:sourceFiles)
		{
			FileUtils.copyDirectory(origFile, targetRoot);
		}
	}
	
	public static void recursiveBrowse(File parent, ArrayList<File> files)
	{
		File[] children = parent.listFiles();
		if(children != null && children.length>0)
		{
			for(File child:children)
			{
	//			if(child.getAbsolutePath().contains("/src/"))
	//			{
	//				String path = child.getAbsolutePath().substring(child.getAbsolutePath().indexOf("src"));
	//				File f = new File(TARGET_PATH+path);
	//				files.add(f);
	//			}
	//			recursiveBrowse(child, files);
				if(child.getName().startsWith("src")){
					files.add(child);
				}
				else{
					recursiveBrowse(child, files);
				}
			}
		}
	}
}
