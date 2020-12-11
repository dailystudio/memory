package com.dailystudio.memory.card;

import com.dailystudio.development.Logger;
import com.dailystudio.memory.Constants;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

public abstract class CardsUpdater {
	
	public class CardUpdaterASyncTask extends AsyncTask<CardBuilder, Void, Boolean> {

		@Override
		protected Boolean doInBackground(CardBuilder... params) {
			if (params == null || params.length <= 0) {
				return false;
			}

			final CardBuilder cardBuilder = params[0];
			Logger.debug("update card: [%s, build: %s]", 
					mCardName, cardBuilder);
			
			if (TextUtils.isEmpty(mCardName)) {
				return false;
			}
			
			if (cardBuilder == null) {
				Logger.warnning("unsupported card: %s", mCardName);
				return false;
			}
			
			return cardBuilder.buildCard(mContext);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			if (result) {
				notifyCardUpdate();
			}
		}
		
	}

	private Context mContext;
	private String mCardName;
	
	public CardsUpdater(Context context, String cardName) {
		mContext = (context == null ?
				null : context.getApplicationContext());
		
		mCardName = cardName;
	}
	
	public Context getContext() {
		return mContext;
	}
	
	public void doUpdate() {
		mHandler.post(mDoUpdateRunnable);
	}
	
	public void hideCard() {
		notifyCardUpdate();
	}
	
	public void showCard() {
		notifyCardUpdate();
	}
	
	private void notifyCardUpdate() {
		Intent notifyIntent = new Intent(
				Constants.ACTION_MEMORY_CARD_UPDATED);
		
		notifyIntent.putExtra(Constants.EXTRA_CARD_NAME, mCardName);
		
		mContext.sendBroadcast(notifyIntent);
		
		Logger.debug("send card update: %s", notifyIntent);
	}
	
	abstract protected CardBuilder getCardBuilder(String cardName);
	
	private Runnable mDoUpdateRunnable = new Runnable() {
		
		@Override
		public void run() {
			final CardBuilder builder = getCardBuilder(mCardName);
			
			new CardUpdaterASyncTask().execute(builder);
		}
		
	};
	
	private Handler mHandler = new Handler(Looper.getMainLooper());

}
