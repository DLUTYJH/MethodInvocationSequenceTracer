package vo.common_obj;

public class PrimitiveFieldInfo extends FieldInfo {
	
	public String value;
	
	/**
	 * ������
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶεļ�����
	 * @param ft �ֶ����͵�ȫ�޶���
	 * @param v ֵ
	 */
	public PrimitiveFieldInfo(String fqn, String fn, String ft, String v){
		super(fqn, fn, ft);
		this.value=v;
	}

	/**
	 * ������
	 * @param ac ���ʱ�ʶ
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶ���
	 * @param ft �ֶ����͵�ȫ�޶���
	 * @param v ֵ
	 */
	public PrimitiveFieldInfo(int ac, String fqn, String fn, String ft, String v) {
		super(ac, fqn, fn, ft);
		// TODO Auto-generated constructor stub
		this.value=v;
	}

	public PrimitiveFieldInfo(FieldInfo field_info, String v) {
		// TODO Auto-generated constructor stub
		super(field_info);
		this.value=v;
	}
	
	/**
	 * ���ƻ������������ֶ�
	 */
	@Override
	public PrimitiveFieldInfo clone(){
		return new PrimitiveFieldInfo(access_flags, fullQualifiedName, fieldName, fieldType, value);
	}

}
