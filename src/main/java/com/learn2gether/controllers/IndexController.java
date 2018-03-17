package com.learn2gether.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.learn2gether.services.ProductService;

/**
 * Created by jt on 1/20/16.
 */
@Controller
public class IndexController {

    private ProductService productService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping({"/", "index"})
    public String getIndex(Model model){

        model.addAttribute("products", productService.listProducts());

        return "index";
    }
    
    @RequestMapping("/about-us")
    public String showAboutUs(Model model){

        model.addAttribute("products", productService.listProducts());

        return "about-us";
    }
    
    @RequestMapping("/articles")
    public String showArticles(Model model){

        model.addAttribute("products", productService.listProducts());

        return "articles";
    }
    
    @RequestMapping("/contact-us")
    public String showContactUs(Model model){

        model.addAttribute("products", productService.listProducts());

        return "contact-us";
    }

}
