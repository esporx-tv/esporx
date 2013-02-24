package tv.esporx.services;


import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Formatter;

import static java.util.Locale.ENGLISH;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static org.slf4j.LoggerFactory.getLogger;

@Service
public class EmailingService {

    private static final String API_LOGIN = "api";
    private static final String API_KEY = "key-997-lp8yeyph26009u4qi36eo5bmgw46";
    private static final String API_URL = "https://api.mailgun.net/v2/esporx.mailgun.org/messages";
    private static final Logger LOGGER = getLogger(EmailingService.class);

    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Transactional
    public void sendMessage(String email, String accountConfirmationHash) {
        String confirmationUrl = new Formatter().format("http://esporx.tv/user/confirm?hash=%s&email=%s", accountConfirmationHash, email).toString();

        Client client = apiClient();
        WebResource webResource = client.resource(API_URL);
        MultivaluedMapImpl formData = messagePayload(email, confirmationUrl);
        ClientResponse response = webResource.type(APPLICATION_FORM_URLENCODED).post(ClientResponse.class, formData);

        if (response.getStatus() == 200) {
            LOGGER.info("Successfully sent email");
        } else {
            LOGGER.error("Could not send email: " + response.getStatus());
        }

        response.close();
    }

    private MultivaluedMapImpl messagePayload(String email, String confirmationUrl) {
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", messageSource.getMessage("emails.from", new String[]{}, ENGLISH));
        formData.add("to", email);
        formData.add("subject", messageSource.getMessage("emails.welcome.subject", new String[]{}, ENGLISH));
        formData.add("text", messageSource.getMessage("emails.welcome.simple", new String[]{confirmationUrl}, ENGLISH));
        formData.add("html", messageSource.getMessage("emails.welcome", new String[]{confirmationUrl}, ENGLISH));
        return formData;
    }

    private Client apiClient() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(API_LOGIN, API_KEY));
        return client;
    }

}
