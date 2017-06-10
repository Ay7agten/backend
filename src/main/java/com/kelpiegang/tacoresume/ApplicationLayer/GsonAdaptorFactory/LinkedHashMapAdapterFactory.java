package com.kelpiegang.tacoresume.ApplicationLayer.GsonAdaptorFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Date;
import java.util.LinkedHashMap;
import org.bson.types.ObjectId;

public class LinkedHashMapAdapterFactory extends CustomizedTypeAdapterFactory<LinkedHashMap> {

    public LinkedHashMapAdapterFactory() {
        super(LinkedHashMap.class);
    }

    @Override
    protected void beforeWrite(LinkedHashMap source, JsonElement toSerialize) {
        Gson gson = new GsonBuilder().create();
        if (source.get("_id") != null) {
            String _id = source.get("_id").toString();
            toSerialize.getAsJsonObject().remove("_id");
            toSerialize.getAsJsonObject().add("_id", gson.toJsonTree(new ObjectId(_id)));
        }
        if (source.get("startDate") != null) {

            Long startDate = (Long) source.get("startDate");
            toSerialize.getAsJsonObject().remove("startDate");
            toSerialize.getAsJsonObject().add("startDate", gson.toJsonTree(new Date(startDate)));
        }
        if (source.get("endDate") != null) {

            Long endDate = (Long) source.get("endDate");
            toSerialize.getAsJsonObject().remove("endDate");
            toSerialize.getAsJsonObject().add("endDate", gson.toJsonTree(new Date(endDate)));
        }
    }

    @Override
    protected void afterRead(JsonElement deserialized) {
    }
}
