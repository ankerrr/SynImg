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
 * @2017年8月23日
 */
public class SyncImages {

	//遍历的图片文件集合
	List<String> pubList = new ArrayList<String>();
	int  copyNum = 0;// 复制个数量
	float  xt = 0;//相同文件数量
	int c = 0;//遍历整个源文件的次数
	int pubLen = 0;//图片的数量 
	boolean ck = true;
	/**
	 * 
	 * @param pubPath 源路径
	 * @param picPath 生成的目标位置
	 */
	public void synImage(String pubPath,String picPath ){
		System.out.println("执行getPubFileName,遍历获取文件名...");
		getPubFileName(pubPath);
		pubLen = pubList.size();
		for(int i=0; i<pubList.size(); i++){
			//去掉路径 截取出文件名 再取出去掉后缀名字的长度 大于10就是标准长度 W020170512
			String fileName = pubList.get(i).substring(pubList.get(i).lastIndexOf("\\")+1);
			int index = fileName.lastIndexOf(".")+1;
			int len = fileName.substring(0, index).length();
			
			//没有抛异常 防止fileName.substring(0,8)空指针 做判定
			if(len >= 10){
				//拼接 文件路径 一级目录 和二级目录
				String ODir = fileName.substring(0,8).trim();//1级 
				String TDir = fileName.substring(0,10).trim();//2级
				String path = picPath+File.separator+ODir+File.separator+TDir;//完整目录
				String SaveFilePath = path+File.separator+fileName;
				File file = new File(path);
				if(!file.exists()){
					System.out.println("创建文件夹:"+path);
					file.mkdirs();
					copyFile(pubList.get(i),SaveFilePath);
				}else {
					//文件夹存在 就判断文件夹是否有这个文件
					File[] files = file.listFiles();
					boolean b = ergodic(fileName,files);
					if(b || files.length ==0){
						//文件名不同;
						copyFile(pubList.get(i),SaveFilePath);
					}else {
						//文件名相同
						xt += 1;
						System.out.println((int)xt+"个文件相同，已复制"+copyNum+"个,已完成:"+(copyNum+xt)/pubLen*100+"%");
						continue;
					}
				}
			}else {
				//文件不是标准命名 新建文件單獨存放
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
							//文件名相同
							xt += 1;
							System.out.println((int)xt+"个文件相同,已复制"+copyNum+"个,已完成:"+(copyNum+xt)/pubLen*100+"%");
							continue;
						}
				}
			}
		}
	}
	/**
	 * 判定文件夹下是否有文件跟 fielName相等
	 * @param fileName 要判断的文件名
	 * @param files 要判断路径的文件集合
	 * @return false 代表文件名相等 ,true 代表不相等
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
	 * 遍历pub文件夹的所有文件,把以W0开头的文件名存入list  W020130411
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
					//只要以W0开头的
					String	frefix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("\\")+1).substring(0, 2);
					String  suffix = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf(".")+1);
					suffix = suffix.toLowerCase();
//					 if(suffix.equals("css") || suffix.equals("jsp") || suffix.equals("htm") || suffix.equals("html")){
//						 System.out.println("排除的后缀格式:"+suffix);
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
							 System.out.println("排除的后缀格式:"+suffix);
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
	 * 复制文件
	 * @param src 源文件路径
	 * @param target 写入的路径
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
        System.out.println("同文件"+(int)xt+"个,已复制"+copyNum+"个文件,已完成:"+(copyNum+xt)/pubLen*100+"%"); 
	 }
}
