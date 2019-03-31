package com.ucm.informatica.spread.Utils;

import android.app.Activity;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.ucm.informatica.spread.R;

public class FlashBarBuilder {

    private Activity activity;
    private String text;

    public FlashBarBuilder(Activity activity){
        this.activity = activity;
    }


    public FlashBarBuilder(Activity activity, String text){
        this.activity = activity;
        this.text = text;
    }

    public Flashbar getAlertSnackBarGPS(){
        return new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .enterAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(450)
                        .accelerateDecelerate())
                .showIcon()
                .backgroundColorRes(R.color.snackbarBackground)
                .message(activity.getString(R.string.snackbar_alert_gps))
                .build();
    }

    public Flashbar getErrorSnackBar(int text){
        return new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .enterAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(450)
                        .accelerateDecelerate())
                .backgroundColorRes(R.color.snackbarBackground)
                .message(activity.getString(text))
                .messageColorRes(R.color.snackbarAlertColor)
                .build();
    }

    public Flashbar getConfirmationSnackBar(){
        return new Flashbar.Builder(activity)
                .gravity(Flashbar.Gravity.BOTTOM)
                .duration(2500)
                .enterAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(activity)
                        .animateBar()
                        .duration(450)
                        .accelerateDecelerate())
                .backgroundColorRes(R.color.snackbarBackground)
                .message(text!=null?text:activity.getString(R.string.snackbar_confirmation_transaction))
                .messageColorRes(R.color.snackbarConfirmColor)
                .build();
    }
}
