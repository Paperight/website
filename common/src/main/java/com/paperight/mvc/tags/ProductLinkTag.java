package com.paperight.mvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;

import com.paperight.product.Product;

@Configurable
public class ProductLinkTag extends SimpleTagSupport {

    private Product product;
    
    @Value("${base.url}")
    private String websiteUrl;
    
    public void doTag() throws JspException, IOException {
        try {
            String url = websiteUrl + "/product/" + product.getId();
            getJspContext().getOut().write(url);
        } catch (IOException e) {
            throw new JspException();
        }

        return;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    
}
