package com.tgfashion.controller;

import com.tgfashion.entity.Product;
import com.tgfashion.repository.CategoryRepository;
import com.tgfashion.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Date;

@Controller
@RequestMapping("/product")
public class ProductController {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    @Autowired
    public ProductController(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/view")
    public String view(Model model) {
        populateProduct(model);
        return "product/viewproduct";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addProduct(@ModelAttribute @Valid Product product, Model model, BindingResult result) {
        if(!result.hasErrors()) {
            product.setLastUpdate(new Date());
            productRepository.save(product);
        }
        populateProduct(model);
        return "product/viewproduct";
    }

    @RequestMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product", productRepository.findOne(id));
        model.addAttribute("categories", categoryRepository.findAll());
        return "product/viewproduct";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @Transactional
    public String deleteCategory(@PathVariable("id") Integer id, Model model) {
        productRepository.deleteProductById(id);
        populateProduct(model);
        return "product/viewproduct";
    }

    private void populateProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
    }
}
