package vo.common_obj;

/**
 * �ֶεļ���
 * @author ��½��
 *
 */
public class FieldName {
	public String fieldName;
	
	/**
	 * ������
	 * @param fieldName �ֶ���
	 */
	public FieldName(String fieldName){
		this.fieldName=fieldName;
	}
	
	@Override
	public int hashCode(){
		return fieldName.hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		if(!(o instanceof FieldName))
			return false;
		FieldName tmp=(FieldName)o;
		if(tmp.fieldName.equals(fieldName))
			return true;
		return false;
	}
}
