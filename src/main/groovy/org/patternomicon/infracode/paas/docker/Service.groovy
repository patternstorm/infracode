package org.patternomicon.infracode.paas.docker

import okhttp3.ResponseBody
import org.patternomicon.infracode.Component
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Service {
    private String url
    private Client client
    private Retrofit retrofit
    private Converter<ResponseBody,Error> converter

    private Error parseError(ResponseBody response) {
        this.converter.convert(response)
    }

    Service(String url) {
        this.url = url
        this.retrofit = new Retrofit.Builder().baseUrl(url).
                addConverterFactory(GsonConverterFactory.create()).
                build()
        this.client = retrofit.create(Client.class)
        this.converter = retrofit.responseBodyConverter(Error.class)
    }

    Version getVersion() {
        Call<Version> callSync = client.getVersion()
        try {
            Response<Version> response = callSync.execute()
            if (!response.isSuccessful()) throw parseError(response.errorBody())
            response.body()
        } catch (Exception ex) {
            throw ex
        }
    }

    void createImage(Component component) {
        Call<ResponseBody> callSync = client.createImage(component.source, component.getTag())
        try {
            def response = callSync.execute()
            if (!response.isSuccessful()) throw parseError(response.errorBody())
        } catch (Exception ex) {
            throw ex
        }
    }

    Image getImage(Component component) {
        Call<Image[]> callSync = client.getImage("{\"reference\":[\""+component.getTag()+"\"]}")
        try {
            Response<Image[]> response = callSync.execute()
            if (!response.isSuccessful()) throw parseError(response.errorBody())
            Image[] images = response.body()
            if (images.size() !=1) throw new Error(message: "zero or more than one image by this name")
            images[0]
        } catch (Exception ex) {
            throw ex
        }
    }
}
