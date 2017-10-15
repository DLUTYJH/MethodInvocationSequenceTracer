package vo.common_obj;

/**
 * ���һ���ֶε���Ϣ
 * @author ��½��
 *
 */
public class FieldInfo {
	
	public int access_flags;
	public String fullQualifiedName;
	public String fieldName;
	public String fieldType;
	
	/**
	 * ������
	 * @param fullQualifiedName �ֶ����ڵ����ȫ�޶���
	 * @param fn �ֶ���
	 * @param ft �ֶ������������ֶ�ȫ�޶�����
	 */
	public FieldInfo(String fullQualifiedName, String fn, String ft){
		this.fullQualifiedName=fullQualifiedName;
		this.access_flags=0x0000;
		this.fieldName=fn;
		this.fieldType=ft;
	}
	
	/**
	 * ������
	 * @param ac �ֶη��ʱ�ʶ
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶ���
	 * @param ft �ֶ������������ֶε�ȫ�޶�����
	 */
	public FieldInfo(int ac, String fqn, String fn, String ft){
		this.access_flags=ac;
		this.fullQualifiedName=fqn;
		this.fieldName=fn;
		this.fieldType=ft;
	}
	
	/**
	 * ������
	 * @param fi �ֶ���Ϣ����
	 */
	public FieldInfo(FieldInfo fi){
		this.access_flags=fi.access_flags;
		this.fullQualifiedName=fi.fullQualifiedName;
		this.fieldName=fi.fieldName;
		this.fieldType=fi.fieldType;
	}
	
	/**
	 * ��ȡFieldName
	 * @return �ֶε�FiedName
	 */
	public FieldName getFieldName(){
		return new FieldName(fieldName);
	}
	
	/**
	 * ��ȡFieldName2
	 * @return �ֶε�FieldName2
	 */
	public FieldName2 getFieldName2(){
		return new FieldName2(fullQualifiedName, fieldName);
	}
	
	@Override
	public int hashCode(){
		return fieldName.hashCode();
	}
	
	@Override
	public boolean equals(Object fi){
		if(!(fi instanceof FieldInfo))
			return false;
		FieldInfo tmp=(FieldInfo)fi;
		if(fullQualifiedName.equals(tmp.fullQualifiedName)&&
				fieldName.equals(tmp.fieldName)&&
				   fieldType.equals(tmp.fieldType))
			return true;
		return false;
	}
	
	@Override
	public FieldInfo clone(){
		return new FieldInfo(access_flags, fullQualifiedName, fieldName, fieldType);
	}
}
