package powershell;
/*
import com.azure.identity.UsernamePasswordCredential;
import com.azure.identity.UsernamePasswordCredentialBuilder;
import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class MicrosoftAuth {

    private static String authority;
    private static List<String> scopes;
    private static String clientId = "998c1855-b327-484a-8997-c8fedd1c9953";
    private static String username = "saraca@01hc5.onmicrosoft.com";
    private static String password = "bitter123_";

    public static void main(String args[]) throws Exception {

        scopes = new ArrayList<>();
        scopes.add("Domain.Read.All");
        scopes.add("User.ReadWrite.All");

        UsernamePasswordCredential usernamePasswordCredential = new UsernamePasswordCredentialBuilder()
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();

        TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, usernamePasswordCredential);

        GraphServiceClient graphClient =
                GraphServiceClient
                        .builder()
                        .authenticationProvider(tokenCredentialAuthProvider)
                        .buildClient();

        User me = graphClient.me().buildRequest().get();

    }



}
*/