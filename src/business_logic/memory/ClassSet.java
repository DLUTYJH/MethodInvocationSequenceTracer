package business_logic.memory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vo.memory_obj.ClassInfo;

/**
 * �ڴ淽����������ѱ����ص��������Ϣ����ʱ�����Ĵ��롢��������̬����<br/>
 * ���ﲻ�ô洢���롢�����;�̬����
 * @author ��½��
 *
 */
public class ClassSet {
	
	public HashMap<String, ClassInfo> cs;   //���ȫ�޶��������������Ϣ
	
	public ClassSet(){
		cs=new HashMap<String, ClassInfo>();
	}
	
	/**
	 * ���һ������Ϣ���󵽷�����
	 * @param c
	 */
	public void addOneClass(ClassInfo c){
		cs.put(c.qualifiedName, c);
	}
	
	/**
	 * �ж����Ƿ��Ѿ�������
	 * @param c ��
	 * @return true �Ѽ���; false δ����
	 */
	public boolean hasLoaded(ClassInfo c){
		String cname=c.qualifiedName;
		if(cs.get(cname)!=null)
			return true;
		return false;
	}
	
	/**
	 * �������ȫ�޶�����ȡ����Ϣ
	 * @param fullQualifiedName ���ȫ�޶���
	 * @return ����Ϣ
	 */
	public ClassInfo getClassInfo(String fullQualifiedName){
		return cs.get(fullQualifiedName);
	}
	
	/**
	 * ���Ʒ�����
	 */
	@Override
	public ClassSet clone(){
		ClassSet ma=new ClassSet();
		 Iterator<Map.Entry<String, ClassInfo>> ite=cs.entrySet().iterator();
		 while(ite.hasNext()){
			 Map.Entry<String, ClassInfo> entry=ite.next();
			 ma.addOneClass(entry.getValue().clone());
		 }
		return ma;
	}

}
