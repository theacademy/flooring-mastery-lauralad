package com.wiley;

import com.wiley.controller.FlooringController;
import com.wiley.dao.*;
import com.wiley.service.FlooringService;
import com.wiley.service.FlooringServiceImpl;
import com.wiley.view.FlooringView;
import com.wiley.view.UserIO;
import com.wiley.view.UserIOConsoleImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args){
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        FlooringController controller = ctx.getBean("controller", FlooringController.class);
        controller.run();

    }
}
