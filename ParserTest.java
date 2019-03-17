import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

public class ParserTest {

  public ParserTest(){
  }

  public void test(int i, String test_expr, int expr_value) {
    try{
      //convert string to InputStream
      InputStream stream = new ByteArrayInputStream(test_expr.getBytes(StandardCharsets.UTF_8));
      CalculatorParser parser = new CalculatorParser(stream);
      //parse string
      int ret_value = parser.Parse();
      //test check
      if(ret_value != expr_value){
        System.out.print("Test"+i+" failed. Got: "+ret_value+" for "+expr_value+'='+test_expr);
      }
      else{
        System.out.println("Test"+i+" success. Got: "+ret_value+" for "+expr_value+'='+test_expr);
      }
    }
    catch(IOException e){
      System.out.println(e.getMessage());
    }
    catch(ParseError e){
      System.out.println(e.getMessage());
    }
  }

  public void all_tests(){
    //this.test(0,"0\n",1); //this test will always fail
    this.test(1,"1^1\n",1^1);
    this.test(2,"1&0\n",1&0);
    this.test(3,"1&1\n",1&1);
    this.test(4,"1&0\n",1&0);
    this.test(5,"1^1&1^0\n",1^1&1^0);
    this.test(6,"1^0&1^0\n",1^0&1^0);
    this.test(7,"1^1&1^0\n",1^1&1^0);
    this.test(8,"(1^1&1^0)\n",(1^1&1^0));
    this.test(9,"1^(1&1)^0\n",1^(1&1)^0);
    this.test(10,"1^(1&1^0)\n",1^(1&1^0));
    this.test(11,"(1^(1&1))^0\n",(1^(1&1))^0);

    System.out.println("---THE FOLLOWING TESTS SHOULD ALL FAIL DUE TO ParseError-----");
  //  this.test_parse(5,"11&1^0\n",1^1&1^0);
  }
}
