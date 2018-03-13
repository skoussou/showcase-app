/*
 * Copyright (C) Schweizerische Bundesbahnen SBB, 2018.
 */
package ch.sbb.kihub.application.mdb;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.jboss.annotation.ejb.ResourceAdapter;

/**
 * Empfaengt Daten der Prognose-Queue.
 *
 * @author u213852 (Guggisberg Marco)
 */
@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "connectionFactoryLookup", propertyValue = "java:/jms/cfQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "useJNDI", propertyValue = "true"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/prognose"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
@ResourceAdapter(value = "wmq.jmsra.rar")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class RcsPrognoseMdb implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(RcsPrognoseMdb.class.toString());

    @Override
    public void onMessage(Message rcvMessage) {
        TextMessage msg = null;
        try {
            if (rcvMessage instanceof TextMessage) {
                msg = (TextMessage) rcvMessage;
                LOGGER.info("Received Message from queue: " + msg.getText());
            } else {
                LOGGER.warning("Message of wrong type: " + rcvMessage.getClass().getName());
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
