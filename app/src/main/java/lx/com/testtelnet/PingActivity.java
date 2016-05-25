package lx.com.testtelnet;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import net.qiujuer.genius.kit.Kit;
import net.qiujuer.genius.kit.command.Command;
import net.qiujuer.genius.kit.util.UiKit;

public class PingActivity extends Activity {

    private EditText et_url;
    private TextView text;
    private Button btn_ping;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ping);

        Kit.initialize(getApplication());

        et_url = (EditText) findViewById(R.id.et_url);
        text = (TextView) findViewById(R.id.text);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        btn_ping = (Button) findViewById(R.id.btn_ping);
        btn_ping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ping(et_url.getText().toString().trim());
                Toast.makeText(PingActivity.this, "请稍后。。。。。。", Toast.LENGTH_SHORT).show();
            }
        });


        net.qiujuer.genius.kit.util.Log.addCallbackListener(new net.qiujuer.genius.kit.util.Log.LogCallbackListener() {
            @Override
            public void onLogArrived(final net.qiujuer.genius.kit.util.Log data) {
                try {
                    // Async to show
                    // 异步显示到界面
                    UiKit.runOnMainThreadAsync(new Runnable() {
                        @Override
                        public void run() {
                            if (text != null) {
                                text.append(data.getMsg());
                                scrollBottom();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void scrollBottom() {
        Handler handler = new Handler();
        handler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    private void Ping(final String url) {
        // Sync
        // 同步
        Thread thread = new Thread() {
            public void run() {
                // The same way call way and the ProcessBuilder mass participation
                // 调用方式与ProcessBuilder传参方式一样
                Command command = new Command(Command.TIMEOUT, "/system/bin/ping",
                        "-c", "4", "-s", "100",
                        url);

                String res = Command.command(command);
                net.qiujuer.genius.kit.util.Log.i("Command", "\n\nCommand Sync: " + res);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}
