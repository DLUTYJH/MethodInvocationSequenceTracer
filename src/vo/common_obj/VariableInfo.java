package vo.common_obj;

/**
 * �ֲ�������Ϣ��
 * @author ��½��
 *
 */
public class VariableInfo{
	
	public String name;  //������
	public boolean isParam=false;   //��ʾ�Ƿ��ǲ����������ھֲ�������
	public String type;   //�������������
//	public String actualType; //������ʵ������
	
	/**
	 * δ��ʼ�������Ĺ�����
	 * @param ov ������
	 * @param isParam �����Ƿ��Ƿ�������
	 * @param t ����������ͣ���һ��ȫ�޶�����
	 */
	public VariableInfo(String ov, boolean isParam, String t){
		name=ov;
		this.isParam =isParam;
		type=t;
	}
	
	@Override
	public VariableInfo clone(){
		return new VariableInfo(name, isParam, type);
	}
	
}
