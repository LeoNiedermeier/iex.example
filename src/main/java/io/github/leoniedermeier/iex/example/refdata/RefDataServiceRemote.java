package io.github.leoniedermeier.iex.example.refdata;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Service
public class RefDataServiceRemote implements RefDataService {

    private final RestOperations restOperations;

    /**
     * According to <a href=
     * "https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-restclient.html#boot-features-restclient">"Calling
     * REST services"</a> a {@link RestTemplateBuilder} is used and not a
     * {@link RestTemplate}.
     */
    @Autowired
    public RefDataServiceRemote(final RestTemplateBuilder builder, @Value("${iextrading.root.uri}") final String rootUri) {
        this.restOperations = builder.rootUri(rootUri).build();
    }

    @Override
    @Cacheable(CACHE_NAME_REF_DATA_SYMBOL)
    public List<RefDataSymbol> getRefDataSymbols() {
        // API descripttion: https://iextrading.com/developer/docs/
        final ResponseEntity<RefDataSymbol[]> entity = this.restOperations.getForEntity("/ref-data/symbols",
                RefDataSymbol[].class);
        if (entity.getBody() != null) {
            return asList(entity.getBody());
        } else {
            return emptyList();
        }
    }
}
