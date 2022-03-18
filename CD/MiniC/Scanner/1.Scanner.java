package MiniC.Scanner;

import MiniC.Scanner.SourceFile;
import MiniC.Scanner.Token;

public final class Scanner {

  private SourceFile sourceFile;

  private char currentChar;
  private boolean verbose;
  private StringBuffer currentLexeme;
  private boolean currentlyScanningToken;
  private int currentLineNr;
  private int currentColNr;
  private int numTokens = 0;
  private Token t1, t2, t3;
  private boolean isDigit(char c) {
    return (c >= '0' && c <= '9');
  }
  private boolean isAlpha(char c) {
    return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
  }

///////////////////////////////////////////////////////////////////////////////

  public Scanner(SourceFile source) {
    sourceFile = source;
    currentChar = sourceFile.readChar();
    verbose = false;
    currentLineNr = 1;
    currentColNr= 1;
  }

  public void enableDebugging() {
    verbose = true;
  }

  // takeIt appends the current character to the current token, and gets
  // the next character from the source program (or the to-be-implemented
  // "untake" buffer in case of look-ahead characters that got 'pushed back'
  // into the input stream).

  private void takeIt() {
    if(currentChar == '\n'){
      currentLineNr++;
      currentColNr = 0;
    }
    if (currentlyScanningToken)
    {
      currentLexeme.append(currentChar);
    }
    currentChar = sourceFile.readChar();
    currentColNr++;
  }

  private void identifier(){
    while(isAlpha(currentChar) || isDigit(currentChar) || currentChar == '_') takeIt();
  }

  private int scanToken(SourcePos pos) {
    if(numTokens > 0) return -1; // need to process saved Tokens
    pos.StartLine = currentLineNr;
    pos.StartCol = currentColNr;
    // look-ahead buffer
    StringBuffer tempLexeme;
    SourcePos tempPos;
    
    switch (currentChar) {

    case '0':  case '1':  case '2':  case '3':  case '4':
    case '5':  case '6':  case '7':  case '8':  case '9':
      // prepare for look-ahead
      tempLexeme = new StringBuffer("");
      tempPos = new SourcePos();
      tempPos.StartLine = currentLineNr;
      tempPos.StartCol = currentColNr;
      tempPos.EndLine = currentLineNr;

      tempLexeme.append(currentChar);
      takeIt();
      while (isDigit(currentChar)) {
        tempLexeme.append(currentChar);
        takeIt();
      }
      //tempPos.EndCol = currentColNr - 1; // digit
      if(currentChar == '.'){ // digit.
	tempLexeme.append(currentChar);
	takeIt();
	if(isDigit(currentChar)){ // digit.digit
	  while(isDigit(currentChar)){
	    tempLexeme.append(currentChar);
	    takeIt();
	  }
	  tempPos.EndCol = currentColNr - 1; // digit.digit
	  if(currentChar == 'e' || currentChar == 'E'){ // digit.digit(e|E)
	    char tempChar1 = currentChar;
	    takeIt();
	    if(currentChar == '+' || currentChar == '-'){ // digit.digit(e|E)(+|-)
	      char tempChar2 = currentChar;
	      takeIt();
	      if(isDigit(currentChar)){ // digit.digit(e|E)(+|-)digit
		while(isDigit(currentChar)) takeIt();
		return Token.FLOATLITERAL;
	      }
	      else{ // digit.digit ID Operator
		numTokens = 3;
		t1 = new Token(Token.FLOATLITERAL, tempLexeme.toString(), tempPos);

		SourcePos p2 = new SourcePos();
		p2.StartLine = tempPos.StartLine;
		p2.StartCol = tempPos.EndCol + 1;
		p2.EndLine = tempPos.EndLine;
		p2.EndCol = tempPos.EndCol + 1;
		t2 = new Token(Token.ID, Character.toString(tempChar1), p2);

		SourcePos p3 = new SourcePos();
		p3.StartLine = tempPos.StartLine;
		p3.StartCol = tempPos.EndCol + 2;
		p3.EndLine = tempPos.EndLine;
		p3.EndCol = tempPos.EndCol + 2;
		if(tempChar2 == '+') t3 = new Token(Token.PLUS, "+", p3);
		else t3 = new Token(Token.MINUS, "-", p3);

		return -1;
	      }
	    }
	    else if(isDigit(currentChar)){ // digit.digit(e|E)digit
	      while(isDigit(currentChar)) takeIt();
	      return Token.FLOATLITERAL;
	    }
	    else{ // digit.digit ID
	      numTokens = 2;
	      t1 = new Token(Token.FLOATLITERAL, tempLexeme.toString(), tempPos);

	      currentLexeme = new StringBuffer("");
	      currentLexeme.append(tempChar1);
	      identifier();
	      SourcePos p2 = new SourcePos();
	      p2.StartLine = tempPos.StartLine;
	      p2.StartCol = tempPos.EndCol + 1;
	      p2.EndLine = tempPos.EndLine;
	      p2.EndCol = currentColNr - 1;
	      if(currentLexeme.toString().equals("else")) t2 = new Token(Token.ELSE, currentLexeme.toString(), p2);
	      else t2 = new Token(Token.ID, currentLexeme.toString(), p2);

	      return -1;
	    }
	  }
	  else return Token.FLOATLITERAL;
	}
	else if(currentChar == 'e' || currentChar == 'E'){ // digit.(e|E)
	  char tempChar1 = currentChar;
	  takeIt();
	  if(currentChar == '+' || currentChar == '-'){ // digit.(e|E)(+|-)
	    char tempChar2 = currentChar;
	    takeIt();
	    if(isDigit(currentChar)){ // digit.(e|E)(+|-)digit
	      while(isDigit(currentChar)) takeIt();
	      return Token.FLOATLITERAL;
	    }
	    else{ // digit. ID Operator
	      numTokens = 3;
	      t1 = new Token(Token.FLOATLITERAL, tempLexeme.toString(), tempPos); // digit.

	      SourcePos p2 = new SourcePos();
	      p2.StartLine = tempPos.StartLine;
	      p2.StartCol = tempPos.EndCol + 1;
	      p2.EndLine = tempPos.EndLine;
	      p2.EndCol = tempPos.EndCol + 1;
	      t2 = new Token(Token.ID, Character.toString(tempChar1), p2);

	      SourcePos p3 = new SourcePos();
	      p3.StartLine = tempPos.StartLine;
	      p3.StartCol = tempPos.EndCol + 2;
	      p3.EndLine = tempPos.EndLine;
	      p3.EndCol = tempPos.EndCol + 2;
	      if(tempChar2 == '+') t3 = new Token(Token.PLUS, "+", p3);
	      else t3 = new Token(Token.MINUS, "-", p3);
	     
	      return -1;
	    }
	  }
	  else if(isDigit(currentChar)){ // digit.(e|E)digit
	    takeIt();
	    while(isDigit(currentChar)) takeIt();
	    return Token.FLOATLITERAL;
	  }
	  else{ // digit. ID
	    numTokens = 2;
	    t1 = new Token(Token.FLOATLITERAL, tempLexeme.toString(), tempPos);

	    currentLexeme = new StringBuffer("");
	    currentLexeme.append(tempChar1);
	    identifier();
	    SourcePos p2 = new SourcePos();
	    p2.StartLine = tempPos.StartLine;
	    p2.StartCol = tempPos.EndCol + 1;
	    p2.EndLine = tempPos.EndLine;
	    p2.EndCol = currentColNr - 1;
	    if(currentLexeme.toString().equals("else")) t2 = new Token(Token.ELSE, currentLexeme.toString(), p2);
	    else t2 = new Token(Token.ID, currentLexeme.toString(), p2);
	   
	    return -1;
	  }
	}
	else{ // digit.
	  return Token.FLOATLITERAL;
	}
      }
      else if(currentChar == 'e' || currentChar == 'E'){
	char tempChar1 = currentChar;
	takeIt();
	if(currentChar == '+' || currentChar == '-'){ // digit (e|E) (+|-)
	  char tempChar2 = currentChar;
	  takeIt();
	  if(isDigit(currentChar)){ // digit (e|E) (+|-) digit
	    while(isDigit(currentChar)) takeIt();
	    return Token.FLOATLITERAL;
	  }
	  else{ // digit ID Operator
	    numTokens = 3;
	    t1 = new Token(Token.INTLITERAL, tempLexeme.toString(), tempPos); // digit

	    SourcePos p2 = new SourcePos();
	    p2.StartLine = tempPos.StartLine;
	    p2.StartCol = tempPos.EndCol + 1;
	    p2.EndLine = tempPos.EndLine;
	    p2.EndCol = tempPos.EndCol + 1;
	    t2 = new Token(Token.ID, Character.toString(tempChar1), p2);

	    SourcePos p3 = new SourcePos();
	    p3.StartLine = tempPos.StartLine;
	    p3.StartCol = tempPos.EndCol + 2;
	    p3.EndLine = tempPos.EndLine;
	    p3.EndCol = tempPos.EndCol + 2;
	    if(tempChar2 == '+') t3 = new Token(Token.PLUS, "+", p3);
	    else t3 = new Token(Token.MINUS, "-", p3);

	    return -1;
	  }
	}
	else if(isDigit(currentChar)){ // digit (e|E) digit
	  takeIt();
	  while(isDigit(currentChar)) takeIt();
	  return Token.FLOATLITERAL;
	}
	else{ // digit ID
	  numTokens = 2;
	  t1 = new Token(Token.INTLITERAL, tempLexeme.toString(), tempPos);

	  currentLexeme = new StringBuffer("");
	  currentLexeme.append(tempChar1);
	  identifier();
	  SourcePos p2 = new SourcePos();
	  p2.StartLine = tempPos.StartLine;
	  p2.StartCol = tempPos.EndCol + 1;
	  p2.EndLine = tempPos.EndLine;
	  p2.EndCol = currentColNr - 1;
	  if(currentLexeme.toString().equals("else")) t2 = new Token(Token.ELSE, currentLexeme.toString(), p2);
	  else t2 = new Token(Token.ID, currentLexeme.toString(), p2);
	 
	  return -1;
	}
      }
      // Note: code for floating point literals is missing here...
      else return Token.INTLITERAL;
    // FLOAT start with .
    case '.':
	// prepare for look-ahead
	tempLexeme = new StringBuffer("");
	tempPos = new SourcePos();
	tempPos.StartLine = currentLineNr;
	tempPos.StartCol = currentColNr;
	tempPos.EndLine = currentLineNr;

	tempLexeme.append(currentChar);
	takeIt();
	if(isDigit(currentChar)){ // Token starts with .digit ?
	  tempLexeme.append(currentChar);
	  takeIt();
	  if(isDigit(currentChar)){ // digit*
	    while(isDigit(currentChar)){
	      tempLexeme.append(currentChar);
	      takeIt();
	    }
	    tempPos.EndCol = currentColNr - 1; // Token .digit
	  }
	  if(currentChar == 'e' || currentChar == 'E'){ // .digit(e|E)
	    char tempChar1 = currentChar;
	    takeIt();
	    if(currentChar == '+' || currentChar == '-'){ // .digit(e|E)(+|-)
	      char tempChar2 = currentChar;
	      takeIt();
	      if(isDigit(currentChar)){ // .digit(e|E)(+|-)digit
		while(isDigit(currentChar)) takeIt();
	      }
	      else{ // .digit ID Operator
		// need to make 3 tokens
		numTokens = 3;
		t1 = new Token(Token.FLOATLITERAL, tempLexeme.toString(), tempPos); // .digit

		SourcePos p2 = new SourcePos();
		p2.StartLine = tempPos.StartLine;
		p2.StartCol = tempPos.StartCol + 1;
		p2.EndLine = tempPos.EndLine;
		p2.EndCol = tempPos.EndCol + 1;
		t2 = new Token(Token.ID, Character.toString(tempChar1), p2);		

		SourcePos p3 = new SourcePos();
		p3.StartLine = tempPos.StartLine;
		p3.StartCol = tempPos.EndCol + 2;
		p3.EndLine = tempPos.EndLine;
		p3.EndCol = tempPos.EndCol + 2;
		if(tempChar2 == '+') t3 = new Token(Token.PLUS, "+", p3);
		else t3 = new Token(Token.MINUS, "-", p3);

		return -1;
	      }
	    }
	    else if(isDigit(currentChar)){ // .digit(e|E)digit
	      takeIt();
	      while(isDigit(currentChar)) takeIt();
	    }
	    else{ // .digit ID
	      numTokens = 2;
	      t1 = new Token(Token.FLOATLITERAL, tempLexeme.toString(), tempPos);

	      currentLexeme = new StringBuffer("");
	      currentLexeme.append(tempChar1);
	      identifier();
	      SourcePos p2 = new SourcePos();
	      p2.StartLine = tempPos.StartLine;
	      p2.StartCol = tempPos.EndCol + 1;
	      p2.EndLine = tempPos.EndLine;
	      p2.EndCol = currentColNr - 1;
	      if(currentLexeme.toString().equals("else")) t2 = new Token(Token.ELSE, currentLexeme.toString(), p2);
	      else t2 = new Token(Token.ID, currentLexeme.toString(), p2);
	     
	      return -1;
	    }
	  }
	  return Token.FLOATLITERAL;
	}
	else return Token.ERROR; // invalid token
    // Arithmetic Operators +, -, *, /
    case '+':
        takeIt();
        return Token.PLUS;
    case '-':
	takeIt();
	return Token.MINUS;
    case '*':
	takeIt();
	return Token.TIMES;
    case '/': // Division and comments
	takeIt();
	if(currentChar == '/'){
	  takeIt();
	  while(currentChar != '\n') takeIt();
	  while(currentChar == ' '
	  	|| currentChar == '\f'
	  	|| currentChar == '\n'
	  	|| currentChar == '\r'
	  	|| currentChar == '\t')
	  {
	  	takeIt();
	  }
	  currentLexeme = new StringBuffer(""); // clear
	  return scanToken(pos);
	}
	else if(currentChar == '*'){
	  takeIt();
	  while(true){
	    while(currentChar != '*'){
	      if(currentChar == '\u0000'){ // un-terminated comment
	        System.out.println("ERROR: unterminated multi-line comment.");
		currentLexeme = new StringBuffer(""); // clear
		return scanToken(pos);
	      }
	      takeIt();
	    }
	    takeIt();
	    if(currentChar == '/'){
	      takeIt();
	      break;
	    }
	  }
	  while(currentChar == ' '
	  	|| currentChar == '\f'
	  	|| currentChar == '\n'
	  	|| currentChar == '\r'
	  	|| currentChar == '\t')
	  {
	  	takeIt();
	  }
	  currentLexeme = new StringBuffer(""); // clear
	  return scanToken(pos);
	}
	else return Token.DIV;

    // Relational Operators <, <=, >, >=, =, ==
    case '<':
	takeIt();
	if(currentChar == '='){
	  takeIt();
	  return Token.LESSEQ;
	}
	else return Token.LESS;
    case '>':
	takeIt();
	if(currentChar == '='){
	  takeIt();
	  return Token.GREATEREQ;
	}
	else return Token.GREATER;
    case '=':
	takeIt();
	if(currentChar == '='){
	  takeIt();
	  return Token.EQ;
	}
	else return Token.ASSIGN;

    // Logical Operators &&, ||, ! and !=
    case '&':
	takeIt();
	if(currentChar == '&'){
	  takeIt();
	  return Token.AND;
	}
	else return Token.ERROR;
    case '|':
	takeIt();
	if(currentChar == '|'){
	  takeIt();
	  return Token.OR;
	}
	else return Token.ERROR;
    case '!':
	takeIt();
	if(currentChar == '='){
	  takeIt();
	  return Token.NOTEQ;
	}
	else return Token.NOT;

    // Seperators
    case '{':
	takeIt();
	return Token.LEFTBRACE;
    case '}':
	takeIt();
	return Token.RIGHTBRACE;
    case '[':
	takeIt();
	return Token.LEFTBRACKET;
    case ']':
	takeIt();
	return Token.RIGHTBRACKET;
    case '(':
	takeIt();
	return Token.LEFTPAREN;
    case ')':
	takeIt();
	return Token.RIGHTPAREN;
    case ',':
	takeIt();
	return Token.COMMA;
    case ';':
	takeIt();
	return Token.SEMICOLON;
    // String Literals
    case '"':
	takeIt();
	currentLexeme = new StringBuffer(""); // clear
	while(currentChar != '"'){
	  if(currentChar == '\u0000'){ // un-terminated string with eof
	    System.out.println("ERROR: unterminated string literal");
	    return Token.STRINGLITERAL;
	  }
	  else if(currentChar == '\n'){ // un-terminated string with new line
	    System.out.println("ERROR: unterminated string literal");
	    return Token.STRINGLITERAL;
	  }
	  else if(currentChar == '\\'){ // illegal escape sequence
	    takeIt();
	    if(currentChar != 'n') System.out.println("ERROR: illegal escape sequence");
	  }
	  takeIt();
	}
	takeIt();
	currentLexeme.deleteCharAt(currentLexeme.length()-1);
	return Token.STRINGLITERAL;

    // Boolean Literals & Keywords & Identifiers
    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g':
    case 'h': case 'i': case 'j': case 'k': case 'l': case 'm': case 'n': 
    case 'o': case 'p': case 'q': case 'r': case 's': case 't': case 'u': 
    case 'v': case 'w': case 'x': case 'y': case 'z': case '_':
    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F': case 'G':
    case 'H': case 'I': case 'J': case 'K': case 'L': case 'M': case 'N':
    case 'O': case 'P': case 'Q': case 'R': case 'S': case 'T': case 'U':
    case 'V': case 'W': case 'X': case 'Y': case 'Z':
	identifier();
	// Boolean Literals
	if(currentLexeme.toString().equals("true")) return Token.BOOLLITERAL;
	else if(currentLexeme.toString().equals("false")) return Token.BOOLLITERAL;
	// Keywords
	else if(currentLexeme.toString().equals("bool")) return Token.BOOL;
	else if(currentLexeme.toString().equals("else")) return Token.ELSE;
	else if(currentLexeme.toString().equals("for")) return Token.FOR;
	else if(currentLexeme.toString().equals("float")) return Token.FLOAT;
	else if(currentLexeme.toString().equals("if")) return Token.IF;
	else if(currentLexeme.toString().equals("int")) return Token.INT;
	else if(currentLexeme.toString().equals("return")) return Token.RETURN;
	else if(currentLexeme.toString().equals("void")) return Token.VOID;
	else if(currentLexeme.toString().equals("while")) return Token.WHILE;
	// Identifiers
	else return Token.ID;

    case '\u0000': // sourceFile.eot:
      currentLexeme.append('$');
      currentColNr++;
      return Token.EOF;
    // Add code here for the remaining MiniC tokens...

    default:
      takeIt();
      return Token.ERROR;
    }
  }

  public Token scan() {
    Token currentToken;
    SourcePos pos;
    int kind;

    currentlyScanningToken = false;
    while (currentChar == ' '
           || currentChar == '\f'
           || currentChar == '\n'
           || currentChar == '\r'
           || currentChar == '\t')
    {
      takeIt();
    } 

    currentlyScanningToken = true;
    currentLexeme = new StringBuffer("");
    pos = new SourcePos();
    // Note: currentLineNr and currentColNr are not maintained yet!
    //pos.StartLine = currentLineNr;
    pos.EndLine = currentLineNr;
    //pos.StartCol = currentColNr;
    kind = scanToken(pos);
    if(numTokens == 3){
      currentToken = t1;
      t1 = t2;
      t2 = t3;
      numTokens--;
    }
    else if(numTokens == 2){
      currentToken = t1;
      t1 = t2;
      numTokens--;
    }
    else if(numTokens == 1){
      currentToken = t1;
      numTokens--;
    }
    else{ // normal case
      currentToken = new Token(kind, currentLexeme.toString(), pos);
      pos.StartLine = currentLineNr;
      pos.EndCol = currentColNr-1;
      pos.EndLine = currentLineNr;
    }
    if (verbose)
      currentToken.print();
    return currentToken;
  }

}
