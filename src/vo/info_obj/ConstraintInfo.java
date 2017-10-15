package vo.info_obj;

/**
 * ·��Լ����
 * @author ��½��
 *
 */
public class ConstraintInfo {
	
	public String info;
	
	public ConstraintInfo(){
		info=null; //null��ʾ��Լ��
	}
	
	public ConstraintInfo(String i){
		info=i;
	}
	
	public String toString(){
		return info==null?"true":info;
	}
	
	/**
	 * �����
	 * @param c ��һ��PC
	 * @return ��ȡ���PC
	 */
	public ConstraintInfo and(ConstraintInfo c){
		ConstraintInfo newInfo=new ConstraintInfo();
		if(info==null||c.info==null)
			newInfo.info=(info==null)?c.info:info;
		else{
			newInfo.info="("+info+"&&"+c.info+")";
		}
		return newInfo;
	}
	
	/**
	 * �����
	 * @param c ��һ��PC
	 * @return ��ȡ���PC
	 */
	public ConstraintInfo or(ConstraintInfo c){
		ConstraintInfo newInfo=new ConstraintInfo();
		if(info==null||c.info==null)
			newInfo.info=null;
		else{
			newInfo.info="("+info+"||"+c.info+")";
		}
		return newInfo;
	}
	
	/**
	 * ȡ������
	 * @return ȡ�����PC
	 */
	public ConstraintInfo not(){
		ConstraintInfo newInfo=new ConstraintInfo();
		if(info==null){
			newInfo.info="false";
		}
		else
			newInfo.info="!"+info;
		return newInfo;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof ConstraintInfo))
			return false;
		ConstraintInfo c=(ConstraintInfo)obj;
		if(info==null){
			return c.info==null;
		}
		return info.equals(c.info);
	}

}
