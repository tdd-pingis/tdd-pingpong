package pingis.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

public class TestStubBuilder extends CodeStubBuilder {

  private CompilationUnit targetCompilationUnit;

  public TestStubBuilder(String code) {
    // Need to supply a fake class name here, because we don't yet know the actual
    // name of the class.
    super("__PLACEHOLDER__");

    targetCompilationUnit = JavaParser.parse(code);

    generateTestClass();
  }

  private ClassOrInterfaceDeclaration getFirstDeclaredClass() {
    return targetCompilationUnit.getTypes().stream()
        .filter(typeDecl -> typeDecl instanceof ClassOrInterfaceDeclaration)
        .map(typeDecl -> (ClassOrInterfaceDeclaration) typeDecl)
        .findFirst().get();
  }

  private String generateTestMethodName(String methodName) {
    String remainder = methodName.length() > 1 ? methodName.substring(1) : "";
    return String.format("test%c%s", Character.toUpperCase(methodName.charAt(0)), remainder);
  }

  private String generateTestClassName(ClassOrInterfaceDeclaration target) {
    return String.format("%sTest", target.getNameAsString());
  }

  private void addTestMethod(ClassOrInterfaceDeclaration clazz, MethodDeclaration method) {
    String testMethodName = generateTestMethodName(method.getNameAsString());
    MethodDeclaration testMethod = clazz.addMethod(testMethodName, Modifier.PUBLIC);

    testMethod.addMarkerAnnotation("Test");
  }

  private void generateTestClass() {
    ClassOrInterfaceDeclaration firstClass = getFirstDeclaredClass();

    this.className = generateTestClassName(firstClass);
    clazz.setName(className);

    // TODO: What value should @Points have?
    clazz.addSingleMemberAnnotation("Points", "\"03-03\"");

    firstClass.getMethods().stream()
        .filter(method -> method.isPublic())
        .forEach(method -> addTestMethod(clazz, method));
  }

  public CodeStubBuilder withTestImports() {
    compilationUnit.addImport("org.junit.Test");
    compilationUnit.addImport("fi.helsinki.cs.tmc.edutestutils.Points");
    compilationUnit.addImport("org.junit.Assert", true, true);

    return this;
  }

  @Override
  public CodeStub build() {
    return new CodeStub(className,
        String.format("test/%s.java", className), super.build().code);
  }
}
