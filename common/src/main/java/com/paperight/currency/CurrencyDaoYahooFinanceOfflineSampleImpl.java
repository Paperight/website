package com.paperight.currency;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

//@Repository
public class CurrencyDaoYahooFinanceOfflineSampleImpl extends CurrencyDaoDefaultImpl implements CurrencyDao {

    private Logger logger = LoggerFactory.getLogger(CurrencyDaoYahooFinanceOfflineSampleImpl.class);

    @Override
    protected CurrencyRates loadCurrencyRates() throws Exception {
        logger.info("loading currency rates");
        ClassPathResource classPathResource = new ClassPathResource("yahoo-sample-currency-rates.json");
        
        try (InputStream instream = classPathResource.getInputStream();) {
            CurrencyRates currencyRates = new CurrencyRates();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonRootNode = objectMapper.readTree(instream);
            List<JsonNode> resourceJsonNodes = jsonRootNode.findValues("resource");
            // resourceJsonNodes.
            for (JsonNode resourceJsonNode : resourceJsonNodes) {
                JsonNode fieldsNode = resourceJsonNode.findValue("fields");
                String currencyName = fieldsNode.get("name").getValueAsText();
                currencyName = StringUtils.remove(currencyName, "USD/");
                BigDecimal currencyRate = new BigDecimal(fieldsNode.get("price").getValueAsText());
                currencyRates.getRates().put(currencyName, currencyRate);
            }
            currencyRates.getRates().put("USD", new BigDecimal(1));
            setLastLoadedCurrencyRates(currencyRates);
            return getLastLoadedCurrencyRates();
        }
    }

}
