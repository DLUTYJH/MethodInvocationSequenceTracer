package vo.info_obj;

/**
 * ������������ֵ-���Ͷ�
 * @author ��½��
 *
 */
public class PrimitiveValueTypePair extends ValueTypePair{
	
	public String value;

	public PrimitiveValueTypePair(String t, String value) {
		super(t);
		// TODO Auto-generated constructor stub
		this.value=value;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof PrimitiveValueTypePair))
			return false;
		PrimitiveValueTypePair vtp=(PrimitiveValueTypePair)obj;
		if(!(vtp instanceof PrimitiveValueTypePair))
			return false;
		else{
			if(((PrimitiveValueTypePair)vtp).type.toString().equals(this.type.toString())&&
					((PrimitiveValueTypePair)vtp).value.equals(this.value))
				return true;
		}
		return false;
	}
	
	@Override
	public ValueTypePair clone(){
		return new PrimitiveValueTypePair(type, value);
	}

}
