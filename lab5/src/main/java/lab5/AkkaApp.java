package lab5;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

import static org.asynchttpclient.Dsl.asyncHttpClient;


public class AkkaApp {
    private static String HOST = "localhost";
    private static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.out.println("start");
        ActorSystem system = ActorSystem.create("routes");
        final Http http = Http.get(system);
        final ActorMaterializer materializer = ActorMaterializer.create(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = new AsyncHttpClient(system).createRouteFlow(materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(
                routeFlow,
                ConnectHttp.toHost(HOST, PORT),
                materializer
        );
        System.out.println("Server online at http://localhost:8080");
        System.in.read();
        binding.thenCompose(ServerBinding::unbind).thenAccept(unbound -> {
            system.terminate();
        });
        try {
            asyncHttpClient().close();
        } catch (IOException err) {
            //System.out.println(err.getStackTrace());
        }
    }
}
