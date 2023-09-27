/*Copyright 2023 by Beverly A Sanders
 * 
 * This code is provided for solely for use of students in COP4020 Programming Language Concepts at the 
 * University of Florida during the fall semester 2023 as part of the course project.  
 * 
 * No other use is authorized. 
 * 
 * This code may not be posted on a public web site either during or after the course.  
 */
package edu.ufl.cise.cop4020fa23;

import static edu.ufl.cise.cop4020fa23.Kind.AND;
import static edu.ufl.cise.cop4020fa23.Kind.BANG;
import static edu.ufl.cise.cop4020fa23.Kind.BITAND;
import static edu.ufl.cise.cop4020fa23.Kind.BITOR;
import static edu.ufl.cise.cop4020fa23.Kind.COLON;
import static edu.ufl.cise.cop4020fa23.Kind.COMMA;
import static edu.ufl.cise.cop4020fa23.Kind.DIV;
import static edu.ufl.cise.cop4020fa23.Kind.EOF;
import static edu.ufl.cise.cop4020fa23.Kind.EQ;
import static edu.ufl.cise.cop4020fa23.Kind.EXP;
import static edu.ufl.cise.cop4020fa23.Kind.GE;
import static edu.ufl.cise.cop4020fa23.Kind.GT;
import static edu.ufl.cise.cop4020fa23.Kind.IDENT;
import static edu.ufl.cise.cop4020fa23.Kind.LE;
import static edu.ufl.cise.cop4020fa23.Kind.LPAREN;
import static edu.ufl.cise.cop4020fa23.Kind.LSQUARE;
import static edu.ufl.cise.cop4020fa23.Kind.LT;
import static edu.ufl.cise.cop4020fa23.Kind.MINUS;
import static edu.ufl.cise.cop4020fa23.Kind.MOD;
import static edu.ufl.cise.cop4020fa23.Kind.NUM_LIT;
import static edu.ufl.cise.cop4020fa23.Kind.OR;
import static edu.ufl.cise.cop4020fa23.Kind.PLUS;
import static edu.ufl.cise.cop4020fa23.Kind.QUESTION;
import static edu.ufl.cise.cop4020fa23.Kind.RARROW;
import static edu.ufl.cise.cop4020fa23.Kind.RES_blue;
import static edu.ufl.cise.cop4020fa23.Kind.RES_green;
import static edu.ufl.cise.cop4020fa23.Kind.RES_height;
import static edu.ufl.cise.cop4020fa23.Kind.RES_red;
import static edu.ufl.cise.cop4020fa23.Kind.RES_width;
import static edu.ufl.cise.cop4020fa23.Kind.RPAREN;
import static edu.ufl.cise.cop4020fa23.Kind.RSQUARE;
import static edu.ufl.cise.cop4020fa23.Kind.STRING_LIT;
import static edu.ufl.cise.cop4020fa23.Kind.TIMES;
import static edu.ufl.cise.cop4020fa23.Kind.CONST;

import java.util.Arrays;

import edu.ufl.cise.cop4020fa23.ast.AST;
import edu.ufl.cise.cop4020fa23.ast.BinaryExpr;
import edu.ufl.cise.cop4020fa23.ast.BooleanLitExpr;
import edu.ufl.cise.cop4020fa23.ast.ChannelSelector;
import edu.ufl.cise.cop4020fa23.ast.ConditionalExpr;
import edu.ufl.cise.cop4020fa23.ast.ConstExpr;
import edu.ufl.cise.cop4020fa23.ast.ExpandedPixelExpr;
import edu.ufl.cise.cop4020fa23.ast.Expr;
import edu.ufl.cise.cop4020fa23.ast.IdentExpr;
import edu.ufl.cise.cop4020fa23.ast.NumLitExpr;
import edu.ufl.cise.cop4020fa23.ast.PixelSelector;
import edu.ufl.cise.cop4020fa23.ast.PostfixExpr;
import edu.ufl.cise.cop4020fa23.ast.StringLitExpr;
import edu.ufl.cise.cop4020fa23.ast.UnaryExpr;
import edu.ufl.cise.cop4020fa23.exceptions.LexicalException;
import edu.ufl.cise.cop4020fa23.exceptions.PLCCompilerException;
import edu.ufl.cise.cop4020fa23.exceptions.SyntaxException;
/**
Expr::=  ConditionalExpr | LogicalOrExpr    
ConditionalExpr ::=  ?  Expr  :  Expr  :  Expr 
LogicalOrExpr ::= LogicalAndExpr (    (   |   |   ||   ) LogicalAndExpr)*
LogicalAndExpr ::=  ComparisonExpr ( (   &   |  &&   )  ComparisonExpr)*
ComparisonExpr ::= PowExpr ( (< | > | == | <= | >=) PowExpr)*
PowExpr ::= AdditiveExpr ** PowExpr |   AdditiveExpr
AdditiveExpr ::= MultiplicativeExpr ( ( + | -  ) MultiplicativeExpr )*
MultiplicativeExpr ::= UnaryExpr (( * |  /  |  % ) UnaryExpr)*
UnaryExpr ::=  ( ! | - | length | width) UnaryExpr  |  UnaryExprPostfix
UnaryExprPostfix::= PrimaryExpr (PixelSelector | ε ) (ChannelSelector | ε )
PrimaryExpr ::=STRING_LIT | NUM_LIT |  IDENT | ( Expr ) | Z 
    ExpandedPixel  
ChannelSelector ::= : red | : green | : blue
PixelSelector  ::= [ Expr , Expr ]
ExpandedPixel ::= [ Expr , Expr , Expr ]
Dimension  ::=  [ Expr , Expr ]                         

 */

public class ExpressionParser implements IParser {
	
	final ILexer lexer;
	private IToken t;

	// This token is updated by the lexer and represents the current token being processed.
//	private IToken currentToken;



	/**
	 * @param lexer
	 * @throws LexicalException 
	 */
	public ExpressionParser(ILexer lexer) throws LexicalException {
		super();
		this.lexer = lexer;
		t = lexer.next();
	}


	@Override
	public AST parse() throws PLCCompilerException {
		Expr e = expr();
		return e;
	}

//	private void match(Kind expectedKind) throws SyntaxException {
//		if (t.kind() == expectedKind) {
//			try {
//				t = lexer.next();  // Move to the next token
//			} catch (LexicalException e) {
//				throw new SyntaxException(t.sourceLocation(), "Lexical error while trying to match " + expectedKind);
//			}
//		} else {
//			throw new SyntaxException(t.sourceLocation(), "Expected " + expectedKind + " but found " + t.kind());
//		}
//	}

	private void match(Kind expectedKind) throws LexicalException, SyntaxException {
		if (t.kind() == expectedKind) {
			try {
				t = lexer.next();  // Move to the next token
			} catch (LexicalException e) {
				throw new LexicalException(t.sourceLocation(), "Lexical error while trying to match " + expectedKind);
			}
		} else {
			throw new SyntaxException(t.sourceLocation(), "Expected " + expectedKind + " but found " + t.kind());
		}
	}



	private ConditionalExpr conditionalExpr() throws PLCCompilerException {
		IToken firstToken = t;

		match(Kind.QUESTION);  // Match the '?' symbol
		Expr condition = expr();  // Parse the condition expression
		match(Kind.RARROW);  // Match the '->' symbol
		Expr trueExpr = expr();  // Parse the expression for the true case
		match(Kind.COMMA);  // Match the ',' symbol
		Expr falseExpr = expr();  // Parse the expression for the false case

		return new ConditionalExpr(firstToken, condition, trueExpr, falseExpr);
	}

	private Expr logicalAndExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr left = comparisonExpr();  // Parse the first ComparisonExpr

		while (t.kind() == Kind.BITAND || t.kind() == Kind.AND) {
			IToken opToken = t;  // Store the operator token
			match(t.kind());  // Match the current 'BITAND' or 'AND' token
			Expr right = comparisonExpr();  // Parse the next ComparisonExpr
			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the operator
		}
		return left;  // Return the combined or original expression
	}

//	private Expr logicalAndExpr() throws PLCCompilerException {
//		IToken firstToken = t;
//		Expr left = comparisonExpr();  // Parse the first ComparisonExpr
//
//		while (t.kind() == Kind.BITAND || t.kind() == Kind.AND) {
//			IToken opToken = t;  // Store the operator token
//			match(t.kind());  // Match the current '&' or '&&' token
//			Expr right = comparisonExpr();  // Parse the next ComparisonExpr
//			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the operator
//		}
//		return left;  // Return the combined or original expression
//	}



	private Expr logicalOrExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr left = logicalAndExpr();  // Parse the first LogicalAndExpr

		while (t.kind() == Kind.OR || t.kind() == Kind.OR) {
			IToken opToken = t;  // Store the operator token
			match(t.kind());  // Match the current 'OR' or 'DOUBLE_OR' token
			Expr right = logicalAndExpr();  // Parse the next LogicalAndExpr
			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the operator
		}
		return left;  // Return the combined or original expression
	}

	private Expr comparisonExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr left = powExpr();  // Parse the first PowExpr

		while (Arrays.asList(Kind.LT, Kind.GT, Kind.EQ, Kind.LE, Kind.GE).contains(t.kind())) {
			IToken opToken = t;  // Store the operator token
			match(t.kind());  // Match the current comparison token
			Expr right = powExpr();  // Parse the next PowExpr
			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the operator
		}
		return left;  // Return the combined or original expression
	}

	private Expr powExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr left = additiveExpr();  // Parse the first AdditiveExpr

		if (t.kind() == Kind.EXP) {  // Check for the '**' token
			IToken opToken = t;  // Store the operator token
			match(Kind.EXP);  // Match the '**' token
			Expr right = powExpr();  // Parse the next PowExpr
			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the '**' operator
		}
		return left;  // Return the combined or original expression
	}

	private Expr additiveExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr left = multiplicativeExpr();  // Parse the first MultiplicativeExpr

		while (t.kind() == Kind.PLUS || t.kind() == Kind.MINUS) {
			IToken opToken = t;  // Store the operator token
			match(t.kind());  // Match the current '+' or '-' token
			Expr right = multiplicativeExpr();  // Parse the next MultiplicativeExpr
			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the operator
		}

		return left;  // Return the combined or original expression
	}

	private Expr multiplicativeExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr left = unaryExpr();  // Parse the first UnaryExpr

		while (t.kind() == Kind.TIMES || t.kind() == Kind.DIV || t.kind() == Kind.MOD) {
			IToken opToken = t;  // Store the operator token
			match(t.kind());  // Match the current '*', '/', or '%' token
			Expr right = unaryExpr();  // Parse the next UnaryExpr
			left = new BinaryExpr(firstToken, left, opToken, right);  // Combine the left and right expressions with the operator
		}
		return left;  // Return the combined or original expression
	}

	private Expr unaryExpr() throws PLCCompilerException {
		IToken firstToken = t;

		if (t.kind() == Kind.BANG || t.kind() == Kind.MINUS ||
				t.kind() == Kind.RES_width || t.kind() == Kind.RES_height) {
			IToken opToken = t;  // Store the operator token

			match(t.kind());  // Match the current operator token

			Expr expression = unaryExpr();  // Parse the next UnaryExpr

			return new UnaryExpr(firstToken, opToken, expression);  // Create a unary expression node with the operator and expression
		} else {
			return postfixExpr();  // If not a unary operator, parse as a PostfixExpr
		}
	}


//	private Expr postfixExpr() throws PLCCompilerException {
//		IToken firstToken = t;
//
//		Expr expression = primaryExpr();  // Parse the PrimaryExpr
//
//		// If the next token is an opening square bracket, it indicates a PixelSelector
//		if (t.kind() == Kind.LSQUARE) {
//			PixelSelector pixelSelector = pixelSelector();  // Parse the PixelSelector
//			expression = new PostfixExpr(firstToken, expression, pixelSelector, null);  // Update the expression with the PixelSelector
//		}
//
//		// If the next token is a colon, it indicates a ChannelSelector
//		if (t.kind() == Kind.COLON) {
//			ChannelSelector channelSelector = channelSelector();  // Parse the ChannelSelector
//			expression = new PostfixExpr(firstToken, expression, null, channelSelector);  // Update the expression with the ChannelSelector
//		}
//
//		return expression;  // Return the updated or original expression
//	}

	private Expr postfixExpr() throws PLCCompilerException {
		IToken firstToken = t;
		Expr expression = primaryExpr();  // Parse the PrimaryExpr
		PixelSelector pixelSelector = null;
		ChannelSelector channelSelector = null;

		// If the next token is an opening square bracket, it indicates a PixelSelector
		if (t.kind() == Kind.LSQUARE) {
			pixelSelector = pixelSelector();  // Parse the PixelSelector
		}

		// If the next token is a colon, it indicates a ChannelSelector
		if (t.kind() == Kind.COLON) {
			channelSelector = channelSelector();  // Parse the ChannelSelector
		}

		if (pixelSelector != null || channelSelector != null) {
			return new PostfixExpr(firstToken, expression, pixelSelector, channelSelector);  // Update the expression with the PixelSelector and/or ChannelSelector
		}

		return expression;  // Return the original expression if no PixelSelector or ChannelSelector was found
	}



	private Expr primaryExpr() throws PLCCompilerException {
		IToken firstToken = t;

        switch (t.kind()) {
            case STRING_LIT -> {
                StringLitExpr stringLit = new StringLitExpr(t);
                match(STRING_LIT);
                return stringLit;
            }
            case NUM_LIT -> {
                NumLitExpr numLit = new NumLitExpr(t);
                match(NUM_LIT);
                return numLit;
            }
            case IDENT -> {
                IdentExpr ident = new IdentExpr(t);
                match(IDENT);
                return ident;
            }
            case LPAREN -> {
                match(LPAREN);
                Expr expression = expr();
                match(RPAREN);
                return expression;
            }
            case CONST -> {
                ConstExpr constExpr = new ConstExpr(t);
                match(CONST);
                return constExpr;
            }
            case LSQUARE -> {
                return expandedPixelExpr();
            }
            default -> throw new SyntaxException(t.sourceLocation(), "Expected token of kind ...");
        }
	}

	private PixelSelector pixelSelector() throws PLCCompilerException {
		IToken firstToken = t;
		match(LSQUARE);
		Expr xExpr = expr();
		match(COMMA);
		Expr yExpr = expr();
		match(RSQUARE);
		return new PixelSelector(firstToken, xExpr, yExpr);
	}

	private ChannelSelector channelSelector() throws PLCCompilerException {
		IToken firstToken = t;
		match(COLON);
		IToken channelToken = t;
		if (channelToken.kind() == RES_red || channelToken.kind() == RES_green || channelToken.kind() == RES_blue) {
			match(channelToken.kind());
			return new ChannelSelector(firstToken, channelToken);
		} else {
			throw new SyntaxException(t.sourceLocation(), "Expected red, green, or blue after colon for ChannelSelector.");
		}
	}

	private ExpandedPixelExpr expandedPixelExpr() throws PLCCompilerException {
		IToken firstToken = t;
		match(LSQUARE);
		Expr e1 = expr();
		match(COMMA);
		Expr e2 = expr();
		match(COMMA);
		Expr e3 = expr();
		match(RSQUARE);
		return new ExpandedPixelExpr(firstToken, e1, e2, e3);
	}


	private Expr expr() throws PLCCompilerException {
		if (t.kind() == Kind.QUESTION) { // This token represents the '?' symbol
			return conditionalExpr();
		} else {
			return logicalOrExpr();
		}
	}
    

}
