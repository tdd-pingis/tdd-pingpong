package pingis.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.LineComment;

public class CodeStubBuilder {
  protected final CompilationUnit compilationUnit;
  protected final ClassOrInterfaceDeclaration clazz;
  protected String className;
  protected String filename;

  public CodeStubBuilder(String className) {
    compilationUnit = new CompilationUnit();
    this.className = className;
    filename = String.format("src/%s.java", className);

    clazz = compilationUnit.addClass(className);
  }

  public CodeStubBuilder withImport(String name) {
    compilationUnit.addImport(name);

    return this;
  }

  public CodeStubBuilder withBodyComment(String comment) {
    // Hack to add some nice whitespace between // and the comment text
    if (!comment.startsWith(" ")) {
      comment = " " + comment;
    }

    LineComment commentNode = new LineComment(comment);
    clazz.addOrphanComment(commentNode);

    return this;
  }

  public CodeStub build() {
    return new CodeStub(className, filename, compilationUnit.toString());
  }

  public static CodeStubBuilder fromCode(String code) {
    CompilationUnit cu = JavaParser.parse(code);

    for (TypeDeclaration<?> type : cu.getTypes()) {

      if (type instanceof ClassOrInterfaceDeclaration
              && type.getModifiers().contains(Modifier.PUBLIC)) {

        String className = type.getNameAsString();
        return new CodeStubBuilder(className);
      }
    }

    throw new IllegalArgumentException("Task instance's code did not contain any public class");
  }
}
