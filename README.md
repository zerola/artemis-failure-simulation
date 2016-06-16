# How to compile and execute examples to simulate the failure

## Compilation

$ _mvn clean package dependency:copy-dependencies_

## Failure case 1

### Problem description

Artemis server complains that client has disconnected after proper disconnect from the client.

### Program description

Create plain connection to the Artemis server using Qpid 0.9.0 client and send 500 messages, each 1MB size.
Every message is sent to the same destination but with different subject (JMSType). Based on the type the server
diverts the message to the particular queue. The following script starts three instances of such senders in parallel
which should reproduce the issue.

### To simulate issue #1:

* change connection settings in _failureCase1.sh_

$ _./failureCase1.sh_

## Failure case 2

### Problem description

Client throws exception that transport has been closed due to the peer exceeding our requested idle-timeout.
Server throws exception:

        java.lang.IllegalArgumentException
        	at java.nio.Buffer.position(Buffer.java:244)
        	at java.nio.HeapByteBuffer.put(HeapByteBuffer.java:210)
        	at org.apache.qpid.proton.engine.impl.FrameWriter.readBytes(FrameWriter.java:229)
        	at org.apache.qpid.proton.engine.impl.TransportImpl.writeInto(TransportImpl.java:335)
        	at org.apache.qpid.proton.engine.impl.TransportOutputAdaptor.pending(TransportOutputAdaptor.java:57)
        	at org.apache.qpid.proton.engine.impl.SaslImpl$SaslTransportWrapper.pending(SaslImpl.java:676)
        	at org.apache.qpid.proton.engine.impl.HandshakeSniffingTransportWrapper.pending(HandshakeSniffingTransportWrapper.java:138)
        	at org.apache.qpid.proton.engine.impl.TransportImpl.pending(TransportImpl.java:1462)
        	at org.apache.qpid.proton.engine.impl.TransportImpl.pop(TransportImpl.java:1479)
        	at org.proton.plug.handler.impl.ProtonHandlerImpl.outputDone(ProtonHandlerImpl.java:225)
        	at org.proton.plug.context.AbstractConnectionContext.outputDone(AbstractConnectionContext.java:120)
        	at org.apache.activemq.artemis.core.protocol.proton.plug.ActiveMQProtonConnectionCallback.onTransport(ActiveMQProtonConnectionCallback.java:115)
        	at org.proton.plug.context.AbstractConnectionContext.flushBytes(AbstractConnectionContext.java:162)
        	at org.proton.plug.context.AbstractConnectionContext$LocalListener.onTransport(AbstractConnectionContext.java:176)
        	at org.proton.plug.handler.Events.dispatch(Events.java:97)
        	at org.proton.plug.handler.impl.ProtonHandlerImpl.dispatch(ProtonHandlerImpl.java:362)
        	at org.proton.plug.handler.impl.ProtonHandlerImpl.access$000(ProtonHandlerImpl.java:49)
        	at org.proton.plug.handler.impl.ProtonHandlerImpl$1.run(ProtonHandlerImpl.java:63)
        	at org.apache.activemq.artemis.utils.OrderedExecutorFactory$OrderedExecutor$ExecutorTask.run(OrderedExecutorFactory.java:100)
        	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1142)
        	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
        	at java.lang.Thread.run(Thread.java:745)


### Program description

Create plain connection to the Artemis server using Qpid 0.9.0 client and send 500 messages, each 1MB size.
Every message is sent to the same destination but with different subject (JMSType). Based on the type the server
diverts the message to the particular queue. At the same time start consumer which reads all messages from the queue.
The following script starts three instances of such senders and consumers in parallel which should reproduce the issue.

### To simulate issue #2:

* change connection settings in _failureCase2.sh_

$ _./failureCase2.sh_
