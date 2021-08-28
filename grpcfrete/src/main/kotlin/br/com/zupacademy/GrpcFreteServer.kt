package br.com.zupacademy

import com.google.protobuf.Any
import com.google.rpc.Code
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
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

        //Retornando Erro de Objeto nulo/vazio
        val cep = request?.cep
        if (cep == null || cep.isBlank()){
            val e = Status.INVALID_ARGUMENT
                .withDescription("CEP Deve ser Informado!")
                .asRuntimeException()
            responseObserver?.onError(e)
        }
        //Retornando Erro de Validação
        if(!cep!!.matches("[0-9]{5}-[0-9]{3}".toRegex())){
            val e = Status.INVALID_ARGUMENT
                .withDescription("CEP Inválido!")
                .augmentDescription("Formato Esperado 00000-000.")
                .asRuntimeException()
            responseObserver?.onError(e)
        }

        //Retornando Erro Simulando Erro Inesperado Negócio BackEnd
        var valorFrete = 0.0
        try {
            valorFrete = Random.nextDouble(from = 0.0, until = 150.0)//deveria ser uma lógica complexa
            if (valorFrete > 100.0){
                throw IllegalStateException("Erro Inesperado ao executar Lógica de Negócio!")
            }
        } catch (e: Exception) {
            responseObserver?.onError(Status.INTERNAL
                .withDescription(e.message) //anexado ao Status, mas não enviado ao Client
                .withCause(e)
                .asRuntimeException()
            )
        }

        //Simulando um verificação de Segurança
        if (cep.endsWith("333")) {
            val statusProto = com.google.rpc.Status.newBuilder()
                .setCode(Code.PERMISSION_DENIED.number)
                .setMessage("Usuário Não Pode Acessar este Recurso!")
                .addDetails(Any.pack(ErrorDetails.newBuilder()
                    .setCode(401)
                    .setMessage("Token Expirado")
                    .build()))
                .build()

            val e = StatusProto.toStatusRuntimeException(statusProto)
            responseObserver?.onError(e)

        }

        val reponse = CalculaFreteResponse.newBuilder()
            .setCep(request!!.cep)
            .setValorFrete(valorFrete)
            .build()

        logger.info("Response - Frete Cálculado: $reponse")

        responseObserver!!.onNext(reponse)
        responseObserver.onCompleted()

    }

}