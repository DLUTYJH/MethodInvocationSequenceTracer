package util;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Type;

/**
 * һЩ����
 * @author ��½��
 *
 */
public class Function {
	
	/**
	 * �ж�һ��String�ǲ��Ǳ�ʾ��������
	 * @param t �ַ���
	 * @return true ��; false ����
	 */
	public static boolean isArrayType(String t){
		if(t.contains("["))
			return true;
		return false;
	}
	
	/**
	 * �ж�һ��String�ǲ��ǻ�����������
	 * @param t �ַ���
	 * @return true ��; false ����
	 */
	public static boolean isPrimitiveType(String t){
		if(t.equals("int")||t.equals("byte")||t.equals("char")||t.equals("short")||t.equals("long")||
				t.equals("float")||t.equals("double")||t.equals("void")||t.equals("boolean"))
			return true;
		return false;
	}
	
	/**
	 * �ж�һ���ַ����ǲ���Eclipse AST����е�SimpleType
	 * @param t �ַ���
	 * @return true ��; false ����
	 */
	public static boolean isSimpleType(String t){
		if(!isPrimitiveType(t)&&!isArrayType(t))
			return true;
		return false;
	}
	
	/**
	 * �ж�һ��String�ǲ���Eclipse AST����е�QualifiedType
	 * @param t �ַ���
	 * @return true ��; false ����
	 */
	public static boolean isQualifiedType(String t){
		if(t.contains(".")&&!isArrayType(t))
			return true;
		return false;
	}
	

	/**
	 * ���������ҵ���Դ����·��<br/>
	 * ���汾�ݲ�����̬����
	 * @param type ���ͣ������Ǽ�����Ҳ������ȫ�޶�����
	 * @param projectRootPath ��Ŀ��·��
	 * @param packageInfo ���ø���������ڵİ�
	 * @param imports ���ø������ĵ�����Ϣ
	 * @return ��·��
	 */
	public static String resolveFilePath(String type, String projectRootPath, String packageInfo,
			ArrayList<ImportDeclaration> imports){
		String typeInStr=type;
		String path=null;
		if(typeInStr.contains(".")){  //�޶�����
			typeInStr=typeInStr.replace('.', '\\');
			path=projectRootPath+typeInStr+".java";
		}
		else{  //�����ͣ�����imports
			for(int i=0; i<=imports.size()-1; i++){
				ImportDeclaration imd=imports.get(i);
				if(imd.isStatic()){
					//�׳��쳣
				}
				else if(imd.isOnDemand()){
					//���Ҹ�Ŀ¼����û�з�����������
					String fullname=imd.getName().toString();
					fullname=fullname.replace('.', '\\');
					String directory=projectRootPath+fullname+"\\";  //Ҫ���ҵ�Ŀ¼
					File file=new File(directory);
					File[] tmpList=file.listFiles();
					for(File afile: tmpList){
						if(afile.isFile()){
							if(afile.getName().equals(typeInStr+".java")){
								path=directory+typeInStr+".java";
								break;
							}
						}
						else{
							//Ŀ¼�����ü�������
						}
					}
				}
				else{
					String fullname=imd.getName().toString();
					int index=fullname.lastIndexOf('.');
					String cname=fullname.substring(index+1, fullname.length());
					if(cname.equals(typeInStr)){
						fullname=fullname.replace('.', '\\');
						path=projectRootPath+fullname+".java";
						break;
					}
				}
			}
			if(path==null){  //����ȫ�޶�����û��ͨ��import��λ����˵����ͬһ������
				if(packageInfo!=null)
					path=projectRootPath+packageInfo+"\\"+typeInStr+".java";
				else
					path=projectRootPath+typeInStr+".java";
			}
		}
		return path;
	}
	
	/**
	 * ������תΪȫ�޶�����
	 * @param t ����
	 * @param projectRootPath ��Ŀ��·��
	 * @param packageInfo ���ø���������ڵİ�
	 * @param imports ���ø������ĵ�����Ϣ
	 * @return ȫ�޶�����
	 */
	public static String toFullQualifiedName(Type t, String projectRootPath, String packageInfo,
			ArrayList<ImportDeclaration> imports){
		if(t==null)
			return null;
		String typeInStr=t.toString();
		return toFullQualifiedName(typeInStr, projectRootPath, packageInfo, imports);
	}
	
	/**
	 * ������תΪȫ�޶�����
	 * @param t ����
	 * @param projectRootPath ��Ŀ��·��
	 * @param packageInfo ���ø���������ڵİ�
	 * @param imports ���ø������ĵ�����Ϣ
	 * @return ȫ�޶�����
	 */
	public static String toFullQualifiedName(String t, String projectRootPath, String packageInfo,
			ArrayList<ImportDeclaration> imports){
		if(t==null)
			return null;
		if(Function.isPrimitiveType(t)) return t;
		if(t.contains("."))  return t;   
		else{  //�����ͣ�����imports
			for(int i=0; i<=imports.size()-1; i++){
				ImportDeclaration imd=imports.get(i);
				if(imd.isStatic()){}
				else if(imd.isOnDemand()){
					//���Ҹ�Ŀ¼����û�з�����������
					String fullname=imd.getName().toString();
					String fullname_p=fullname.replace('.', '\\');
					String directory=projectRootPath+fullname_p+"\\";  //Ҫ���ҵ�Ŀ¼
					File file=new File(directory);
					File[] tmpList=file.listFiles();
					for(File afile: tmpList){
						if(afile.isFile()){
							if(afile.getName().equals(t+".java")){
								return fullname+"."+t;
							}
						}
						else{
							//Ŀ¼�����ü�������
						}
					}
				}
				else{
					String fullname=imd.getName().toString();
					int index=fullname.lastIndexOf('.');
					String cname=fullname.substring(index+1, fullname.length());
					if(cname.equals(t)){
						return fullname;
					}
				}
			}
			//����ȫ�޶�����û��ͨ��import��λ����˵����ͬһ������
			if(packageInfo!=null)
				return packageInfo+"."+t;
			else
				return t;
		}	
	}

}
