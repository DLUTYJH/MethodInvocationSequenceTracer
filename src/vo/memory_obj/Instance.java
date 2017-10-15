package vo.memory_obj;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vo.common_obj.FieldInfo;
import vo.common_obj.FieldName;
import vo.common_obj.FieldName2;

/**
 * ����ʵ��
 * @author ��½��
 *
 */
public class Instance {
	public String type; //����ͷ��ʵ�����������͵�ȫ�޶���
	public HashMap<FieldName2, FieldInfo> field;    //�����ʵ�����ݣ�KΪ�ֶ�����VΪ�ֶ���Ϣ
	
	/**
	 * ������
	 * @param t ʵ�����������ȫ�޶���
	 */
	public Instance(String t){
		this.type=t;
		field=new HashMap<FieldName2, FieldInfo>();
	}
	
	/**
	 * ����һ���ֶ���Ϣ
	 * @param fi �ֶ���Ϣ
	 */
	public void addOneField(FieldInfo fi){
		field.put(fi.getFieldName2(), fi);
	}
	
	/**
	 * ��ȡһ���ֶε���Ϣ
	 * @param simpleName Ҫ��ȡ���ֶε�FieldName
	 * @return �ֶ���Ϣ
	 */
	public FieldInfo getOneField(FieldName simpleName){
		return field.get(new FieldName2(type, simpleName.fieldName));
		
	}
	
	@Override
	public Instance clone(){
		Instance ins=new Instance(type);
		 Iterator<Map.Entry<FieldName2, FieldInfo>> ite=field.entrySet().iterator();
		 while(ite.hasNext()){
			 Map.Entry<FieldName2, FieldInfo> entry=ite.next();
			 ins.addOneField(entry.getValue().clone());
		 }
		 return ins;
	}
}
