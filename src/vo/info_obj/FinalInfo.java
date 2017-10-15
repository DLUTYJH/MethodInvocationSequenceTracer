package vo.info_obj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.Function;
import vo.common_obj.ArrFieldInfo;
import vo.common_obj.ArrayVarInfo;
import vo.common_obj.FieldInfo;
import vo.common_obj.FieldName2;
import vo.common_obj.PrimitiveFieldInfo;
import vo.common_obj.PrimitiveVarInfo;
import vo.common_obj.ReferenceFieldInfo;
import vo.common_obj.ReferenceVarInfo;
import vo.common_obj.VariableInfo;

/**
 * ���նԳ������ִ�����õ�������Ϣ
 * @author ��½��
 *
 */
public class FinalInfo {
	
	public String className;
	
	public ArrayList<InfoUnit> infs;
	
	public FinalInfo(){
		this.className=null;
		this.infs=new ArrayList<InfoUnit>();
	}
	
	@Override
	public String toString(){
		String result="";
		for(InfoUnit table: infs){
			if(table.cons.info!=null)
				result=result+"·��Լ��Ϊ"+table.cons.toString()+"ʱ��\n";
			if(table.methods.size()!=0){
				result=result+"������������Ϊ��\n";
				for(int i=0; i<= table.methods.size()-2; i++){
					InvocationInfo m=table.methods.get(i);
					result=result+m.toString()+", ";	
				}
				result=result+table.methods.get(table.methods.size()-1)+"��";
				result=result+"\n";
			}
			result=result+"�����޸���ϢΪ��\n";
			
			List<Map.Entry<String, VariableInfo>> e=new ArrayList<Map.Entry<String, VariableInfo>>(table.variables.entrySet());
			Collections.sort(e, new Comparator<Map.Entry<String, VariableInfo>>(){

				@Override
				public int compare(Entry<String, VariableInfo> arg0,
						Entry<String, VariableInfo> arg1) {
					// TODO Auto-generated method stub
					return (arg0.getKey()).toString().compareTo(arg1.getKey());
				}
				
			});
			
			for (Map.Entry<String, VariableInfo> entry: e) {
			        String vname=entry.getKey();
			        VariableInfo vi=entry.getValue();
				    if(vi.isParam){
				        String vvalue=varToString(table, vi);
				    	result=result+vname+"�ķ��ű��ʽ��"+vvalue+"\n";
				    }
				}
		}
		return result;
	}
	
	
	/**
	 * �ѱ�����ϢתΪ�ַ�����ʽ
	 * @return ������Ϣ���ַ�����ʽ
	 */
	public String varToString(InfoUnit table, VariableInfo vi){
		String str="";
		if(Function.isPrimitiveType(vi.type)){
			//�����������ͣ�ֱ�ӷ��������ֵ
			PrimitiveVarInfo pvi=(PrimitiveVarInfo)vi;
			str=str+(String)pvi.value.peek();
			return str;
		}
		else if(Function.isArrayType(vi.type)){
			//�������ͣ��ݹ��������ÿ����Ԫ�ķ���ֵ
			ArrayVarInfo avi=(ArrayVarInfo)vi;
			str=str+"[";
			for(int i=0; i<=table.mm.aa.arr[avi.first_address].length-2; i++){
				VariableInfo unit=table.mm.aa.arr[avi.first_address][i];
				String unitstr=varToString(table, unit);
				str=str+unitstr+", ";
			}
			VariableInfo unitlast=table.mm.aa.arr[avi.first_address][table.mm.aa.arr[avi.first_address].length-1];
			str=str+varToString(table, unitlast)+"]";
			return str;
		}
		else if(Function.isQualifiedType(vi.type)||Function.isSimpleType(vi.type)){
			//�������ͣ��ݹ����������ָ��Ķ����ÿ���ֶεķ���ֵ
			ReferenceVarInfo rvi=(ReferenceVarInfo)vi;
			str=str+"[";
			Iterator<Map.Entry<FieldName2, FieldInfo>> ite=table.mm.jh.objs[rvi.handle].field.entrySet().iterator();
			while(ite.hasNext()){
				Map.Entry<FieldName2, FieldInfo> entry = ite.next();
				if(ite.hasNext())
					str=str+entry.getKey().fieldName+": "+fieldToString(table, entry.getValue())+", ";
				else
					str=str+entry.getKey().fieldName+": "+fieldToString(table, entry.getValue());
			}
			str=str+"]";
			return str;
		}
		return null;
	}
	
	/**
	 * ���ֶ���Ϣת��ΪString��ʽ
	 * @param table ���ű�
	 * @param fi �ֶ���Ϣ
	 * @return �ֶε�String��ʽ
	 */
	public String fieldToString(InfoUnit table, FieldInfo fi){
		String str="";
		if(Function.isPrimitiveType(fi.fieldType)){
		//	System.out.println(fi.getClass());
			PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)fi;
			str=str+pfi.value;
			return str;
		}
		else if(Function.isArrayType(fi.fieldType)){
			ArrFieldInfo afi=(ArrFieldInfo)fi;
			str=str+"[";
			for(int i=0; i<=table.mm.aa.arr[afi.f].length-2; i++){
				VariableInfo unit=table.mm.aa.arr[afi.f][i];
				String unitstr=varToString(table, unit);
				str=str+unitstr+", ";
			}
			VariableInfo unitlast=table.mm.aa.arr[afi.f][table.mm.aa.arr[afi.f].length-1];
			str=str+varToString(table, unitlast)+"]";
			return str;
		}
		else if(Function.isQualifiedType(fi.fieldType)||Function.isSimpleType(fi.fieldType)){
			ReferenceFieldInfo rvi=(ReferenceFieldInfo)fi;
			str=str+"[";
			Iterator<Map.Entry<FieldName2, FieldInfo>> ite=table.mm.jh.objs[rvi.handle].field.entrySet().iterator();
			while(ite.hasNext()){
				Map.Entry<FieldName2, FieldInfo> entry = ite.next();
				if(ite.hasNext())
					str=str+entry.getKey().fieldName+": "+fieldToString(table, entry.getValue())+", ";
				else
					str=str+entry.getKey().fieldName+": "+fieldToString(table, entry.getValue());
			}
			str=str+"]";
			return str;
		}
		return null;
	}

}
