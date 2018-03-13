/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */

package ch.sbb.kihub.application.mdb;

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.jboss.annotation.ejb.ResourceAdapter;

import ch.sbb.kihub.application.persistence.stammdaten.dao.PropertyDaoLocal;

/**
 * Empfaengt Daten der Prodplan-Queue und schickt diese weiter auf die Prognose-Queue und das Event-Topic.
 *
 * Der Versand ist gleich wie bei Kihub auch aufgebaut.
 *
 * TODO:
 * - Transaktionsmanagement (XA)
 * - Anzahl gleichzeitig Active Session und deren Anzahl Meldungen
 *
 * @author u213852 (Guggisberg Marco)
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:/jms/cfQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "true"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/pplan"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
@ResourceAdapter(value = "wmq.jmsra.rar")
 @TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RcsProdplanMdb implements MessageListener {

    @Resource(lookup = "java:/jms/cfQueue")
    private ConnectionFactory connectionFactoryQueue;

    @Resource(lookup = "java:/jms/cfTopic")
    private ConnectionFactory connectionFactoryTopic;

    @Resource(lookup = "java:/jms/topic/event")
    private Topic topic;

    @Resource(lookup = "java:/jms/queue/prognose")
    private Queue queue;

    @EJB
    private PropertyDaoLocal propertyDao;

    private static final Logger LOGGER = Logger.getLogger(RcsProdplanMdb.class.toString());

    @Override
    public void onMessage(Message rcvMessage) {
        if (rcvMessage instanceof TextMessage) {
            TextMessage msg = (TextMessage) rcvMessage;

            try {
                LOGGER.info("Received Message from queue ----: " + msg.getText());
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

            send(msg, queue, connectionFactoryQueue);
            send(msg, topic, connectionFactoryTopic);
            LOGGER.warning("Message of wrong type: " + rcvMessage.getClass().getName());

            System.out.println(propertyDao.getAllProperties());
        }
    }

    private void send(final TextMessage msg, final Destination destination, final ConnectionFactory connectionFactory) {
        try {
            final Connection connection = connectionFactory.createConnection();
            final Session session = connection.createSession(false, 0);
            sendMessageToDestination(session, msg, destination);
            // context.createProducer().send(queue, text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToDestination(final Session session, final Message message, final Destination destination) {

        MessageProducer messageProducer = null;
        try {
            messageProducer = session.createProducer(destination);
            messageProducer.setTimeToLive(3600);

            messageProducer.send(message);

        } catch (final JMSException jmsE) {
            LOGGER.severe(jmsE.getMessage());
            jmsE.printStackTrace();
        } finally {
            closeMessageProducer(messageProducer);
        }
    }

    private void closeMessageProducer(final MessageProducer messageProducer) {
        if (messageProducer != null) {
            try {
                messageProducer.close();
            } catch (final JMSException jmsE) {

                LOGGER.severe(jmsE.getMessage());
                jmsE.printStackTrace();
            }
        }
    }
}

