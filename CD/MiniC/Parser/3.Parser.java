package MiniC.Parser;

import MiniC.Scanner.Scanner;
import MiniC.Scanner.Token;
import MiniC.Scanner.SourcePos;
import MiniC.Parser.SyntaxError;
import MiniC.ErrorReporter;
import MiniC.AstGen.*;


public class Parser {

  private Scanner scanner;
  private ErrorReporter errorReporter;
  private Token currentToken;
  private SourcePos previousTokenPosition;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    scanner = lexer;
    errorReporter = reporter;
  }

  // accept() checks whether the current token matches tokenExpected.
  // If so, it fetches the next token.
  // If not, it reports a syntax error.
  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      previousTokenPosition = currentToken.GetSourcePos();
      currentToken = scanner.scan();
    } else {
      syntaxError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  // acceptIt() unconditionally accepts the current token
  // and fetches the next token from the scanner.
  void acceptIt() {
    previousTokenPosition = currentToken.GetSourcePos();
    currentToken = scanner.scan();
  }

  // start records the position of the start of a phrase.
  // This is defined to be the position of the first
  // character of the first token of the phrase.
  void start(SourcePos pos) {
    pos.StartCol = currentToken.GetSourcePos().StartCol;
    pos.StartLine = currentToken.GetSourcePos().StartLine;
  }

  // finish records the position of the end of a phrase.
  // This is defined to be the position of the last
  // character of the last token of the phrase.
  void finish(SourcePos pos) {
    pos.EndCol = previousTokenPosition.EndCol;
    pos.EndLine = previousTokenPosition.EndLine;
  }

  void syntaxError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePos pos = currentToken.GetSourcePos();
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

  boolean isTypeSpecifier(int token) {
    if(token == Token.VOID ||
        token == Token.INT  ||
        token == Token.BOOL ||
        token == Token.FLOAT) {
      return true;
    } else {
      return false;
    }
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseArrayIndexDecl (Type T):
  //
  // Take [INTLITERAL] and generate an ArrayType
  //
  ///////////////////////////////////////////////////////////////////////////////

  public ArrayType parseArrayIndexDecl(Type T) throws SyntaxError {
    IntLiteral L;
    IntExpr IE;
    accept(Token.LEFTBRACKET);
    SourcePos pos = currentToken.GetSourcePos();
    L = new IntLiteral(currentToken.GetLexeme(), pos);
    accept(Token.INTLITERAL);
    accept(Token.RIGHTBRACKET);
    IE = new IntExpr (L, pos);
    return new ArrayType (T, IE, previousTokenPosition);
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // toplevel parse() routine:
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Program parse() {

    Program ProgramAST = null;

    previousTokenPosition = new SourcePos();
    previousTokenPosition.StartLine = 0;
    previousTokenPosition.StartCol = 0;
    previousTokenPosition.EndLine = 0;
    previousTokenPosition.EndCol = 0;

    currentToken = scanner.scan(); // get first token from scanner...

    try {
      ProgramAST = parseProgram();
      if (currentToken.kind != Token.EOF) {
        syntaxError("\"%\" not expected after end of program",
            currentToken.GetLexeme());
      }
    }
    catch (SyntaxError s) { return null; }
    return ProgramAST;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseProgram():
  //
  // program ::= ( (VOID|INT|BOOL|FLOAT) ID ( FunPart | VarPart ) )* ";"
  //
  ///////////////////////////////////////////////////////////////////////////////

  // parseProgDecls: recursive helper function to facilitate AST construction.
  public Decl parseProgDecls () throws SyntaxError {
    if (! isTypeSpecifier(currentToken.kind)) {
      return new EmptyDecl (previousTokenPosition);
    }
    SourcePos pos = new SourcePos();
    start(pos);
    Type T = parseTypeSpecifier();
    ID Ident = parseID();
    if(currentToken.kind == Token.LEFTPAREN) {
      Decl newD = parseFunPart(T, Ident, pos);
      return new DeclSequence (newD, parseProgDecls(), previousTokenPosition);
    } else {
      DeclSequence Vars = parseVarPart(T, Ident);
      DeclSequence VarsTail = Vars.GetRightmostDeclSequenceNode();
      Decl RemainderDecls = parseProgDecls();
      VarsTail.SetRightSubtree (RemainderDecls);
      return Vars;
    }
  }

  public Program parseProgram() throws SyntaxError {
    SourcePos pos = new SourcePos();
    start(pos);
    Decl D = parseProgDecls();
    finish(pos);
    Program P = new Program (D, pos);
    return P;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseFunPart():
  //
  // FunPart ::= ( "(" ParamsList? ")" CompoundStmt )
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Decl parseFunPart(Type T, ID Ident, SourcePos pos) throws SyntaxError {

    // We already know that the current token is "(".
    // Otherwise use accept() !
    acceptIt();
    Decl PDecl = parseParamsList(); // can also be empty...
    accept(Token.RIGHTPAREN);
    CompoundStmt CStmt = parseCompoundStmt();
    finish(pos);
    return new FunDecl (T, Ident, PDecl, CStmt, pos);
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseParamsList():
  //
  // ParamsList ::= ParameterDecl ( "," ParameterDecl ) *
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Decl parseParamsList() throws SyntaxError {
    if (!isTypeSpecifier(currentToken.kind)) {
      return new EmptyFormalParamDecl(previousTokenPosition);
    }
    Decl Decl_1 = parseParameterDecl();
    Decl Decl_r = new EmptyFormalParamDecl(previousTokenPosition);
    if (currentToken.kind == Token.COMMA) {
      acceptIt();
      Decl_r = parseParamsList();
      if (Decl_r instanceof EmptyFormalParamDecl) {
        syntaxError("Declaration after comma expected", "");
      }
    }
    return new FormalParamDeclSequence (Decl_1, Decl_r, previousTokenPosition);
  } 


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseParameterDecl():
  //
  // ParameterDecl ::= (VOID|INT|BOOL|FLOAT) Declarator
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Decl parseParameterDecl() throws SyntaxError {
    Type T = null;
    Decl D = null;

    SourcePos pos = new SourcePos();
    start(pos);
    if (isTypeSpecifier(currentToken.kind)) {
      T = parseTypeSpecifier();
    } else {
      syntaxError("Type specifier instead of % expected",
          Token.spell(currentToken.kind));
    }
    D = parseDeclarator(T, pos);
    return D;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseDeclarator():
  //
  // Declarator ::= ID ( "[" INTLITERAL "]" )?
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Decl parseDeclarator(Type T, SourcePos pos) throws SyntaxError {
    ID Ident = parseID();
    if (currentToken.kind == Token.LEFTBRACKET) {
      ArrayType ArrT = parseArrayIndexDecl(T);
      finish(pos);
      return new FormalParamDecl (ArrT, Ident, pos);
    }
    finish(pos);
    return new FormalParamDecl (T, Ident, pos);
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseVarPart():
  //
  // VarPart ::= ( "[" INTLITERAL "]" )?  ( "=" initializer ) ? ( "," init_decl)* ";"
  //
  ///////////////////////////////////////////////////////////////////////////////

  public DeclSequence parseVarPart(Type T, ID Ident) throws SyntaxError {
    Type theType = T;
    Decl D;
    DeclSequence Seq = null;
    Expr E = new EmptyExpr(previousTokenPosition);
    if (currentToken.kind == Token.LEFTBRACKET) {
      theType = parseArrayIndexDecl(T);
    }
    if (currentToken.kind == Token.ASSIGN) {
      acceptIt();
      // You can use the following code after you have implemented
      //parseInitializer():
      E = parseInitializer();
    }
    D = new VarDecl (theType, Ident, E, previousTokenPosition);
    // You can use the following code after implementatin of parseInitDecl():
    
       if (currentToken.kind == Token.COMMA) {
       acceptIt();
       Seq = new DeclSequence (D, parseInitDecl(T), previousTokenPosition);
       } else {
       Seq = new DeclSequence (D, new EmptyDecl (previousTokenPosition),
       previousTokenPosition);
       }
     
    accept (Token.SEMICOLON);
    return Seq;
  }
  
  // boolean checking function
  boolean isExpr(int token){
    if(token == Token.ID
    || token == Token.LEFTPAREN
    || token == Token.INTLITERAL
    || token == Token.BOOLLITERAL
    || token == Token.FLOATLITERAL
    || token == Token.STRINGLITERAL
    || token == Token.PLUS
    || token == Token.MINUS
    || token == Token.NOT){
      return true;
    }
    else return false;
  }  

  // expr | { expr (, expr)* }
  public Expr parseInitializer() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    if(isExpr(currentToken.kind)){
      return parseExpr();
    }
    else{
      accept(Token.LEFTBRACE);
      Expr e = parseExpr();
      if(currentToken.kind == Token.COMMA){
	acceptIt();
	ExprSequence ES = new ExprSequence(e, exprsequence(pos), pos);
	accept(Token.RIGHTBRACE);
	return ES;
      }
      else{
	e = new ExprSequence(e, new EmptyExpr(previousTokenPosition), pos);
      }
      accept(Token.RIGHTBRACE);
      finish(pos);
      return e;
    }
  }

  // recursive expr sequence (, expr)*
  public ExprSequence exprsequence(SourcePos pos) throws SyntaxError{
    Expr e = parseExpr();
    if(currentToken.kind == Token.COMMA){
      acceptIt();
      return new ExprSequence(e, exprsequence(pos), pos);
    }
    else{
      return new ExprSequence(e, new EmptyExpr(previousTokenPosition), pos);
    }
  }

  // ID ([ INTLITERAL ])? (= initializer)?
  public Decl parseInitDecl(Type T) throws SyntaxError{
    Decl d = null;
    Type t = T;
    SourcePos pos = new SourcePos();
    start(pos);
    ID i = parseID();      
    if(currentToken.kind == Token.LEFTBRACKET) t = parseArrayIndexDecl(T);
    if(currentToken.kind == Token.ASSIGN){
      acceptIt();
      d = new VarDecl(t, i, parseInitializer(), previousTokenPosition);
    }
    else d = new VarDecl(t, i, new EmptyExpr(previousTokenPosition), previousTokenPosition);
    if(currentToken.kind == Token.COMMA){ // if recursive init-decl
      acceptIt();
      return new DeclSequence(d, parseInitDecl(T), pos);
    }
    else{
      finish(pos);
      return new DeclSequence(d, new EmptyDecl(previousTokenPosition), previousTokenPosition);
    }
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseUnaryExpr():
  //
  // UnaryExpr ::= ("+"|"-"|"!")* PrimaryExpr
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Expr parseUnaryExpr() throws SyntaxError {
    if (currentToken.kind == Token.PLUS ||
        currentToken.kind == Token.MINUS ||
        currentToken.kind == Token.NOT) {
      Operator opAST = new Operator (currentToken.GetLexeme(),
          previousTokenPosition);
      acceptIt();
      return new UnaryExpr (opAST, parseUnaryExpr(), previousTokenPosition);
    }
    return parsePrimaryExpr();
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parsePrimaryExpr():
  //
  // PrimaryExpr ::= ID arglist?
  //              |  ID "[" expr "]"
  //              |  "(" expr ")"
  //              |  INTLITERAL | BOOLLITERAL | FLOATLITERAL | STRINGLITERAL
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Expr parsePrimaryExpr() throws SyntaxError {
    Expr retExpr = null;
    // your code goes here...
    if(currentToken.kind == Token.ID){
      ID i = parseID();
      retExpr = new VarExpr(i, previousTokenPosition);
      if(currentToken.kind == Token.LEFTPAREN){
	Expr e = parseArgList();
	retExpr = new CallExpr(i, e, previousTokenPosition);
      }
      else if(currentToken.kind == Token.LEFTBRACKET){
	acceptIt();
	Expr e = parseExpr();
	accept(Token.RIGHTBRACKET);
	retExpr = new ArrayExpr(retExpr, e, previousTokenPosition);
      }
    }
    else if(currentToken.kind == Token.LEFTPAREN){
      acceptIt();
      retExpr = parseExpr();
      accept(Token.RIGHTPAREN);
    }
    else if(currentToken.kind == Token.INTLITERAL){
      retExpr = new IntExpr(new IntLiteral(currentToken.GetLexeme(), previousTokenPosition), previousTokenPosition);
      acceptIt();
    }
    else if(currentToken.kind == Token.BOOLLITERAL){
      retExpr = new BoolExpr(new BoolLiteral(currentToken.GetLexeme(), previousTokenPosition), previousTokenPosition);
      acceptIt();
    }
    else if(currentToken.kind == Token.FLOATLITERAL){
      retExpr = new FloatExpr(new FloatLiteral(currentToken.GetLexeme(), previousTokenPosition), previousTokenPosition);
      acceptIt();
    }
    else{
      retExpr = new StringExpr(new StringLiteral(currentToken.GetLexeme(), previousTokenPosition), previousTokenPosition);
      accept(Token.STRINGLITERAL);
    }
    return retExpr;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseCompoundStmt():
  //
  // CompoundStmt ::= "{" VariableDef* Stmt* "}"
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Decl parseCompoundDecls () throws SyntaxError {
    if (!isTypeSpecifier(currentToken.kind)) {
      return new EmptyDecl (previousTokenPosition);
    }
    Type T = parseTypeSpecifier();
    ID Ident = parseID();
    DeclSequence Vars = parseVarPart(T, Ident);
    DeclSequence VarsTail = Vars.GetRightmostDeclSequenceNode();
    Decl RemainderDecls = parseCompoundDecls();
    VarsTail.SetRightSubtree (RemainderDecls);
    return Vars;       
  }

  public Stmt parseCompoundStmts () throws SyntaxError {
    if (! (currentToken.kind == Token.LEFTBRACE ||
          currentToken.kind == Token.IF ||
          currentToken.kind == Token.WHILE ||
          currentToken.kind == Token.FOR ||
          currentToken.kind == Token.RETURN ||
          currentToken.kind == Token.ID)
       ) {
      return new EmptyStmt(previousTokenPosition);
    }
    Stmt S = null;
    // You can use the following code after implementation of parseStmt():
    S = parseStmt();
    return new StmtSequence (S, parseCompoundStmts(), previousTokenPosition);
  }

  public CompoundStmt parseCompoundStmt() throws SyntaxError {
    SourcePos pos = new SourcePos();
    start(pos);
    accept(Token.LEFTBRACE);
    Decl D = parseCompoundDecls();
    Stmt S = parseCompoundStmts();
    accept(Token.RIGHTBRACE);
    finish(pos);
    if ( (D.getClass() == EmptyDecl.class) &&
        (S.getClass() == EmptyStmt.class)) {
      return new EmptyCompoundStmt (previousTokenPosition);
    } else {
      return new CompoundStmt (D, S, pos);
    }
  }

  // compound-stmt | if-stmt | while-stmt | for-stmt | return expr? ; | ID (=expr | [expr]=expr | arglist) ;
  public Stmt parseStmt() throws SyntaxError{
    Stmt s = null;
    Expr e = null;
    if(currentToken.kind == Token.LEFTBRACE){
      s = parseCompoundStmt();
    }
    else if(currentToken.kind == Token.IF){
      s = parseIf_stmt();
    }
    else if(currentToken.kind == Token.WHILE){
      s = parseWhile_stmt();
    }
    else if(currentToken.kind == Token.FOR){
      s = parseFor_stmt();
    }
    else if(currentToken.kind == Token.RETURN){
      acceptIt();
      if(isExpr(currentToken.kind)){
	e = parseExpr();
	s = new ReturnStmt(e, previousTokenPosition);
      }
      else s = new ReturnStmt(new EmptyExpr(previousTokenPosition), previousTokenPosition);
      accept(Token.SEMICOLON);
    }
    else{
      ID i = parseID();
      Expr id = new VarExpr(i, previousTokenPosition);
      Expr e1 = null;
      Expr e2 = null;
      if(currentToken.kind == Token.LEFTPAREN){
 	e1 = parseArgList();
	s = new CallStmt(new CallExpr(i, e1, previousTokenPosition), previousTokenPosition);
      }
      else if(currentToken.kind == Token.LEFTBRACKET){
	acceptIt();
	e1 = parseExpr();
	accept(Token.RIGHTBRACKET);
	id = new ArrayExpr(id, e1, previousTokenPosition);
	accept(Token.ASSIGN);
	e2 = parseExpr();
	s = new AssignStmt(id, e2, previousTokenPosition);
      }
      else{
	accept(Token.ASSIGN);
	e1 = parseExpr();
	s = new AssignStmt(id, e1, previousTokenPosition);	
      }
      accept(Token.SEMICOLON);
    }
    return s;
  }

  // if (expr) stmt (else stmt)?
  public Stmt parseIf_stmt() throws SyntaxError{
    Stmt s = null;
    Expr e = null;
    Stmt s1 = null;
    Stmt s2 = null;
    accept(Token.IF);
    accept(Token.LEFTPAREN);
    e = parseExpr();
    accept(Token.RIGHTPAREN);
    s1 = parseStmt();
    if(currentToken.kind == Token.ELSE){
      acceptIt();
      s2 = parseStmt();
      s = new IfStmt(e, s1, s2, previousTokenPosition);
    }
    else{
      s = new IfStmt(e, s1, previousTokenPosition); 
    }
    return s;
  }

  // while (expr) stmt
  public Stmt parseWhile_stmt() throws SyntaxError{
    Stmt s = null;
    Expr e = null;
    accept(Token.WHILE);
    accept(Token.LEFTPAREN);
    e = parseExpr();
    accept(Token.RIGHTPAREN);
    s = parseStmt();
    return new WhileStmt(e, s, previousTokenPosition);
  }

  // for (asgnexpr? ; expr? ; asgnexpr? ) stmt
  public Stmt parseFor_stmt() throws SyntaxError{
    Stmt s = null;
    Expr e1 = null;
    Expr e2 = null;
    Expr e3 = null;
    accept(Token.FOR);
    accept(Token.LEFTPAREN);
    if(currentToken.kind == Token.ID){
      e1 = parseAsgnexpr();
    }
    else e1 = new EmptyExpr(previousTokenPosition);
    accept(Token.SEMICOLON);
    if(isExpr(currentToken.kind)){
      e2 = parseExpr();
    }
    else e2 = new EmptyExpr(previousTokenPosition);
    accept(Token.SEMICOLON);
    if(currentToken.kind == Token.ID){
      e3 = parseAsgnexpr();
    }
    else e3 = new EmptyExpr(previousTokenPosition);
    accept(Token.RIGHTPAREN);
    s = parseStmt();
    return new ForStmt(e1, e2, e3, s, previousTokenPosition);
  }

  // ID = expr
  public Expr parseAsgnexpr() throws SyntaxError{
    ID i = parseID();
    Expr e1 = new VarExpr(i, previousTokenPosition);
    accept(Token.ASSIGN);
    Expr e2 = parseExpr();
    return new AssignExpr(e1, e2, previousTokenPosition);
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseArgList():
  //
  // ArgList ::= "(" ( arg ( "," arg )* )? ")"
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Expr parseArgs() throws SyntaxError {
    if (currentToken.kind == Token.RIGHTPAREN) {
      return new  EmptyActualParam (previousTokenPosition);
    } 
    Expr Params = null;
    /*
     * You can use the following code after you have implemented parseExpr() aso.:
     *
     */
     Params = new ActualParam (parseExpr(), previousTokenPosition);
     if (currentToken.kind == Token.COMMA) {
     acceptIt();
     }
     
    return new ActualParamSequence (Params, parseArgs(), previousTokenPosition);
  }

  public Expr parseArgList() throws SyntaxError {
    accept(Token.LEFTPAREN);
    Expr Params = parseArgs();
    accept(Token.RIGHTPAREN);
    return Params;
  }

  // or-expr
  public Expr parseExpr() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    Expr e = parseOr_expr();
    finish(pos);
    return e;
  }

  // and-expr (|| and-expr)*
  public Expr parseOr_expr() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    Expr e = parseAnd_expr();
    while(currentToken.kind == Token.OR){
      Operator o = new Operator(currentToken.GetLexeme(), previousTokenPosition);
      acceptIt();
      e = new BinaryExpr(e, o, parseAnd_expr(), pos);
    }
    finish(pos);
    return e;
  }

  // relational-expr (&& relational-expr)*
  public Expr parseAnd_expr() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    Expr e = parseRelational_expr();
    while(currentToken.kind == Token.AND){
      Operator o = new Operator(currentToken.GetLexeme(), previousTokenPosition);
      acceptIt();
      e = new BinaryExpr(e, o, parseRelational_expr(), pos);
    }
    finish(pos);
    return e;
  }

  // add-expr ((== | != | < | <= | > | >=) add-expr)?
  public Expr parseRelational_expr() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    Expr e = parseAdd_expr();
    if(currentToken.kind == Token.EQ
    || currentToken.kind == Token.NOTEQ
    || currentToken.kind == Token.LESS
    || currentToken.kind == Token.LESSEQ
    || currentToken.kind == Token.GREATER
    || currentToken.kind == Token.GREATEREQ){
      Operator o = new Operator(currentToken.GetLexeme(), previousTokenPosition);
      acceptIt();
      e = new BinaryExpr(e, o, parseAdd_expr(), pos);
    }
    finish(pos);
    return e;
  }

  // mult-expr ((+ | -) mult-expr)*
  public Expr parseAdd_expr() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    Expr e = parseMult_expr();
    while(currentToken.kind == Token.PLUS || currentToken.kind == Token.MINUS){
      Operator o = new Operator(currentToken.GetLexeme(), previousTokenPosition);
      acceptIt();
      e = new BinaryExpr(e, o, parseMult_expr(), pos);
    }
    finish(pos);
    return e;
  }

  // unary-expr ((* | /) unary-expr)*
  public Expr parseMult_expr() throws SyntaxError{
    SourcePos pos = new SourcePos();
    start(pos);
    Expr e = parseUnaryExpr();
    while(currentToken.kind == Token.TIMES || currentToken.kind == Token.DIV){
      Operator o = new Operator(currentToken.GetLexeme(), previousTokenPosition);
      acceptIt();
      e = new BinaryExpr(e, o, parseUnaryExpr(), pos);
    }
    finish(pos);
    return e;
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseID():
  //
  // ID (terminal)
  //
  ///////////////////////////////////////////////////////////////////////////////

  public ID parseID() throws SyntaxError {
    ID Ident = new ID(currentToken.GetLexeme(), currentToken.GetSourcePos());
    accept(Token.ID);
    return Ident;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseTypeSpecifier():
  //
  // VOID | INT | FLOAT | BOOL (all terminals)
  //
  ///////////////////////////////////////////////////////////////////////////////

  public Type parseTypeSpecifier() throws SyntaxError {
    Type T = null;
    switch (currentToken.kind) {
      case Token.INT:
        T = new IntType(currentToken.GetSourcePos());
        break;
      case Token.FLOAT:
        T = new FloatType(currentToken.GetSourcePos());
        break;
      case Token.BOOL:
        T = new BoolType(currentToken.GetSourcePos());
        break;
      case Token.VOID:
        T = new VoidType(currentToken.GetSourcePos());
        break;
      default:
        syntaxError("Type specifier expected", "");
    }
    acceptIt();
    return T;
  }

}
