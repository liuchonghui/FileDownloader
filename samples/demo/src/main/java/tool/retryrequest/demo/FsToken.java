package tool.retryrequest.demo;

import android.text.TextUtils;

import tools.android.retryrequest.Legally;

public class FsToken implements Legally {
    public Data data;

    @Override
    public boolean isLegal() {
        return data != null && "success".equals(data.result)
                && data.token != null && !TextUtils.isEmpty(data.token.access_token);
    }
}

class Data {
    public String result;
    Token token;
}

class Token {
    public String code;
    public String at;
    public String access_token;
}