package org.lowcarbon.soda.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import org.lowcarbon.soda.R;

/**
 * TODO description
 *
 * @author zhenqilai@kugou.net
 * @since 18-12-8
 */
public class CommonDialog extends Dialog {

    public static class Builder {

        private Context context;
        private String message;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public CommonDialog build() {
            CommonDialog dialog = new CommonDialog(context);
            dialog.setMessage(message);
            return dialog;
        }
    }

    private TextView mMessageTv;

    CommonDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_content);
        mMessageTv = (TextView) findViewById(R.id.message);
    }

    private void setMessage(String message) {
        if (mMessageTv != null) {
            mMessageTv.setText(message);
        }
    }
}
