package syn;

import java.io.File;
import java.util.Scanner;
/**
 * 
 * @author Zhaoxh
 * @2017��8��23��
 */
public class SyncMain {

	public static void main(String[] args) {
		// D:\webpic\W0200510   D:\webpic\W0200905 
	//	String picPath = "D:"+File.separator+"test1";
	//	String pubPath = "D:"+File.separator+"test";
		Scanner scan = new Scanner(System.in);		
		System.out.println("����pubĿ¼·��(D:\\**\\pub):");
		
		String pubPath = scan.nextLine();
		
		pubPath = pubPath.replace("\\",File.separator);
		
		System.out.println("pubPath:"+pubPath);
		
		System.out.println("����webpicĿ¼·��(D:\\**\\webpic):");
		
		String picPath = scan.nextLine();
		
		picPath = picPath.replace("\\",File.separator);
		
		SyncImages syn = new SyncImages();
		
		syn.synImage(pubPath, picPath);
		
		System.out.println("ִ����ϣ�");
	
	}

}
