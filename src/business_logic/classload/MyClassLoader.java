package business_logic.classload;

import org.eclipse.jdt.core.dom.CompilationUnit;

import business_logic.ast.ASTCreator;
import business_logic.memory.SetManager;
import vo.memory_obj.ClassInfo;

/**
 * ������������������<br/>
 * JVM�淶Ҫ������ذ��������ء���֤��׼������������ʼ���Ƚ׶Σ�<br/>
 * ���ﲻ��Ҫ��֤�׶Ρ������׶Ρ�
 * @author ��½��
 *
 */
public class MyClassLoader {
	
	public SetManager mm;
	
	public MyClassLoader(SetManager mm){
		this.mm=mm;
	}
	
	/**
	 * ������<br/>
	 * ִ��JVM�淶�еļ��ء�׼���ͳ�ʼ���׶�
	 * @param path Դ�ļ�·��
	 * @return ����Ϣ
	 */
	public ClassInfo loadClass(String path, String projectRootPath){
		 ASTCreator ac=new ASTCreator();
		 CompilationUnit root=ac.createAST(path);
		 ClassResolveVisitor crv=new ClassResolveVisitor(projectRootPath);
		 root.accept(crv);
		 
		 ClassInfo cl=crv.getCl();
		 if(mm.ma.hasLoaded(cl)){
			 //do nothing
		 }
		 else{
			 mm.ma.addOneClass(cl);
		 }
		 return cl;
	}

}
