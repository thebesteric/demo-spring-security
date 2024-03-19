/*
 * Copyright 2020-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Rommel
 * @version 1.0
 * @date 2023/8/10-22:35
 * @description TODO
 */
@Controller
public class OrderController {

	@GetMapping("/index")
	public String home() {
		return "index";
	}

	@GetMapping("/order1")
	public String order1(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("authentication:"+ authentication);

		model.addAttribute("authentication",authentication);
		return "order_1";
	}

	@GetMapping("/order2")
	public String order2(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		System.out.println("authentication:"+ authentication);

		model.addAttribute("authentication",authentication);
		return "order_2";
	}

}
