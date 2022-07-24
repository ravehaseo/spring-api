package com.maybank.sample.util;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestfulUtil {

	private static final XLogger log = XLoggerFactory.getXLogger(RestfulUtil.class);

	private static final String RESPONSE = "----- Response received-----";
	private static final String CHARSET = "UTF-8";
	@Value("${util.rest.use.hostname:false}")
	private boolean sendRestUsingHostname;

	@Value("${util.rest.use.proxy:false}")
	private boolean useProxy;

	@Value("${util.rest.proxy.host:127.0.0.1}")
	private String proxyHost;

	@Value("${util.rest.proxy.port:8080}")
	private String proxyPort;

	public <T> T send(final String url, final String body, final HttpMethod httpMethod, final HttpHeaders httpHeaders,
			final int timeoutInMillis, final Class<T> responseObjectType, final boolean useSSL) {
		log.entry();
		T response = send(null, url, body, httpMethod, httpHeaders, timeoutInMillis, responseObjectType, useSSL);
		log.exit();
		return response;
	}

	private <T> T send(final URI url, final String urlString, final String body, final HttpMethod httpMethod, HttpHeaders httpHeaders,
			final int timeoutInMillis, final Class<T> responseObjectType, final boolean useSSL) {
		log.entry();

		try {
			if (StringUtils.isNotBlank(urlString) && url != null) {
				throw new IllegalArgumentException("URI and String URL cannot be both present");
			}
			if (StringUtils.isBlank(urlString) && url == null) {
				throw new IllegalArgumentException("Both URI and String URL is null");
			}

			RestTemplate restTemplate = null;

			if (useProxy) {
				log.debug("Proxy Host: {} {}", proxyHost, proxyPort);
				restTemplate = getRestTemplate(timeoutInMillis, proxyHost, Integer.parseInt(proxyPort));
			} else {
				restTemplate = getRestTemplate(timeoutInMillis, useSSL);
			}

			String targetUrlForLog = null;
			if (url == null) {
				targetUrlForLog = urlString;
			} else {
				targetUrlForLog = url.toString();
			}
			log.debug("----- Sending Request [{}]------", targetUrlForLog);
			ResponseEntity<T> responseEntity = null;
			switch (httpMethod) {

				case GET:
					if (url != null) {
						responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), responseObjectType);
					} else {
						responseEntity = restTemplate.exchange(urlString, HttpMethod.GET, new HttpEntity<>(httpHeaders),
								responseObjectType);
					}
					break;

				case POST:
					if (httpHeaders == null) {
						httpHeaders = new HttpHeaders();
					}
					httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
					HttpEntity<String> requestBody = new HttpEntity<>(body, httpHeaders);

					if (url != null) {
						responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestBody, responseObjectType);
					} else {
						responseEntity = restTemplate.exchange(urlString, HttpMethod.POST, requestBody, responseObjectType);
					}

					break;

				default:
					throw new RestClientException("Http method is not supported");
			}

			if (responseEntity == null) {
				log.warn("Empty response received!!");
				return null;
			}

			T response = responseEntity.getBody();
			log.debug(RESPONSE);

			log.exit();
			return response;

		} catch (ResourceAccessException e) {
			if (e.contains(ConnectException.class) || e.contains(SocketTimeoutException.class)) {
				log.error("ConnectException | SocketTimeoutException caught!", e);
				throw e;
			}
			log.error("Other ResourceAccessException caught!", e);
			return null;
		} catch (Exception e) {
			log.error("Error occurred when sending rest request!", e);
			return null;
		}
	}

	private RestTemplate getRestTemplate(final int timeoutInMillis, final String proxyHost, final int proxyPort) {
		SimpleClientHttpRequestFactory clientHttpReq = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
		clientHttpReq.setProxy(proxy);
		clientHttpReq.setConnectTimeout(timeoutInMillis);
		clientHttpReq.setReadTimeout(timeoutInMillis);

		RestTemplate restTemplate = new RestTemplate(clientHttpReq);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(CHARSET)));
		return restTemplate;
	}

	private RestTemplate getRestTemplate(final int timeoutInMillis, final boolean useSSL)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		HttpComponentsClientHttpRequestFactory rf = null;
		if (!useSSL) {
			TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();

			BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(socketFactoryRegistry);
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).setConnectionManager(connectionManager)
					.build();

			rf = new HttpComponentsClientHttpRequestFactory(httpClient);
			rf.setHttpClient(httpClient);
		} else {
			rf = new HttpComponentsClientHttpRequestFactory();
		}

		if (timeoutInMillis != 0) {
			log.trace("Setting timeout to {} ms", timeoutInMillis);
			rf.setConnectTimeout(timeoutInMillis);
			rf.setReadTimeout(timeoutInMillis);
		} else {
			log.trace("Timeout not defined, default to 10000 ms");
			rf.setReadTimeout(10000);
			rf.setConnectTimeout(5000);
		}
		RestTemplate restTemplate = new RestTemplate(rf);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName(CHARSET)));
		return restTemplate;
	}

}
