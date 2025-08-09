import javax.tools.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.reflect.Method;

public class TinyCompiler {

    enum TokenType { NUMBER, PLUS, MINUS, MUL, DIV, LPAREN, RPAREN, EOF }
    static class Token {
        TokenType type; String text;
        Token(TokenType t, String s){ type=t; text=s; }
        public String toString(){ return type + ":" + text; }
    }
    static class Lexer {
        private final String input;
        private int pos = 0;
        Lexer(String input){ this.input = input; }
        char peek(){ return pos < input.length() ? input.charAt(pos) : '\0'; }
        void skip(){ pos++; }
        Token next() {
            while (Character.isWhitespace(peek())) skip();
            char c = peek();
            if (c == '\0') return new Token(TokenType.EOF, "");
            if (Character.isDigit(c)) {
                StringBuilder sb = new StringBuilder();
                while (Character.isDigit(peek()) || peek()=='.') {
                    sb.append(peek()); skip();
                }
                return new Token(TokenType.NUMBER, sb.toString());
            }
            switch(c) {
                case '+': skip(); return new Token(TokenType.PLUS, "+");
                case '-': skip(); return new Token(TokenType.MINUS, "-");
                case '*': skip(); return new Token(TokenType.MUL, "*");
                case '/': skip(); return new Token(TokenType.DIV, "/");
                case '(': skip(); return new Token(TokenType.LPAREN, "(");
                case ')': skip(); return new Token(TokenType.RPAREN, ")");
                default: throw new RuntimeException("Unknown char: " + c);
            }
        }
    }

    interface Expr { String toJava(); }
    static class NumberExpr implements Expr {
        String value; NumberExpr(String v){ value=v; }
        public String toJava(){
            if (value.contains(".")) {
                return value;
            } else {
                return value + ".0";
            }
        }

    }
    static class BinaryExpr implements Expr {
        Expr left; Expr right; String op;
        BinaryExpr(Expr l, String op, Expr r){ left=l; this.op=op; right=r; }
        public String toJava(){ return "(" + left.toJava() + op + right.toJava() + ")"; }
    }

    static class Parser {
        Lexer lex;
        Token cur;
        Parser(String src){ lex = new Lexer(src); cur = lex.next(); }
        void eat(TokenType t) {
            if (cur.type == t) cur = lex.next();
            else throw new RuntimeException("Expected " + t + " but got " + cur);
        }
        Expr parse() { return parseExpr(); }
        Expr parseExpr() {
            Expr node = parseTerm();
            while (cur.type == TokenType.PLUS || cur.type == TokenType.MINUS) {
                String op = cur.text; eat(cur.type);
                node = new BinaryExpr(node, op, parseTerm());
            }
            return node;
        }
        Expr parseTerm() {
            Expr node = parseFactor();
            while (cur.type == TokenType.MUL || cur.type == TokenType.DIV) {
                String op = cur.text; eat(cur.type);
                node = new BinaryExpr(node, op, parseFactor());
            }
            return node;
        }
        Expr parseFactor() {
            if (cur.type == TokenType.NUMBER) {
                String v = cur.text; eat(TokenType.NUMBER);
                return new NumberExpr(v);
            } else if (cur.type == TokenType.LPAREN) {
                eat(TokenType.LPAREN);
                Expr e = parseExpr();
                eat(TokenType.RPAREN);
                return e;
            } else {
                throw new RuntimeException("Unexpected token in factor: " + cur);
            }
        }
    }

    static String generateJavaSource(String className, Expr expr) {
        String src =
            "public class " + className + " implements java.util.concurrent.Callable<Double> {\n" +
            "  public Double call() {\n" +
            "    return " + expr.toJava() + ";\n" +
            "  }\n" +
            "}\n";
        return src;
    }

    static Class<?> compileAndLoad(String className, String source) throws Exception {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null)
            throw new RuntimeException("No system Java compiler (are you running a JRE instead of JDK?)");
        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);

        JavaFileObject fileObject = new JavaSourceFromString(className, source);
        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(fileObject);

        File outDir = new File(System.getProperty("java.io.tmpdir"), "tinycompiler_out");
        outDir.mkdirs();

        List<String> options = Arrays.asList("-d", outDir.getAbsolutePath());
        JavaCompiler.CompilationTask task = compiler.getTask(null, stdFileManager, null, options, null, compilationUnits);
        Boolean ok = task.call();
        stdFileManager.close();
        if (!ok) throw new RuntimeException("Compilation failed");

        URL[] urls = new URL[]{ outDir.toURI().toURL() };
        URLClassLoader cl = new URLClassLoader(urls);
        return cl.loadClass(className);
    }


    static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;
        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name + Kind.SOURCE.extension), Kind.SOURCE);
            this.code = code;
        }
        public CharSequence getCharContent(boolean ignoreEncodingErrors) { return code; }
    }


    public static void main(String[] args) throws Exception {
        System.out.println("****** Here this is a Tiny compiler *****");
        if (args.length == 0) {
            System.out.println("Usage: java TinyCompiler \"expression\"");
            System.out.println("Example: java TinyCompiler \"(1+2)*3 - 4/2\"");
            return;
        }
        String exprSrc = args[0];
        Parser p = new Parser(exprSrc);
        Expr ast = p.parse();
        String className = "CompiledExpr" + System.currentTimeMillis();
        String javaSrc = generateJavaSource(className, ast);

        System.out.println("Generated Java source:\n" + javaSrc);

        Class<?> clazz = compileAndLoad(className, javaSrc);
        java.util.concurrent.Callable<Double> instance = (java.util.concurrent.Callable<Double>) clazz.getDeclaredConstructor().newInstance();
        Double result = instance.call();
        System.out.println("Result = " + result);
    }
}
