package vo.info_obj;

/**
 * ���ʽ��ֵ-���ͶԻ��߷������÷��ص�ֵ-���Ͷ�
 * @author ��½��
 *
 */
public class ValueTypePair {
	
	public String type;
	
	/**
	 * ������
	 * @param t ȫ�޶�����
	 */
	public ValueTypePair(String t){
		type=t;
	}
	
	/**
	 * �ж��Ƿ����
	 * @param o ��һ��ֵ-���Ͷ�
	 * @return true ��ȣ�false ����
	 */
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof ValueTypePair))
			return false;
		ValueTypePair o=(ValueTypePair)obj;
		if(o.type.toString().equals(this.type.toString())) return true;
		return false;
	}
	
	/**
	 * ����
	 */
	@Override
	public ValueTypePair clone(){
		return new ValueTypePair(type);
	}

}
