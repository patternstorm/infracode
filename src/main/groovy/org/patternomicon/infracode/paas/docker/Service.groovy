package org.patternomicon.infracode.paas.docker

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.patternomicon.infracode.Component
import org.patternomicon.infracode.ComponentInstance
import org.patternomicon.infracode.paas.docker.model.Container
import org.patternomicon.infracode.paas.docker.model.ContainerDefinition
import org.patternomicon.infracode.paas.docker.model.ContainerHandle
import org.patternomicon.infracode.paas.docker.model.Image
import org.patternomicon.infracode.paas.docker.model.Version
import retrofit2.Call
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit


class Service {
    private String url
    private Client client
    private Retrofit retrofit
    private Converter<ResponseBody,Error> json2Error

    private Error parseError(ResponseBody response) {
        this.json2Error.convert(response)
    }

    Service(String url) {
        this.url = url
        OkHttpClient httpClient = new OkHttpClient.Builder().
                connectTimeout(100, TimeUnit.SECONDS).
                build()
        this.retrofit = new Retrofit.Builder().baseUrl(url).
                client(httpClient).
                addConverterFactory(GsonConverterFactory.create()).
                build()
        this.client = retrofit.create(Client.class)
        this.json2Error = retrofit.responseBodyConverter(Error.class)
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

    ComponentInstance createContainer(Component component) {
        ContainerDefinition condef = new ContainerDefinition(Image: component.getTag())
        Call<ResponseBody> callSync = client.createContainer(condef,"")
        def response = callSync.execute()
        if (!response.isSuccessful()) throw parseError(response.errorBody())
        Converter<ResponseBody,ContainerHandle> fromJson = retrofit.responseBodyConverter(ContainerHandle.class)
        ContainerHandle handle = fromJson.convert(response.body())
        new ComponentInstance(component: component, id: handle.Id)
    }

    Container getContainer(ComponentInstance component) {
        Call<Container> callSync = client.getContainer(component.id)
        def response = callSync.execute()
        if (!response.isSuccessful()) throw parseError(response.errorBody())
        response.body()
    }
}
