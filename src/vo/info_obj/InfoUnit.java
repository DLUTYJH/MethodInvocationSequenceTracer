package vo.info_obj;

import java.util.ArrayList;
import java.util.HashMap;

import vo.common_obj.VariableInfo;

import business_logic.memory.SetManager;

/**
 * ��Ϣ��Ԫ���󣬼�¼ĳPC�µĸ�����Ϣ
 * @author ��½��
 */
public class InfoUnit {
	public ConstraintInfo cons;
	public HashMap<String, VariableInfo> variables;
	public ArrayList<InvocationInfo> methods;  
	public ValueTypePair retval;  //�����ڸ�PC�µķ���ֵ��δ����return���ʱʼ��Ϊnull������Ϊnullʱ��ʾ��·����ִ�н���
    public SetManager mm; //��PC�µ��ڴ�������
	
	public InfoUnit(){
		variables=new HashMap<String, VariableInfo>();
		cons=new ConstraintInfo();
		methods=new ArrayList<InvocationInfo>();
		retval=null;
		mm=new SetManager();
	}
	
	/**
	 * �����ű������±���
	 * @param vi Ҫ���������ű��еı�����Ϣ
	 */
	public void addOneVariable(VariableInfo vi){
		variables.put(vi.name, vi);
	}
	
	/**
	 * ��ȡ������Ϣ
	 * @param vname ������
	 * @return ��������Ϣ
	 */
	public VariableInfo getVariableInfo(String vname){
		return variables.get(vname);
	}
	
	/**
	 * ɾ������
	 * @param vname ������
	 */
	public void removeOneVariable(String vname){
		variables.remove(vname);
	}
	
}
