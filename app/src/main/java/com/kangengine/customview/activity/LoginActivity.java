package com.kangengine.customview.activity;

import android.annotation.TargetApi;
import android.app.KeyguardManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.kangengine.customview.R;
import com.kangengine.customview.fragment.FingerprintDialogFragment;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class LoginActivity extends AppCompatActivity {

    private static final String DEFAULT_KEY_NAME = "default_key";

    KeyStore keyStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(supportFingerPrint()) {
            initKey();
            initCipher();
        }
    }

    public boolean supportFingerPrint(){

        if(Build.VERSION.SDK_INT < 23) {
            showToast("系统版本过低，不支持指纹识别功能");
            return false;
        } else {
            KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
            FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);

            if(!fingerprintManager.isHardwareDetected()) {
                showToast("手机不支持指纹识别功能");
                return false;
            } else if(!keyguardManager.isKeyguardSecure()) {
                showToast("暂未设置锁屏，先设置一个指纹");
                return false;
            } else if(!fingerprintManager.hasEnrolledFingerprints()) {
                showToast("至少在系统中添加一个指纹");
                return false;
            }
        }
        return true;

    }


    @TargetApi(23)
    private void initKey(){
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(DEFAULT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |

                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);
            keyGenerator.init(builder.build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(23)
    private void initCipher(){
        try {
            SecretKey key = (SecretKey) keyStore.getKey(DEFAULT_KEY_NAME, null);
            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            showFingerPrintDialog(cipher);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFingerPrintDialog(Cipher cipher) {
        FingerprintDialogFragment fingerprintDialogFragment = new FingerprintDialogFragment();
        fingerprintDialogFragment.setCipher(cipher);
        fingerprintDialogFragment.show(getSupportFragmentManager(),"fingerprint");
    }



    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}
