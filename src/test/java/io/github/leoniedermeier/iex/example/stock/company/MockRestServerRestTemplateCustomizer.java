package io.github.leoniedermeier.iex.example.stock.company;

import java.io.IOException;

import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.RequestExpectationManager;
import org.springframework.test.web.client.RequestMatcher;
import org.springframework.test.web.client.ResponseActions;
import org.springframework.test.web.client.SimpleRequestExpectationManager;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

public class MockRestServerRestTemplateCustomizer implements RestTemplateCustomizer {

    private final RequestExpectationManager expectationManager = new SimpleRequestExpectationManager();

    private final ClientHttpRequestFactory factory = (uri, httpMethod) -> {
        return new MockClientHttpRequest(httpMethod, uri) {
            @Override
            protected ClientHttpResponse executeInternal() throws IOException {
                return createResponse(this);
            }
        };
    };

    private ClientHttpResponse createResponse(final ClientHttpRequest clientHttpRequest) throws IOException {
        Assert.notNull(clientHttpRequest.getURI(), "'uri' must not be null");
        Assert.notNull(clientHttpRequest.getMethod(), "'httpMethod' must not be null");
        return this.expectationManager.validateRequest(clientHttpRequest);
    }

    @Override
    public void customize(final RestTemplate restTemplate) {
        restTemplate.setRequestFactory(this.factory);
    }

    public ResponseActions expect(final ExpectedCount count, final RequestMatcher matcher) {
        return this.expectationManager.expectRequest(count, matcher);
    }

    public ResponseActions expect(final RequestMatcher matcher) {
        return expect(ExpectedCount.once(), matcher);
    }

    public RequestExpectationManager getExpectationManager() {
        return this.expectationManager;
    }

    public void reset() {
        this.expectationManager.reset();
    }

    public void verify() {
        this.expectationManager.verify();
    }

}
