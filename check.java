package com.example.app;

import android.content.Context;
import android.content.SharedPreferences;

public class CoinManager {

    private static final String PREF_NAME = "englora_coin";
    private static final String KEY_COIN = "user_coin";
    

    public static int getCoin(Context context){

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return prefs.getInt(KEY_COIN, 0);
    }

    public static void addCoin(Context context, int amount){

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        int current = prefs.getInt(KEY_COIN, 0);

        prefs.edit()
                .putInt(KEY_COIN, current + amount)
                .apply();
    }

    public static boolean unlockCriteria(Context context, int amount){

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        int current = prefs.getInt(KEY_COIN, 0);

        if(current < amount){
            return false;
        }
        else{

            return true;
        }
    }

    public static boolean spendCoin(Context context, int amount){

        SharedPreferences prefs =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        int current = prefs.getInt(KEY_COIN, 0);

        if(current < amount){
            return false;
        }

        prefs.edit()
                .putInt(KEY_COIN, current - amount)
                .apply();

        return true;
    }




}


//==========================Admob Rewaeded Ad===== Another Activity======================

loadRewardedAd(); //call in onCreate()

//============================================================================================

//call in button onClick
private void showRewardedAd() {


        if ( rewardedAd != null ) {

            rewardedAd.show(
                    EarnCoin.this,
                    new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            Log.d(TAG, "User earned the reward.");
                            // Handle the reward.

                            //you can controll reward from Admob. int rewardAmount = rewardItem.getRewardAmount
                            int rewardAmount = 50; // প্রতি ad এ 50 coin

                            CoinManager.addCoin(EarnCoin.this, rewardAmount);

                            int currentCount = CoinManager.getHourlyAdCount(EarnCoin.this);
                            CoinManager.setHourlyAdCount(EarnCoin.this, currentCount + 1);
                            CoinManager.setLastAdTime(EarnCoin.this, System.currentTimeMillis());
                            updateAdStatus();

                            showTotalCoin();

                            Toast.makeText(
                                    EarnCoin.this,
                                    "+" + rewardAmount + " Coin Added",
                                    Toast.LENGTH_SHORT
                            ).show();


                        }
                    });

        }else {


            Toast.makeText(EarnCoin.this, "Ads not Ready Yet", Toast.LENGTH_SHORT).show();
        }



    }


    private void loadRewardedAd() {

        RewardedAd.load(
                EarnCoin.this,
                getString(R.string.RewardedAd_Unit_ID),
                new AdRequest.Builder().build(),
                new RewardedAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        Log.d(TAG, "Ad was loaded.");
                        rewardedAd = ad;

                        setRewardedAdCallBack();
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        Log.d(TAG, loadAdError.getMessage());
                        rewardedAd = null;
                    }
                });


    }


    private void setRewardedAdCallBack() {

        rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {

                        Log.d(TAG, "Ad was dismissed.");
                        rewardedAd = null;
                        loadRewardedAd();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {

                        Log.d(TAG, "Ad failed to show.");
                        rewardedAd = null;
                        loadRewardedAd();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {

                        Log.d(TAG, "Ad showed fullscreen content.");
                    }

                    @Override
                    public void onAdImpression() {

                        Log.d(TAG, "Ad recorded an impression.");
                    }

                    @Override
                    public void onAdClicked() {

                        Log.d(TAG, "Ad was clicked.");
                    }
                });

    }

    //==========================================================