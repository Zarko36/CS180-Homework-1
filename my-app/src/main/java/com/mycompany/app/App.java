package com.mycompany.app;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.io.InputStream;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.ModifierVisitor;
import com.github.javaparser.ast.visitor.Visitable;
import com.github.javaparser.printer.YamlPrinter;

public class App 
{
    private static final String FILE_PATH = "../my-app/src/main/resources/SimpleComparison.java";
    private static final String OUTPUT_PATH = "../my-app/dataPiece.yaml";
    public static void main( String[] args )
    {
        try{
            Path pathway = (Path)Paths.get(FILE_PATH);
            Path outputPathway = (Path)Paths.get(OUTPUT_PATH);
            InputStream fileStream = Files.newInputStream(pathway);
            CompilationUnit compilationUnit =  StaticJavaParser.parse(Files.newInputStream(Paths.get(FILE_PATH)));
            System.out.println("Original Program File: ");
            System.out.println("```````````````````````````````````````");
            System.out.println(compilationUnit);
            System.out.println("```````````````````````````````````````");
            System.out.println("Program file after code patching: ");
            System.out.println("```````````````````````````````````````");
            compilationUnit.accept(new ModifierVisitor<Void>() {
                @Override
                public Visitable visit(IfStmt node, Void arg) {
                    Expression conditionalExpression = node.getCondition();
                    if (conditionalExpression instanceof BinaryExpr) {
                        BinaryExpr conditional = (BinaryExpr) conditionalExpression;
                        if (conditional.getOperator() == BinaryExpr.Operator.NOT_EQUALS && node.getElseStmt().isPresent()) {
                            Statement thenStatement = node.getThenStmt().clone();
                            Statement elseStatement = node.getElseStmt().get().clone();
                            node.setThenStmt(elseStatement);
                            node.setElseStmt(thenStatement);
                            conditional.setOperator(BinaryExpr.Operator.EQUALS);
                        }
                    }
                    return super.visit(node, arg);
                }
            }, null);
            System.out.println(compilationUnit);
            System.out.println("```````````````````````````````````````");
            YamlPrinter printer = new YamlPrinter(true);
            System.out.println(printer.output(compilationUnit));
            Files.write(outputPathway, printer.output(compilationUnit).toString().getBytes());
            fileStream.close();
        }
        catch(IOException e){
            System.out.println(e);
        }
    }
}