package Asgardia.World.Npc;

import java.io.*;
import java.util.concurrent.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/*
 * server/database/NpcActionTable參考
 */
public class NpcActionXmlLoader
{
	private static final String XML_PATH = "./XML/NpcActions" ;
	
	/*
	 * 解析標前的遞迴層階計算
	 */
	private static int Depth = 0;
	
	public NpcActionXmlLoader (ConcurrentHashMap<String, Document> NpcActionTable) {
		File XmlFileDir = new File (XML_PATH) ;
		
		try {
			File[] XmlFileList = XmlFileDir.listFiles () ;
			for (File XmlFile : XmlFileList) {
				ParseXmlFile (XmlFile, NpcActionTable) ;
			}
		} catch (Exception e) {
			System.out.println (e.toString () ) ;
			e.printStackTrace () ;
		}
	}
	
	private static void ParseXmlFile (File XmlFile, ConcurrentHashMap<String, Document> NpcActionTable) {
		try {
			DocumentBuilder XmlBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder () ;
			Document Result = XmlBuilder.parse (XmlFile) ;
			
			NpcActionTable.put (XmlFile.getName (), Result) ;
			
			/*
			 * 建立至快取內
			 */
			Document doc = (Document) NpcActionTable.get (XmlFile.getName () ) ;
			NodeList ActionList = doc.getDocumentElement ().getChildNodes () ;
			
			/*
			 * 開始遞迴走訪
			 */
			ParseXmlAction (ActionList) ; 
			
		} catch (Exception e) {
			System.out.println (e.toString () ) ;
			e.printStackTrace ();
		}
		
	}
	
	public static void ParseXmlAction (NodeList RootNode) {
		Depth ++;
		
		for (int index = 0; index < RootNode.getLength (); index++) {
			
			Node node = RootNode.item (index) ;
			if (!node.getNodeName ().startsWith ("#") ) {
				//System.out.println () ;
				for (int d = 0; d < Depth; d++) {
					//System.out.print ("   ") ;
				}
				//System.out.printf ("[%s] ", node.getNodeName () ) ;
			}
			
			if (node.hasAttributes ()) {
				NamedNodeMap attr_list = node.getAttributes () ;
				for (int j = 0; j < attr_list.getLength (); j++) {
					Node attr = attr_list.item (j) ;
					if (!attr.getNodeName ().startsWith ("#") ) {
						//System.out.printf ("%s=%s ", attr.getNodeName (), attr.getNodeValue () ) ;
					}
				}
			}
			
			if (node.hasChildNodes ()) {
				NodeList SubNodes = node.getChildNodes () ;
				for (int k = 0; k < SubNodes.getLength (); k++) {
					ParseXmlAction (SubNodes) ;
				}
			}
			
		}
		
		Depth --;
	}
}
