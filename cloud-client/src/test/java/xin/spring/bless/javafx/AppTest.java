package xin.spring.bless.javafx;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }


    @Test
    public void aaa(){
        String userDir = System.getProperties().getProperty("user.dir");
        String userHome = System.getProperties().getProperty("user.home");

        System.out.println(userDir);
        System.out.println(userHome);
    }
}
