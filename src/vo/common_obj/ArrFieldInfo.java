package vo.common_obj;

public class ArrFieldInfo extends FieldInfo {
	
	public int f;
	
	/**
	 * ������
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶ���
	 * @param ft �ֶ������������ֶ�ȫ�޶�����
	 * @param f �׵�ַ
	 */
	public ArrFieldInfo(String fqn, String fn, String ft, int f){
		super(fqn, fn, ft);
		this.f=f;
	}

	/**
	 * ������
	 * @param ac ���ʱ�־
	 * @param fqn �ֶ��������ȫ�޶���
	 * @param fn �ֶ���
	 * @param ft �ֶ���
	 * @param f ��һ����Ԫ�ĵ�ַ
	 */
	public ArrFieldInfo(int ac, String fqn, String fn, String ft, int f) {
		super(ac, fqn, fn, ft);
		// TODO Auto-generated constructor stub
		this.f=f;
	}

	public ArrFieldInfo(FieldInfo field_info, int i) {
		// TODO Auto-generated constructor stub
		super(field_info);
		this.f=i;
	}
	
	/**
	 * �������������ֶ�
	 */
	@Override
	public ArrFieldInfo clone(){
		return new ArrFieldInfo(access_flags, fullQualifiedName, fieldName, fieldType, f);
	}

}
