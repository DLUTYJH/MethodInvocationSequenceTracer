package business_logic.memory;

/**
 * �ڴ������<br/>
 * ����������а��ڴ��Ϊ��������������ȱ����JVM�淶�е�ջ���ͳ����������<br/>
 * ջ����Ϣ��VariableTable����洢��
 * ��Ϊ�Ǳ����﷨�������Բ��ó����������
 * @author ��½��
 *
 */
public class SetManager {
	public InstanceSet jh;
	public ClassSet ma;
	public ArrArea aa;
	
	public SetManager(){
		partition();
	}

	/**
	 * �����ڴ�
	 */
	public void partition(){
		jh=new InstanceSet();
		ma=new ClassSet();
		aa=new ArrArea();
	}
	
	@Override
	public SetManager clone(){
		SetManager mm=new SetManager();
		mm.jh=this.jh.clone();
		mm.ma=this.ma.clone();
		mm.aa=this.aa.clone();
		return mm;
	}

}
