package business_logic.tested_class_info;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vo.common_obj.MethodInfo;

/**
 * ��ȡ���������Ӧ��Ϣ�����ڽ���չʾ�ȡ�
 * @author ��½��
 *
 */
public class TestedClassInfoVisitor extends ASTVisitor {
	  private String packageAndName;  //�������ӡ�\�������������ļ�������.java��׺
	  private String packageInfo; //��·��
	  private ArrayList<ImportDeclaration> imports=new ArrayList<ImportDeclaration>(); //������Ϣ
      private ArrayList<MethodInfo> methods=new ArrayList<MethodInfo>();  //�෽����Ϣ
      
      @Override
      public boolean visit(MethodDeclaration node){ 
    	  SimpleName name=node.getName();
    	  String iden=name.getIdentifier();
    	  
    	 @SuppressWarnings("unchecked")
	   	  List<SingleVariableDeclaration> params=node.parameters();
    	  ArrayList<String> paramtypes=new ArrayList<String>();
    	  for(SingleVariableDeclaration svd: params){
    		 Type t=svd.getType(); 
    		 paramtypes.add(t.toString());
    	  }   
    	 int modifiers=node.getModifiers();
    	 String tmp1=packageAndName.substring(0, packageAndName.length()-5);
		 String tmp2=tmp1.replace("\\", ".");
    	 MethodInfo mi=new MethodInfo(tmp2, modifiers, iden, paramtypes);
         methods.add(mi);  
    	 return false;
      }
      
      @Override
      public boolean visit(PackageDeclaration node){
    	  Name name=node.getName();
    	  String fullQualifiedName=name.getFullyQualifiedName();
    	  fullQualifiedName=fullQualifiedName.replace('.', '\\');
    	  packageAndName=fullQualifiedName;
    	  packageInfo=fullQualifiedName;
    	  return false;
      }
      
      @Override
      public boolean visit(TypeDeclaration node){
    	  SimpleName sn=node.getName();
    	  String className=sn.getIdentifier();
    	  if(packageAndName==null)   //default��
    		   packageAndName=className+".java";
    	  else{
    		  packageAndName=packageAndName+"\\"+className+".java";
    	  }
    	  return true;
      }
      
      @Override
      public boolean visit(ImportDeclaration node){
    	  imports.add(node);
    	  return false;
      }
      
      public String getPackageAndName(){
    	  return packageAndName;
      }
      
      public String getPackageInfo(){
    	  return packageInfo;
      }
      
      public ArrayList<ImportDeclaration> getImports(){
    	  return imports;
      }
      
      public ArrayList<MethodInfo> getMethods(){
    	  return methods;
      }
}
