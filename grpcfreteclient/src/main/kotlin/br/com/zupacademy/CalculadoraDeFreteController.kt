package br.com.zupacademy

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller
class CalculadoraDeFreteController(val gRpcClient: GrpcFreteServiceGrpc.GrpcFreteServiceBlockingStub) {

    @Get("/api/frete")
    fun calcular(@QueryValue cep: String): FreteResponse {

        val resquest = CalculaFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        val response = gRpcClient.calculaFrete(resquest)

        return FreteResponse(response.cep, response.valorFrete)

    }

}

data class FreteResponse(val cep: String, val valorFrete: Double) {

}
