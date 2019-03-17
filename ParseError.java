import java.io.IOException;

public class ParseError extends Exception {
  private String message;

  public ParseError(String m) throws IOException{
    this.message = m;
    read_till_newline();
  }

  public String getMessage() {
    return "\tParseError:"+message;
  }

  public static void read_till_newline() throws IOException{
    if(System.in.available()>0)
      while(System.in.read() != '\n');
  }
}
