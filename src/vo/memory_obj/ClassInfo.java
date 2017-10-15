package vo.memory_obj;

import java.util.ArrayList;

/**
 * �ѱ����ص�����ڴ�class���󣬰���JVM�淶��class�ļ���ʽ�洢�����Ϣ
 * @author ��½��
 *
 */
public class ClassInfo {
	
	public int access_flags;
	public String qualifiedName; //ȫ�޶�����this��
	public ClassInfo father; //����
	public ArrayList<ClassInfo> interfaces; //ʵ�ֵĽӿ�
	public FieldTable ft;
	public MethodTable mt;
	
	/**
	 * ������
	 */
    public ClassInfo(){
    	access_flags=0x0000;
    	qualifiedName=null;
    	father=null;
    	interfaces=new ArrayList<ClassInfo>();
    	ft=new FieldTable();
    	mt=new MethodTable();
    }
    
    /**
     * ������
     * @param qn ���ȫ�޶���
     */
    public ClassInfo(String qn){
    	access_flags=0x0000;
    	qualifiedName=qn;
    	father=null;
    	interfaces=new ArrayList<ClassInfo>();
    	ft=new FieldTable();
    	mt=new MethodTable();
    }
    
    /**
     * ������
     * @param ac ���ʱ�ʶ
     * @param qn ���ȫ�޶���
     * @param f ������Ϣ
     * @param is �ӿ���Ϣ
     * @param ft �ֶα�
     * @param mt ������
     */
    public ClassInfo(int ac, String qn, ClassInfo f, ArrayList<ClassInfo> is, FieldTable ft,
    		MethodTable mt){
    	this.access_flags=ac;
    	this.qualifiedName=qn;
    	this.father=f;
    	this.interfaces=is;
    	this.ft=ft;
    	this.mt=mt;
    }
    
    @Override
    public ClassInfo clone(){
    	ArrayList<ClassInfo> is=new ArrayList<ClassInfo>();
    	for(int i=0; i<=interfaces.size()-1; i++)
    		is.add(interfaces.get(i).clone());
    	ClassInfo cl=new ClassInfo(access_flags, qualifiedName, father==null?null:father.clone(), is, ft.clone(), mt.clone());
    	return cl;
    }

}
