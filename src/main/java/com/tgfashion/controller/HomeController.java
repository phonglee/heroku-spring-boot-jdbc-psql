/*
 * Copyright 2015 Benedikt Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tgfashion.controller;

import com.tgfashion.model.RecordRepository;
import com.tgfashion.model.Users;
import com.tgfashion.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {

    private RecordRepository repository;
    private UserRepository userRepository;

    @Autowired
    public HomeController(RecordRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String home(Model model) {
        return "home";
    }

    @RequestMapping("/createuserform")
    public String createUserForm(Model model) {
        model.addAttribute("user", new Users());
        return "createuser";
    }

    @RequestMapping("/users")
    public String users(Model model) {
        List<Users> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "user";
    }

    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public String createUser(@ModelAttribute @Valid Users user, Model model, BindingResult result) {
        model.addAttribute("user", user);
        if(!result.hasErrors()) {
            userRepository.save(user);
        }
        return "result";
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
