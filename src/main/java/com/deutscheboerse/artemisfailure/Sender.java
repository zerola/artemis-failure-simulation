package com.deutscheboerse.artemisfailure;

import java.util.Properties;
import java.util.Random;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Sender {

    private static String createTextMessageContent(int messageSize) {
        final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(messageSize);
        for (int i = 0; i < messageSize; i++ ) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }

    public static void main(String[] args) throws NamingException, JMSException {
        final String hostname = System.getProperty("host", "localhost");
        final String port = System.getProperty("port", "5672");
        final String user = System.getProperty("user", "admin");
        final String password = System.getProperty("password", "admin");
        final String destination = "broadcast.test" + System.getProperty("index", "0");

        final InitialContext context;
        final Properties properties = new Properties();
        properties.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
        properties.setProperty("connectionfactory.connection", String.format("amqp://%s:%s?jms.username=%s&jms.password=%s",
                hostname, port, user, password));
        properties.setProperty("queue.broadcastAddress", "broadcast");
        context = new InitialContext(properties);
        final Connection connection = ((ConnectionFactory) context.lookup("connection")).createConnection();
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();
        final Destination broadcastDestination = (Destination) context.lookup("broadcastAddress");
        final MessageProducer producer = session.createProducer(broadcastDestination);
        final TextMessage message = session.createTextMessage(Sender.createTextMessageContent(1024 * 1024));
        message.setJMSType(destination);
        for (int i = 0; i < 500; i++) {
            producer.send(message);
        }
        connection.stop();
        producer.close();
        session.close();
        connection.close();
    }

}
