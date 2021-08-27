package br.com.zupacademy

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcFreteClientFactory {

    @Singleton
    fun freteClientStub(@GrpcChannel("frete") channel: ManagedChannel): GrpcFreteServiceGrpc.GrpcFreteServiceBlockingStub? {

//        val channel: ManagedChannel = ManagedChannelBuilder
//            .forAddress("localhost", 50051)
//            .usePlaintext()
//            .maxRetryAttempts(10)
//            .build()

        return GrpcFreteServiceGrpc.newBlockingStub(channel)

    }

}