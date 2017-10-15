package vo.memory_obj;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vo.common_obj.FieldInfo;
import vo.common_obj.FieldName;

/**
 * �ֶα�
 * @author ��½��
 *
 */
public class FieldTable {
	
	public HashMap<FieldName, FieldInfo> fields;
	
	/**
	 * ������
	 */
	public FieldTable(){
		fields=new HashMap<FieldName, FieldInfo>();
	}
	
	/**
	 * ���ֶα��м���һ��
	 * @param fi
	 */
	public void addOneField(FieldInfo fi){
		fields.put(fi.getFieldName(), fi);
	}
	
	@Override
	public FieldTable clone(){
		FieldTable ft=new FieldTable();
		Iterator<Map.Entry<FieldName, FieldInfo>> ite=fields.entrySet().iterator();
		while(ite.hasNext()){
			 Map.Entry<FieldName, FieldInfo> entry=ite.next();
			 ft.addOneField(entry.getValue().clone());
		 }
		 return ft;
	}

}
