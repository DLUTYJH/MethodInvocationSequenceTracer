package vo.common_obj;

import java.util.ArrayList;

/**
 * �����������ȫ�޶����ͷ�������������������ȫ�޶�����<br/>
 * ��Ψһ��������һ������
 * @author ��½��
 *
 */
public class MethodName {
	public String fullQualifiedName;
	public String simpleName;
	public ArrayList<String> paramTypes;
	
	/**
	 * ������
	 */
	public MethodName(){
		fullQualifiedName=null;
		simpleName=null;
		paramTypes=new ArrayList<String>();
	}
	
	/**
	 * ������
	 * @param f �����������ȫ�޶���
	 * @param s �����ļ�����
	 * @param pts �����Ĳ�����ȫ�޶�����
	 */
	public MethodName(String f, String s, ArrayList<String> pts){
		this.fullQualifiedName=f;
		this.simpleName=s;
		this.paramTypes=pts;
	}
}
