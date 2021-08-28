package br.com.zupacademy

import io.grpc.Status
import io.grpc.Status.INVALID_ARGUMENT
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.exceptions.*
import io.micronaut.http.exceptions.HttpStatusException

@Controller //API REST Com chamada a um Server gRPC
class CalculadoraDeFreteController(val gRpcClient: GrpcFreteServiceGrpc.GrpcFreteServiceBlockingStub) {

    @Get("/api/frete")
    fun calcular(@QueryValue cep: String): FreteResponse {

        val resquest = CalculaFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        try {
            val response = gRpcClient.calculaFrete(resquest) // Chamada gRPC que pode lançar exeção
            return FreteResponse(response.cep, response.valorFrete)

        } catch (e: StatusRuntimeException) {

            val statusCode = e.status.code
            val description = e.status.description

            if (statusCode == Status.Code.INVALID_ARGUMENT) {

                    throw HttpStatusException(HttpStatus.BAD_REQUEST, description)

                }

            if (statusCode == Status.Code.PERMISSION_DENIED){

                val statusProto = StatusProto.fromThrowable(e)
                if (statusProto == null){
                    throw HttpStatusException(HttpStatus.FORBIDDEN, description)
                }
                val anyDetails = statusProto.detailsList.get(0)
                val errorDetails = anyDetails.unpack(ErrorDetails::class.java)

                throw HttpStatusException(HttpStatus.FORBIDDEN, "${errorDetails.code}, ${errorDetails.message}")

            }

            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }



    }

}

data class FreteResponse(val cep: String, val valorFrete: Double) {

}
