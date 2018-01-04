package org.patternomicon.infracode.paas.docker

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.patternomicon.infracode.Component
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

import java.util.concurrent.TimeUnit

interface Service {
    @GET("/version")
    Call<Version> getVersion()
    @POST("/build")
    Call<ResponseBody> createImage(@Query("remote") String dockerFileURI, @Query("t") String tag)
    @GET("/images/json")
    Call<Image[]> getImage(@Query("filters") String name)
}


class Error extends Exception {
    String body
}


class Client {
    String url
    Service service

    Client(String url) {
        this.url = url
//        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .connectTimeout(60, TimeUnit.SECONDS)
//                .readTimeout(60, TimeUnit.SECONDS)
//                .writeTimeout(60, TimeUnit.SECONDS)
//                .build();
        this.service = new Retrofit.Builder().baseUrl(url).
              //  client(okHttpClient).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(Service.class)
    }

    Version getVersion() {
        Call<Version> callSync = service.getVersion()
        try {
            Response<Version> response = callSync.execute()
            if (!response.isSuccessful()) throw new Error(body: response.errorBody())
            response.body()
        } catch (Exception ex) {
            throw ex
        }
    }

    void createImage(Component component) {
        Call<ResponseBody> callSync = service.createImage(component.source, component.getTag())
        try {
            def response = callSync.execute()
            if (!response.isSuccessful()) throw new Error(body: response.errorBody())
        } catch (Exception ex) {
            throw ex
        }
    }

    Image getImage(Component component) {
        Call<Image[]> callSync = service.getImage("{\"reference\":[\""+component.getTag()+"\"]}")
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
