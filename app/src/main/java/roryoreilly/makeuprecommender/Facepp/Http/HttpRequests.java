package roryoreilly.makeuprecommender.Facepp.Http;


import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;


import org.json.JSONException;
import org.json.JSONObject;

import roryoreilly.makeuprecommender.Facepp.Error.FaceppParseException;
import roryoreilly.makeuprecommender.Facepp.Result.FaceppResult;

public class HttpRequests {
    private static String WEBSITE = "http://apicn.faceplusplus.com/";
    private static final int BUFFERSIZE = 1048576;
    private static final int TIMEOUT = 30000;
    private static final int TRAINTIMEOUT = 60000;
    private String apiKey;
    private String apiSecret;
    private PostParameters params;
    private int httpTimeOut = 30000;

    public void setHttpTimeOut(int timeOut) {
        this.httpTimeOut = timeOut;
    }

    public int getHttpTimeOut() {
        return this.httpTimeOut;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return this.apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public FaceppResult request(String control, String action) throws FaceppParseException {
        return this.request(control, action, this.getParams());
    }

    public FaceppResult train() throws FaceppParseException {
        return this.train(this.getParams(), 60000L);
    }

    public FaceppResult train(long timeOut) throws FaceppParseException {
        return this.train(this.getParams(), timeOut);
    }

    public FaceppResult train(PostParameters params) throws FaceppParseException {
        return this.train(params, 60000L);
    }

    public FaceppResult train(PostParameters params, long timeOut) throws FaceppParseException {
        String sessionIdTmp = null;
        FaceppResult fj = null;

        try {
            fj = this.request("recognition", "train", params);
            sessionIdTmp = fj.get("session_id").toString();
        } catch (FaceppParseException var15) {
            if(fj.isError()) {
                return fj;
            }

            throw new FaceppParseException("Train error.");
        }

        StringBuilder sb = new StringBuilder();
        String sessionId = sessionIdTmp;
        long t = (new Date()).getTime() + timeOut;

        while(true) {
            FaceppResult rst = this.request("info", "get_session", (new PostParameters()).setSessionId(sessionId));

            if(rst.get("status").toString().equals("SUCC")) {
                sb.append(rst.toString());
                break;
            }

            if(rst.get("status").toString().equals("INVALID_SESSION")) {
                sb.append("INVALID_SESSION");
                break;
            }

            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var16) {
                sb.append("Thread.sleep error.");
                break;
            }

            if((new Date()).getTime() >= t) {
                sb.append("Time Out");
                break;
            }
        }

        String rst1 = sb.toString();
        if(rst1.equals("INVALID_SESSION")) {
            throw new FaceppParseException("Invaild session, unknow error.");
        } else if(rst1.equals("Unknow error.")) {
            throw new FaceppParseException("Unknow error.");
        } else if(rst1.equals("Thread.sleep error.")) {
            throw new FaceppParseException("Thread.sleep error.");
        } else if(rst1.equals("Time Out")) {
            return fj;
        } else {
            try {
                return new FaceppResult(new JSONObject(rst1), 200);
            } catch (JSONException var14) {
                return null;
            }
        }
    }

    public FaceppResult request(String control, String action, PostParameters params) throws FaceppParseException {
        HttpURLConnection urlConn = null;
        String resultString = null;

        try {
            URL url = new URL(WEBSITE + control + "/" + action);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            urlConn.setConnectTimeout(this.httpTimeOut);
            urlConn.setReadTimeout(this.httpTimeOut);
            urlConn.setDoOutput(true);
            urlConn.setRequestProperty("connection", "keep-alive");
            urlConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + params.boundaryString());
            MultipartEntityBuilder e = params.getMultiPart();
            e.addTextBody("api_key", this.apiKey);
            e.addTextBody("api_secret", this.apiSecret);
            HttpEntity entity = e.build();
            entity.writeTo(urlConn.getOutputStream());

            if (urlConn.getResponseCode() == 200) {
                resultString = readString(urlConn.getInputStream());
            } else {
                resultString = readString(urlConn.getErrorStream());
            }

            FaceppResult result = new FaceppResult(new JSONObject(resultString), urlConn.getResponseCode());
            if(result.isError()) {
                throw new FaceppParseException("API error.", result.getErrorCode(), result.getErrorMessage(), result.getHttpResponseCode());
            } else {
                return result;
            }
        } catch (Exception var9) {
            throw new FaceppParseException("error : " + var9.toString());
        }
    }

    private static String readString(InputStream is) {
        StringBuffer rst = new StringBuffer();
        byte[] buffer = new byte[1048576];
        boolean len = false;

        int var6;
        try {
            while((var6 = is.read(buffer)) > 0) {
                for(int e = 0; e < var6; ++e) {
                    rst.append((char)buffer[e]);
                }
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return rst.toString();
    }

    public HttpRequests(String apiKey, String apiSecret) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public HttpRequests() {
    }

    public HttpRequests(String apiKey, String apiSecret, boolean debug) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        if(debug) {
            WEBSITE = "http://api.faceplusplus.com/";
        }

    }

    public HttpRequests(boolean debug) {
        if(debug) {
            WEBSITE = "http://api.faceplusplus.com/";
        }

    }

    public PostParameters getParams() {
        if(this.params == null) {
            this.params = new PostParameters();
        }

        return this.params;
    }

    public void setParams(PostParameters params) {
        this.params = params;
    }

    public FaceppResult detectionDetect() throws FaceppParseException {
        return this.request("detection", "detect");
    }

    public FaceppResult detectionDetect(PostParameters params) throws FaceppParseException {
        return this.request("detection", "detect", params);
    }

    public FaceppResult detectionLandmark(PostParameters params) throws FaceppParseException {
        return this.request("detection", "landmark", params);
    }

    public FaceppResult infoGetImage() throws FaceppParseException {
        return this.request("info", "get_image");
    }

    public FaceppResult infoGetImage(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_image", params);
    }

    public FaceppResult infoGetFace() throws FaceppParseException {
        return this.request("info", "get_face");
    }

    public FaceppResult infoGetFace(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_face", params);
    }

    public FaceppResult infoGetSession() throws FaceppParseException {
        return this.request("info", "get_session");
    }

    public FaceppResult infoGetSession(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_session", params);
    }

    public FaceppResult infoGetQuota() throws FaceppParseException {
        return this.request("info", "get_quota");
    }

    public FaceppResult infoGetQuota(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_quota", params);
    }

    public FaceppResult infoGetPersonList() throws FaceppParseException {
        return this.request("info", "get_person_list");
    }

    public FaceppResult infoGetPersonList(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_person_list", params);
    }

    public FaceppResult infoGetGroupList() throws FaceppParseException {
        return this.request("info", "get_group_list");
    }

    public FaceppResult infoGetGroupList(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_group_list", params);
    }

    public FaceppResult infoGetApp() throws FaceppParseException {
        return this.request("info", "get_app");
    }

    public FaceppResult infoGetApp(PostParameters params) throws FaceppParseException {
        return this.request("info", "get_app", params);
    }

    public FaceppResult personCreate() throws FaceppParseException {
        return this.request("person", "create");
    }

    public FaceppResult personCreate(PostParameters params) throws FaceppParseException {
        return this.request("person", "create", params);
    }

    public FaceppResult personDelete() throws FaceppParseException {
        return this.request("person", "delete");
    }

    public FaceppResult personDelete(PostParameters params) throws FaceppParseException {
        return this.request("person", "delete", params);
    }

    public FaceppResult personAddFace() throws FaceppParseException {
        return this.request("person", "add_face");
    }

    public FaceppResult personAddFace(PostParameters params) throws FaceppParseException {
        return this.request("person", "add_face", params);
    }

    public FaceppResult personRemoveFace() throws FaceppParseException {
        return this.request("person", "remove_face");
    }

    public FaceppResult personRemoveFace(PostParameters params) throws FaceppParseException {
        return this.request("person", "remove_face", params);
    }

    public FaceppResult personGetInfo() throws FaceppParseException {
        return this.request("person", "get_info");
    }

    public FaceppResult personGetInfo(PostParameters params) throws FaceppParseException {
        return this.request("person", "get_info", params);
    }

    public FaceppResult personSetInfo() throws FaceppParseException {
        return this.request("person", "set_info");
    }

    public FaceppResult personSetInfo(PostParameters params) throws FaceppParseException {
        return this.request("person", "set_info", params);
    }

    public FaceppResult groupCreate() throws FaceppParseException {
        return this.request("group", "create");
    }

    public FaceppResult groupCreate(PostParameters params) throws FaceppParseException {
        return this.request("group", "create", params);
    }

    public FaceppResult groupDelete() throws FaceppParseException {
        return this.request("group", "delete");
    }

    public FaceppResult groupDelete(PostParameters params) throws FaceppParseException {
        return this.request("group", "delete", params);
    }

    public FaceppResult groupAddPerson() throws FaceppParseException {
        return this.request("group", "add_person");
    }

    public FaceppResult groupAddPerson(PostParameters params) throws FaceppParseException {
        return this.request("group", "add_person", params);
    }

    public FaceppResult groupRemovePerson() throws FaceppParseException {
        return this.request("group", "remove_person");
    }

    public FaceppResult groupRemovePerson(PostParameters params) throws FaceppParseException {
        return this.request("group", "remove_person", params);
    }

    public FaceppResult groupGetInfo() throws FaceppParseException {
        return this.request("group", "get_info");
    }

    public FaceppResult groupGetInfo(PostParameters params) throws FaceppParseException {
        return this.request("group", "get_info", params);
    }

    public FaceppResult groupSetInfo() throws FaceppParseException {
        return this.request("group", "set_info");
    }

    public FaceppResult groupSetInfo(PostParameters params) throws FaceppParseException {
        return this.request("group", "set_info", params);
    }

    public FaceppResult recognitionCompare() throws FaceppParseException {
        return this.request("recognition", "compare");
    }

    public FaceppResult recognitionCompare(PostParameters params) throws FaceppParseException {
        return this.request("recognition", "compare", params);
    }

    public FaceppResult recognitionTrain() throws FaceppParseException {
        return this.request("recognition", "train");
    }

    public FaceppResult recognitionTrain(PostParameters params) throws FaceppParseException {
        return this.request("recognition", "train", params);
    }

    public FaceppResult recognitionVerify() throws FaceppParseException {
        return this.request("recognition", "verify");
    }

    public FaceppResult recognitionVerify(PostParameters params) throws FaceppParseException {
        return this.request("recognition", "verify", params);
    }

    public FaceppResult recognitionRecognize() throws FaceppParseException {
        return this.request("recognition", "recognize");
    }

    public FaceppResult recognitionRecognize(PostParameters params) throws FaceppParseException {
        return this.request("recognition", "recognize", params);
    }

    public FaceppResult recognitionSearch() throws FaceppParseException {
        return this.request("recognition", "search");
    }

    public FaceppResult recognitionSearch(PostParameters params) throws FaceppParseException {
        return this.request("recognition", "search", params);
    }

    public FaceppResult groupingGrouping() throws FaceppParseException {
        return this.request("grouping", "grouping");
    }

    public FaceppResult groupingGrouping(PostParameters params) throws FaceppParseException {
        return this.request("grouping", "grouping", params);
    }
}

