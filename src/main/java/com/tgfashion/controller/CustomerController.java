package com.tgfashion.controller;

import com.tgfashion.entity.Customer;
import com.tgfashion.entity.CustomerOrder;
import com.tgfashion.entity.OrderedProduct;
import com.tgfashion.entity.Product;
import com.tgfashion.repository.CustomerOrderRepository;
import com.tgfashion.repository.CustomerRepository;
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
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private CustomerRepository customerRepository;
    private CustomerOrderRepository customerOrderRepository;
    private ProductRepository productRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, CustomerOrderRepository customerOrderRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.customerOrderRepository = customerOrderRepository;
        this.productRepository = productRepository;
    }

    @RequestMapping("/view")
    public String addCategory(Model model) {
        populateCustomer(model);
        return "customer/viewcustomer";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String addCustomer(@ModelAttribute @Valid Customer customer, Model model, BindingResult result) {
        if(!result.hasErrors()) {
            customerRepository.save(customer);
        }
        populateCustomer(model);
        return "customer/viewcustomer";
    }

    @RequestMapping("/edit/{id}")
    public String editCustomer(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("customer", customerRepository.findOne(id));
        return "customer/viewcustomer";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @Transactional
    public String deleteCustomer(@PathVariable("id") Integer id, Model model) {
        customerRepository.deleteCustomerId(id);
        populateCustomer(model);
        return "customer/viewcustomer";
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String order(@ModelAttribute @Valid CustomerOrder customerOrder, Model model, BindingResult result) {
        Customer customer = customerRepository.findByName("guest");
        if (null == customer) {
            customer = new Customer();
            customer.setName("guest");
            customer.setEmail("phongle2512@gmail.com");
            customer.setAddress("2 Hai Trieu, P Ben Nghe, Q1");
            customer.setPhone("0932060768");
            customer.setCcNumber("1");
            customer.setCityRegion("HCM");
            customerRepository.save(customer);
        }

        Date today = new Date();
        customerOrder.setCustomer(customer);
        customerOrder.setDateCreated(today);
        customerOrder.setTotal(0);
        customerOrder.setConfirmationNumber((int)today.getTime());
        customerOrderRepository.save(customerOrder);
        return "redirect:/";
    }

    @RequestMapping("/order/view")
    public String viewCustomerOrder(Model model) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAllAvailable();
        for (CustomerOrder customerOrder : customerOrders) {
            populateProduct(customerOrder);
        }
        model.addAttribute("customerOrders", customerOrders);
        return "customer/viewcustomerorder";
    }

    @RequestMapping("/order/viewpl")
    public String viewCustomerOrderPl(Model model) {
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        for (CustomerOrder customerOrder : customerOrders) {
            populateProduct(customerOrder);
        }
        model.addAttribute("customerOrders", customerOrders);
        return "customer/viewcustomerorderpl";
    }

    @RequestMapping("/order/edit/{id}")
    public String editCustomerOrder(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("customerOrder", customerOrderRepository.findOne(id));
        return "customer/viewcustomerorder";
    }

    @RequestMapping(value = "order/update/{id}", method = RequestMethod.POST)
    public String updateCustomerOrder(@PathVariable("id") Integer id, Model model) {
        CustomerOrder customerOrder = customerOrderRepository.findOne(id);
        customerOrder.setDeleteFlag(true);
        customerOrderRepository.save(customerOrder);
        return "redirect:/customer/order/view";
    }

    @RequestMapping(value = "order/delete/{id}", method = RequestMethod.DELETE)
    @Transactional
    public String deleteCustomerOrder(@PathVariable("id") Integer id, Model model) {
        customerOrderRepository.deleteCustomerOrderById(id);
        return "redirect:/customer/order/viewpl";
    }

    private void populateCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("customers", customerRepository.findAll());
    }

    private void populateProduct(CustomerOrder customerOrder) {
        String items = customerOrder.getItems();
        String[] data = items.split(";");
        String[] array;
        Product product = null;
        OrderedProduct orderedProduct;
        int sum;
        int total = 0;
        List<OrderedProduct> orderedProductCollection = new ArrayList<>();
        for (String item: data) {
            array = item.split(":");
            if (array.length == 2) {
                product = productRepository.findByName(array[0]);
            }
            if (null != product) {
                orderedProduct = new OrderedProduct();
                orderedProduct.setQuantity(Short.parseShort(array[1]));
                sum = product.getPrice() * orderedProduct.getQuantity();
                total = total + sum;
                orderedProduct.setProduct(product);
                orderedProductCollection.add(orderedProduct);
            }
        }
        customerOrder.setTotal(total);
        customerOrder.setOrderedProductCollection(orderedProductCollection);
    }
}
