package com.tgfashion.controller;

import com.tgfashion.entity.Category;
import com.tgfashion.entity.CustomerOrder;
import com.tgfashion.entity.Product;
import com.tgfashion.repository.CategoryRepository;
import com.tgfashion.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
@RequestMapping("/")
public class HomeController {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    @Autowired
    public HomeController(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        populateProduct(model);
        return "home";
    }

    @RequestMapping("/administration")
    public String admin(Model model) {
        return "admin";
    }

    @RequestMapping("/init")
    public String init(Model model) {
        initProducts();
        return "redirect:/product/view";
    }

    private void populateProduct(Model model) {
        model.addAttribute("dresses", productRepository.findByCategory("Dress"));
        model.addAttribute("shirts", productRepository.findByCategory("T-shirt"));
        List<Product> sales = productRepository.findByStatus("sale");
        List<Product> news = productRepository.findByStatus("new");
        model.addAttribute("sales", sales);
        model.addAttribute("news", news);
        model.addAttribute("populars", Stream.concat(sales.stream(), news.stream()).collect(Collectors.toList()));
        model.addAttribute("customerOrder", new CustomerOrder());
    }

    private void initProducts() {
        List<Product> products = productRepository.findAll();
        if (products == null || products.size() == 0) {
            Category dress = getCategory("Dress");
            Category tshirt = getCategory("T-shirt");
            Date today = new Date();

            Product product = new Product("TG.NH17", "TG.NH17-Đầm công chúa 2 lớp", new Integer("680"), "../assets/img/women/TGNH17.jpg", today, dress);
            product.setColor("Trắng");
            product.setSize("S,M,L");
            product.setStatus("sale");
            product.setDiscount(new Integer("470"));
            productRepository.save(product);

            product = new Product("TG.NH18", "TG.NH18-Đầm yếm ôm body 2 lớp", new Integer("360"), "../assets/img/women/TGNH18.jpg", today, dress);
            product.setColor("Hồng Cam");
            product.setSize("S,M,L");
            product.setStatus("new");
            product.setDiscount(new Integer("255"));
            productRepository.save(product);

            product = new Product("TG.NH19", "TG.NH19-Đầm suông 2 lớp", new Integer("380"), "../assets/img/women/TGNH19.jpg", today, dress);
            product.setColor("Trắng");
            product.setSize("S,M,L");
            product.setStatus("sale");
            product.setDiscount(new Integer("270"));
            productRepository.save(product);

            product = new Product("TG.NH15", "TG.NH15-Đầm tiểu thư 2 lớp", new Integer("540"), "../assets/img/women/TGNH15.jpg", today, dress);
            product.setColor("Trắng");
            product.setSize("S,M,L");
            product.setStatus("new");
            product.setDiscount(new Integer("380"));
            productRepository.save(product);

            product = new Product("TG.NH07", "TG.NH07-Đầm tiểu thư Vintage", new Integer("800"), "../assets/img/women/TGNH07.jpg", today, dress);
            product.setColor("Vintage");
            product.setSize("S,M,L");
            product.setStatus("sale");
            product.setDiscount(new Integer("560"));
            productRepository.save(product);

            product = new Product("TG.NH06", "TG.NH06-Đầm công chúa cao cấp", new Integer("1000"), "../assets/img/women/TGNH06.jpg", today, dress);
            product.setColor("Đỏ đen");
            product.setSize("S,M,L");
            product.setStatus("sale");
            product.setDiscount(new Integer("700"));
            productRepository.save(product);

            product = new Product("TG.NH04", "TG.NH04-Đầm yếm midi hai lớp chất liệu gấm Thái Tuấn", new Integer("680"), "../assets/img/women/TGNH04.jpg", today, dress);
            product.setColor("Đỏ, Xanh");
            product.setSize("S,M,L");
            product.setStatus("sale");
            product.setDiscount(new Integer("420"));
            productRepository.save(product);

            product = new Product("TG.NH02", "TG.NH02-Đầm yếm midi hai lớp vintage chất liệu Gấm xanh ngọc", new Integer("680"), "../assets/img/women/TGNH02.jpg", today, dress);
            product.setColor("Xanh Ngọc");
            product.setSize("S,M,L");
            productRepository.save(product);

            product = new Product("TG.TS01", "TG.TS01-Polo T-Shirt", new Integer("120"), "../assets/img/man/polo-shirt-2.png", today, tshirt);
            product.setColor("Trắng");
            product.setSize("S,M,L");
            product.setStatus("sale");
            product.setDiscount(new Integer("99"));
            productRepository.save(product);

            product = new Product("TG.TS02", "TG.TS02-T-Shirt", new Integer("99"), "../assets/img/man/polo-shirt-1.png", today, tshirt);
            product.setColor("Xám");
            product.setSize("S,M,L");
            productRepository.save(product);
        }
    }

    private Category getCategory(String name) {
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            category = new Category(name);
            categoryRepository.save(category);
        }
        return category;
    }

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri;
        if (System.getenv("DATABASE_URL") != null) {
            dbUri = new URI(System.getenv("DATABASE_URL"));
        } else {
            String DATABASE_URL = "postgres://tgfashion:tgfashion@10.189.222.139:5432/tgfashion";
            dbUri = new URI(DATABASE_URL);
        }

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':'
                + dbUri.getPort() + dbUri.getPath()
                + "?sslmode=require";
        return DriverManager.getConnection(dbUrl, username, password);
    }
}
