/*
 *  Copyright 2019, Liwei Wang <daveywang@live.com>.
 *  All rights reserved.
 *  Author: Liwei Wang
 *  Date: 06/2019
 */

package org.liwei;

import com.amazonaws.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class SQSMessageListener implements MessageListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message) {
        try {
            handleMessage(message);
            message.acknowledge();
            logger.info("Acknowledged message " + message.getJMSMessageID());
        }
        catch (JMSException e) {
            logger.error("Error processing message: " + e.getMessage());
        }
    }

    private void handleMessage(Message message) throws JMSException {
        logger.info("Got message " + message.getJMSMessageID());

        if (message instanceof TextMessage) {
            TextMessage txtMessage = (TextMessage)message;
            logger.info("Content: " + txtMessage.getText());
        }
        else if (message instanceof BytesMessage){
            BytesMessage byteMessage = (BytesMessage)message;
            byte[] bytes = new byte[(int)byteMessage.getBodyLength()];
            byteMessage.readBytes(bytes);
            logger.info("Content: " +  Base64.encodeAsString(bytes));
        }
        else if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            logger.info("Content: " + objMessage.getObject());
        }
    }
}
