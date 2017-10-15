package business_logic.memory;

import util.InitialValues;
import vo.common_obj.VariableInfo;

/**
 * ����洢��
 * @author ��½��
 *
 */
public class ArrArea {
	
	public VariableInfo[][] arr;
	public int currentSize;
	public int maxSize;
	
	public ArrArea(){
		maxSize=InitialValues.arrAreaInitSize;
		arr=new VariableInfo[maxSize][];  //��ʼ�����⼸��
		currentSize=0;
	}
	
	/**
	 * Ϊһ��cols�е��������ռ䲢���ص�һ����Ԫ�ĵ�ַ
	 * @param cols ����
	 * @return ��һ����Ԫ�ĵ�ַ
	 */
	public int alloc(int cols){
		if(currentSize==maxSize){
			resize();
		}
		int pointer=currentSize;
		arr[pointer]=new VariableInfo[cols];
		currentSize++;
		return pointer;
	}
	
	/**
	 * ���·����С
	 */
	public void resize(){
		VariableInfo[][] tmp=new VariableInfo[maxSize][];
		for(int i=0; i<=maxSize-1; i++){
			tmp[i]=arr[i];
		}
		arr=new VariableInfo[2*maxSize][];
		for(int i=0; i<=maxSize-1; i++){
			arr[i]=tmp[i];
		}
		maxSize=2*maxSize;
	}
	
	/**
	 * ������������
	 */
	@Override
	public ArrArea clone(){
		ArrArea aa=new ArrArea();
		aa.currentSize=this.currentSize;
		aa.maxSize=this.maxSize;
		aa.arr=new VariableInfo[aa.maxSize][];
		for(int i=0; i<=this.currentSize-1; i++){
			for(int j=0; j<=this.arr[i].length-1; j++)
				aa.arr[i][j]=this.arr[i][j].clone();
		}
		return aa;
	}

}
