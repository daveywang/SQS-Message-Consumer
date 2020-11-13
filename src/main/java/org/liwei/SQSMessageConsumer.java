/*
 *  Copyright 2019, Liwei Wang <daveywang@live.com>.
 *  All rights reserved.
 *  Author: Liwei Wang
 *  Date: 06/2019
 */

package org.liwei;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.io.IOException;

public class SQSMessageConsumer {
    private static final String queueName = "training_queue_liwei_org";
    private static Logger logger = LoggerFactory.getLogger(SQSMessageConsumer.class);

    public static void main(String[] args) throws JMSException, IOException {
        logger.info("The message consumer is listening... press ENTER to exit the application.");

        // Create the connection factory
        SQSConnectionFactory connectionFactory = new SQSConnectionFactory(new ProviderConfiguration(),
                                                                          AmazonSQSClientBuilder.standard()
                                                                          .withCredentials(new DefaultAWSCredentialsProviderChain())
                                                                          .withRegion(Regions.US_EAST_2)
        );

        //Create the connection
        SQSConnection connection = connectionFactory.createConnection();
        //Create the session
        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        //Create the consumer
        javax.jms.MessageConsumer consumer = session.createConsumer(session.createQueue(queueName));
        SQSMessageListener messageListener = new SQSMessageListener();
        consumer.setMessageListener(messageListener);
        connection.start();
        System.in.read();
        connection.close();
        logger.info( "The Connection closed." );
    }
}
