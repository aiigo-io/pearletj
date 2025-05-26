package hk.zdl.crypto.pearlet.tx_history_query;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import hk.zdl.crypto.pearlet.component.event.AccountChangeEvent;
import hk.zdl.crypto.pearlet.component.event.TxHistoryEvent;
import hk.zdl.crypto.pearlet.ds.CryptoNetwork;

public class TxHistoryQueryExecutor {

	private final ExecutorService es = Executors.newSingleThreadExecutor();

	public TxHistoryQueryExecutor() {
		EventBus.getDefault().register(this);
	}

	@Subscribe(threadMode = ThreadMode.ASYNC)
	public void onMessage(AccountChangeEvent e) {
		if (e.account == null || e.account.isBlank())
			return;
		EventBus.getDefault().post(new TxHistoryEvent<>(e.network, TxHistoryEvent.Type.START, null));
		es.submit(new MyWorker(e.network, e.account));
	}

	private final class MyWorker implements Runnable {
		CryptoNetwork network;
		String account;

		public MyWorker(CryptoNetwork network, String account) {
			this.network = network;
			this.account = account;
		}

		@Override
		public void run() {
			if (account == null) {
				return;
			} else
				try {
					EventBus.getDefault().post(new TxHistoryEvent<>(network, TxHistoryEvent.Type.START, null));
					if (network.isBurst()) {
						new SignumTxHistoryQuery(network).queryTxHistory(account);
					} else if (network.isWeb3J()) {
						new EtherTxQuery(network).queryTxHistory(account);
					}
				} catch (Exception e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				} finally {
					EventBus.getDefault().post(new TxHistoryEvent<>(network, TxHistoryEvent.Type.FINISH, null));
				}
		}
		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("MyThread [network=").append(network).append(", account=").append(account).append("]");
			return builder.toString();
		}

	}
}
