package vo.common_obj;

/**
 * �����ͱ�����Ϣ��
 * @author ��½��
 *
 */
public class ArrayVarInfo extends VariableInfo{

	public int first_address;
	
	/**
	 * �����ͱ���������
	 * @param ov ������
	 * @param isParam �Ƿ��ǲ���
	 * @param t ����
	 * @param f �����׵�Ԫ��ַ
	 */
	public ArrayVarInfo(String ov, boolean isParam, String t, int f) {
		super(ov, isParam, t);
		first_address=f;
	}
	
	@Override
	public VariableInfo clone(){
		ArrayVarInfo avi=new ArrayVarInfo(name, isParam, type, first_address);
		return avi;
	}

}
