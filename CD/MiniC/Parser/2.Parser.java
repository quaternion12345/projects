package MiniC.Parser;


import MiniC.Scanner.Token;
import MiniC.Scanner.SourcePos;
import MiniC.Parser.SyntaxError;
import MiniC.Scanner.Scanner;
import MiniC.ErrorReporter;

public class Parser {

  private Scanner scanner;
  private ErrorReporter errorReporter;
  private Token currentToken;

  public Parser(Scanner lexer, ErrorReporter reporter) {
    scanner = lexer;
    errorReporter = reporter;
  }

  // accept() checks whether the current token matches tokenExpected.
  // If so, it fetches the next token.
  // If not, it reports a syntax error.
  void accept (int tokenExpected) throws SyntaxError {
    if (currentToken.kind == tokenExpected) {
      currentToken = scanner.scan();
    } else {
      syntaxError("\"%\" expected here", Token.spell(tokenExpected));
    }
  }

  // acceptIt() unconditionally accepts the current token
  // and fetches the next token from the scanner.
  void acceptIt() {
    currentToken = scanner.scan();
  }

  void syntaxError(String messageTemplate, String tokenQuoted) throws SyntaxError {
    SourcePos pos = currentToken.GetSourcePos();
    errorReporter.reportError(messageTemplate, tokenQuoted, pos);
    throw(new SyntaxError());
  }

  boolean isTypeSpecifier(int token) {
    if (token == Token.VOID ||
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
  // CFG consists of terminals
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void declarator() throws SyntaxError{ // ID ( [ INTLITERAL ] )?
    accept(Token.ID);
    if(currentToken.kind == Token.LEFTBRACKET){
      acceptIt();
      accept(Token.INTLITERAL);
      accept(Token.RIGHTBRACKET);
    }
  }

  public void typespecifier() throws SyntaxError{ // void | int | bool | float
    if(isTypeSpecifier(currentToken.kind)){
      acceptIt();
    }
    else syntaxError("\"\" expected here", "typespecifier");
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // Boolean Checking Functions
  //
  ///////////////////////////////////////////////////////////////////////////////

  boolean isExpr(int token){ // FIRST(unary-expr) --> ID ( INTLITERAL BOOLLITERAL FLOATLITERAL STRINGLITERAL + - !
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

  ///////////////////////////////////////////////////////////////////////////////
  //
  // toplevel parse() routine:
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void parse() {

    currentToken = scanner.scan(); // get first token from scanner...

    try {
      parseProgram();
      if (currentToken.kind != Token.EOF) {
        syntaxError("\"%\" not expected after end of program",
            currentToken.GetLexeme());
      }
    }
    catch (SyntaxError s) {return; /* to be refined in Assignment 3...*/ }
    return;
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseProgram():
  //
  // program ::= ( (VOID|INT|BOOL|FLOAT) ID ( FunPart | VarPart ) )*
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void parseProgram() throws SyntaxError {
    while (isTypeSpecifier(currentToken.kind)) {
      acceptIt();
      accept(Token.ID);
      if(currentToken.kind == Token.LEFTPAREN) {
        parseFunPart();
      } else {
        parseVarPart();
      }
    }
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseFunPart():
  //
  // FunPart ::= ( "(" ParamsList? ")" CompoundStmt )
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void parseFunPart() throws SyntaxError {
    // We already know that the current token is "(".
    // Otherwise use accept() !
    acceptIt();
    if (isTypeSpecifier(currentToken.kind)) {
      parseParamsList();
    }
    accept(Token.RIGHTPAREN);
    parseCompoundStmt();
  }


  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseParamsList():
  //
  // ParamsList ::= ParamsDecl ( "," ParamsDecl ) *
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void parseParamsList() throws SyntaxError {
    // to be completed by you...
    parseParamsDecl();
    while(currentToken.kind == Token.COMMA){
      acceptIt();
      parseParamsDecl();
    }
  } 
  
  public void parseParamsDecl() throws SyntaxError{ // typespecifier declarator
    typespecifier();
    declarator();
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseCompoundStmt():
  //
  // CompoundStmt ::= "{" VariableDefinition* Stmt* "}"
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void parseCompoundStmt() throws SyntaxError {
    // to be completed by you...
    accept(Token.LEFTBRACE);
    while(isTypeSpecifier(currentToken.kind)){
      acceptIt();
      accept(Token.ID);
      parseVarPart();
    }
    while(currentToken.kind == Token.LEFTBRACE
       || currentToken.kind == Token.IF
       || currentToken.kind == Token.WHILE
       || currentToken.kind == Token.FOR
       || currentToken.kind == Token.RETURN
       || currentToken.kind == Token.ID){
      parseStmt();
    }
    accept(Token.RIGHTBRACE);
  } 

  public void parseStmt() throws SyntaxError{
    if(currentToken.kind == Token.LEFTBRACE){ // compund-stmt
      parseCompoundStmt();
    }
    else if(currentToken.kind == Token.IF){ // if-stmt
      parseIf_stmt();
    }
    else if(currentToken.kind == Token.WHILE){ // while-stmt
      parseWhile_stmt();
    }
    else if(currentToken.kind == Token.FOR){ // for-stmt
      parseFor_stmt();
    }
    else if(currentToken.kind == Token.RETURN){ // return expr? ;
      acceptIt();
      if(isExpr(currentToken.kind)){
	parseExpr();
      }
      accept(Token.SEMICOLON);
    }
    else{ // ID (=expr | [expr]=expr | arglist) ;
      accept(Token.ID);
      if(currentToken.kind == Token.LEFTPAREN){
	parseArglist();
      }
      else if(currentToken.kind == Token.LEFTBRACKET){
	acceptIt();
	parseExpr();
	accept(Token.RIGHTBRACKET);
	accept(Token.ASSIGN);
	parseExpr();
      }
      else{
	accept(Token.ASSIGN);
	parseExpr();
      }
      accept(Token.SEMICOLON);
    }
  }

  public void parseIf_stmt() throws SyntaxError{ // if ( expr ) stmt (else stmt)?
    accept(Token.IF);
    accept(Token.LEFTPAREN);
    parseExpr();
    accept(Token.RIGHTPAREN);
    parseStmt();
    if(currentToken.kind == Token.ELSE){
      acceptIt();
      parseStmt();
    }
  }

  public void parseWhile_stmt() throws SyntaxError{ // while ( expr ) stmt
    accept(Token.WHILE);
    accept(Token.LEFTPAREN);
    parseExpr();
    accept(Token.RIGHTPAREN);
    parseStmt();
  }

  public void parseFor_stmt() throws SyntaxError{ // for ( asgnexpr? ; expr? ; asgnexpr? ) stmt
    accept(Token.FOR);
    accept(Token.LEFTPAREN);
    if(currentToken.kind == Token.ID){
      parseAsgnexpr();
    }
    accept(Token.SEMICOLON);
    if(isExpr(currentToken.kind)){
      parseExpr();
    }
    accept(Token.SEMICOLON);
    if(currentToken.kind == Token.ID){
      parseAsgnexpr();
    }
    accept(Token.RIGHTPAREN);
    parseStmt();
  }
  
  public void parseExpr() throws SyntaxError{ // or-expr
    parseOr_expr();
  }

  public void parseOr_expr() throws SyntaxError{ // and-expr (|| and-expr)*
    parseAnd_expr();
    while(currentToken.kind == Token.OR){
      acceptIt();
      parseAnd_expr();
    }
  }

  public void parseAnd_expr() throws SyntaxError{ // relational-expr (&& relational-expr)*
    parseRelational_expr();
    while(currentToken.kind == Token.AND){
      acceptIt();
      parseRelational_expr();
    }
  }

  public void parseRelational_expr() throws SyntaxError{ // add-expr ((== | != | < | <= | > | >=) add-expr)?
    parseAdd_expr();
    if(currentToken.kind == Token.EQ
    || currentToken.kind == Token.NOTEQ
    || currentToken.kind == Token.LESS
    || currentToken.kind == Token.LESSEQ
    || currentToken.kind == Token.GREATER
    || currentToken.kind == Token.GREATEREQ){
      acceptIt();
      parseAdd_expr();
    }
  }

  public void parseAdd_expr() throws SyntaxError{ // mult-expr ((+|-) mult-expr)*
    parseMult_expr();
    while(currentToken.kind == Token.PLUS || currentToken.kind == Token.MINUS){
      acceptIt();
      parseMult_expr();
    }
  }
  
  public void parseMult_expr() throws SyntaxError{ // unary-expr ((*|/) unary-expr)*
    parseUnary_expr();
    while(currentToken.kind == Token.TIMES || currentToken.kind == Token.DIV){
      acceptIt();
      parseUnary_expr();
    }
  }

  public void parseUnary_expr() throws SyntaxError{ // (+|-|!)* primary-expr 
    while(currentToken.kind == Token.PLUS || currentToken.kind == Token.MINUS || currentToken.kind == Token.NOT){
      acceptIt();
    }
    parsePrimary_expr();
  }

  public void parsePrimary_expr() throws SyntaxError{
    // ID(arglist? | [expr])  |  (expr) | INTLITERAL | BOOLLITERAL | FLOATLITERAL | STRINGLITERAL
    if(currentToken.kind == Token.ID){
      acceptIt();
      if(currentToken.kind == Token.LEFTPAREN) parseArglist();
      else if(currentToken.kind == Token.LEFTBRACKET){
	acceptIt();
	parseExpr();
	accept(Token.RIGHTBRACKET);
      }
    }
    else if(currentToken.kind == Token.LEFTPAREN){
      acceptIt();
      parseExpr();
      accept(Token.RIGHTPAREN);
    }
    else if(currentToken.kind == Token.INTLITERAL) acceptIt();
    else if(currentToken.kind == Token.BOOLLITERAL) acceptIt();
    else if(currentToken.kind == Token.FLOATLITERAL) acceptIt();
    else accept(Token.STRINGLITERAL);
  }

  public void parseAsgnexpr() throws SyntaxError{ // ID = expr
    accept(Token.ID);
    accept(Token.ASSIGN);
    parseExpr();
  }

  public void parseArglist() throws SyntaxError{ // ( args? )
    accept(Token.LEFTPAREN);
    if(isExpr(currentToken.kind)) parseArgs();
    accept(Token.RIGHTPAREN);
  }

  public void parseArgs() throws SyntaxError{ // arg (, arg)* --> expr (, expr)*
    parseExpr();
    while(currentToken.kind == Token.COMMA){
      acceptIt();
      parseExpr();
    }
  }

  ///////////////////////////////////////////////////////////////////////////////
  //
  // parseVarPart():
  //
  // VarPart ::= ( "[" INTLITERAL "]" )?  ( "=" initializer ) ? ( "," init_decl)* ";"
  //
  ///////////////////////////////////////////////////////////////////////////////

  public void parseVarPart() throws SyntaxError {
    // to be completed by you...
    if(currentToken.kind == Token.LEFTBRACKET){
      acceptIt();
      accept(Token.INTLITERAL);
      accept(Token.RIGHTBRACKET);
    }
    if(currentToken.kind == Token.ASSIGN){
      acceptIt();
      parseInitializer();
    }
    while(currentToken.kind == Token.COMMA){
      acceptIt();
      parseInit_decl();
    }
    accept(Token.SEMICOLON);
  }

  // to be completed by you...
  public void parseInit_decl() throws SyntaxError{ // declarator (= initializer)?
    declarator();
    if(currentToken.kind == Token.ASSIGN){
      acceptIt();
      parseInitializer();
    }
  }

  public void parseInitializer() throws SyntaxError{ // expr | { expr (, expr)* }
    if(isExpr(currentToken.kind)){
      parseExpr(); 
    }
    else{
      accept(Token.LEFTBRACE);
      parseExpr();
      while(currentToken.kind == Token.COMMA){
	acceptIt();
	parseExpr();
      }
      accept(Token.RIGHTBRACE);
    }
  }
}
