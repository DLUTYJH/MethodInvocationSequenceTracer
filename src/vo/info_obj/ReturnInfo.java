package vo.info_obj;

/**
 * ������������Ӧ·��Լ���µķ���ֵ
 * @author ��½��
 *
 */
public class ReturnInfo {
	public ConstraintInfo pc;
	public ValueTypePair retvalue;
	
	/**
	 * ������
	 * @param p ֵ-���Ͷ�
	 */
	public ReturnInfo(ValueTypePair p){
		retvalue=p;
		pc=new ConstraintInfo();
	}
	
	/**
	 * ������
	 * @param pc ·��Լ��
	 * @param p ֵ-���Ͷ�
	 */
	public ReturnInfo(ConstraintInfo pc, ValueTypePair p){
		this.pc=pc;
		retvalue=p;
	}
}
