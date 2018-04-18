package NettyHttp.NettyHttp;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	HttpServer hServer = new HttpServer();
    	hServer.start();
        System.out.println( "Hello World!" );
    }
}
