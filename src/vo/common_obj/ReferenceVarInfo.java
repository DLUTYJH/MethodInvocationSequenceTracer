package vo.common_obj;

/**
 * �������ͱ�����Ϣ��
 * @author ��½��
 *
 */
public class ReferenceVarInfo extends VariableInfo {

	public int handle;	

	/**
	 * �������ͱ���������
	 * @param ov ������
	 * @param isParam �Ƿ����
	 * @param t ȫ�޶�����
	 * @param h ���
	 */
	public ReferenceVarInfo(String ov, boolean isParam, String t, int h) {
		super(ov, isParam, t);
		this.handle=h;
	}
	
	@Override
	public VariableInfo clone(){
		return new ReferenceVarInfo(name, isParam, type, handle);
	}

}
