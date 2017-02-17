package com.tgfashion.controller;

import com.tgfashion.entity.Category;
import com.tgfashion.repository.CategoryRepository;
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
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @RequestMapping("/view")
    public String addCategory(Model model) {
        populateCategories(model);
        return "category/viewcategory";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addCategory(@ModelAttribute @Valid Category category, Model model, BindingResult result) {
        if(!result.hasErrors()) {
            categoryRepository.save(category);
        }
        populateCategories(model);
        return "category/viewcategory";
    }

    @RequestMapping("/edit/{id}")
    public String editCategory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("category", categoryRepository.findOne(id));
        return "category/viewcategory";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @Transactional
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        categoryRepository.deleteCategoryById(id);
        populateCategories(model);
        return "category/viewcategory";
    }

    private void populateCategories(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("category", new Category());
    }
}
