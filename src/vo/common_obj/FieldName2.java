package vo.common_obj;

/**
 * �ֶ��������ȫ�޶������ֶεļ���<br/>
 * ��Ψһ��������һ���ֶ�
 * @author ��½��
 *
 */
public class FieldName2 {
    public String fullQualifiedName;
	public String fieldName;
	
	/**
	 * ������
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fieldName �ֶ���
	 */
	public FieldName2(String fqn, String fieldName){
		this.fullQualifiedName=fqn;
		this.fieldName=fieldName;
	}
	
	@Override
	public int hashCode(){
		return fieldName.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof FieldName2))
			return false;
		FieldName2 tmp=(FieldName2)o;
		if(tmp.fullQualifiedName.equals(fullQualifiedName)&&
				tmp.fieldName.equals(fieldName))
			return true;
		return false;
	}

}
