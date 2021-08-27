package br.com.zupacademy

import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import javax.inject.Singleton
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.random.Random

@Singleton
class GrpcFreteServer: GrpcFreteServiceGrpc.GrpcFreteServiceImplBase() {

    private val logger = LoggerFactory.getLogger(GrpcFreteServer::class.java)

    override fun calculaFrete(request: CalculaFreteRequest?, responseObserver: StreamObserver<CalculaFreteResponse>?) {

        logger.info("Request - Enviado para Cálculo: $request")

        val reponse = CalculaFreteResponse.newBuilder()
            .setCep(request!!.cep)
            .setValorFrete(Random.nextDouble(from = 0.0, until = 150.0))
            .build()

        logger.info("Response - Frete Cálculado: $reponse")

        responseObserver!!.onNext(reponse)
        responseObserver.onCompleted()

    }

}