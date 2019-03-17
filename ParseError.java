import java.io.IOException;

public class ParseError extends Exception {
  private String message;

  public ParseError(String m){
    this.message = m;
    read_till_newline();
  }

  public String getMessage() {
    return "\tParseError:"+message;
  }

  public static void read_till_newline(){
    try{
      while(System.in.read() != '\n');
    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }
  }
}
