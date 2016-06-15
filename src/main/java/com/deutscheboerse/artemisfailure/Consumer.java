package com.deutscheboerse.artemisfailure;

import java.util.Properties;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Consumer {
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
        properties.setProperty("queue.broadcastAddress", destination);
        context = new InitialContext(properties);
        final Connection connection = ((ConnectionFactory) context.lookup("connection")).createConnection();
        final Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        connection.start();
        final Destination broadcastDestination = (Destination) context.lookup("broadcastAddress");
        final MessageConsumer consumer = session.createConsumer(broadcastDestination);
        Message message = null;
        for (int i = 0; i < 500; i++) {
            message = consumer.receive();
        }
        message.acknowledge();
        connection.stop();
        consumer.close();
        session.close();
        connection.close();
    }

}
