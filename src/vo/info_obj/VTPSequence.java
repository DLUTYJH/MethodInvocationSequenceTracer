package vo.info_obj;

import java.util.ArrayList;



/**
 * ��ӦPC�¸������ʽ��ֵ
 * @author ��½��
 *
 */
public class VTPSequence {
	
	public ConstraintInfo pc;
	public ArrayList<ValueTypePair> seq;
	
	/**
	 * ������
	 * @param c ·��Լ��
	 * @param seq ��Լ���¸����ʽ��ֵ
	 */
	public VTPSequence(ConstraintInfo c, ArrayList<ValueTypePair> seq){
		this.pc=c;
		this.seq=seq;
	}

}
