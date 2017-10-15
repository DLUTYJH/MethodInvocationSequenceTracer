package business_logic.execute;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import business_logic.ast.ASTCreator;
import business_logic.classload.MyClassLoader;
import business_logic.tested_class_info.TestedClassInfoGetter;
import business_logic.tested_class_info.TestedClassInfoVisitor;

import util.Function;
import util.InitialValues;
import vo.common_obj.ArrFieldInfo;
import vo.common_obj.ArrayVarInfo;
import vo.common_obj.FieldInfo;
import vo.common_obj.FieldName;
import vo.common_obj.FieldName2;
import vo.common_obj.MethodInfo;
import vo.common_obj.PrimitiveFieldInfo;
import vo.common_obj.PrimitiveVarInfo;
import vo.common_obj.ReferenceFieldInfo;
import vo.common_obj.ReferenceVarInfo;
import vo.common_obj.VariableInfo;
import vo.info_obj.ArrayValueTypePair;
import vo.info_obj.ConstraintInfo;
import vo.info_obj.FinalInfo;
import vo.info_obj.InvocationInfo;
import vo.info_obj.PrimitiveValueTypePair;
import vo.info_obj.ReferenceValueTypePair;
import vo.info_obj.ReturnInfo;
import vo.info_obj.ValueTypePair;
import vo.info_obj.InfoUnit;
import vo.info_obj.Wrapper;
import vo.memory_obj.ClassInfo;
import vo.memory_obj.Instance;

public class SEVisitor extends ASTVisitor {
	
	public MethodInfo mi;
	public CompilationUnit root;
	public ArrayList<MethodInfo> all;
	
	private String projectRootPath; //��Ŀ��·��
	private String packageInfo; //���������ڰ���·��
	private ArrayList<ImportDeclaration> imports;
	private int pointer=0;   //��ǰ�������Ϣ��Ԫָ��
	protected FinalInfo inf;
	
	/**
	 * ����ִ��Visitor������
	 * @param mi Ҫ�����õķ����ķ�����Ϣ
	 * @param root AST��
	 * @param all ��AST�����еķ���
	 * @param projectRootPath ��Ŀ��·��
	 * @param imports ��AST�ĵ�����Ϣ
	 * @param packageInfo ��AST�İ���Ϣ
	 */
	public SEVisitor(MethodInfo mi, CompilationUnit root, ArrayList<MethodInfo> all, String projectRootPath
			, ArrayList<ImportDeclaration> imports, String packageInfo){
		inf=new FinalInfo();
	    this.mi=mi;
	    this.root=root;
	    this.all=all;
	    this.projectRootPath=projectRootPath;
	    this.imports=imports;
	    this.packageInfo=packageInfo;
	}

	@Override
	public boolean visit(TypeDeclaration node){
		SimpleName name=node.getName();
		String className=name.getIdentifier();
		if(className.contains("."))
			inf.className=className;
		else{
			if(packageInfo!=null)
				inf.className=packageInfo+"."+className;
			else
				inf.className=className;
		}
		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node){
		//�����ж��Ƿ���Ҫ����ִ�еķ�����meetΪtrue��ʾ�ǣ�Ϊfalse��ʾ���ǡ�
		boolean meet=false;
		int modifiers=node.getModifiers();
		
		SimpleName name=node.getName();
  	    String iden=name.getIdentifier();
  	    @SuppressWarnings("unchecked")
    	List<SingleVariableDeclaration> params=node.parameters();
	    ArrayList<String> paramtypes=new ArrayList<String>();
	    for(int i=0; i<=params.size()-1; i++){
	    	SingleVariableDeclaration svd=params.get(i);
	    	Type t=svd.getType();
	    	paramtypes.add(t.toString());
	    }
	 	MethodInfo cmi=new MethodInfo(inf.className, iden, paramtypes);
	 	if(mi.equals(cmi))
	 		meet=true;
	  
	  if(meet){
		  //�ҵ���Ҫ����ִ�еķ�������������ִ��	��	
	      //һ��ʼֻ��һ�����ű�����PCΪnull, ����ֻ�з���������
		  //�Ǿ�̬�����Ⱦ�̬������һ��this����
		  InfoUnit vt=new InfoUnit();
		  //�ڷ���ִ��ǰ�ȼ�����
		  MyClassLoader mcl=new MyClassLoader(vt.mm);
		  mcl.loadClass(Function.resolveFilePath(inf.className, projectRootPath, packageInfo, imports),
				  projectRootPath);
		  if((modifiers&0x0008)==0x0008){
			  //��̬������do nothing
		  }
		  else{
			  //�Ǿ�̬��������ʼ��this��������ӵ����ű�
			  vt.addOneVariable(initSimpleTypeParam(vt, "this", inf.className));
		  }
		  for(int i=0; i<=params.size()-1; i++){
			  SingleVariableDeclaration svd=params.get(i);
			  String paramname=svd.getName().getIdentifier();
			  Type type=svd.getType();
			  if(type.isArrayType()){
		//		vt.addOneVariable(initArrayTypeParam(vt, paramname, type, ((ArrayType)type).getComponentType()));
			  }
			  else if(type.isParameterizedType()){
				 //�˰汾������
			  }
			  else if(type.isPrimitiveType()){
				 vt.addOneVariable(initPrimitiveTypeParam(vt, paramname, type.toString()));
			  }
			  else if(type.isQualifiedType()){
			     vt.addOneVariable(initQualifiedTypeParam(vt, paramname, type.toString()));
			  }
			  else if(type.isSimpleType()){
				 vt.addOneVariable(initSimpleTypeParam(vt, paramname, type.toString()));
			  }
			  else if(type.isUnionType()){
				  //�˰汾������
			  }
			  else if(type.isWildcardType()){
				  //�˰汾������
			  }
		  }
		  ArrayList<InfoUnit> infos=new ArrayList<InfoUnit>();
		  infos.add(vt);
		  seBlock(node.getBody(), infos);
		  
		  for(InfoUnit unit: infos){
			  inf.infs.add(unit);
		  }
		  return true;
	  }
	  else{
		  //����Ҫ����ִ�еķ�������ֱ�Ӻ��Ըýڵ㣬���ٷ�����������
		  return false;
	     }
	}
	
	/**
	 * ��ʼ����������ʵ��
	 * @param vt ���ű�
	 * @param paramname ������
	 * @param type ��������
	 * @param componentType ����������ͣ���ȥ��һ��ά�Ⱥ������
	 * @return ������Ϣ��Ŀ
	 */
	/*
	public VariableInfo initArrayTypeParam(VariableTable vt, String paramname, Type type, Type componentType){
		 //�������ͣ�����Ӧ�ռ��з����ڴ沢��ʼ����Щ�ڴ棬�ѱ�����ӵ����ű�
		  int firstAddress=vt.aa.alloc(Util.arrCols);
		  VariableInfo vi=new ArrayVarInfo(paramname, true, type, firstAddress);
		  for(int j=0; j<=Util.arrCols-1; j++){
			  VariableInfo toBeInit=vt.aa.arr[firstAddress][j];
			  if(componentType.isArrayType()){
				 toBeInit =initArrayTypeParam(vt, paramname+"["+j+"]", componentType, ((ArrayType)componentType).getComponentType());
			  }
			  else if(componentType.isPrimitiveType()){
				  toBeInit=initPrimitiveTypeParam(vt, paramname+"["+j+"]", componentType);
			  }
			  else if(componentType.isQualifiedType()){
				  toBeInit=initQualifiedTypeParam(vt, paramname+"["+j+"]", componentType);
			  }
			  else if(componentType.isSimpleType()){
				 toBeInit= initSimpleTypeParam(vt, paramname+"["+j+"]", componentType);
			  }
			  else{
				  
			  }
			  toBeInit.isParam=false;
		  }
		  return vi;
	}
	*/
	
	/**
	 * ��ʼ�������������͵�ʵ��
	 * @param vt ���ű�
	 * @param paramname ������
	 * @param type ��������
	 * @return ������Ϣ��Ŀ
	 */
	public VariableInfo initPrimitiveTypeParam(InfoUnit vt, String paramname, String type){
		 VariableInfo vi=new PrimitiveVarInfo(paramname, true, type, paramname);
		 return vi;
	}
	
	/**
	 * ��ʼ���޶�����ʵ��
	 * @param vt ���ű�
	 * @param paramname ������
	 * @param type ��������
	 * @return ������Ϣ��Ŀ
	 */
	public VariableInfo initQualifiedTypeParam(InfoUnit vt, String paramname, String type){
		  //�������ͣ������ಢ����ʵ���������ñ�����ӵ����ű�
		  String codePath=Function.resolveFilePath(type, projectRootPath, packageInfo, imports);
		  String fullQualifiedName=Function.toFullQualifiedName(type, projectRootPath, packageInfo, imports);
		  ClassInfo cl=null;
		  if(vt.mm.ma.hasLoaded(new ClassInfo(fullQualifiedName))){
			  cl=vt.mm.ma.getClassInfo(fullQualifiedName);
		  }
		  else{
			  MyClassLoader mcl=new MyClassLoader(vt.mm);
		      cl=mcl.loadClass(codePath, projectRootPath);
		  }
		  int index=vt.mm.jh.alloc(cl);
		  Iterator<Map.Entry<FieldName2, FieldInfo>> ite=vt.mm.jh.objs[index].field.entrySet().iterator();
		  while(ite.hasNext()){
			  Map.Entry<FieldName2, FieldInfo> entry = ite.next();
			  FieldInfo fieldinfo=entry.getValue();
			  if(Function.isArrayType(fieldinfo.fieldType)){
				  //...
			  }
			  else if(Function.isPrimitiveType(fieldinfo.fieldType)){
				  PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)fieldinfo;
				  pfi.value=paramname+"."+fieldinfo.fieldName;
			  }
			  else if(Function.isQualifiedType(fieldinfo.fieldType)){
				  VariableInfo tmp=initQualifiedTypeParam(vt, paramname+"."+fieldinfo.fieldName, fieldinfo.fieldType);	
				  ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;	
				  ReferenceFieldInfo rfi=(ReferenceFieldInfo)fieldinfo;
				  rfi.handle=rvi.handle;
			  }
			  else if(Function.isSimpleType(fieldinfo.fieldType)){
				  VariableInfo tmp=initSimpleTypeParam(vt, paramname+"."+fieldinfo.fieldName, fieldinfo.fieldType);
				  ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;
				  ReferenceFieldInfo rfi=(ReferenceFieldInfo)fieldinfo;
				  rfi.handle=rvi.handle;
			  }
			  else{
				  //�׳��쳣
			  }
		  }
		  VariableInfo vi=new ReferenceVarInfo(paramname, true, type, index);
		  return vi;
	}
	
	/**
	 * ��ʼ��������ʵ��
	 * @param vt ���ű�
	 * @param paramname ������
	 * @param type ��������
	 * @return ������Ϣ��Ŀ
	 */
	public VariableInfo initSimpleTypeParam(InfoUnit vt, String paramname, String type){
		//����ͬȫ�޶�����
		return initQualifiedTypeParam(vt, paramname, type);
	}

	/**
	 * ��������ִ��
	 * @param stmt ���
	 * @param infos ���ű�
	 */
	public void seStatement(Statement stmt, ArrayList<InfoUnit> infos){
		if(stmt instanceof AssertStatement){
			seAssertStatement((AssertStatement)stmt, infos);
		}
		else if(stmt instanceof Block){
			seBlock((Block)stmt, infos);
		}
		else if(stmt instanceof BreakStatement){
			seBreakStatement((BreakStatement)stmt, infos);
		}
		else if(stmt instanceof ConstructorInvocation){
			seConstructorInvocation((ConstructorInvocation)stmt, infos);
		}
		else if(stmt instanceof ContinueStatement){
			seContinueStatement((ContinueStatement)stmt, infos);
		}
		else if(stmt instanceof DoStatement){
			seDoStatement((DoStatement)stmt, infos);
		}
		else if(stmt instanceof EmptyStatement){
			//do nothing
		}
		else if(stmt instanceof EnhancedForStatement){
			seEnhancedForStatement((EnhancedForStatement)stmt, infos);
		}
		else if(stmt instanceof ExpressionStatement){
			seExpressionStatement((ExpressionStatement)stmt, infos);
		}
		else if(stmt instanceof ForStatement){
			seForStatement((ForStatement)stmt, infos);
		}
		else if(stmt instanceof IfStatement){
			seIfStatement((IfStatement)stmt, infos);
		}
		else if(stmt instanceof LabeledStatement){
			seLabeledStatement((LabeledStatement)stmt, infos);
		}
		else if(stmt instanceof ReturnStatement){
			seReturnStatement((ReturnStatement)stmt, infos);
		}
		else if(stmt instanceof SuperConstructorInvocation){
			seSuperConstructorInvocation((SuperConstructorInvocation)stmt, infos);
		}
		else if(stmt instanceof SwitchCase){
			seSwitchCase((SwitchCase)stmt, infos);
		}
		else if(stmt instanceof SwitchStatement){
			seSwitchStatement((SwitchStatement)stmt, infos);
		}
		else if(stmt instanceof SynchronizedStatement){
			seSynchronizedStatement((SynchronizedStatement)stmt, infos);
		}
		else if(stmt instanceof ThrowStatement){
			
		}
		else if(stmt instanceof TryStatement){
			seTryStatement((TryStatement)stmt, infos);
		}
		else if(stmt instanceof TypeDeclarationStatement){
			//��Ȼ����Block��ִ��
			//do nothing
		}
		else if(stmt instanceof VariableDeclarationStatement){
			//��Ȼ����Block��ִ��
			//do nothing
		}
		else if(stmt instanceof WhileStatement){
			seWhileStatement((WhileStatement)stmt, infos);
		}
	}

	/**
	 * ��������з���ִ��
	 * @param block ����
	 * @param infos ���ű�
	 */
	@SuppressWarnings("unchecked")
	public void seBlock(Block block, ArrayList<InfoUnit> infos){
			List<Statement> stmts=block.statements();
			//����һ���б�����������������������ı��������˳�����ʱ��������б�ɾ����Щ����������ľֲ�������
			List<VariableDeclarationFragment> vdfList=null;    
			for(Statement stmt: stmts){
				if(stmt instanceof AssertStatement){
				   AssertStatement as=(AssertStatement)stmt;
				   seAssertStatement(as, infos);
				}
				else if(stmt instanceof  Block){
					Block blk=(Block)stmt;
					seBlock(blk, infos);
				}
				else if(stmt instanceof BreakStatement){
				   BreakStatement bs=(BreakStatement)stmt;
				   seBreakStatement(bs, infos);
				}
				else if(stmt instanceof  ConstructorInvocation){
					ConstructorInvocation ci=(ConstructorInvocation)stmt;
					seConstructorInvocation(ci, infos);
				}
				else if(stmt instanceof ContinueStatement){
				     ContinueStatement cs=(ContinueStatement)stmt;
				     seContinueStatement(cs, infos);
				}
				else if(stmt instanceof DoStatement){
					DoStatement ds=(DoStatement)stmt;
					seDoStatement(ds, infos);
				}
				else if(stmt instanceof  EmptyStatement){
				  //do nothing
				}
				else if(stmt instanceof EnhancedForStatement){
					EnhancedForStatement ef=(EnhancedForStatement)stmt;
					seEnhancedForStatement(ef, infos);
				}
				else if(stmt instanceof ExpressionStatement){
					ExpressionStatement exps=(ExpressionStatement)stmt;
					seExpressionStatement(exps, infos);
				}
				else if(stmt instanceof ForStatement){
					ForStatement fs=(ForStatement)stmt;
					seForStatement(fs, infos);
				}
				else if(stmt instanceof IfStatement){
				 	IfStatement is=(IfStatement)stmt;
					seIfStatement(is, infos);
				}
				else if(stmt instanceof LabeledStatement){
					seLabeledStatement((LabeledStatement)stmt, infos);
				}
				else if(stmt instanceof ReturnStatement){
				   ReturnStatement rs=(ReturnStatement)stmt;
				   seReturnStatement(rs, infos);
				}
				else if(stmt instanceof SuperConstructorInvocation){
				   seSuperConstructorInvocation((SuperConstructorInvocation)stmt, infos);
				}
				else if(stmt instanceof SwitchCase){
				   seSwitchCase((SwitchCase)stmt, infos);
				}
				else if(stmt instanceof SwitchStatement){
				   seSwitchStatement((SwitchStatement)stmt, infos);
				}
				else if(stmt instanceof SynchronizedStatement){
				    seSynchronizedStatement((SynchronizedStatement)stmt, infos);
				}
				else if(stmt instanceof ThrowStatement){
					//...
				}
				else if(stmt instanceof  TryStatement){
					seTryStatement((TryStatement)stmt, infos);
				}
				else if(stmt instanceof TypeDeclarationStatement){
					//...
				}
				else if(stmt instanceof  VariableDeclarationStatement){
				    //�����е�PC�µķ��ű���и��¡�
					VariableDeclarationStatement vds=(VariableDeclarationStatement)stmt;
					String type=Function.toFullQualifiedName(vds.getType(), projectRootPath, packageInfo, imports);
					vdfList=vds.fragments();
					int isize=infos.size();
					int counter=1;
					for(pointer=0; counter<=isize; pointer++, counter++){
						InfoUnit info=infos.get(pointer);
						if(info.retval==null){
						  for(VariableDeclarationFragment vdf: vdfList){
							String vname=vdf.getName().getIdentifier();
							Expression exp= vdf.getInitializer();
							if(exp==null){
								//����δ��ʼ��
								if(info.getVariableInfo(vname)==null){
									if(Function.isArrayType(type)){
							    		info.addOneVariable(new ArrayVarInfo(vname, false, type, -1));
							    	}
							    	else if(Function.isPrimitiveType(type)){
							    		info.addOneVariable(new PrimitiveVarInfo(vname, false, type, null));
							    	}
							    	else if(Function.isQualifiedType(type)||Function.isSimpleType(type)){
							    		info.addOneVariable(new ReferenceVarInfo(vname, false, type, -1));
							    	}
							    	else{
							    	}
								}
								else{
									//�ֲ����������׳��쳣
								}
							}
							else{
						    Wrapper wrapper=symbolOpe(exp, info, infos);
						    if(wrapper.type==0){
							    ValueTypePair initialValue=wrapper.vtp;
							    if(info.getVariableInfo(vname)==null){
							    	if(Function.isArrayType(initialValue.type)){
							    		info.addOneVariable(new ArrayVarInfo(vname, false, type, 
							    				((ArrayValueTypePair)initialValue).first_address));
							    	}
							    	else if(Function.isPrimitiveType(initialValue.type)){
							    		info.addOneVariable(new PrimitiveVarInfo(vname, false, type,
							    				((PrimitiveValueTypePair)initialValue).value));
							    	}
							    	else if(Function.isQualifiedType(initialValue.type)||Function.isSimpleType(initialValue.type)){
							    		info.addOneVariable(new ReferenceVarInfo(vname, false, type,
							    				((ReferenceValueTypePair)initialValue).handle));
							    	}
							    	else{
							    	}
							    }
							    else{
							    	//�ֲ������������׳��쳣
							    }
							}
							 else if(wrapper.type==1){
								 for(ReturnInfo ri: wrapper.ret){
										for(InfoUnit iu: infos){
											if(ri.pc.equals(iu.cons)){
												if(iu.getVariableInfo(vname)==null){
													//ע��info�ѱ�ɾ����Ӧ�ò���iu
													if(Function.isArrayType(ri.retvalue.type)){
										    			iu.addOneVariable(new ArrayVarInfo(vname, false, type, 
										    					((ArrayValueTypePair)ri.retvalue).first_address));
										    		}
										    		else if(Function.isPrimitiveType(ri.retvalue.type)){
										    			iu.addOneVariable(new PrimitiveVarInfo(vname, false, type,
										    					((PrimitiveValueTypePair)ri.retvalue).value));
										    		}
										    		else if(Function.isQualifiedType(ri.retvalue.type)||Function.isSimpleType(ri.retvalue.type)){
										    			iu.addOneVariable(new ReferenceVarInfo(vname, false, type, 
										    					((ReferenceValueTypePair)ri.retvalue).handle));
										    		}
										    		else{
										    		}
												}
												else{
													//�ֲ������������׳��쳣			
												}
										      }
										   }
									    }
							         }
							      }
							   }
							}
						}
					resetptr();
				}
				else if(stmt instanceof  WhileStatement){
					WhileStatement ws=(WhileStatement)stmt;
					seWhileStatement(ws, infos);
				}
			}
			
			//��������ʱ���Ѿֲ�����ȫ������
			if(vdfList!=null)
				for(InfoUnit info: infos){
					for(VariableDeclarationFragment frg: vdfList){
						String name=frg.getName().getIdentifier();
						info.removeOneVariable(name);
				  }
			 }
		}
	
	/**
	 * �Զ��������з���ִ��
	 * @param stmt �������
	 * @param infos ���ű�
	 */
	public void seAssertStatement(AssertStatement stmt, ArrayList<InfoUnit> infos){
		int isize=infos.size();
		int counter=1;
		for(pointer=0; counter<=isize; pointer++, counter++){
			InfoUnit info=infos.get(pointer);
			if(info.retval==null){
			Expression exp=stmt.getExpression();
		    symbolOpe(exp, info, infos);
			}
		}
		resetptr();
	}
	
	/**
	 * ��break������ִ��<br/>
	 * �ð汾��δʵ��
	 * @param stmt break���
	 * @param infos ���ű�
	 */
	public void seBreakStatement(BreakStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * �Թ��캯�����������з���ִ��
	 * @param stml ���캯���������
	 * @param infos ���ű�
	 */
	public void seConstructorInvocation(ConstructorInvocation stmt, ArrayList<InfoUnit> infos){
		
	}
	
	/**
	 * ��continue������ִ��<br/>
	 * �ð汾��δʵ��
	 * @param stmt continue���
	 * @param infos ���ű�
	 */
	public void seContinueStatement(ContinueStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��do..while..ѭ������ִ��
	 * @param stmt do..while..���
	 * @param infos ���ű�
	 */
	public void seDoStatement(DoStatement stmt, ArrayList<InfoUnit> infos){
		//����ѭ�����ָ��ѭ������
		int time=InitialValues.loop_times;
		for(int i=0; i<=time-1; i++){                //ִ��time��ѭ������������ʽ
			int isize=infos.size();
			int counter=1;
			ArrayList<InfoUnit> tmp2=new ArrayList<InfoUnit>();
			for(int tmppointer=0; counter<=isize; tmppointer++, counter++){
				InfoUnit info=infos.get(tmppointer);
			    if(info.retval==null){
					Statement body=stmt.getBody();
					ArrayList<InfoUnit> tmp=new ArrayList<InfoUnit>();
					tmp.add(info);
					seStatement(body, tmp);
					tmp2.addAll(tmp);
				}
			}
			infos.clear();
			infos.addAll(tmp2);
			
			isize=infos.size();
			counter=1;
			for(pointer=0; counter<=isize; pointer++, counter++){
				InfoUnit info=infos.get(pointer);
				if(info.retval==null){
					Expression condition=stmt.getExpression();
					symbolOpe(condition, info, infos);
				}
			}
		}
		resetptr();
	}
	
	/**
	 * �Լ�ǿ��forѭ������ִ��<br/>
	 * �ð汾��δʵ��
	 * @param stmt ��ǿ��forѭ��
	 * @param infos ���ű�
	 */
	public void seEnhancedForStatement(EnhancedForStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * �Ա��ʽ������ִ��
	 * @param stmt ���ʽ���
	 * @param infos ���ű�
	 */
	public void seExpressionStatement(ExpressionStatement stmt, ArrayList<InfoUnit> infos){
		//Ҫ��ֹ����Ϣ��Ԫ�ظ�����
		int isize=infos.size();  //ԭ����Ҫִ�е���Ϣ��Ԫ����Ŀ
		int counter=1;  //��ִ�е���Ϣ��Ԫ��Ŀ
		for(pointer=0; counter<=isize; pointer++, counter++){
			InfoUnit info=infos.get(pointer);
			if(info.retval==null){
			Expression exp=stmt.getExpression();
		    symbolOpe(exp, info, infos);	   
			}
		}
		resetptr();
	}
	
	/**
	 * ��forѭ������ִ��
	 * @param stmt forѭ��
	 * @param infos ���ű�
	 */
	public void seForStatement(ForStatement stmt, ArrayList<InfoUnit> infos){
		//����������while��do...while
	}
	
	/**
	 * ��if������ִ��
	 * @param stmt if���
	 * @param infos ���ű�
	 */
	public void seIfStatement(IfStatement stmt, ArrayList<InfoUnit> infos){
		ArrayList<InfoUnit> tmp=new ArrayList<InfoUnit>();
		int isize=infos.size();
		int counter=1;
		for(pointer=0; counter<=isize; pointer++, counter++){
			InfoUnit info=infos.get(pointer);
			if(info.retval==null){
			Expression decision=stmt.getExpression();
			Wrapper wrapper=symbolOpe(decision, info, infos);
			//wrapper.type==0
			ConstraintInfo di=new ConstraintInfo("("+((PrimitiveValueTypePair)wrapper.vtp).value.toString()+")");
			ConstraintInfo pc1=info.cons.and(di);
			ConstraintInfo pc2=info.cons.and(di.not());
			InfoUnit table1=new InfoUnit();
			table1.cons=pc1;
			for(InvocationInfo iii: info.methods)
				table1.methods.add(iii.clone());
			List<Map.Entry<String, VariableInfo>> e=new ArrayList<Map.Entry<String, VariableInfo>>(info.variables.entrySet());
			for (Map.Entry<String, VariableInfo> entry: e) {
				table1.addOneVariable(entry.getValue().clone());
			}
			table1.retval=info.retval==null?null:info.retval.clone();
			table1.mm=info.mm.clone();
			InfoUnit table2=new InfoUnit();
			table2.cons=pc2;
			for(InvocationInfo iii: info.methods)
				table2.methods.add(iii.clone());
			for (Map.Entry<String, VariableInfo> entry: e) {
				table2.addOneVariable(entry.getValue().clone());
			}
			table2.retval=info.retval==null?null:info.retval.clone();
			table2.mm=info.mm.clone();
			infos.remove(info);
			pointer--;
			//��table1ִ��then
			Statement thenstmt=stmt.getThenStatement();
			ArrayList<InfoUnit> theninfos=new ArrayList<InfoUnit>();
			theninfos.add(table1);
			seStatement(thenstmt, theninfos);
			//��table2ִ��else
			Statement elsestmt=stmt.getElseStatement();
			ArrayList<InfoUnit> elseinfos=new ArrayList<InfoUnit>();
			elseinfos.add(table2);
			seStatement(elsestmt, elseinfos);
			tmp.addAll(theninfos);
			tmp.addAll(elseinfos);
			//wrapper.type==1
			//...
			}
		}
		infos.addAll(tmp);
		resetptr();
	}
	
	/**
	 * �Ա��������ִ��<br/>
	 * �˰汾�ݲ�ʵ��
	 * @param stmt ������
	 * @param infos ���ű�
	 */
	public void seLabeledStatement(LabeledStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��return������ִ��
	 * @param stmt return���
	 * @param infos ���ű�
	 */
	public void seReturnStatement(ReturnStatement stmt, ArrayList<InfoUnit> infos ){
		int isize=infos.size();
		int counter=1;
		for(pointer=0; counter<=isize; pointer++, counter++){
			InfoUnit info=infos.get(pointer);
			if(info.retval==null){
				Expression exp=stmt.getExpression();
				if(exp==null){
					info.retval=new ValueTypePair(null);  //ֱ����return;��䣬��retval������ֵ����
				}
				else{
					Wrapper wrapper=symbolOpe(exp, info, infos);
					if(wrapper.type==0)
						info.retval=wrapper.vtp;
					else if(wrapper.type==1){
						for(ReturnInfo ri: wrapper.ret){
							for(InfoUnit ddd: infos){
								if(ri.pc.equals(ddd.cons)){
									ddd.retval=ri.retvalue;
									break;
								}
							}
						}
					}
				}
			}
		}
		resetptr();
	}
	
	/**
	 * �Ը�����������������ִ��<br/>
	 * �ð汾��δʵ�ּ̳�
	 * @param stmt super���
	 * @param infos ���ű�
	 */
	public void seSuperConstructorInvocation(SuperConstructorInvocation stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��case������ִ��
	 * @param stmt case���
	 * @param infos ���ű�
	 */
	public void seSwitchCase(SwitchCase stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��switch������ִ��
	 * @param stmt switch���
	 * @param infos ���ű�
	 */
	public void seSwitchStatement(SwitchStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��ͬ���������ִ��
	 * @param stmt ͬ������
	 * @param infos ���ű�
	 */
	public void seSynchronizedStatement(SynchronizedStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��try-catch-finally�������ִ��
	 * @param stmt try-catch-finally����
	 * @param infos ���ű�
	 */
	public void seTryStatement(TryStatement stmt, ArrayList<InfoUnit> infos){}
	
	/**
	 * ��while������ִ��
	 * @param stmt while���
	 * @param infos ���ű�
	 */
	public void seWhileStatement(WhileStatement stmt, ArrayList<InfoUnit> infos){
		//����ѭ�����ָ��ѭ������
		int time=InitialValues.loop_times;
		for(int i=0; i<=time-1; i++){                //ִ��time���������ʽ��ѭ����
			int isize=infos.size();
			int counter=1;
			for(pointer=0; counter<=isize; pointer++, counter++){
				InfoUnit info=infos.get(pointer);
				if(info.retval==null){
					Expression condition=stmt.getExpression();
					symbolOpe(condition, info, infos);
				}
			}
			isize=infos.size();
			counter=1;
			ArrayList<InfoUnit> tmp2=new ArrayList<InfoUnit>();
			for(int tmppointer=0; counter<=isize; tmppointer++, counter++){
				InfoUnit info=infos.get(tmppointer);
				if(info.retval==null){
					Statement body=stmt.getBody();
					ArrayList<InfoUnit> tmp=new ArrayList<InfoUnit>();
					tmp.add(info);
					seStatement(body, tmp);
					tmp2.addAll(tmp);
				}
			}
			infos.clear();
			infos.addAll(tmp2);
		}
		resetptr();
	}
	
	/**
	 * �Ա��ʽ���з���ִ�в����������ֵ
	 * @param exp ���ʽ
	 * @param table ��ǰִ�е�·��Լ���µķ��ű�
	 * @param tables ����·��Լ���µķ��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpe(Expression exp, InfoUnit table, ArrayList<InfoUnit> tables){
			if(exp instanceof Annotation){
				//ע����䣬��ִ��
			}
			else if(exp instanceof ArrayAccess){
			
			}
			else if(exp instanceof ArrayCreation){
			
			}
			else if(exp instanceof ArrayInitializer){
			
			}
			else if(exp instanceof Assignment){
				Assignment assignment=(Assignment)exp;
			    return symbolOpeOnAssignment(assignment, table, tables);		
			}
			else if(exp instanceof BooleanLiteral){
				BooleanLiteral bl=(BooleanLiteral)exp;
				return symbolOpeOnBooleanLiteral(bl, table, tables);
			}
			else if(exp instanceof CastExpression){
			
			}
			else if(exp instanceof CharacterLiteral){
				CharacterLiteral cl=(CharacterLiteral)exp;
				return symbolOpeOnCharacterLiteral(cl, table, tables);
			}
			else if(exp instanceof ClassInstanceCreation){
				ClassInstanceCreation cic=(ClassInstanceCreation)exp;
				return symbolOpeOnClassInstanceCreation(cic, table, tables);
			}
			else if(exp instanceof ConditionalExpression){
				
			}
			else if(exp instanceof FieldAccess){
				FieldAccess fa=(FieldAccess)exp;
				return symbolOpeOnFieldAccess(fa, table, tables);
			}
			else if(exp instanceof InfixExpression){
				InfixExpression ie=(InfixExpression)exp;
				return symbolOpeOnInfixExpression(ie, table, tables);
			}
			else if(exp instanceof InstanceofExpression){
			
			}
			else if(exp instanceof MethodInvocation){
				MethodInvocation invocation=(MethodInvocation)exp;
				return symbolOpeOnMethodInvocation(invocation, table, tables);
			}
			else if(exp instanceof Name){
				return symbolOpeOnName((Name)exp, table, tables);
			}
			else if(exp instanceof NullLiteral){
				return null;
			}
			else if(exp instanceof NumberLiteral){
				NumberLiteral nl=(NumberLiteral)exp;
				return symbolOpeOnNumberLiteral(nl, table, tables);
			}
			else if(exp instanceof ParenthesizedExpression){
				ParenthesizedExpression pe=(ParenthesizedExpression)exp;
				return symbolOpeOnParenthesizedExpression(pe, table, tables);
			}
			else if(exp instanceof PostfixExpression){
				PostfixExpression pe=(PostfixExpression)exp;
				return symbolOpeOnPostfixExpression(pe, table, tables);
			}
			else if(exp instanceof PrefixExpression){
				PrefixExpression pe=(PrefixExpression)exp;
				return symbolOpeOnPrefixExpression(pe, table, tables);
			}
			else if(exp instanceof StringLiteral){
				//�׳��쳣
			}
			else if(exp instanceof SuperFieldAccess){
				// out of consideration
			}
			else if(exp instanceof SuperMethodInvocation){
			
			}
			else if(exp instanceof ThisExpression){
				//This���ʽ������[classname].this��ʽ�ı��ʽ
				ThisExpression te=(ThisExpression)exp;
				return symbolOpeOneThisExpression(te, table, tables);
			}
			else if(exp instanceof TypeLiteral){
				// out of consideration
			}
			else if(exp instanceof VariableDeclarationExpression){
				//unknown
			}
		return null;
	}
	
	/**
	 * �Ը�ֵ���ʽ����ִ��
	 * @param exp ��ֵ���ʽ
	 * @param table ��ǰ���ű�
	 * @param tables ���з��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpeOnAssignment(Assignment assignment, InfoUnit table, ArrayList<InfoUnit> tables){
		Expression lh=assignment.getLeftHandSide();
		String vname=null;
		VariableInfo vi=null; //��ʾҪ�������ı����ı�����Ϣ����
		FieldInfo theOne=null; //��ʾҪ���������ֶε��ֶ���Ϣ����
		//vi��theOneһ��Ϊnull��һ����Ϊnull
		if(lh instanceof ArrayAccess){
			//...
		}
		else if(lh instanceof Name){
			//�����������ֶ�����Ϊ��ֵ
			String name=((Name)lh).toString();
			if(name.contains(".")){
				//�޶����ƣ�������
				String split[]=name.split("\\.");
				VariableInfo tmp=null;
				Instance instance=null;
				for(int i=0; i<=split.length-2; i++){
					if(i==0){
						tmp=table.getVariableInfo(split[i]);
						if(tmp==null){
							//...
						}
						else{
							ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;
							instance=table.mm.jh.objs[rvi.handle];
						}
					}
					else{
						FieldInfo fi=instance.getOneField(new FieldName(split[i]));
						ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
						instance=table.mm.jh.objs[rfi.handle];
					}
				}
				theOne=instance.getOneField(new FieldName(split[split.length-1]));		
			}
			else{
				//�����ƣ�ֱ�Ӳ��ҷ��ű�
				//�����п���ȱʡ��this������Ҫ�ж�һ��
				vname=name;
				vi=table.getVariableInfo(vname);
				if(vi!=null){
					//ֱ�Ӿ���Ҫ�����ľֲ�����
				}
				else{
					//��ȡ��Ҫ�������ֶ���Ϣ
					ReferenceVarInfo rrr=(ReferenceVarInfo)table.getVariableInfo("this");
					theOne=table.mm.jh.objs[rrr.handle].getOneField(new FieldName(vname));
				}
			}
		}
		else if(lh instanceof FieldAccess){
			FieldAccess fa=(FieldAccess)lh;
			Expression exp=fa.getExpression();
			SimpleName sn=fa.getName();
			Wrapper w=symbolOpe(exp, table, tables);
			if(w.type==0){
				ValueTypePair vtp=w.vtp;
				if(vtp instanceof ReferenceValueTypePair){
					ReferenceValueTypePair rvtp=(ReferenceValueTypePair)vtp;
					Instance i=table.mm.jh.objs[rvtp.handle];
					theOne=i.getOneField(new FieldName(sn.toString()));
				}
				else{
					//����
				}
			}
			else if(w.type==1){
				//...
			}
		}
		else if(lh instanceof SuperFieldAccess){
			//...
		}
		else{
			//��������Ϊ��ֵ������ֵ���׳��쳣
		}
		if(vi!=null){
			Expression ope=assignment.getRightHandSide();
			Wrapper wrapper=symbolOpe(ope, table, tables);
			if(wrapper.type==0){
				ValueTypePair nvalue=wrapper.vtp;
				if(vi instanceof PrimitiveVarInfo && nvalue instanceof PrimitiveValueTypePair){
					if(((PrimitiveVarInfo)vi).value.size()==0) 
						((PrimitiveVarInfo)vi).value.push(((PrimitiveValueTypePair)nvalue).value);
					else { 
						((PrimitiveVarInfo)vi).value.pop(); ((PrimitiveVarInfo)vi).value.push(((PrimitiveValueTypePair)nvalue).value);
						}
					//ֵ�ɱ��ʽ���㣬���ͱ��ֲ���
					return new Wrapper(new PrimitiveValueTypePair(vi.type, ((PrimitiveValueTypePair)nvalue).value));   
				}
				else if(vi instanceof ReferenceVarInfo && nvalue instanceof ReferenceValueTypePair){
					((ReferenceVarInfo)vi).handle=((ReferenceValueTypePair)nvalue).handle;
					return new Wrapper(new ReferenceValueTypePair(vi.type, ((ReferenceValueTypePair)nvalue).handle));
				}
				else if(vi instanceof ArrayVarInfo && nvalue instanceof ArrayValueTypePair){
					((ArrayVarInfo)vi).first_address=((ArrayValueTypePair)nvalue).first_address;
					return new Wrapper(new ArrayValueTypePair(vi.type, ((ArrayValueTypePair)nvalue).first_address));
				}
				else{
					//�׳��쳣
				}
			}
			else if(wrapper.type==1){  //˵���ұ߰����˷������ã�����Ӧ��Ϣ��Ԫ�µ���ֵ��������ֵ��ָ�봦�����Ϣ��Ԫ���ϵĸ����ڷ�������������
				ArrayList<ReturnInfo> result=new ArrayList<ReturnInfo>();
				for(ReturnInfo ri: wrapper.ret){
					for(InfoUnit iu: tables){
						if(ri.pc.equals(iu.cons)){
							VariableInfo vvv=iu.getVariableInfo(vname);
							if(vvv instanceof PrimitiveVarInfo && ri.retvalue instanceof PrimitiveValueTypePair){
								if(((PrimitiveVarInfo)vvv).value.size()==0) ((PrimitiveVarInfo)vvv).value.push(((PrimitiveValueTypePair)(ri.retvalue)).value);
								else{
									((PrimitiveVarInfo)vvv).value.pop(); 
									((PrimitiveVarInfo)vvv).value.push(((PrimitiveValueTypePair)(ri.retvalue)).value);
									}
							}
							else if(vvv instanceof ReferenceVarInfo && ri.retvalue instanceof ReferenceValueTypePair){
								((ReferenceVarInfo)vvv).handle=((ReferenceValueTypePair)(ri.retvalue)).handle;
							}
							else if(vvv instanceof ArrayVarInfo && ri.retvalue instanceof ArrayValueTypePair){
								((ArrayVarInfo)vvv).first_address=((ArrayValueTypePair)ri.retvalue).first_address;
							}
						}
					}
					result.add(ri);
				}
				return new Wrapper(result);
			}
		}
		else if(theOne!=null){
			Expression ope=assignment.getRightHandSide();
			Wrapper wrapper=symbolOpe(ope, table, tables);
			if(wrapper.type==0){
				ValueTypePair nvalue=wrapper.vtp;
				if(theOne instanceof PrimitiveFieldInfo && nvalue instanceof PrimitiveValueTypePair){
					((PrimitiveFieldInfo)theOne).value=((PrimitiveValueTypePair)nvalue).value;
					//ֵ�ɱ��ʽ���㣬���ͱ��ֲ���
					return new Wrapper(new PrimitiveValueTypePair(theOne.fieldType, ((PrimitiveValueTypePair)nvalue).value));   
				}
				else if(theOne instanceof ReferenceFieldInfo && nvalue instanceof ReferenceValueTypePair){
					((ReferenceFieldInfo)theOne).handle=((ReferenceValueTypePair)nvalue).handle;
					return new Wrapper(new ReferenceValueTypePair(theOne.fieldType, ((ReferenceValueTypePair)nvalue).handle));
				}
				else if(theOne instanceof ArrFieldInfo && nvalue instanceof ArrayValueTypePair){
					((ArrFieldInfo)theOne).f=((ArrayValueTypePair)nvalue).first_address;
					return new Wrapper(new ArrayValueTypePair(theOne.fieldType, ((ArrayValueTypePair)nvalue).first_address));
				}
				else{
					//�׳��쳣
				}
			}
			else if(wrapper.type==1){  //˵���ұ߰����˷������ã�����Ӧ��Ϣ��Ԫ�µ���ֵ��������ֵ��ָ�봦�����Ϣ��Ԫ���ϵĸ����ڷ�������������
				ArrayList<ReturnInfo> result=new ArrayList<ReturnInfo>();
				for(ReturnInfo ri: wrapper.ret){
					for(InfoUnit iu: tables){
						if(ri.pc.equals(iu.cons)){
							if(theOne instanceof PrimitiveFieldInfo && ri.retvalue instanceof PrimitiveValueTypePair){
								((PrimitiveFieldInfo)theOne).value=((PrimitiveValueTypePair)(ri.retvalue)).value;
							}
							else if(theOne instanceof ReferenceFieldInfo && ri.retvalue instanceof ReferenceValueTypePair){
								((ReferenceFieldInfo)theOne).handle=((ReferenceValueTypePair)(ri.retvalue)).handle;
							}
							else if(theOne instanceof ArrFieldInfo && ri.retvalue instanceof ArrayValueTypePair){
								((ArrFieldInfo)theOne).f=((ArrayValueTypePair)ri.retvalue).first_address;
							}
						}
					}
					result.add(ri);
				}
				return new Wrapper(result);
			}
		}
		else{
			//û�дӷ��ű��в鵽���������û�дӶ��в鵽���ֶ���Ϣ�����쳣����
		}	
		return null;
	}
	
	/**
	 * �Բ�������ֵ����ִ��
	 * @param bl ��������ֵ
	 * @param table ��ǰ���ű�
	 * @param tables ���еķ��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpeOnBooleanLiteral(BooleanLiteral bl, InfoUnit table, ArrayList<InfoUnit> tables){
		boolean value=bl.booleanValue();
		PrimitiveType pt=root.getAST().newPrimitiveType(PrimitiveType.BOOLEAN);
		return new Wrapper(new PrimitiveValueTypePair(pt.toString(), String.valueOf(value)));
	}
	
	/**
	 * ���ַ�����ֵ����ִ��
	 * @param cl �ַ�����ֵ
	 * @param table ��ǰ���ű�
	 * @param tables ���з��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpeOnCharacterLiteral(CharacterLiteral cl, InfoUnit table, ArrayList<InfoUnit> tables){
		char value=cl.charValue();
		PrimitiveType pt=root.getAST().newPrimitiveType(PrimitiveType.CHAR);
		return new Wrapper(new PrimitiveValueTypePair(pt.toString(), "\'"+String.valueOf(value)+"\'"));
	}
	
	/**
	 * ��ʵ����������ִ��
	 * @param cic ʵ�������
	 * @param table ��ǰ���ű�
	 * @param tables ���з��ű�
	 * @return ������������
	 */
	@SuppressWarnings("unchecked")
	public Wrapper symbolOpeOnClassInstanceCreation(ClassInstanceCreation cic, InfoUnit table, ArrayList<InfoUnit> tables){
		Type type=cic.getType(); //��Ҫʵ���������ͣ�������һ�������ͣ�Ҳ������ȫ�޶�����
		String tInStr=type.toString();
		String fullQualifiedName=Function.toFullQualifiedName(tInStr,  projectRootPath, packageInfo, imports);  //���ȫ�޶���
		ClassInfo cl=null;
		if(table.mm.ma.hasLoaded(new ClassInfo(fullQualifiedName))){
			//���Ѽ��أ�ֱ�Ӵӷ�������ȡ����Ϣ
			cl=table.mm.ma.getClassInfo(fullQualifiedName);
		}
		else{
			//û�м��أ�Ҫ�ȼ���
			MyClassLoader mcl=new MyClassLoader(table.mm);
			cl=mcl.loadClass(Function.resolveFilePath(tInStr,  projectRootPath, packageInfo, imports),
					projectRootPath);
		}
		//Ϊ������������ڴ沢��ʼ���ֶ�
		 int index=table.mm.jh.alloc(cl);
		 //ִ�й��캯�������������������ƣ���ͬ������û��return�����ص�������
        ReferenceValueTypePair this_pointer=new ReferenceValueTypePair(fullQualifiedName, index);
		List<Expression> args=cic.arguments();
		//����򻯴�������wrappers�����Ͷ���0����û�з���������Ϊʵ��
		 ArrayList<String> passedTypes=new ArrayList<String>();		
         ArrayList<ValueTypePair> argInfo=new ArrayList<ValueTypePair>();
         argInfo.add(this_pointer);  //thisָ��
         for(int i=0; i<=args.size()-1; i++){
				Wrapper w=symbolOpe(args.get(i), table, tables);
				passedTypes.add(w.vtp.type);
				argInfo.add(w.vtp.clone());
			}
         int index2=fullQualifiedName.lastIndexOf(".");
         String methodname=fullQualifiedName.substring(index2+1); //��ȡ���캯������Ҳ������ļ�����
         MethodInfo calledMi=new MethodInfo(fullQualifiedName, methodname, passedTypes);
		 MethodInfo theOne=null;
		//����Ŀ���ļ��ĳ����﷨��
		 CompilationUnit cu=null;
		 ArrayList<MethodInfo> am=null;
		 ArrayList<ImportDeclaration> is=null;
		 String pi=null;
		 if(inf.className.equals(fullQualifiedName)){
			 cu=root;
			 am=all;
			 is=imports;
			 pi=packageInfo;
			 theOne=mostMatch(calledMi, all);
		 }
		 else{
			 ASTCreator ac=new ASTCreator();
			 cu=ac.createAST(Function.resolveFilePath(tInStr, projectRootPath, packageInfo, imports)); 
			 TestedClassInfoGetter mg=new TestedClassInfoGetter();
			 TestedClassInfoVisitor mvisitor=mg.getMethods(cu);
			 am=mvisitor.getMethods();
			 is=mvisitor.getImports();
			 pi=mvisitor.getPackageInfo();
			 theOne=mostMatch(calledMi, am);
		 }
		 InvocationInfo ii=new InvocationInfo(mi, theOne, 0);
		 table.methods.add(ii);
		 MethodInvocationVisitor miv=new MethodInvocationVisitor(theOne, cu, argInfo, am, projectRootPath,
				 is, pi, table.mm);
		 cu.accept(miv);
		 ArrayList<ReturnInfo> result=new ArrayList<ReturnInfo>();
		 for(InfoUnit ttt: miv.inf.infs){
			 	InfoUnit n=new InfoUnit();
			 	n.mm=ttt.mm.clone();	//����Ϣ��Ԫ���ڴ����ǿ�¡�ķ���ִ��֮�����Ϣ��Ԫ���ڴ�������Ϊ�������µ�
				n.cons=table.cons.and(ttt.cons);
				List<Map.Entry<String, VariableInfo>> e=new ArrayList<Map.Entry<String, VariableInfo>>(table.variables.entrySet());
				for (Map.Entry<String, VariableInfo> entry: e) {
					n.addOneVariable(entry.getValue().clone());
				}
				for(InvocationInfo iii: table.methods)
					n.methods.add(iii.clone());
				for(InvocationInfo iii: ttt.methods)
					n.methods.add(iii.clone());
				n.methods.add(new InvocationInfo(theOne, mi, 1));
				tables.add(n);
				result.add(new ReturnInfo(n.cons, this_pointer));
		 }
	     tables.remove(table);
		 pointer--;
		 return new Wrapper(result);
	}
	
	/**
	 * ���ֶη��ʱ��ʽ����ִ��
	 * @param fa �ֶη��ʱ��ʽ
	 * @param table ���ű�
	 * @param tables ���з��ű�
	 * @return ����ִ�к�Ľ��
	 */
	public Wrapper symbolOpeOnFieldAccess(FieldAccess fa, InfoUnit table, ArrayList<InfoUnit> tables){
		Expression exp=fa.getExpression();
		SimpleName sn=fa.getName();
		Wrapper w=symbolOpe(exp, table, tables);
		if(w.type==0){
			ValueTypePair vtp=w.vtp;
			if(vtp instanceof ReferenceValueTypePair){
				ReferenceValueTypePair rvtp=(ReferenceValueTypePair)vtp;
				Instance i=table.mm.jh.objs[rvtp.handle];
				FieldInfo fi=i.getOneField(new FieldName(sn.toString()));
				ValueTypePair r=null;
				if(fi instanceof PrimitiveFieldInfo){
					PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)fi;
					r=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
				}
				else if(fi instanceof ArrFieldInfo){
					ArrFieldInfo afi=(ArrFieldInfo)fi;
					r=new ArrayValueTypePair(afi.fieldType, afi.f);
				}
				else if(fi instanceof ReferenceFieldInfo){
					ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
					r=new ReferenceValueTypePair(rfi.fieldType, rfi.handle);
				}
				return new Wrapper(r);
			}
			else{
				//����
			}
		}
		else if(w.type==1){
			ArrayList<ReturnInfo> result=new ArrayList<ReturnInfo>();
			for(ReturnInfo ri: w.ret){
				for(InfoUnit iu: tables){
					if(ri.pc.equals(iu.cons)){
						ValueTypePair vtp=ri.retvalue;
						if(vtp instanceof ReferenceValueTypePair){
							ReferenceValueTypePair rvtp=(ReferenceValueTypePair)vtp;
							Instance i=table.mm.jh.objs[rvtp.handle];
							FieldInfo fi=i.getOneField(new FieldName(sn.toString()));
							ValueTypePair r=null;
							if(fi instanceof PrimitiveFieldInfo){
								PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)fi;
								r=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
							}
							else if(fi instanceof ArrFieldInfo){
								ArrFieldInfo afi=(ArrFieldInfo)fi;
								r=new ArrayValueTypePair(afi.fieldType, afi.f);
								
							}
							else if(fi instanceof ReferenceFieldInfo){
								ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
								r=new ReferenceValueTypePair(rfi.fieldType, rfi.handle);
							}
							result.add(new ReturnInfo(ri.pc, r));
						}
						else{
							//����
						}
					}
				}
			}
			return new Wrapper(result);
		}
		return null;
	}
	
	/**
	 * ����׺���ʽ����ִ��
	 * @param ie ��׺���ʽ
	 * @param table ��ǰ���ű�
	 * @param tables ���з��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	@SuppressWarnings("unchecked")
	public Wrapper symbolOpeOnInfixExpression(InfixExpression ie, InfoUnit table, ArrayList<InfoUnit> tables ){
		Expression leftope=ie.getLeftOperand();
		Expression rightope=ie.getRightOperand();
		InfixExpression.Operator operator=ie.getOperator();
	
		Wrapper wrapperl=symbolOpe(leftope, table, tables);
		Wrapper wrapperr=symbolOpe(rightope, table, tables);
		List<Expression> ext=null;
		ArrayList<Wrapper> extw=new ArrayList<Wrapper>();
		if(ie.hasExtendedOperands()){
			ext=ie.extendedOperands();   //eclipse AST�ڲ�����һ��������»�ʹ�ö��������������AST����
			for(Expression extexp: ext){
				extw.add(symbolOpe(extexp, table, tables));
			}
		}
		//wrapper.type==0...
		ValueTypePair leftSymbolValue=wrapperl.vtp;
		ValueTypePair rightSymbolValue=wrapperr.vtp;
		ArrayList<ValueTypePair> extv=new ArrayList<ValueTypePair>();
		for(Wrapper w: extw)
			extv.add(w.vtp);
		String operatorInStr=operator.toString();
		if(leftSymbolValue instanceof PrimitiveValueTypePair && rightSymbolValue instanceof PrimitiveValueTypePair){
			String result=((PrimitiveValueTypePair)leftSymbolValue).value+operatorInStr+
					((PrimitiveValueTypePair)rightSymbolValue).value;
			int extvSize=extv.size();
			for(int i=0; i<=extvSize-1; i++){
				if(extv.get(i) instanceof PrimitiveValueTypePair){
					result=result+operatorInStr+((PrimitiveValueTypePair)extv.get(i)).value;
				}
				else{
					//�����ܵĲ��������ͣ��׳��쳣
				}
			}

			String rettype=null;
			//����java��������ԭ�������׺���ʽ������
			ArrayList<String> alltypes=new ArrayList<String>();
			alltypes.add(leftSymbolValue.type);
			alltypes.add(rightSymbolValue.type);
			for(int i=0; i<=extvSize-1; i++)
				alltypes.add(extv.get(i).type);
			int[] tmp=new int[]{0, 0, 0, 0};  //��һ��һά�����ʾbool�ͣ�double�ͣ�float�ͣ�long��ֵ�ĸ���
			for(int i=0; i<=alltypes.size()-1; i++){
				String t=alltypes.get(i);
				if(Function.isPrimitiveType(t)){
					if(t.equals("boolean")) tmp[0]++;
					else if(t.equals("double")) tmp[1]++;
					else if(t.equals("float")) tmp[2]++;
					else if(t.equals("long")) tmp[3]++;
					else{
						//do nothing
					}
				}
				else{
					//�׳��쳣
				}
			}
			if(tmp[0]!=0) rettype="boolean";
			else if(tmp[1]!=0) rettype="double";
			else if(tmp[2]!=0) rettype="float";
			else if(tmp[3]!=0) rettype="long";
			else {
				rettype="int";
			}
			return new Wrapper(new PrimitiveValueTypePair(rettype, "("+result+")"));
		}
		else{
			//�����ܵĲ��������ͣ��׳��쳣
			return null;
		}
	}
	
	public Wrapper symbolOpeOnMethodInvocation(MethodInvocation invocation, InfoUnit table, ArrayList<InfoUnit> tables){
		if(invocation.typeArguments().size()==0){
			String methodname=invocation.getName().getIdentifier();
			@SuppressWarnings("unchecked")
			List<Expression> args=invocation.arguments();
			//wrapper.type==0...
	        ArrayList<String> passedTypes=new ArrayList<String>();		
	        ArrayList<ValueTypePair> argInfo=new ArrayList<ValueTypePair>();
			for(int i=0; i<=args.size()-1; i++){
				Wrapper w=symbolOpe(args.get(i), table, tables);
				passedTypes.add(w.vtp.type.toString());
				argInfo.add(w.vtp.clone());
			}
			MethodInfo calledMi=new MethodInfo(inf.className, methodname, passedTypes);
			MethodInfo theOne=mostMatch(calledMi, all);
			InvocationInfo ii=new InvocationInfo(mi, theOne, 0);
			table.methods.add(ii);
			MethodInvocationVisitor miv=new MethodInvocationVisitor(theOne, root, argInfo, all, projectRootPath, imports, packageInfo, table.mm);
			root.accept(miv);
			ArrayList<ReturnInfo> result=new ArrayList<ReturnInfo>();
			for(InfoUnit ttt: miv.inf.infs){
				InfoUnit n=new InfoUnit();
				n.mm=ttt.mm;
				n.cons=table.cons.and(ttt.cons);
				List<Map.Entry<String, VariableInfo>> e=new ArrayList<Map.Entry<String, VariableInfo>>(table.variables.entrySet());
				for (Map.Entry<String, VariableInfo> entry: e) {
					n.addOneVariable(entry.getValue().clone());
				}
				for(InvocationInfo iii: table.methods)
					n.methods.add(iii.clone());
				for(InvocationInfo iii: ttt.methods)
					n.methods.add(iii.clone());
				n.methods.add(new InvocationInfo(theOne, mi, 1));
				tables.add(n);
				result.add(new ReturnInfo(n.cons, ttt.retval));
			}
			tables.remove(table);
			pointer--;
			return new Wrapper(result);
		}
		else{
			//...
		}
	return null;
}
	
	/**
	 * �Ա�ʶ������ִ��
	 * @param exp ��ʶ��
	 * @param table ��ǰ���ű�
	 * @param tables ���з��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpeOnName(Name name, InfoUnit table, ArrayList<InfoUnit> tables){
		String nameInStr=name.toString();
		if(!nameInStr.contains(".")){
			//�����ƣ�ֱ�Ӳ��ҷ��ű��ȡ�������Ϣ���ߴ�this�����л�ȡ����Ϣ
			VariableInfo theOne=table.getVariableInfo(nameInStr);
			ValueTypePair vtp=null;
			if(theOne!=null){ //�ֲ�����
				if(theOne instanceof PrimitiveVarInfo){
					vtp=new PrimitiveValueTypePair(table.getVariableInfo(nameInStr).type, 
							(String)(((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.peek()));
				}
				else if(theOne instanceof ReferenceVarInfo){
					vtp=new ReferenceValueTypePair(table.getVariableInfo(nameInStr).type,
							((ReferenceVarInfo)(table.getVariableInfo(nameInStr))).handle);
				}
				else if(theOne instanceof ArrayVarInfo){
					vtp=new ArrayValueTypePair(table.getVariableInfo(nameInStr).type,
							((ArrayVarInfo)(table.getVariableInfo(nameInStr))).first_address);
				}
				else{
					//�׳��쳣
				}
			}
			else{ //this�����һ���ֶ�
				ReferenceVarInfo rrr=(ReferenceVarInfo)table.getVariableInfo("this");
				FieldInfo fi=table.mm.jh.objs[rrr.handle].getOneField(new FieldName(nameInStr));
				if(fi instanceof PrimitiveFieldInfo){
					PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)fi;
					vtp=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
				}
				else if(fi instanceof ReferenceFieldInfo){
					ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
					vtp=new ReferenceValueTypePair(rfi.fieldType, rfi.handle);
				}
				else if(fi instanceof ArrFieldInfo){
					ArrFieldInfo afi=(ArrFieldInfo)fi;
					vtp=new ArrayValueTypePair(afi.fieldType, afi.f);
				}
				else{
					//�׳��쳣
				}
			}
			return new Wrapper(vtp);
		}
		else{
			FieldInfo theOne=null;
			//ȫ�޶����ƣ���ȡ����ײ����������������ֵ
			String[] split=nameInStr.split("\\.");
			VariableInfo tmp=null;
			Instance instance=null;
			for(int i=0; i<=split.length-2; i++){
				if(i==0){
					tmp=table.getVariableInfo(split[i]);
					if(tmp==null){
						//û�в��ҵ�˵������һ�����������޶�����һ����̬�ֶλ�̬�ڲ����
						//�˰汾Ϊ�˼�����ﲻ������
					}
					else{	
						ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;
						instance=table.mm.jh.objs[rvi.handle];
					}
				}
				else{
					FieldInfo fi=instance.getOneField(new FieldName(split[i]));
					ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
					instance=table.mm.jh.objs[rfi.handle];
				}
			}
			theOne=instance.getOneField(new FieldName(split[split.length-1]));
			ValueTypePair vtp=null;
			if(theOne instanceof ArrFieldInfo){
				ArrFieldInfo afi=(ArrFieldInfo)theOne;
				vtp=new ArrayValueTypePair(afi.fieldType, afi.f);
			}
			else if(theOne instanceof PrimitiveFieldInfo){
				PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)theOne;
				vtp=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
			}
			else if(theOne instanceof ReferenceFieldInfo){
				ReferenceFieldInfo rfi=(ReferenceFieldInfo)theOne;
				vtp=new ReferenceValueTypePair(rfi.fieldType, rfi.handle);
			}
			return new Wrapper(vtp);
		}
	}
	
	/**
	 * ����ֵ������ֵ����ִ��
	 * @param nl ��ֵ������ֵ
	 * @param table ���ű�
	 * @param tables ���з��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpeOnNumberLiteral(NumberLiteral nl, InfoUnit table, ArrayList<InfoUnit> tables){
		String num= nl.getToken();
		PrimitiveType numtype=null;
		//��ֵ������ֵֻ�����ͺ͸�����
		if(num.contains(".")){
			if(num.contains("f")||num.contains("F")){
				numtype=root.getAST().newPrimitiveType(PrimitiveType.FLOAT);
			}
			else{
				numtype=root.getAST().newPrimitiveType(PrimitiveType.DOUBLE);
			}
		}
		else{
			if(num.contains("l")||num.contains("L")){
				numtype=root.getAST().newPrimitiveType(PrimitiveType.LONG);
			}
			else{
				numtype=root.getAST().newPrimitiveType(PrimitiveType.INT);
			}
		}
		return new Wrapper(new PrimitiveValueTypePair(numtype.toString(), num));
	}
	
	/**
	 * ��this���ʽ����ִ��
	 * @param te this���ʽ
	 * @param table ���ű�
	 * @param tables ���з��ű�
	 * @return ����ִ�н��
	 */
	public Wrapper symbolOpeOneThisExpression(ThisExpression te, InfoUnit table, ArrayList<InfoUnit> tables){
		//this���ʽ���ص�ǰ�����޶�����һ���������ڱ��汾��û�п����ڲ��࣬�����޶������Ǳ�������
		VariableInfo this_pointer=table.variables.get("this");
		if(this_pointer instanceof ReferenceVarInfo){
			ReferenceVarInfo rvi=(ReferenceVarInfo)this_pointer;
			ReferenceValueTypePair rvtp=new ReferenceValueTypePair(rvi.type, rvi.handle);
			return new Wrapper(rvtp);
		}
		else{
			//����
			return null;
		}
	}
	
	/**
	 * �����ű��ʽ����ִ��
	 * @param pe ���ű��ʽ
	 * @param table ��ǰ���ű�
	 * @param tables ���еķ��ű�
	 * @return ���ʽ�ķ���ֵ
	 */
	public Wrapper symbolOpeOnParenthesizedExpression(ParenthesizedExpression pe, InfoUnit table, ArrayList<InfoUnit> tables){
		Expression inner=pe.getExpression();
		return symbolOpe(inner, table, tables);
	}
	
	/**
	 * �Ժ�׺���ʽ����ִ��
	 * @param pe ��׺���ʽ
	 * @param table ��ǰ���ű�
	 * @param tables ���з��ű�
	 * @return
	 */
	public Wrapper symbolOpeOnPostfixExpression(PostfixExpression pe, InfoUnit table, ArrayList<InfoUnit> tables){
		//��׺���ʽֻ��++��--���ֲ�������ǰ������Ǳ����������Ը��·��ű�Ϊԭֵ+1/-1��������ֵ
		//��������ֻ�п����ǻ�����������
		Expression opd=pe.getOperand();
		if(opd instanceof Name){
			Name name=(Name)opd;
			String nameInStr=name.toString();
			PostfixExpression.Operator ope=pe.getOperator();		
			if(!nameInStr.contains(".")){
				if(ope.toString().equals("++")){
					if(table.getVariableInfo(nameInStr) instanceof PrimitiveVarInfo){
						String oldValue=(String)(((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.pop());
						String newValue="("+oldValue+"+1"+")";
						((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.push(newValue);
						return new Wrapper(new PrimitiveValueTypePair(table.getVariableInfo(nameInStr).type,
								newValue));
					}
					else{
						//�׳��쳣
					}
				}
				else if(ope.toString().equals("--")){
					if(table.getVariableInfo(nameInStr) instanceof PrimitiveVarInfo){
						String oldValue=(String)(((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.pop());
						String newValue="("+oldValue+"-1"+")";
						((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.push(newValue);
						return new Wrapper(new PrimitiveValueTypePair(table.getVariableInfo(nameInStr).type,
								newValue));
					}
					else{
						//�׳��쳣
					}
				}
			}
			else{
				FieldInfo theOne=null;
				//ȫ�޶����ƣ���ȡ����ײ����������������ֵ
				String[] split=nameInStr.split("\\.");
				VariableInfo tmp=null;
				Instance instance=null;
				for(int i=0; i<=split.length-2; i++){
					if(i==0){
						tmp=table.getVariableInfo(split[i]);
						if(tmp==null){
							//û�в��ҵ�˵������һ�����������޶�����һ����̬�ֶλ�̬�ڲ����
							//�˰汾Ϊ�˼�����ﲻ������
						}
						else{
							ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;
							instance=table.mm.jh.objs[rvi.handle];
						}
					}
					else{
						FieldInfo fi=instance.getOneField(new FieldName(split[i]));
						ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
						instance=table.mm.jh.objs[rfi.handle];
					}
				}
				theOne=instance.getOneField(new FieldName(split[split.length-1]));
				ValueTypePair vtp=null;
				if(theOne instanceof PrimitiveFieldInfo){
					PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)theOne;
					if(ope.toString().equals("++")){
						pfi.value="("+pfi.value+"+1"+")";
					}
					else if(ope.toString().equals("--")){
						pfi.value="("+pfi.value+"-1"+")";
					}
					vtp=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
				}
				else{
					//�����ܵĲ��������ͣ��׳��쳣
				}
				return new Wrapper(vtp);
			}
		}
		else{
			//�׳��쳣
		}
		return null;
	}
	
	public Wrapper symbolOpeOnPrefixExpression(PrefixExpression pe, InfoUnit table, ArrayList<InfoUnit> tables){
		Expression opd=pe.getOperand();
		PrefixExpression.Operator ope=pe.getOperator();
		if(ope.toString().equals("++")){
			if(opd instanceof Name){
					Name name=(Name)opd;
					String nameInStr=name.toString();
					if(!nameInStr.contains(".")){
						if(table.getVariableInfo(nameInStr) instanceof PrimitiveVarInfo){
							String oldValue=(String)(((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.pop());
							String newValue="("+oldValue+"+1"+")";
							((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.push(newValue);
							return new Wrapper(new PrimitiveValueTypePair(table.getVariableInfo(nameInStr).type,
									newValue));
						}
					}
					else{
						FieldInfo theOne=null;
						//ȫ�޶����ƣ���ȡ����ײ����������������ֵ
						String[] split=nameInStr.split("\\.");
						VariableInfo tmp=null;
						Instance instance=null;
						for(int i=0; i<=split.length-2; i++){
							if(i==0){
								tmp=table.getVariableInfo(split[i]);
								if(tmp==null){
									//û�в��ҵ�˵������һ�����������޶�����һ����̬�ֶλ�̬�ڲ����
									//�˰汾Ϊ�˼�����ﲻ������
								}
								else{
									ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;
									instance=table.mm.jh.objs[rvi.handle];
								}
							}
							else{
								FieldInfo fi=instance.getOneField(new FieldName(split[i]));
								ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
								instance=table.mm.jh.objs[rfi.handle];
							}
						}
						theOne=instance.getOneField(new FieldName(split[split.length-1]));
						ValueTypePair vtp=null;
						if(theOne instanceof PrimitiveFieldInfo){
							PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)theOne;
							pfi.value="("+pfi.value+"+1"+")";
							vtp=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
						}
						else{
							//�����ܵĲ��������ͣ��׳��쳣
						}
						return new Wrapper(vtp);
					}
			}
			else{
				//�����ܵĲ��������ͣ��׳��쳣
			}
		}
		else if(ope.toString().equals("--")){
			if(opd instanceof Name){
				Name name=(Name)opd;
				String nameInStr=name.toString();
				if(!(nameInStr.contains("."))){
					if(table.getVariableInfo(nameInStr) instanceof PrimitiveVarInfo){
						String oldValue=(String)(((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.pop());
						String newValue="("+oldValue+"-1"+")";
						((PrimitiveVarInfo)table.getVariableInfo(nameInStr)).value.push(newValue);
						return new Wrapper(new PrimitiveValueTypePair(table.getVariableInfo(nameInStr).type,
								newValue));
						}
					}
				else{
					FieldInfo theOne=null;
					//ȫ�޶����ƣ���ȡ����ײ����������������ֵ
					String[] split=nameInStr.split("\\.");
					VariableInfo tmp=null;
					Instance instance=null;
					for(int i=0; i<=split.length-2; i++){
						if(i==0){
							tmp=table.getVariableInfo(split[i]);
							if(tmp==null){
								//û�в��ҵ�˵������һ�����������޶�����һ����̬�ֶλ�̬�ڲ����
								//�˰汾Ϊ�˼�����ﲻ������
							}
							else{
								ReferenceVarInfo rvi=(ReferenceVarInfo)tmp;
								instance=table.mm.jh.objs[rvi.handle];
							}
						}
						else{
							FieldInfo fi=instance.getOneField(new FieldName(split[i]));
							ReferenceFieldInfo rfi=(ReferenceFieldInfo)fi;
							instance=table.mm.jh.objs[rfi.handle];
						}
					}
					theOne=instance.getOneField(new FieldName(split[split.length-1]));
					ValueTypePair vtp=null;
					if(theOne instanceof PrimitiveFieldInfo){
						PrimitiveFieldInfo pfi=(PrimitiveFieldInfo)theOne;
						pfi.value="("+pfi.value+"-1"+")";
						vtp=new PrimitiveValueTypePair(pfi.fieldType, pfi.value);
					}
					else{
						//�����ܵĲ��������ͣ��׳��쳣
					}
					return new Wrapper(vtp);
				}
			  }
			}
			else{
				Wrapper value=symbolOpe(opd, table, tables);
				if(value.type==0){
					if(Function.isPrimitiveType(value.vtp.type)){
						return new Wrapper(new PrimitiveValueTypePair(value.vtp.type,
								"("+ope.toString()+((PrimitiveValueTypePair)value.vtp).value+")"));
					}
					else{
						//�����ܵ��������ͣ��׳��쳣
					}
				}
				else if(value.type==1){ 
					ArrayList<ReturnInfo> result=new ArrayList<ReturnInfo>();
					for(ReturnInfo ri: value.ret){
						if(ri.retvalue instanceof PrimitiveValueTypePair){
							result.add(new ReturnInfo(ri.pc, new PrimitiveValueTypePair(ri.retvalue.type, "("+ope.toString()+
									((PrimitiveValueTypePair)ri.retvalue).value+")")));
						}
						else{
							//ǰ׺�������������Զ�����������������
							//�׳��쳣
						}
					}
				   return new Wrapper(result);
				}
			}
		return null;
	}
	
	/**
	 * ����java�ķ������ù����ҳ��ڷ����б�����ƥ��ʵ���б����һ��������
	 * ����������void f(int)��void f(double)�������صķ�������int a=1�������f(a)�ǵ��õĵ�һ��f��
	 * ���û��void f(int)��ֻ��void f(double)����f(a)���õ���void f(double)
	 * @param call �������
	 * @param ms ���еķ���
	 * @return ��ƥ��ʵ�εķ���������󱻵��õķ���
	 */
	public MethodInfo mostMatch(MethodInfo call, ArrayList<MethodInfo> ms){
		//������������ʵ������ƥ��ķ���
		/*
		ArrayList<MethodInfo> matched=new ArrayList<MethodInfo>();
		for(MethodInfo m: ms){
			if(m.method_name.equals(call.method_name)&&m.param_types.size()==call.param_types.size()){
				if(call.param_types.size()==0){
					return m;
				}
				else{
					for(int i=0; i<=call.param_types.size()-1; i++){
						
					}
				}
			}
		}
		*/
		for(MethodInfo m: ms){
			if(m.equals(call)){
				return m;
			}
		}
		return null;
	}
	
	public void resetptr(){
		pointer=0;
	}
	
	/**
	 * ���wrapper�б����п��ܵ���Ϣ��ϡ�<br/>
	 * ���һ��wrapper����Լ��ΪA��returnInfo1��!A��returnInfo2���ڶ���wrapper����Լ��ΪB��returnInfo3��!B��returnInfo4, <br/>
	 * �����е����ΪA&&B��ֵreturnInfo1 returnInfo3��A&&!B��ֵreturnInfo1 returnInfo4��������4��
	 * @param wrappers wrapper�б�
	 * @return ���е���Ϣ���
	 */
	/*
	public ArrayList<VTPSequence> combinations(ArrayList<Wrapper> wrappers){
		ArrayList<VTPSequence> list=new ArrayList<VTPSequence>();
		int size=wrappers.size();
		Wrapper[] warr=new Wrapper[size];
		int[] sizearr=new int[size];
		int[] ptr=new int[size];
		for(int i=0; i<=size-1; i++){
			warr[i]=wrappers.get(i);
			if(warr[i].type==0)
				sizearr[i]=1;
			else if(warr[i].type==1)
				sizearr[i]=warr[i].ret.size();
			else{
				sizearr[i]=0;
			}
			ptr[i]=0;
		}
		while(ptr[0]!=sizearr[0]){
			ConstraintInfo ci=new ConstraintInfo();
			ArrayList<ValueTypePair> vs=new ArrayList<ValueTypePair>();
			for(int i=0; i<=size-1; i++){
				if(warr[i].type==0){
				   vs.add(warr[i].vtp);
				}
				else if(warr[i].type==1){
					ReturnInfo selected=warr[i].ret.get(ptr[i]);
					ci=ci.and(selected.pc);
					vs.add(selected.retvalue);
				}
			}
			VTPSequence vtps=new VTPSequence(ci, vs);
			list.add(vtps);
			
			int decisionptr=size-1;
			ptr[decisionptr]++;
			while(ptr[decisionptr]==sizearr[decisionptr]&&decisionptr!=0){
				ptr[decisionptr]=0;
				decisionptr--;
				ptr[decisionptr]++;
			}
		}
		
		return list;
	}
	*/
}
