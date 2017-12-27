package org.patternomicon.infracode.clients

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface DockerService {
    @GET("/version")
    Call<DockerVersion> getVersion()
}

class DockerVersion {
    String Os
}

class DockerClient {
    String url
    DockerService service

    DockerClient(String url) {
        this.url = url
        this.service = new Retrofit.Builder().baseUrl(url).
                addConverterFactory(GsonConverterFactory.create()).
                build().
                create(DockerService.class)
    }

    DockerVersion getVersion() {
        Call<DockerVersion> callSync = service.getVersion()
        try {
            Response<DockerVersion> response = callSync.execute()
            response.body()
        } catch (Exception ex) {
            throw ex
        }
    }
}
