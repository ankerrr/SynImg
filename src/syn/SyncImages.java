package syn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Zhaoxh
 * @2017��8��23��
 */
public class SyncImages {

	//������ͼƬ�ļ�����
	List<String> pubList = new ArrayList<String>();
	int  copyNum = 0;// ���Ƹ�����
	float  xt = 0;//��ͬ�ļ�����
	int c = 0;//��������Դ�ļ��Ĵ���
	int pubLen = 0;//ͼƬ������ 
	boolean ck = true;
	/**
	 * 
	 * @param pubPath Դ·��
	 * @param picPath ���ɵ�Ŀ��λ��
	 */
	public void synImage(String pubPath,String picPath ){
		System.out.println("ִ��getPubFileName,������ȡ�ļ���...");
		getPubFileName(pubPath);
		pubLen = pubList.size();
		for(int i=0; i<pubList.size(); i++){
			//ȥ��·�� ��ȡ���ļ��� ��ȡ��ȥ����׺���ֵĳ��� ����10���Ǳ�׼���� W020170512
			String fileName = pubList.get(i).substring(pubList.get(i).lastIndexOf("\\")+1);
			int index = fileName.lastIndexOf(".")+1;
			int len = fileName.substring(0, index).length();
			
			//û�����쳣 ��ֹfileName.substring(0,8)��ָ�� ���ж�
			if(len >= 10){
				//ƴ�� �ļ�·�� һ��Ŀ¼ �Ͷ���Ŀ¼
				String ODir = fileName.substring(0,8).trim();//1�� 
				String TDir = fileName.substring(0,10).trim();//2��
				String path = picPath+File.separator+ODir+File.separator+TDir;//����Ŀ¼
				String SaveFilePath = path+File.separator+fileName;
				File file = new File(path);
				if(!file.exists()){
					System.out.println("�����ļ���:"+path);
					file.mkdirs();
					copyFile(pubList.get(i),SaveFilePath);
				}else {
					//�ļ��д��� ���ж��ļ����Ƿ�������ļ�
					File[] files = file.listFiles();
					boolean b = ergodic(fileName,files);
					if(b || files.length ==0){
						//�ļ�����ͬ;
						copyFile(pubList.get(i),SaveFilePath);
					}else {
						//�ļ�����ͬ
						xt += 1;
						System.out.println((int)xt+"���ļ���ͬ���Ѹ���"+copyNum+"��,�����:"+(copyNum+xt)/pubLen*100+"%");
						continue;
					}
				}
			}else {
				//�ļ����Ǳ�׼���� �½��ļ��Ϊ����
				String path = picPath+File.separator+"NameException";
				System.out.println(path);
				String SaveFilePath = path+File.separator+fileName;
				File file = new File(path);
				File[] files = file.listFiles();
				if(!file.exists()){
					file.mkdirs();
					copyFile(pubList.get(i),SaveFilePath);
				}else {
						boolean b = ergodic(fileName,files);
						if(b || files.length ==0){
							copyFile(pubList.get(i),SaveFilePath);
						}else {
							//�ļ�����ͬ
							xt += 1;
							System.out.println((int)xt+"���ļ���ͬ,�Ѹ���"+copyNum+"��,�����:"+(copyNum+xt)/pubLen*100+"%");
							continue;
						}
				}
			}
		}
	}
	/**
	 * �ж��ļ������Ƿ����ļ��� fielName���
	 * @param fileName Ҫ�жϵ��ļ���
	 * @param files Ҫ�ж�·�����ļ�����
	 * @return false �����ļ������ ,true �������
	 */
	public boolean  ergodic(String fileName,File[] files){
		for(File file : files){
			if(fileName.equals(file.getName())){
				return false;
			}
		}
		return true;
		
	}
	/**
	 * ����pub�ļ��е������ļ�,����W0��ͷ���ļ�������list  W020130411
	 * @param pubPath
	 * @return
	 */
	public  void getPubFileName(String pubPath){
		File root = new File(pubPath);
		File[] files = root.listFiles();
		if(ck){
			ck = false;
		}
		for(File file : files){
				if(!file.isDirectory()){
					//ֻҪ��W0��ͷ��
					String	frefix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1).substring(0, 2);
					String  suffix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
					suffix = suffix.toLowerCase();
//					 if(suffix.equals("css") || suffix.equals("jsp") || suffix.equals("htm") || suffix.equals("html")){
//						 System.out.println("�ų��ĺ�׺��ʽ:"+suffix);
//					 }else {
//						 pubList.add(file.getAbsolutePath());
//						 c += 1;
//					}
					if(frefix.equals("W0")){
					//doc docx xls xlsx pdf ppt pptx jpg png gif swf
						if(suffix.equals("doc") || suffix.equals("docx") || suffix.equals("xls") || suffix.equals("xlsx") 
							|| suffix.equals("pdf") || suffix.equals("ppt") ||suffix.equals("pptx") || suffix.equals("png") 
							|| suffix.equals("gif") || suffix.equals("swf") || suffix.equals("jpg") || suffix.equals("bmp") 
							|| suffix.equals("mpg")){
							 pubList.add(file.getAbsolutePath());
							 c += 1;
						 }else {
							 System.out.println("�ų��ĺ�׺��ʽ:"+suffix);
						}
					}
					 
				}else{
					String a = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1);
					c += 1;
					if(a.equals("System Volume Information")){
						continue;
					}
				getPubFileName(file.getAbsolutePath());
				}
		}
		//
	 }
	/**
	 * �����ļ�
	 * @param src Դ�ļ�·��
	 * @param target д���·��
	 */
	public void copyFile(String src,String target){
		 
		File srcFile = new File(src);    
        File targetFile = new File(target);    
        try {    
            InputStream in = new FileInputStream(srcFile);     
            OutputStream out = new FileOutputStream(targetFile);    
            byte[] bytes = new byte[1024];    
            int len = -1;    
            while((len=in.read(bytes))!=-1){    
                out.write(bytes, 0, len);    
            }    
            in.close();    
            out.close();    
        } catch (FileNotFoundException e) {   
       	 System.out.println("FileNotFound");
            e.printStackTrace();    
        } catch (IOException e) { 
       	 System.out.println("IOException");
            e.printStackTrace();    
        }    
        
        copyNum += 1;
        System.out.println("ͬ�ļ�"+(int)xt+"��,�Ѹ���"+copyNum+"���ļ�,�����:"+(copyNum+xt)/pubLen*100+"%"); 
	 }
}
