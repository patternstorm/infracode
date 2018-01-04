package org.patternomicon.infracode.paas.docker

import okhttp3.ResponseBody
import org.patternomicon.infracode.Component
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Service {
    String url
    Client client

    Service(String url) {
        this.url = url
        this.client = new Retrofit.Builder().baseUrl(url).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(Client.class)
    }

    Version getVersion() {
        Call<Version> callSync = client.getVersion()
        try {
            Response<Version> response = callSync.execute()
            if (!response.isSuccessful()) throw new Error(body: response.errorBody())
            response.body()
        } catch (Exception ex) {
            throw ex
        }
    }

    void createImage(Component component) {
        Call<ResponseBody> callSync = client.createImage(component.source, component.getTag())
        try {
            def response = callSync.execute()
            if (!response.isSuccessful()) throw new Error(body: response.errorBody())
        } catch (Exception ex) {
            throw ex
        }
    }

    Image getImage(Component component) {
        Call<Image[]> callSync = client.getImage("{\"reference\":[\""+component.getTag()+"\"]}")
        try {
            Response<Image[]> response = callSync.execute()
            if (!response.isSuccessful()) throw new Error(body: response.errorBody())
            Image[] images = response.body()
            if (images.size() !=1) throw new Error(body: "zero or more than one image by this name")
            images[0]
        } catch (Exception ex) {
            throw ex
        }
    }
}
