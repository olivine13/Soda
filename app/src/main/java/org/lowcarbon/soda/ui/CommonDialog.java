package org.lowcarbon.soda.ui;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
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
        private boolean show;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder showSelection(boolean flag) {
            this.show = flag;
            return this;
        }

        public CommonDialog build() {
            CommonDialog dialog = new CommonDialog(context);
            dialog.setMessage(message);
            dialog.showSelection(show);
            return dialog;
        }
    }

    private TextView mMessageTv;
    private View mSelection;

    CommonDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_content);
        mMessageTv = (TextView) findViewById(R.id.message);
        mSelection = findViewById(R.id.selection);
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void setMessage(String message) {
        if (mMessageTv != null) {
            mMessageTv.setText(message);
        }
    }

    private void showSelection(boolean show) {
        mSelection.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
