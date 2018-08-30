package com.cloudoc.share.yybpg.customview.fragment;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudoc.share.yybpg.customview.R;
import com.cloudoc.share.yybpg.customview.activity.LoginActivity;

import javax.crypto.Cipher;

/**
 * @author : Vic
 * time   : 2018/08/28
 * desc   :
 */
@TargetApi(23)
public class FingerprintDialogFragment extends DialogFragment {
    private FingerprintManager fingerprintManager;
    private CancellationSignal mCancellationSignal;
    private Cipher mCipher;
    private LoginActivity mActivity;
    private TextView errorMsg;

    private boolean isSelfCancelled;

    public void setCipher(Cipher cipher) {
        mCipher = cipher;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (LoginActivity) getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fingerprit_dialog,container,false);
        errorMsg = view.findViewById(R.id.error_msg);
        TextView cancel = view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                stopListening();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fingerprintManager = getContext().getSystemService(FingerprintManager.class);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public void onResume() {
        super.onResume();
        startListening(mCipher);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopListening();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startListening(Cipher cipher) {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(new FingerprintManager.CryptoObject(cipher),
                mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        if(!isSelfCancelled) {
                            errorMsg.setText("指纹识别错误");
                            if(errorCode == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
                                Toast.makeText(mActivity, "指纹识别错误", Toast.LENGTH_SHORT).show();
                                dismiss();
                            }
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                        super.onAuthenticationHelp(helpCode, helpString);
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(mActivity, "指纹认证成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        errorMsg.setText("指纹认证失败，请再试一次");
                    }
                }, null);
    }

    private void stopListening(){
        if(mCancellationSignal != null) {
            mCancellationSignal.cancel();
            mCancellationSignal = null;
            isSelfCancelled = true;
        }
    }
}
