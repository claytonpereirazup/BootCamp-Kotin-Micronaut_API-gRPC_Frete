package br.com.zupacademy

import io.micronaut.grpc.server.GrpcEmbeddedServer
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import javax.inject.Inject


@Controller
class GrpcFreteServerController(@Inject val grpcServer: GrpcEmbeddedServer) {

    @Get("/grpc-server/stop")
    fun stop(): HttpResponse<String> {

        grpcServer.stop()

        return HttpResponse.ok("Is-Running? ${grpcServer.isRunning}")
    }
}