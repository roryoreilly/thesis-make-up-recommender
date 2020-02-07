package roryoreilly.makeuprecommender.Facepp.Result;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Dictionary;
import java.util.Hashtable;

import roryoreilly.makeuprecommender.Facepp.Error.FaceppParseException;


public class FaceppResult {
    private static Dictionary<String, FaceppResult.JsonType> keyWordDict = new Hashtable();
    private Object json;
    private FaceppResult.JsonType type;
    private int httpResponseCode = 0;

    static {
        keyWordDict.put("session_id", JsonType.STRING);
        keyWordDict.put("url", JsonType.STRING);
        keyWordDict.put("img_id", JsonType.STRING);
        keyWordDict.put("face_id", JsonType.STRING);
        keyWordDict.put("person_id", JsonType.STRING);
        keyWordDict.put("group_id", JsonType.STRING);
        keyWordDict.put("img_width", JsonType.INT);
        keyWordDict.put("img_height", JsonType.INT);
        keyWordDict.put("face", JsonType.ARRAY);
        keyWordDict.put("width", JsonType.DOUBLE);
        keyWordDict.put("height", JsonType.DOUBLE);
        keyWordDict.put("eye_left", JsonType.JSON);
        keyWordDict.put("eye_right", JsonType.JSON);
        keyWordDict.put("mouth_left", JsonType.JSON);
        keyWordDict.put("mouth_right", JsonType.JSON);
        keyWordDict.put("attribute", JsonType.JSON);
        keyWordDict.put("gender", JsonType.JSON);
        keyWordDict.put("age", JsonType.JSON);
        keyWordDict.put("race", JsonType.JSON);
        keyWordDict.put("similarity", JsonType.DOUBLE);
        keyWordDict.put("component_similarity", JsonType.JSON);
        keyWordDict.put("is_same_person", JsonType.BOOL);
        keyWordDict.put("candidate", JsonType.ARRAY);
        keyWordDict.put("results", JsonType.ARRAY);
        keyWordDict.put("status", JsonType.STRING);
        keyWordDict.put("response", JsonType.STRING);
        keyWordDict.put("x", JsonType.DOUBLE);
        keyWordDict.put("y", JsonType.DOUBLE);
        keyWordDict.put("has_untrained_face", JsonType.BOOL);
        keyWordDict.put("has_untrained_person", JsonType.BOOL);
        keyWordDict.put("tag", JsonType.STRING);
        keyWordDict.put("person_name", JsonType.STRING);
        keyWordDict.put("group_name", JsonType.STRING);
        keyWordDict.put("eyebrow", JsonType.DOUBLE);
        keyWordDict.put("nose", JsonType.DOUBLE);
        keyWordDict.put("eye", JsonType.DOUBLE);
        keyWordDict.put("mouth", JsonType.DOUBLE);
        keyWordDict.put("confidence", JsonType.DOUBLE);
        keyWordDict.put("success", JsonType.BOOL);
        keyWordDict.put("name", JsonType.STRING);
        keyWordDict.put("description", JsonType.STRING);
        keyWordDict.put("position", JsonType.JSON);
        keyWordDict.put("person", JsonType.ARRAY);
        keyWordDict.put("group", JsonType.ARRAY);
        keyWordDict.put("QUOTA_ALL", JsonType.INT);
        keyWordDict.put("QUOTA_SEARCH", JsonType.INT);
        keyWordDict.put("center", JsonType.JSON);
        keyWordDict.put("ungrouped", JsonType.JSON);
        keyWordDict.put("result", JsonType.JSON);
    }

    public FaceppResult(Object json) {
        this.json = json;
        this.type = JsonType.JSON;
    }

    public FaceppResult(Object json, int httpResponseCode) {
        this.json = json;
        this.type = JsonType.JSON;
        this.httpResponseCode = httpResponseCode;
    }

    public FaceppResult(Object json, FaceppResult.JsonType type, int httpResponseCode) {
        this.json = json;
        this.type = type;
        this.httpResponseCode = httpResponseCode;
    }

    private JSONArray a(String key) {
        try {
            return ((JSONObject)this.json).getJSONArray(key);
        } catch (JSONException var3) {
            return null;
        }
    }

    private JSONObject j(String key) {
        try {
            return ((JSONObject)this.json).getJSONObject(key);
        } catch (JSONException var3) {
            return null;
        }
    }

    private String s(String key) {
        try {
            return ((JSONObject)this.json).getString(key);
        } catch (JSONException var3) {
            return null;
        }
    }

    private Double d(String key) {
        try {
            return ((JSONObject) this.json).getDouble(key);
        } catch (JSONException var3) {
            return null;
        }
    }

    private Boolean b(String key) {
        try {
            return ((JSONObject) this.json).getBoolean(key);
        } catch (JSONException var3) {
            return null;
        }
    }

    private Integer i(String key) {
        try {
            return ((JSONObject) this.json).getInt(key);
        } catch (JSONException var3) {
            return null;
        }
    }

    private JSONArray getArray(String key) {
        return this.a(key);
    }

    private JSONObject getJson(String key) {
        return this.j(key);
    }

    private String getString(String key) {
        return this.s(key);
    }

    private Double getDouble(String key) {
        return this.d(key);
    }

    private Boolean getBoolean(String key) {
        return this.b(key);
    }

    private Integer getInt(String key) {
        return this.i(key);
    }

    public String toString() {
        return this.json.toString();
    }

    public Integer toInteger() throws FaceppParseException {
        if(this.type != JsonType.INT) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not an integer.");
        } else {
            return (Integer)this.json;
        }
    }

    public Double toDouble() throws FaceppParseException {
        if(this.type != JsonType.DOUBLE) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not a float number.");
        } else {
            return (Double)this.json;
        }
    }

    public Boolean toBoolean() throws FaceppParseException {
        if(this.type != JsonType.BOOL) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not a boolean value.");
        } else {
            return (Boolean)this.json;
        }
    }

    public int getCount() throws FaceppParseException {
        if(this.type != JsonType.ARRAY) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not an array.");
        } else {
            return ((JSONArray)this.json).length();
        }
    }

    public FaceppResult get(int index) throws FaceppParseException {
        if(this.type != JsonType.ARRAY) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not an array.");
        } else {
            try {
                return new FaceppResult(((JSONArray)this.json).getJSONObject(index), JsonType.JSON, this.httpResponseCode);
            } catch (JSONException var3) {
                throw new FaceppParseException("Json string can not be parsed.");
            }
        }
    }

    public FaceppResult get(String key) throws FaceppParseException {
        if(this.type != JsonType.JSON) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not json string.");
        } else {
            FaceppResult.JsonType returnType = (FaceppResult.JsonType)keyWordDict.get(key);
            if(returnType == null) {
                throw new FaceppParseException("invalid key word : " + key + ".");
            } else if(!((JSONObject)this.json).has(key)) {
                throw new FaceppParseException("( " + this.json.toString() + " ) do not have the key " + key);
            } else {
                switch(returnType) {
                    case INT:
                        return new FaceppResult(this.getInt(key), JsonType.INT, this.httpResponseCode);
                    case BOOL:
                        return new FaceppResult(this.getBoolean(key), JsonType.BOOL, this.httpResponseCode);
                    case DOUBLE:
                        return new FaceppResult(this.getDouble(key), JsonType.DOUBLE, this.httpResponseCode);
                    case STRING:
                        return new FaceppResult(this.getString(key), JsonType.STRING, this.httpResponseCode);
                    case JSON:
                        return new FaceppResult(this.getJson(key), JsonType.JSON, this.httpResponseCode);
                    case ARRAY:
                        return new FaceppResult(this.getArray(key), JsonType.ARRAY, this.httpResponseCode);
                    default:
                        return null;
                }
            }
        }
    }

    public FaceppResult get(String key, FaceppResult.JsonType type) throws FaceppParseException {
        if(this.type != JsonType.JSON) {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not json string.");
        } else {
            switch(type) {
                case INT:
                    return new FaceppResult(this.getInt(key), JsonType.INT, this.httpResponseCode);
                case BOOL:
                    return new FaceppResult(this.getBoolean(key), JsonType.BOOL, this.httpResponseCode);
                case DOUBLE:
                    return new FaceppResult(this.getDouble(key), JsonType.DOUBLE, this.httpResponseCode);
                case STRING:
                    return new FaceppResult(this.getString(key), JsonType.STRING, this.httpResponseCode);
                case JSON:
                    return new FaceppResult(this.getJson(key), JsonType.JSON, this.httpResponseCode);
                case ARRAY:
                    return new FaceppResult(this.getArray(key), JsonType.ARRAY, this.httpResponseCode);
                default:
                    return null;
            }
        }
    }

    public boolean isError() {
        return (this.type == JsonType.JSON) && ((JSONObject) this.json).has("error");
    }

    public String getErrorMessage() throws FaceppParseException {
        if(this.type == JsonType.JSON && this.isError()) {
            try {
                return ((JSONObject)this.json).getString("error");
            } catch (JSONException var2) {
                throw new FaceppParseException("Json string can not be parsed.");
            }
        } else {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not an error message.");
        }
    }

    public int getErrorCode() throws FaceppParseException {
        if(this.type == JsonType.JSON && this.isError()) {
            try {
                return ((JSONObject)this.json).getInt("error_code");
            } catch (JSONException var2) {
                throw new FaceppParseException("Json string can not be parsed.");
            }
        } else {
            throw new FaceppParseException("( " + this.json.toString() + " ) is not an error message.");
        }
    }

    public int getHttpResponseCode() {
        return this.httpResponseCode;
    }

    public enum JsonType {
        INT,
        BOOL,
        DOUBLE,
        STRING,
        JSON,
        ARRAY;

        JsonType() {
        }
    }
}
