package us.m410.osgi.service;

/**
 * Document Me..
 *
 * @author Michael Fortin
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello() {
        System.out.println("from side helloService");
        return "Hello there";
    }
}
