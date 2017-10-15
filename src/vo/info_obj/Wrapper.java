package vo.info_obj;

import java.util.ArrayList;

/**
 * ��װֵ���Ͷ����ͺͷ�����Ϣ�б�
 * @author ��½��
 *
 */
public class Wrapper {
	
	public ValueTypePair vtp;
	public ArrayList<ReturnInfo> ret;
	public int type; //Ϊ0��ʾʹ��ֵ���Ͷԣ�Ϊ1��ʾʹ�÷�����Ϣ�б�
	
	public Wrapper(){
		this.vtp=null;
		this.ret=null;
		type=-1;
	}
	
	/**
	 * ������
	 * @param vtp ����װ��ֵ-���Ͷ�
	 */
	public Wrapper(ValueTypePair vtp){
		this.vtp=vtp;
		ret=null;
		type=0;
	}
	
	/**
	 * ������
	 * @param ret ����װ�ķ���������Ϣ�б�
	 */
	public Wrapper(ArrayList<ReturnInfo> ret){
		this.ret=ret;
		vtp=null;
		type=1;
	}

}
