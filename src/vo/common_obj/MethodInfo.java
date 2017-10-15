package vo.common_obj;

import java.util.ArrayList;

/**
 * ������Ϣ������class�ļ��������ʽ���д洢
 * @author ��½��
 *
 */
public class MethodInfo {
	
	public String fullQualifiedName_class;
	public int access_flags;  
	public String method_name;  
	public ArrayList<String> param_types;
	public String fullQualifiedName_return;
	
	/**
	 * ������
	 * @param method_name ������
	 */ 
	public MethodInfo(String fullQualifiedName_class, String method_name){
		this.fullQualifiedName_class=fullQualifiedName_class;
		access_flags=0x0000;
		this.method_name=method_name;
		param_types=new ArrayList<String>();
		fullQualifiedName_return=null;
	}
	
	/**
	 * ������
	 * @param method_name ������
	 * @param list �������������б�
	 */
	public MethodInfo(String fullQualifiedName_class, String method_name, ArrayList<String> list){
		this.fullQualifiedName_class=fullQualifiedName_class;
		access_flags=0x0000;
		this.method_name=method_name;
		param_types=list;
		fullQualifiedName_return=null;
	}
	
	/**
	 * ������
	 * @param access_flags ���ʱ�ʶ
	 * @param method_name ������
	 * @param list ���������б�
	 */
	public MethodInfo(String fullQualifiedName_class, int access_flags, String method_name, ArrayList<String> list){
		this.fullQualifiedName_class=fullQualifiedName_class;	
		this.access_flags=access_flags;
		this.method_name=method_name;
		this.param_types=list;
	}

	/**
	 * ������
	 * @param fullQulifiedName_class �����������ȫ�޶���
	 * @param access_flags ���ʱ�ʶ
	 * @param method_name ������
	 * @param list ����������������
	 * @param fullQualifiedName_return �����������͵�ȫ�޶���
	 */
	public MethodInfo(String fullQualifiedName_class,int access_flags, String method_name, 
			ArrayList<String> list, String fullQualifiedName_return){
		this.fullQualifiedName_class=fullQualifiedName_class;
		this.access_flags=access_flags;
		this.method_name=method_name;
		param_types=list;
		this.fullQualifiedName_return=fullQualifiedName_return;
	}
	
	/**
	 * �ж����������Ƿ��Ӧ��ͬ�ķ���
	 * @param mi ��һ������
	 * @return true ��ͬ; false ��ͬ
	 */
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof MethodInfo))
			return false;
		MethodInfo mi=(MethodInfo)obj;
		if(!fullQualifiedName_class.equals(mi.fullQualifiedName_class))
			return false;
		boolean nameSame=method_name.equals(mi.method_name);
		boolean param_types_len_same=param_types.size()==mi.param_types.size();
		boolean paramNotSame=false;
		if(param_types_len_same){
			for(int i=0; i<=param_types.size()-1; i++){
				String t1=param_types.get(i);
				String t2=mi.param_types.get(i);
				if(!t1.equals(t2)){
					paramNotSame=true;
					break;
				}
			}
		}
		else{
			paramNotSame=true;
		}
		return nameSame&&(!paramNotSame);
	}
	
	@Override
	public String toString(){
		String result=method_name+"(";
		for(int i=0; i<=param_types.size()-2; i++)
			result=result+param_types.get(i)+", ";
		if(param_types.size()>=1)
			result=result+param_types.get(param_types.size()-1)+")";
		else
			result=result+")";
		return result;
	}
	
	@Override
	public MethodInfo clone(){
		ArrayList<String> ns=new ArrayList<String>();
		for(String s: param_types)
			ns.add(s);
		return new MethodInfo(fullQualifiedName_class, access_flags, method_name, ns, fullQualifiedName_return);
	}
	
	/**
	 * ��ȡ������MehodName
	 * @return �÷�����MethodName
	 */
	public MethodName getMethodName(){
		return new MethodName(fullQualifiedName_class, method_name, param_types);
	}

}
