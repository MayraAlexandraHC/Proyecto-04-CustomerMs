package com.bank.CustomerMs.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CuentaClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${accountms.url}")
    private String accountMsUrl;

    public boolean tieneCuentasActivas(Integer clienteId) {
        try {
            String url = accountMsUrl + "/cuentas?clienteId=" + clienteId;
            restTemplate.getForObject(url, String.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}