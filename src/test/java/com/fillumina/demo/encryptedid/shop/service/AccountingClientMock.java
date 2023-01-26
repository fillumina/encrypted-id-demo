package com.fillumina.demo.encryptedid.shop.service;

import com.fillumina.demo.encryptedid.accounting.dto.InvoiceDTO;
import com.fillumina.demo.encryptedid.shop.client.AccountingClient;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class AccountingClientMock implements AccountingClient {

    private InvoiceDTO invoice;
    private long response;
    private String customerId;
    private List<BigDecimal> list;

    @Override
    public long registerNewInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
        return 123L;
    }

    @Override
    public List<BigDecimal> getExpenses(String customerId) {
        this.customerId = customerId;
        return list;
    }

    public AccountingClientMock invoice(final InvoiceDTO value) {
        this.invoice = value;
        return this;
    }

    public AccountingClientMock response(final long value) {
        this.response = value;
        return this;
    }

    public AccountingClientMock customerId(final String value) {
        this.customerId = value;
        return this;
    }

    public AccountingClientMock list(final List<BigDecimal> value) {
        this.list = value;
        return this;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public long getResponse() {
        return response;
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<BigDecimal> getList() {
        return list;
    }
}
