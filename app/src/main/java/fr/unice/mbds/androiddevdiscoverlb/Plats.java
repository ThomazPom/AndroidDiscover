package fr.unice.mbds.androiddevdiscoverlb;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;

import utils.CallAPI;

/**
 * Created by Thoma on 21/11/2016.
 */

public class Plats implements Serializable {
    /*
    "name": "12-volt Lith-ion Drill W/ Bonus Case & 150-piece Socket Set",
            "description": "Lorem ipsum"
                  "price": 88,
            "calories": 791,
            "type": "Plat ",
            "picture": "http://iberhunting.com/img/mainsections/spanish-gastronomy-main.jpg",
            "discount": 18,
            "createdAt": "2016-11-14T14:04:04.629Z",
            "updatedAt": "2016-11-14T14:04:04.629Z",
            "id": "5829c454cdd80705719c4ac3"

            */

    private String name = "NO NAME";
    private String description = "NO NAME";
    private int price = 0;
    private int calories = 0;
    private String type = "NO TYPE";
    private String picture = "NO PIC";
    private int discount = 0;
    private String id = "NO ID";
    private transient JSONObject jsonOfPlat;

    public Plats(String name, String description, int price, int calories, String type, String picture, int discount) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.calories = calories;
        this.type = type;
        this.picture = picture;
        this.discount = discount;
    }

    public static HashMap<String, Plats > cache = new HashMap<>();
    public Plats(){

    }

    public JSONObject reConstructJson() {
        jsonOfPlat = new JSONObject();
        try {
            jsonOfPlat.put("name", name);
            jsonOfPlat.put("price", price);
            jsonOfPlat.put("description", description);
            jsonOfPlat.put("calories", calories);
            jsonOfPlat.put("type", type);
            jsonOfPlat.put("picture", picture);
            jsonOfPlat.put("discount", discount);
            jsonOfPlat.put("id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonOfPlat;
    }

    final static int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static LruCache<String, Bitmap> bitmapLruCache = new LruCache<>(maxMemory);

    public void pleaseComplete(final CallAPI.Callback callback) {
        final Plats self = this;
        if (jsonOfPlat ==null || !jsonOfPlat.has("createdAt")) {

            new CallAPI("http://95.142.161.35:8080/product/" + this.id, new CallAPI.CallbackClass() {
                @Override
                public void postCall(JSONArray result) {
                    try {
                        self.construct(result.getJSONObject(0));
                        callback.postCall(self);
                    } catch (JSONException e) {
                    }
                }
            }, new HashMap<String, Object>(), null).execute("GET");

        }

    }

    public Plats construct(JSONObject jsonObject) {
        setJsonOfPlat(jsonObject);

        try {
            this.id = this.jsonOfPlat.getString("id");
        } catch (JSONException e) {
            Log.d("Plats", "no id");
            //e.printStackTrace();
        }
        cache.put(id,this);
        if(this.jsonOfPlat.has("idProduit"))
        {

            try {
                this.id = this.jsonOfPlat.getString("idProduit");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(!this.jsonOfPlat.has("createdAt"))
        {/*
            this.pleaseComplete(new CallAPI.Callback() {
                @Override
                public void postCall(Object returnvalue) {

                }
            });
            */
            return this;
        }
        try {
            this.name = this.jsonOfPlat.getString("name");
        } catch (JSONException e) {
            Log.d("Plats", "no name");
            //e.printStackTrace();
        }


        final String pid = this.id;
        try {
            final String pict = this.picture = this.jsonOfPlat.getString("picture");

            if (getBitmap() == null) {

                setBitmap(Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888));
                new CallAPI(getPicture(), new CallAPI.CallbackClass() {

                    @Override
                    public void postCall(JSONArray arrayresult) {

                        try {
                            setBitmap((Bitmap) arrayresult.get(0));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }, new HashMap<String, Object>(), null).execute("GETIMAGE");

            }
        } catch (JSONException e) {
            Log.d("Plats", "no picture");
            //e.printStackTrace();
        }
        try {
            this.description = this.jsonOfPlat.getString("description");
        } catch (JSONException e) {
            Log.d("Plats", "no description");
            //e.printStackTrace();
        }
        try {
            this.price = this.jsonOfPlat.getInt("price");
        } catch (JSONException e) {
            Log.d("Plats", "no price");
            //e.printStackTrace();
        }
        try {
            this.calories = this.jsonOfPlat.getInt("calories");
        } catch (JSONException e) {
            Log.d("Plats", "no calories");
            //e.printStackTrace();
        }
        try {
            this.type = this.jsonOfPlat.getString("type");
        } catch (JSONException e) {
            Log.d("Plats", "no type");
            //e.printStackTrace();
        }
        try {
            this.discount = this.jsonOfPlat.getInt("discount");
        } catch (JSONException e) {
            Log.d("Plats", "no discount");
            //e.printStackTrace();
        }
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBitmap() {
        return bitmapLruCache.get(getPicture());
    }

    public void setBitmap(Bitmap bitmap) {
        bitmapLruCache.put(getPicture(), bitmap);
    }

    public JSONObject getJsonOfPlat() {
        return jsonOfPlat;
    }

    public JSONObject getJsonIdOfPlat() {
        JSONObject returnJson = new JSONObject();
        try {
            returnJson.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnJson;
    }

    public void setJsonOfPlat(JSONObject jsonOfPlat) {
        this.jsonOfPlat = jsonOfPlat;
    }
}
