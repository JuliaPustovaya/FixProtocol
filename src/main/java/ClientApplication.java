import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.FileStoreFactory;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RejectLogon;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.UnsupportedMessageType;
import quickfix.field.ClOrdID;
import quickfix.field.HandlInst;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix42.NewOrderSingle;

public class ClientApplication implements Application {
	private static volatile SessionID sessionID;

	public void onCreate(SessionID sessionID) {
		System.out.println("client OnCreate !!!");
	}

	public void onLogon(SessionID sessionID) {
		System.out.println(" client OnLogon");
		ClientApplication.sessionID = sessionID;
	}

	public void onLogout(SessionID sessionID) {
		System.out.println(" client OnLogout");
		ClientApplication.sessionID = null;
	}

	public void toAdmin(Message message, SessionID sessionID) {
		System.out.println(" client ToAdmin");
	}

	public void fromAdmin(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		System.out.println(" client FromAdmin");
	}

	public void toApp(Message message, SessionID sessionID) throws DoNotSend {
		System.out.println(" client ToApp: " + message);
	}

	public void fromApp(Message message, SessionID sessionID) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		System.out.println(" client FromApp");
	}

	public static void main(String[] args) throws ConfigError, InterruptedException, SessionNotFound {
		SessionSettings settings = new SessionSettings("initiator.properties");
		Application application = new ClientApplication();
		MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new ScreenLogFactory(true, true, true);
		MessageFactory messageFactory = new DefaultMessageFactory();
		Initiator initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory, messageFactory);
		initiator.start();
		while (sessionID == null) {
			Thread.sleep(1000);
		}
		final String orderId = "342";
		NewOrderSingle newOrder = new NewOrderSingle(
				new ClOrdID("CLIENT_ORDER_ID"),
				new HandlInst('1'),
				new Symbol("BTCUSD"),
				new Side(Side.BUY),
				new TransactTime(),
				new OrdType(OrdType.MARKET));
		Session.sendToTarget(newOrder, sessionID);
		Thread.sleep(5000);
	}
}
