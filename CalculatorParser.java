import java.io.InputStream;
import java.io.IOException;

public class CalculatorParser {

  private InputStream stream;
  public int next_char;

  public CalculatorParser(InputStream in) throws IOException, ParseError {
    this.stream = in;
    lookahead();
  }

  /**
  *read next char from stream
  */
  private void lookahead() throws IOException, ParseError{
    next_char = stream.read();
    //System.out.print((char)next_char);
    //check that the symbol you read is valid
    if(next_char!='^' && next_char!='&' && next_char!='(' &&next_char!=')' && !is_digit(next_char) && next_char!='\n' && next_char!=-1) {
       throw new ParseError("Invalid symbol: \""+(char)next_char+'"');
    }
  }

  public int Parse() throws IOException, ParseError {
    if(next_char == '\n' || next_char == -1)
      throw new ParseError("empty expr");
    //calculate expr value
    int value = expr();
    //check that expr ended
    if(next_char != '\n' && next_char != -1)
      throw new ParseError("expr didn't end with newline/EOF but with \""+(char)next_char+'"');
    //all good, print expr value
    return value;
  }

  private int expr() throws IOException, ParseError {
    //System.out.print("expr->");
    if(next_char == '('){
      return expr2(term());
    }
    if(is_digit(next_char)){
      return expr2(term());
    }
    throw new ParseError("no rule for expr at \""+(char)next_char+'"');
  }

  private int expr2(int interim) throws IOException, ParseError {
    //System.out.print("expr2->");
    if(next_char == '^'){
      lookahead();  //consume ^
      return expr2(interim ^ term());
    }
    if(next_char == ')'){
      return interim;
    }
    if(next_char != '\n' || next_char != -1){
      return interim;
    }
    throw new ParseError("no rule for expr2 at \""+(char)next_char+'"');
  }

  private int term() throws IOException, ParseError {
    //System.out.print("term->");
    if(next_char == '('){
      return term2(num());
    }
    if(is_digit(next_char)){
      return term2(num());
    }
    throw new ParseError("no rule for term at \""+(char)next_char+'"');
  }

  private int term2(int interim) throws IOException, ParseError {
    //System.out.print("term2->");
    if(next_char == '^'){
      return interim;
    }
    if(next_char == '&'){
      lookahead(); //consume: &
      return term2(interim & num());
    }
    if(next_char == '\n' || next_char != -1)
      return interim;
    throw new ParseError("no rule for term2 at \""+(char)next_char+'"');
  }

  private int num() throws IOException, ParseError {
    if(next_char == '('){
      lookahead(); //consume: (
      int value = expr();
      if(next_char == ')'){
        lookahead(); //consume: )
      }
      else{
        throw new ParseError("expected ) instead of \""+(char)next_char+'"');
      }
      return value;
    }
    if(is_digit(next_char)){
      //System.out.print("num->"+(char)next_char+'\n');
      int temp = next_char;
      lookahead();  //consume: 0|1|2|...|9
      return temp-'0';
    }
    throw new ParseError("no rule for num at \""+(char)next_char+'"');
  }

  public static void main(String[] args){
    //for testing run with java CalculatorParser  test
    if(args.length==1){
      ParserTest test = new ParserTest();
      test.all_tests();
      return;
    }
    //normal execution with System.in
    do{
      System.out.print("\nType expression (or 'q' to exit): ");
      try{
        System.in.mark(1); //mark this point in InputStream
        int c = System.in.read();
        if(c == 'q')
          break;  //q means exit, break loop
        System.in.reset();  //reset to marked point in InputStream
        CalculatorParser parser = new CalculatorParser(System.in);
        System.out.println("\texpr value is: "+parser.Parse());
      }
      catch(ParseError e){
        System.out.println(e.getMessage());
      }
      catch(IOException e){
        System.out.println(e.getMessage());
      }
    }while(true);
  }

  private static boolean is_digit(int i){
    if(i>='0' && i<='9'){
      return true;
    }
    return false;
  }

}
