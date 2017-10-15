package vo.common_obj;

public class ReferenceFieldInfo extends FieldInfo {
	
	public int handle;
	
	/**
	 * ������
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶμ�����
	 * @param ft �ֶ�ȫ�޶�����
	 * @param h ���
	 */
	public ReferenceFieldInfo(String fqn, String fn, String ft, int h){
		super(fqn, fn, ft);
		this.handle=h;
	}

	/**
	 * ������
	 * @param ac ���ʱ�ʶ
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶμ�����
	 * @param ft �ֶ�ȫ�޶�����
	 * @param h ���
	 */
	public ReferenceFieldInfo(int ac, String fqn, String fn, String ft, int handle) {
		super(ac, fqn, fn, ft);
		// TODO Auto-generated constructor stub
		this.handle=handle;
	}

	public ReferenceFieldInfo(FieldInfo field_info, int i) {
		// TODO Auto-generated constructor stub
		super(field_info);
		this.handle=i;
	}
	
	/**
	 * �������������ֶ�
	 */
	@Override
	public ReferenceFieldInfo clone(){ 
		return new ReferenceFieldInfo(access_flags, fullQualifiedName, fieldName, fieldType, handle);
	}

}
