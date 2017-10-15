package vo.info_obj;

import vo.common_obj.MethodInfo;

/**
 * ���淽���뷽��֮��ĵ����뷵����Ϣ
 * @author ��½��
 *
 */
public class InvocationInfo {
	
	public MethodInfo m1;
	public MethodInfo m2;
	public int relationship; //��ʾ����֮��ĵ��ù�ϵ����0��ʾm1����m2����1��ʾ��m1���ص�m2
	
	/**
	 * ������
	 * @param m1 ����1
	 * @param m2 ����2
	 * @param rel ������ϵ
	 */
	public InvocationInfo(MethodInfo m1, MethodInfo m2, int rel){
		this.m1=m1;
		this.m2=m2;
		relationship=rel;
	}
	
	public String toString(){
		String str1=m1.fullQualifiedName_class+"."+m1.toString();
		String str2=m2.fullQualifiedName_class+"."+m2.toString();
		String str3=(relationship==0?"������":"���ص�");
		return str1+str3+str2;
	}

	@Override
	public InvocationInfo clone(){
		return new InvocationInfo(m1.clone(), m2.clone(), relationship);
	}
}
