package pingis.utils;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Created by dwarfcrank on 7/21/17.
 */
public class JavaSyntaxChecker {

    public static String[] parseCode(String code) {
        try {
            CompilationUnit cu = JavaParser.parse(code);
        } catch (ParseProblemException parseProblems) {
            return parseProblems.getProblems().stream().map((prob) ->
                    prob.getVerboseMessage()
                ).toArray(String[]::new);
        }

        return null;
    }
}
