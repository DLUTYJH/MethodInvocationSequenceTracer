package vo.common_obj;

import java.util.Stack;

/**
 * �����������ͱ�����Ϣ��
 * @author ��½��
 *
 */
public class PrimitiveVarInfo extends VariableInfo{

	public Stack<Object> value;
	
	/**
	 * �����������ͱ���������
	 * @param ov ������
	 * @param isParam �Ƿ����
	 * @param t ����
	 * @param nv ����ֵ
	 */
	public PrimitiveVarInfo(String ov, boolean isParam, String t, String nv) {
		super(ov, isParam, t);
		value=new Stack<Object>();
	    value.push(nv);
	}

	@Override
	public VariableInfo clone(){
		return new PrimitiveVarInfo(name, isParam, type, (String)value.peek());
	}
}
