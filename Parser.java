package parser;

import inter.*;

import java.awt.image.renderable.RenderableImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import symbols.Array;
import symbols.Env;
import symbols.Type;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import lexer.Num;


class FailHintCollection
{
    public static final String ParenthesisDoesNotMatch = "括号没有成对匹配";
    public static final String MissingEndOfLine = "missing ; at the end of line.";
    public static final String TypeWithoutId = "定义变量的类型后必须跟随变量名";
    public static final String OperandTypeMismatch = "操作数类型不一致";
}

public class Parser
{

    private Lexer lex;    // lexical analyzer for this parser
    private Token look;   // lookahead tagen
    Env top = null;       // current or top symbol table
    int used = 0;         // storage used for declarations

    public Parser(Lexer l) throws IOException
    {
        lex = l;
        move();
    }

    void move() throws IOException
    {
        look = lex.scan();
    }

    void error(String s, String reason)
    {
        throw new Error("near line " + lex.line + ": " + s + " , reason : " + reason);
    }

    void error(String s)
    {
        throw new Error("near line " + lex.line + ": " + s);
    }

    void match(int t) throws IOException
    {
        if (look.tag == t) move();
        else error("syntax error");
    }

    void match(int t, String failedHint) throws IOException
    {
        if (look.tag == t) move();
        else error("syntax error", failedHint);
    }

    public void program() throws IOException
    {  // program -> block
        Stmt s = block();
        int begin = s.newlabel();
        int after = s.newlabel();
        s.emitlabel(begin);
        s.gen(begin, after);
        s.emitlabel(after);

    }

    Stmt block() throws IOException
    {  // block -> { decls stmts }
        match('{');
        Env savedEnv = top;
        top = new Env(top);
        decls();
        Stmt s = stmts();
        match('}', FailHintCollection.ParenthesisDoesNotMatch);
        top = savedEnv;
        return s;
    }

    void decls() throws IOException
    {

        while (look.tag == Tag.BASIC)
        {   // D -> type ID ;
            Type p = type();
            Token tok = look;
            match(Tag.ID, FailHintCollection.TypeWithoutId);
            match(';', FailHintCollection.MissingEndOfLine);
            Id id = new Id((Word) tok, p, used);
            top.put(tok, id);
            used = used + p.width;
        }
    }

    Type type() throws IOException
    {

        Type p = (Type) look;            // expect look.tag == Tag.BASIC
        match(Tag.BASIC);
        if (look.tag != '[') return p; // T -> basic
        else return dims(p);            // return array type
    }

    Type dims(Type p) throws IOException
    {
        match('[');
        Token tok = look;
        match(Tag.NUM);
        match(']', FailHintCollection.ParenthesisDoesNotMatch);
        if (look.tag == '[')
            p = dims(p);
        return new Array(((Num) tok).value, p);
    }

    Stmt stmts() throws IOException
    {
        if (look.tag == '}') return Stmt.Null;
        else return new Seq(stmt(), stmts());
    }

    Stmt stmt() throws IOException
    {
        Expr x;
        Stmt s, s1, s2;
        Stmt savedStmt;         // save enclosing loop for breaks

        switch (look.tag)
        {

            case ';':
                move();
                return Stmt.Null;

            case Tag.IF:
                match(Tag.IF);
                match('(');
                x = bool();
                match(')', FailHintCollection.ParenthesisDoesNotMatch);
                s1 = stmt();
                if (look.tag != Tag.ELSE) return new If(x, s1);
                match(Tag.ELSE);
                s2 = stmt();
                return new Else(x, s1, s2);

            case Tag.WHILE:
                While whilenode = new While();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = whilenode;

                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')', FailHintCollection.ParenthesisDoesNotMatch);

                s1 = stmt();
                whilenode.init(x, s1);
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return whilenode;

            case Tag.DO:
                Do donode = new Do();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = donode;
                match(Tag.DO);
                s1 = stmt();
                match(Tag.WHILE);
                match('(');
                x = bool();
                match(')', FailHintCollection.ParenthesisDoesNotMatch);
                match(';', FailHintCollection.MissingEndOfLine);
                donode.init(s1, x);
                Stmt.Enclosing = savedStmt;  // reset Stmt.Enclosing
                return donode;

            case Tag.BREAK:
                match(Tag.BREAK);
                match(';', FailHintCollection.MissingEndOfLine);
                return new Break();

            case Tag.FOR:
                For fornode = new For();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = fornode;
                match(Tag.FOR);
                match('(');
                Stmt fors1 = forassign();
                match(';', FailHintCollection.MissingEndOfLine);
                Expr forx = bool();
                match(';', FailHintCollection.MissingEndOfLine);
                Stmt fors2 = forassign();
                match(')', FailHintCollection.ParenthesisDoesNotMatch);
                Stmt fors3 = stmt();
                fornode.init(fors1, forx, fors2, fors3);
                Stmt.Enclosing = savedStmt;
                return fornode;

            case Tag.SWITCH:
            {
                Switch switchNode = new Switch();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = switchNode;
                match(Tag.SWITCH);
                match('(');
                Expr castTarget = expr();
                match(')');
                match('{');
                List<Case> relax = new ArrayList<>();
                while (look.tag == Tag.CASE || look.tag == Tag.DEFAULT)
                {
                    relax.add((Case) stmt());
                }
                match('}');
                switchNode.init(castTarget, relax);
                Stmt.Enclosing = savedStmt;
                return switchNode;
            }
            case Tag.CASE:
            {
                Case caseNode = new Case();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = caseNode;
                match(Tag.CASE);
                Expr tag = expr();
                match(':');
                if (look.tag == Tag.CASE || look.tag == Tag.DEFAULT)
                {
                    caseNode.init(tag, null, false);
                    Stmt.Enclosing = savedStmt;
                    return caseNode;
                }
                if (look.tag == Tag.BREAK)
                {
                    stmt();
                    caseNode.init(tag, null, true);
                    Stmt.Enclosing = savedStmt;
                    return caseNode;
                }
                Stmt caseBody = stmt();
                if (look.tag == Tag.BREAK)
                {
                    stmt();
                    caseNode.init(tag, caseBody, true);
                } else
                {
                    caseNode.init(tag, caseBody, false);
                }
                Stmt.Enclosing = savedStmt;
                return caseNode;
            }
            case Tag.DEFAULT:
            {
                Case caseNode = new Case();
                savedStmt = Stmt.Enclosing;
                Stmt.Enclosing = caseNode;
                match(Tag.DEFAULT);
                match(':');
                Stmt caseBody = stmt();
                if (look.tag == Tag.BREAK)
                {
                    stmt();
                    caseNode.init(null, caseBody, true);
                } else
                {
                    caseNode.init(null, caseBody, false);
                }
                Stmt.Enclosing = savedStmt;
                return caseNode;
            }
            case '{':
                return block();

            default:
                return assign();
        }
    }

    Stmt forassign() throws IOException
    {
        Stmt stmt = null;
        Token t = look;
        match(Tag.ID, FailHintCollection.TypeWithoutId);
        Id id = top.get(t);
        if (id == null) error(t.toString() + " undeclared");
        if (!look.toString().equals("["))
        {
            if (!look.toString().equals("="))
            {
                error("Symbol error.", "赋值语句中必须使用=号。");
            }
            if (look.tag == '=')
            {       // S -> id = E ;
                move();
                Expr right = expr();
                if (!id.type.lexeme.equals(right.type.lexeme))
                {
                    error("语法错误", "赋值操作的左右两边操作数类型不一致. 左边为 " + id.type.lexeme + ", 而右边为 " + right.type.lexeme);
                }
                stmt = new Set(id, right);
            }
        } else
        {                        // S -> L = E ;
            Access x = offset(id);
            match('=');
            Expr right = expr();

            stmt = new SetElem(x, right);
        }
        return stmt;
    }

    Stmt assign() throws IOException
    {
        Stmt stmt = forassign();
        match(';', FailHintCollection.MissingEndOfLine);
        return stmt;
    }

    Expr bool() throws IOException
    {
        Expr x = join();
        int[] bools = {Tag.OR, Tag.AND, Tag.LE, Tag.GE, '<', '>', Tag.EQ, Tag.TRUE, Tag.FALSE};
        boolean InBools = false;
        for (int i = 0; i < bools.length; i++)
        {
            if (x.op.tag == (bools[i]))
            {
                InBools = true;
                break;
            }
        }
        if (!InBools)
        {
            error("语法错误", "The type of a condition expression is not bool");
        }
        while (look.tag == Tag.OR)
        {
            Token tok = look;
            move();
            x = new Or(tok, x, join());
        }
        return x;
    }

    Expr join() throws IOException
    {
        Expr x = equality();
        while (look.tag == Tag.AND)
        {
            Token tok = look;
            move();
            x = new And(tok, x, equality());
        }
        return x;
    }

    Expr equality() throws IOException
    {
        Expr x = rel();
        while (look.tag == Tag.EQ || look.tag == Tag.NE)
        {
            Token tok = look;
            move();
            x = new Rel(tok, x, rel());
        }
        return x;
    }

    Expr rel() throws IOException
    {
        Expr x = expr();

        switch (look.tag)
        {
            case '<':
            case Tag.LE:
            case Tag.GE:
            case '>':
                Token tok = look;
                move();
                Expr right = expr();
                if (!CheckOperandType(x, right))
                {
                    error("语法错误", FailHintCollection.OperandTypeMismatch);
                }
                return new Rel(tok, x, right);
            default:
                return x;
        }
    }

    Expr expr() throws IOException
    {
        Expr x = term();
        while (look.tag == '+' || look.tag == '-')
        {
            Token tok = look;
            move();
            Expr right = term();
            if (!CheckOperandType(x, right))
            {
                error("语法错误", FailHintCollection.OperandTypeMismatch);
            }
            x = new Arith(tok, x, right);
        }
        return x;
    }

    Expr term() throws IOException
    {
        Expr x = unary();
        while (look.tag == '*' || look.tag == '/')
        {
            Token tok = look;
            move();
            Expr right = unary();
            if (!CheckOperandType(x, right))
            {
                error("语法错误", FailHintCollection.OperandTypeMismatch);
            }
            x = new Arith(tok, x, right);
        }
        return x;
    }

    Expr unary() throws IOException
    {
        if (look.tag == '-')
        {
            move();
            return new Unary(Word.minus, unary());
        } else if (look.tag == '!')
        {
            Token tok = look;
            move();
            return new Not(tok, unary());
        } else return factor();
    }

    Expr factor() throws IOException
    {
        Expr x = null;
        switch (look.tag)
        {
            case '(':
                move();
                x = bool();
                match(')', FailHintCollection.ParenthesisDoesNotMatch);
                return x;
            case Tag.NUM:
                x = new Constant(look, Type.Int);
                move();
                return x;
            case Tag.REAL:
                x = new Constant(look, Type.Float);
                move();
                return x;
            case Tag.TRUE:
                x = Constant.True;
                move();
                return x;
            case Tag.FALSE:
                x = Constant.False;
                move();
                return x;
            default:
                error("syntax error");
                return x;
            case Tag.ID:
                String s = look.toString();
                Id id = top.get(look);
                if (id == null) error(look.toString() + " undeclared");
                move();
                if (look.tag != '[') return id;
                else return offset(id);
        }
    }

    Access offset(Id a) throws IOException
    {   // I -> [E] | [E] I
        Expr i;
        Expr w;
        Expr t1, t2;
        Expr loc;  // inherit id

        Type type = a.type;
        match('[');
        i = expr(); // xx
        match(']', FailHintCollection.ParenthesisDoesNotMatch);     // first index, I -> [ E ]
        type = ((Array) type).of;
        w = new Constant(type.width);
        t1 = new Arith(new Token('*'), i, w);
        loc = t1;
        while (look.tag == '[')
        {      // multi-dimensional I -> [ E ] I
            match('[');
            i = bool();
            match(']', FailHintCollection.ParenthesisDoesNotMatch);
            type = ((Array) type).of;
            w = new Constant(type.width);
            t1 = new Arith(new Token('*'), i, w);
            t2 = new Arith(new Token('+'), loc, t1);
            loc = t2;
        }

        return new Access(a, loc, type);
    }

    boolean CheckOperandType(Expr left, Expr right)
    {
        return left.type.lexeme.equals(right.type.lexeme);
    }
}
