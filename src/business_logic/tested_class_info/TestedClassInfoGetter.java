package business_logic.tested_class_info;

import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * ��������Ϣ��ȡ����
 * @author ��½��
 *
 */
public class TestedClassInfoGetter {
	
	public TestedClassInfoVisitor getMethods(CompilationUnit root){
		TestedClassInfoVisitor mv=new TestedClassInfoVisitor();
		root.accept(mv);
		return mv;
	}

}
