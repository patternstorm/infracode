package org.patternomicon.infracode.paas.docker

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface Service {
    @GET("/version")
    Call<Version> getVersion()
}

class Version {
    String Os
}

class Client {
    String url
    Service service

    Client(String url) {
        this.url = url
        this.service = new Retrofit.Builder().baseUrl(url).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(Service.class)
    }

    Version getVersion() {
        Call<Version> callSync = service.getVersion()
        try {
            Response<Version> response = callSync.execute()
            response.body()
        } catch (Exception ex) {
            throw ex
        }
    }
}
