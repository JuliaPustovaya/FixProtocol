import java.io.FileNotFoundException;
import java.util.concurrent.CountDownLatch;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.UnsupportedMessageType;

public class ServerApplication implements Application {
	public void onCreate(SessionID sessionID) {
	}

	public void onLogon(SessionID sessionID) {
	}

	public void onLogout(SessionID sessionID) {
	}

	public void toAdmin(Message message, SessionID sessionID) {
	}

	public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
	}

	public void toApp(Message message, SessionID sessionID) throws DoNotSend {
	}

	public void fromApp(Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		System.out.println("From App serverapplication " + message);
	}

	public static void main(String[] args) throws ConfigError, FileNotFoundException, InterruptedException, SessionNotFound
	{
		SessionSettings settings = new SessionSettings("acceptor.properties");
  		Application application = new ServerApplication();
		MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new ScreenLogFactory( true, true, true);
		MessageFactory messageFactory = new DefaultMessageFactory();

		Acceptor acceptor = new SocketAcceptor(application, messageStoreFactory, settings, logFactory, messageFactory);
		acceptor.start();

		CountDownLatch latch = new CountDownLatch(1);
		latch.await();
	}
}
